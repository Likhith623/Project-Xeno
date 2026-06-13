import urllib.request
import json
import time

BASE_URL = "http://localhost:8080/api/v1"
API_KEY = "likhit@178926a"
HEADERS = {
    "Content-Type": "application/json",
    "X-API-KEY": API_KEY
}

def request(method, path, body=None):
    req = urllib.request.Request(BASE_URL + path, method=method, headers=HEADERS)
    if body:
        req.data = json.dumps(body).encode('utf-8')
    try:
        with urllib.request.urlopen(req) as res:
            return res.status, json.loads(res.read().decode())
    except urllib.error.HTTPError as e:
        return e.code, json.loads(e.read().decode())

print("=== FINAL DIRECT MAIL TEST ===")

# 1. Create a Customer for the target email (just in case they don't exist yet)
print("[1] Ensuring customer exists...")
customer_payload = {
    "name": "Likhith Vasireddy Final Test",
    "email": "kingjames.08623@gmail.com",
    "phone": "+910000000000",
    "tags": ["final-mail-test"],
    "isGloballyOptedOut": False
}
st, res = request("POST", "/customers", customer_payload)
print(f"Customer creation/update status: {st}")

# 2. Create a segment exactly for this email
print("\n[2] Creating Segment...")
seg_payload = {
    "name": f"Final Mail Target {int(time.time())}",
    "description": "Targeting only kingjames",
    "type": "DYNAMIC",
    "filterSql": "SELECT id FROM customers WHERE email = 'kingjames.08623@gmail.com'"
}
st, res = request("POST", "/segments", seg_payload)
seg_id = res['data']['id']
print(f"Segment created: {seg_id}")

# 3. Create Campaign
print("\n[3] Creating Campaign...")
camp_payload = {
    "name": f"Final Mail Delivery Test {int(time.time())}",
    "description": "Direct mail test",
    "segmentId": seg_id,
    "status": "DRAFT",
    "goal": "Verify final delivery to specific email address"
}
st, res = request("POST", "/campaigns", camp_payload)
print(res)
if res and 'data' in res and res['data']:
    camp_id = res['data']['id']
    print(f"Campaign created: {camp_id}")
else:
    print("Failed to create campaign. Exiting.")
    exit(1)

# 4. Create Variant
print("\n[4] Creating Variant...")
var_payload = {
    "campaignId": camp_id,
    "name": "Direct Test Variant",
    "channel": "email",
    "subjectLine": "🚀 Xeno CRM: Final Direct Mail Delivery Test!",
    "previewText": "This is a direct test message...",
    "bodyHtml": "<h1>Hello Likhith!</h1><p>If you are reading this, the Xeno CRM Cloud deployment is working perfectly. The Async Campaign Execution and SMTP Dispatch systems are operational.</p>",
    "bodyText": "Hello Likhith! If you are reading this, the Xeno CRM Cloud deployment is working perfectly.",
    "mabIsActive": True
}
st, res = request("POST", "/variants", var_payload)
print(f"Variant created status: {st}")

# 5. Execute
print("\n[5] Executing Campaign...")
st, res = request("POST", f"/campaigns/{camp_id}/execute")
print(f"Execute response: {st}")

print("\nCampaign dispatched to the backend. Please check your inbox for 'likhithchowdary_vasireddy@srmap.edu.in'!")
