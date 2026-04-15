import requests
import pytest
import time
from datetime import datetime, timezone

BASE_URL = "http://localhost:8080/api"

def wait_for_server():
    max_retries = 10
    for _ in range(max_retries):
        try:
            res = requests.get(f"{BASE_URL}/admin/gates")
            if res.status_code == 200:
                return
        except requests.exceptions.ConnectionError:
            time.sleep(2)
    pytest.fail("Server is not running")

def create_flight_payload():
    stamp = datetime.now(timezone.utc).strftime("%Y%m%d%H%M%S")
    return {
        "flightNumber": f"OP{stamp}",
        "destination": "NYC",
        "departureTime": "2026-04-20T10:00:00",
        "status": "SCHEDULED"
    }

def seed_admin_data():
    response = requests.post(f"{BASE_URL}/admin/seed")
    assert response.status_code == 200

def create_flight():
    payload = create_flight_payload()
    response = requests.post(f"{BASE_URL}/airline/schedule", json=payload)
    assert response.status_code == 200
    return response.json()["id"], payload

def test_health_check():
    wait_for_server()

def test_ping_endpoint():
    response = requests.get("http://localhost:8080/ping")
    assert response.status_code == 200
    assert response.json()["status"] == "ok"

def test_admin_seed_and_list_resources():
    seed_admin_data()
    gates_res = requests.get(f"{BASE_URL}/admin/gates")
    runways_res = requests.get(f"{BASE_URL}/admin/runways")
    assert gates_res.status_code == 200
    assert runways_res.status_code == 200
    assert len(gates_res.json()) >= 1
    assert len(runways_res.json()) >= 1

def test_admin_list_flights():
    flight_id, _ = create_flight()
    response = requests.get(f"{BASE_URL}/admin/flights")
    assert response.status_code == 200
    ids = [item["id"] for item in response.json()]
    assert flight_id in ids

def test_airline_schedule_flight_and_list():
    flight_id, payload = create_flight()
    response = requests.get(f"{BASE_URL}/airline/flights/{flight_id}")
    assert response.status_code == 200
    data = response.json()
    assert data["flightNumber"] == payload["flightNumber"]
    assert data["status"] == "SCHEDULED"
    list_res = requests.get(f"{BASE_URL}/airline/flights")
    assert list_res.status_code == 200
    assert len(list_res.json()) >= 1

def test_passenger_search_flights():
    create_flight()
    response = requests.get(f"{BASE_URL}/passenger/flights/search?dest=NYC")
    assert response.status_code == 200
    data = response.json()
    assert len(data) >= 1
    assert data[-1]["destination"] == "NYC"

def test_groundops_assign_gate_and_list_available():
    seed_admin_data()
    flight_id, _ = create_flight()
    gate_list = requests.get(f"{BASE_URL}/groundops/gates/available")
    assert gate_list.status_code == 200
    response = requests.post(f"{BASE_URL}/groundops/assign-gate/{flight_id}")
    assert response.status_code == 200

def test_gate_availability_decreases_after_assignment():
    seed_admin_data()
    before = requests.get(f"{BASE_URL}/groundops/gates/available").json()
    flight_id, _ = create_flight()
    assign_res = requests.post(f"{BASE_URL}/groundops/assign-gate/{flight_id}")
    assert assign_res.status_code == 200
    after = requests.get(f"{BASE_URL}/groundops/gates/available").json()
    assert len(after) == max(0, len(before) - 1)

def test_passenger_checkin():
    flight_id, _ = create_flight()
    response = requests.post(f"{BASE_URL}/passenger/checkin?passengerId=1&flightId={flight_id}")
    assert response.status_code == 200
    assert response.json() is True

def test_airline_advance_flight():
    flight_id, _ = create_flight()
    response = requests.post(f"{BASE_URL}/airline/{flight_id}/advance")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "BOARDING"

def test_atc_assign_runway_and_issue_clearance():
    seed_admin_data()
    flight_id, _ = create_flight()
    runway_list = requests.get(f"{BASE_URL}/atc/runways/available")
    assert runway_list.status_code == 200
    assign_res = requests.post(f"{BASE_URL}/atc/assign-runway/{flight_id}")
    assert assign_res.status_code == 200
    clearance_res = requests.post(f"{BASE_URL}/atc/issue-clearance/{flight_id}")
    assert clearance_res.status_code == 200
    assert clearance_res.json()["status"] == "CLEARED_FOR_TAKEOFF"

def test_runway_assignment_sets_status_and_runway():
    seed_admin_data()
    available = requests.get(f"{BASE_URL}/atc/runways/available").json()
    if len(available) == 0:
        runway_id = f"R{datetime.now(timezone.utc).strftime('%H%M%S')}"
        create_res = requests.post(
            f"{BASE_URL}/admin/runways",
            json={"runwayId": runway_id, "length": 3800.0, "occupied": False}
        )
        assert create_res.status_code == 200
    flight_id, _ = create_flight()
    assign_res = requests.post(f"{BASE_URL}/atc/assign-runway/{flight_id}")
    assert assign_res.status_code == 200
    data = assign_res.json()
    assert data["runwayId"] is not None
    assert data["status"] == "TAXIING"

def test_airline_update_status():
    flight_id, _ = create_flight()
    response = requests.patch(f"{BASE_URL}/airline/flights/{flight_id}/status?status=DELAYED")
    assert response.status_code == 200
    assert response.json()["status"] == "DELAYED"

def test_full_flow_integration():
    seed_admin_data()
    flight_id, _ = create_flight()
    gate_res = requests.post(f"{BASE_URL}/groundops/assign-gate/{flight_id}")
    assert gate_res.status_code == 200
    checkin_res = requests.post(f"{BASE_URL}/passenger/checkin?passengerId=99&flightId={flight_id}")
    assert checkin_res.status_code == 200
    advance_res = requests.post(f"{BASE_URL}/airline/{flight_id}/advance")
    assert advance_res.status_code == 200
    assign_runway = requests.post(f"{BASE_URL}/atc/assign-runway/{flight_id}")
    assert assign_runway.status_code == 200
    clearance_res = requests.post(f"{BASE_URL}/atc/issue-clearance/{flight_id}")
    assert clearance_res.status_code == 200
    assert clearance_res.json()["status"] == "CLEARED_FOR_TAKEOFF"
