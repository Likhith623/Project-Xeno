import urllib.request
import json
import time

BASE_URL = "https://project-xeno.onrender.com/api/v1"
API_KEY = "likhit@178926a"
HEADERS = {"Content-Type": "application/json", "X-API-KEY": API_KEY}

def request(method, path, data=None):
    req = urllib.request.Request(BASE_URL + path, method=method, headers=HEADERS)
    if data: req.data = json.dumps(data).encode('utf-8')
    try:
        with urllib.request.urlopen(req) as res:
            b = res.read().decode()
            return res.status, json.loads(b) if b else {}
    except Exception as e:
        return 500, str(e)

print("Starting GUARANTEED Live Cloud Email Dispatch...")

# The IDs were created in the last test
c1 = "c728218c-fe9a-4cef-99c1-b012d1f83117"
c2 = "b714c15d-e190-4e79-afe6-555f621dd101"

seg_payload = {
    "name": f"Direct STATIC Send {int(time.time())}",
    "type": "STATIC",
    "filterSql": None
}
_, res = request("POST", "/segments", seg_payload)
seg_id = res['data']['id']

print(f"Adding customers {c1} and {c2} to segment {seg_id} manually...")
request("POST", f"/segments/{seg_id}/members", {"customerIds": [c1, c2]})

_, res = request("GET", f"/segments/{seg_id}/members?size=100")
data_seg = res.get('data', {}) if isinstance(res, dict) else {}
members = data_seg.get('content', []) if isinstance(data_seg, dict) else (data_seg if isinstance(data_seg, list) else [])
print(f"STATIC Segment Evaluated. Members matched: {len(members)}")

camp_payload = {"name": f"Live Cloud Delivery Guarantee {int(time.time())}", "segmentId": seg_id, "goal": "Deliver"}
_, res = request("POST", "/campaigns", camp_payload)
camp_id = res['data']['id']

v1_payload = {
    "campaignId": camp_id, "name": "V1", "channel": "email",
    "subjectLine": "Important: Xeno CRM Cloud is Live! 🎉", 
    "bodyHtml": "<h3>Hello Likhith and Anikitha!</h3><p>If you are reading this email, the Xeno CRM Sovereign backend has been successfully deployed to the Render Cloud and the SMTP dispatcher is perfectly functioning!</p>"
}
request("POST", "/variants", v1_payload)

print(f"Executing Campaign {camp_id}...")
st, ex_res = request("POST", f"/campaigns/{camp_id}/execute")
print(f"Execute response: {st} {ex_res}")

print("Waiting 15 seconds for SMTP dispatch via Spring Mail...")
time.sleep(15)

_, perf = request("GET", f"/campaigns/{camp_id}/performance")
print("Final Performance:", json.dumps(perf.get('data') if isinstance(perf, dict) else perf, indent=2))
