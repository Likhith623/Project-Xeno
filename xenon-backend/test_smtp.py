import urllib.request
import urllib.error
import json
import time

BASE_URL = "http://localhost:8080/api/v1"
API_KEY = "likhit@178926a"

HEADERS = {
    "Content-Type": "application/json",
    "X-API-KEY": API_KEY
}

EMAILS_TO_TEST = [
    "nikitha7865@gmail.com",
    "n54547330@gmail.com",
    "apparipadmasri@gmail.com"
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

def print_result(method, path, expected_statuses, status, res_json):
    if not isinstance(expected_statuses, list):
        expected_statuses = [expected_statuses]
    success = status in expected_statuses
    print(f"{method} {path}: {status} -> {'PASSED' if success else 'FAILED'}")
    if not success:
        print(f"Details: {res_json}")
    return success, res_json

def run_tests():
    print("=== SMTP REAL EMAIL EXECUTION TEST ===")
    timestamp = str(int(time.time()))
    
    # 1. Ensure Users Exist
    print("\n--- 1. Checking / Creating Target Customers ---")
    customer_ids = []
    for email in EMAILS_TO_TEST:
        # Create Customer
        data = {
            "name": f"Tester {email.split('@')[0]}",
            "email": email,
            "phone": f"+91{str(int(time.time()*100))[-10:]}", # Random unique phone
            "preferredChannel": "EMAIL",
            "tags": ["smtp_test"]
        }
        st, res = make_request("POST", "/customers", data)
        success, res = print_result("POST", f"/customers ({email})", [201, 400], st, res)
        if st == 201:
            customer_ids.append(res['data']['id'])
        elif st == 400 and "already exists" in str(res):
            print(f"User {email} already exists, skipping creation.")
        else:
            print("Failed to handle user creation.")

    # 2. Create Segment specifically for these emails
    print("\n--- 2. Creating Dynamic Segment ---")
    filter_emails = "','".join(EMAILS_TO_TEST)
    sql_filter = f"SELECT id FROM customers WHERE email IN ('{filter_emails}')"
    data = {"name": f"SMTP Target Segment {timestamp}", "description": "SMTP Testing", "type": "DYNAMIC", "filterSql": sql_filter}
    st, res = make_request("POST", "/segments", data)
    success, res = print_result("POST", "/segments", 201, st, res)
    if not success: return
    segment_id = res['data']['id']

    # 3. Create Campaign
    print("\n--- 3. Creating Campaign ---")
    data = {"name": f"SMTP Test Campaign {timestamp}", "segmentId": segment_id, "goal": "Verify real emails work"}
    st, res = make_request("POST", "/campaigns", data)
    success, res = print_result("POST", "/campaigns", 201, st, res)
    if not success: return
    campaign_id = res['data']['id']

    # 4. Create Variant
    print("\n--- 4. Creating Email Variant ---")
    data = {
        "campaignId": campaign_id,
        "name": "SMTP Test Variant",
        "channel": "email",
        "subjectLine": "Xeno CRM - Live SMTP Execution Test",
        "bodyHtml": "<h2>Hello from Xeno AI CRM!</h2><p>If you are reading this, the JavaMailSender SMTP integration is working flawlessly in production.</p>"
    }
    st, res = make_request("POST", "/variants", data)
    success, res = print_result("POST", "/variants", 201, st, res)
    if not success: return

    # 5. Execute Campaign
    print("\n--- 5. Triggering Campaign Execution ---")
    st, res = make_request("POST", f"/campaigns/{campaign_id}/execute")
    success, res = print_result("POST", f"/campaigns/{campaign_id}/execute", 202, st, res)

    print("\nCampaign execution triggered. Waiting 5 seconds to let async execution complete...")
    time.sleep(5)

    # 6. Verify Campaign Status
    st, res = make_request("GET", f"/campaigns/{campaign_id}")
    success, res = print_result("GET", f"/campaigns/{campaign_id}", 200, st, res)
    if success:
        print(f"Final Campaign Status: {res['data']['status']}")

    print("\n=== TEST COMPLETED ===")

if __name__ == "__main__":
    run_tests()
