import urllib.request
import urllib.error
import json
import time
import sys

BASE_URL = "http://localhost:8080/api/v1"
API_KEY = "likhit@178926a"

HEADERS = {
    "Content-Type": "application/json",
    "X-API-KEY": API_KEY
}

def make_request(method, path, data=None):
    url = BASE_URL + path
    req = urllib.request.Request(url, method=method, headers=HEADERS)
    if data:
        req.data = json.dumps(data).encode('utf-8')
    try:
        with urllib.request.urlopen(req) as response:
            res_body = response.read().decode('utf-8')
            return response.status, json.loads(res_body) if res_body else None
    except urllib.error.HTTPError as e:
        res_body = e.read().decode('utf-8')
        return e.code, json.loads(res_body) if res_body else {"error": res_body}
    except Exception as e:
        return 500, {"error": str(e)}

def print_result(method, path, expected_status, status, res_json):
    success = status == expected_status
    print(f"{method} {path}: {status} -> {success}")
    if not success:
        print(f"Error: {res_json}")
    return success, res_json

def run_tests():
    print("--- Testing Real Customer Workflow ---")
    
    # 1. Create Customer
    timestamp = str(int(time.time()))
    customer_data = {
        "name": "King James",
        "email": f"kingjames.08623+{timestamp}@gmail.com",
        "phone": f"+919876{timestamp[-4:]}",
        "preferredChannel": "EMAIL",
        "tags": ["real_test"]
    }
    status, res = make_request("POST", "/customers", customer_data)
    success, res = print_result("POST", "/customers", 201, status, res)
    
    if not success:
        if "already exists" in str(res):
            # Try fetching existing
            print("Customer exists, skipping creation for test.")
        else:
            sys.exit(1)
        
    customer_id = res['data']['id']
    
    # 2. Retrieve Customer
    status, res2 = make_request("GET", f"/customers/{customer_id}")
    success, _ = print_result("GET", f"/customers/{customer_id}", 200, status, res2)
    
    # 3. Retrieve Customer 360
    status, res3 = make_request("GET", f"/customers/{customer_id}/360")
    success, _ = print_result("GET", f"/customers/{customer_id}/360", 200, status, res3)
    
    # 4. Update Customer
    update_data = {
        "name": "King James Updated",
        "preferredChannel": "WHATSAPP"
    }
    status, res4 = make_request("PUT", f"/customers/{customer_id}", update_data)
    success, _ = print_result("PUT", f"/customers/{customer_id}", 200, status, res4)
    
    # 5. Test Deletion
    status, res5 = make_request("DELETE", f"/customers/{customer_id}")
    success, _ = print_result("DELETE", f"/customers/{customer_id}", 200, status, res5)
    
    # Verify Deletion
    status, res6 = make_request("GET", f"/customers/{customer_id}")
    print(f"GET (After Delete) /customers/{customer_id}: {status} -> {status == 404}")
    
    print("\n--- Testing AI Features ---")
    chat_data = {
        "prompt": "Analyze kingjames.08623@gmail.com data."
    }
    status, res7 = make_request("POST", "/agent/chat", chat_data)
    success, _ = print_result("POST", "/agent/chat", 200, status, res7)

if __name__ == "__main__":
    run_tests()
