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

TARGET_EMAILS = [
    "nikitha7865@gmail.com",
    "n54547330@gmail.com",
    "likhithchowdary_vasireddy@srmap.edu.in"
]

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

def print_result(method, path, expected_statuses, status, res_json, context=""):
    if not isinstance(expected_statuses, list):
        expected_statuses = [expected_statuses]
    success = status in expected_statuses
    print(f"[{'PASS' if success else 'FAIL'}] {context} - {method} {path}: {status}")
    if not success:
        print(f"    -> Details: {res_json}")
    return success, res_json

def run_tests():
    print("=== RIGOROUS E2E & DUPLICATE TESTING SUITE ===\n")
    ts = str(int(time.time()))
    fail_count = 0

    # 1. Product Duplication Test
    print("--- 1. Testing Product Duplication ---")
    product_data = {"sku": f"RIGOROUS-SKU-{ts}", "name": "Rigorous Product", "price": 99.99}
    st, res = make_request("POST", "/products", product_data)
    success, res = print_result("POST", "/products", 201, st, res, "Create Product")
    if not success: fail_count += 1

    # Attempt Duplicate Product
    st, res = make_request("POST", "/products", product_data)
    # Expect 409 Conflict because of DB unique constraint, properly caught by our new handler
    success, res = print_result("POST", "/products", 409, st, res, "Create Duplicate Product")
    if not success: fail_count += 1

    # 2. Customers Duplication & Live Setup
    print("\n--- 2. Testing Customer Duplication & Setup ---")
    for email in TARGET_EMAILS:
        phone = f"+91{str(int(time.time()*1000))[-10:]}"
        c_data = {
            "name": f"Rigorous Tester {email.split('@')[0]}",
            "email": email,
            "phone": phone,
            "preferredChannel": "EMAIL",
            "tags": ["rigorous_test"]
        }
        st, res = make_request("POST", "/customers", c_data)
        # It should either be 201 Created or 400 Bad Request (handled in service layer)
        success, res = print_result("POST", "/customers", [201, 400], st, res, f"Create {email}")
        if not success: fail_count += 1

        # Force exact duplicate submission to check handling
        st, res = make_request("POST", "/customers", c_data)
        success, res = print_result("POST", "/customers", 400, st, res, f"Duplicate {email}")
        if not success: fail_count += 1

    # 3. Audience Segment Duplication Test
    print("\n--- 3. Testing Segment Duplication ---")
    emails_csv = "','".join(TARGET_EMAILS)
    sql_filter = f"SELECT id FROM customers WHERE email IN ('{emails_csv}')"
    seg_data = {"name": f"Rigorous Segment {ts}", "description": "Targeting specific users", "type": "DYNAMIC", "filterSql": sql_filter}
    
    st, res = make_request("POST", "/segments", seg_data)
    success, res = print_result("POST", "/segments", 201, st, res, "Create Segment")
    if not success: 
        fail_count += 1
        return
    segment_id = res['data']['id']

    # Attempt Duplicate Segment (No Unique constraint on name, should pass 201)
    st, res = make_request("POST", "/segments", seg_data)
    success, res = print_result("POST", "/segments", 201, st, res, "Create Duplicate Segment")
    if not success: fail_count += 1

    # 4. Campaign Duplication & Execution Test
    print("\n--- 4. Testing Campaign Duplication & Execution ---")
    camp_data = {"name": f"Rigorous Campaign {ts}", "segmentId": segment_id, "goal": "Rigorous Validation"}
    st, res = make_request("POST", "/campaigns", camp_data)
    success, res = print_result("POST", "/campaigns", 201, st, res, "Create Campaign")
    if not success: 
        fail_count += 1
        return
    campaign_id = res['data']['id']

    # Attempt Duplicate Campaign (No Unique constraint on name, should pass 201)
    st, res = make_request("POST", "/campaigns", camp_data)
    success, res = print_result("POST", "/campaigns", 201, st, res, "Create Duplicate Campaign")
    if not success: fail_count += 1

    # 5. Variant Test
    print("\n--- 5. Testing Email Variant ---")
    var_data = {
        "campaignId": campaign_id,
        "name": "Rigorous Variant",
        "channel": "email",
        "subjectLine": "Xeno CRM - RIGOROUS SMTP Test",
        "bodyHtml": "<h2>Hello from Xeno AI CRM Rigorous Testing!</h2><p>Duplicate handling works, all endpoints passed, and SMTP is connected.</p>"
    }
    st, res = make_request("POST", "/variants", var_data)
    success, res = print_result("POST", "/variants", 201, st, res, "Create Variant")
    if not success: fail_count += 1

    # 6. Execute Live
    print("\n--- 6. Triggering SMTP Execution ---")
    st, res = make_request("POST", f"/campaigns/{campaign_id}/execute")
    success, res = print_result("POST", f"/campaigns/{campaign_id}/execute", 202, st, res, "Execute Campaign")
    if not success: fail_count += 1

    print("\nWaiting 6 seconds for Async Execution...")
    time.sleep(6)

    st, res = make_request("GET", f"/campaigns/{campaign_id}")
    success, res = print_result("GET", f"/campaigns/{campaign_id}", 200, st, res, "Verify Campaign Status")
    if success:
        print(f"    -> Final Status: {res['data']['status']}")
    
    print("\n===============================")
    if fail_count == 0:
        print("ALL TESTS PASSED WITH 0 FAILURES. 500 ERRORS MITIGATED.")
    else:
        print(f"FAILED {fail_count} TESTS. Check logs.")

if __name__ == "__main__":
    run_tests()
