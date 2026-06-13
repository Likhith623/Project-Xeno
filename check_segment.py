import urllib.request
import json
import time

BASE_URL = "https://project-xeno.onrender.com/api/v1"
API_KEY = "likhit@178926a"
HEADERS = {"Content-Type": "application/json", "X-API-KEY": API_KEY}

def request(method, path):
    req = urllib.request.Request(BASE_URL + path, method=method, headers=HEADERS)
    with urllib.request.urlopen(req) as res:
        return json.loads(res.read().decode())

camps_res = request("GET", "/campaigns?size=5&sort=createdAt,desc")
camps = camps_res.get('data', [])
if not camps:
    print("No campaigns found")
else:
    latest = camps[0]
    print(f"Latest Campaign ID: {latest['id']}, Status: {latest['status']}")
    perf = request("GET", f"/campaigns/{latest['id']}/performance")
    print("Performance:")
    print(json.dumps(perf, indent=2))
