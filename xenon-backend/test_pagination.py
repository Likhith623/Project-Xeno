import urllib.request
import json

BASE_URL = "http://localhost:8080/api/v1"
HEADERS = {"X-API-KEY": "likhit@178926a"}

ENDPOINTS = [
    "/customers",
    "/products",
    "/orders",
    "/segments",
    "/campaigns"
]

def test_pagination():
    print("=== XENO CRM: DASHBOARD PAGINATION TEST ===")
    all_success = True
    
    for ep in ENDPOINTS:
        url = f"{BASE_URL}{ep}?page=0&size=5"
        print(f"\n[TESTING] GET {url}")
        
        req = urllib.request.Request(url, headers=HEADERS)
        try:
            with urllib.request.urlopen(req) as resp:
                if resp.status == 200:
                    data = json.loads(resp.read().decode())
                    pagination = data.get("pagination")
                    
                    if pagination:
                        print(f"  [SUCCESS] Got 200 OK. Pagination Metadata: {pagination}")
                        print(f"  [SUCCESS] Number of items returned: {len(data.get('data', []))}")
                    else:
                        print(f"  [ERROR] Response missing pagination metadata: {data}")
                        all_success = False
                else:
                    print(f"  [ERROR] Failed with status {resp.status}")
                    all_success = False
        except Exception as e:
            print(f"  [ERROR] Request failed: {e}")
            all_success = False

    print("\n===========================================")
    if all_success:
        print("ALL DASHBOARD ENDPOINTS ARE WORKING PERFECTLY!")
    else:
        print("SOME ENDPOINTS FAILED.")

if __name__ == "__main__":
    test_pagination()
