import urllib.request
import urllib.error
import json
import time
import uuid

BASE_URL = "https://project-xeno.onrender.com/api/v1"
API_KEY = "likhit@178926a"
HEADERS = {
    "Content-Type": "application/json",
    "X-API-KEY": API_KEY
}

def request(method, path, data=None):
    url = BASE_URL + path
    req = urllib.request.Request(url, method=method, headers=HEADERS)
    if data:
        req.data = json.dumps(data).encode('utf-8')
    try:
        with urllib.request.urlopen(req) as response:
            body = response.read().decode('utf-8')
            return response.status, json.loads(body) if body else {}
    except urllib.error.HTTPError as e:
        body = e.read().decode('utf-8')
        return e.code, json.loads(body) if body else {"error": body}
    except Exception as e:
        return 500, {"error": str(e)}

print("=== RIGOROUS API TESTING FOR LIVE CLOUD RENDER ENVIRONMENT ===")

print("1. Testing Customers endpoints...")
customers = [
    {"email": "likhithchowdary_vasireddy@srmap.edu.in", "name": "Likhith VIP", "phone": "+919999000010", "preferredChannel": "EMAIL", "tags": ["live-test", "vip"]},
    {"email": "anikitha_kunapareddy@srmap.edu.in", "name": "Anikitha Kunapareddy", "phone": "+919999000011", "preferredChannel": "EMAIL", "tags": ["live-test", "regular"]}
]
c_ids = {}
for c in customers:
    st, res = request("POST", "/customers", c)
    if st == 201: c_ids[c['email']] = res['data']['id']
    elif st == 400:
        st, res = request("GET", f"/customers/by-email?email={c['email']}")
        c_ids[c['email']] = res['data']['id']

print("Customers created/fetched:", c_ids)

print("2. Testing AI Agent (Create Segment + Campaign)...")
chat_payload = {
    "prompt": "Create a new campaign targeting customers with emails likhithchowdary_vasireddy@srmap.edu.in and anikitha_kunapareddy@srmap.edu.in. Create 2 email variants sending them a very warm thank you greeting for joining our platform!"
}
st, res = request("POST", "/agent/chat", chat_payload)
print("AI Agent Chat status:", st)
if st == 200 and 'data' in res:
    print("AI Response:", res['data'].get('textReply'))
    
print("Waiting for AI background tasks on Render to complete (15 seconds)...")
time.sleep(15)

print("3. Manual Segment/Campaign/Variant for Dispatch Guarantee...")
seg_payload = {
    "name": f"Live Cloud Target {int(time.time())}",
    "type": "DYNAMIC",
    "filterSql": "SELECT id FROM customers WHERE email IN ('likhithchowdary_vasireddy@srmap.edu.in', 'anikitha_kunapareddy@srmap.edu.in')"
}
st, res = request("POST", "/segments", seg_payload)
if st != 201: print("Segment failed:", res)
seg_id = res['data']['id']

st, res = request("POST", f"/segments/{seg_id}/evaluate")
print("Segment Evaluate status:", st)
time.sleep(3) # Wait for Supabase evaluation
st, res = request("GET", f"/segments/{seg_id}/members?size=100")
data_seg = res.get('data') if isinstance(res, dict) else None
data_seg_list = data_seg.get('content', []) if isinstance(data_seg, dict) else (data_seg if isinstance(data_seg, list) else [])
print("Segment Members count:", len(data_seg_list))

camp_payload = {"name": f"Live Cloud Welcome {int(time.time())}", "segmentId": seg_id, "goal": "Welcome"}
st, res = request("POST", "/campaigns", camp_payload)
camp_id = res['data']['id']

v1_payload = {
    "campaignId": camp_id, "name": "V1", "channel": "email",
    "subjectLine": "A Very Warm Welcome from Xeno CRM (Live from Render!)", "bodyHtml": "Hello! Thank you for joining our platform. This is a live email test."
}
v2_payload = {
    "campaignId": camp_id, "name": "V2", "channel": "email",
    "subjectLine": "Welcome to Xeno CRM! (Live from Render!)", "bodyHtml": "Hi! We are so glad to have you. Enjoy your stay!"
}
request("POST", "/variants", v1_payload)
request("POST", "/variants", v2_payload)

print("4. Executing Campaign (Dispatching Live Emails via Gmail SMTP)...")
st, res = request("POST", f"/campaigns/{camp_id}/execute")
print("Execute status:", st)

print("Waiting for dispatch to complete...")
time.sleep(10)

st, perf = request("GET", f"/campaigns/{camp_id}/performance")
print("Performance metrics:", json.dumps(perf.get('data') if isinstance(perf, dict) else perf, indent=2))

print("=== LIVE CLOUD TEST DISPATCH COMPLETED ===")
