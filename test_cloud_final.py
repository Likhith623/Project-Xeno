import urllib.request
import json
import time
import random
import uuid

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
    except urllib.error.HTTPError as e:
        b = e.read().decode()
        print(f"HTTPError {e.code} on {path}: {b}")
        return e.code, {}
    except Exception as e:
        print(f"Exception on {path}: {e}")
        return 500, {}

print("=== RIGOROUS XENO CLOUD INTEGRATION TEST ===")
print("Inserting Huge Realtime Data into Supabase...")

# 1. Product Ingestion
print("\n[1] Ingesting Products...")
prod_res = []
for i in range(5):
    st, res = request("POST", "/products", {
        "sku": f"SKU-TEST-{int(time.time())}-{i}",
        "name": f"Rigorous Cloud Product {i}",
        "price": random.uniform(10.0, 500.0),
        "currency": "USD"
    })
    if st in (200, 201) and 'data' in res:
        prod_res.append(res['data']['id'])
print(f"Created {len(prod_res)} products.")

# 2. Huge Customer Ingestion
print("\n[2] Ingesting 100 Customers (incl. likhithchowdary_vasireddy@srmap.edu.in)...")
customers = []
customers.append({
    "email": "likhithchowdary_vasireddy@srmap.edu.in",
    "name": "Likhith Vasireddy",
    "phone": f"+1000{int(time.time())}0",
    "preferredChannel": "EMAIL",
    "tags": ["rigorous-test", "vip"]
})

for i in range(1, 100):
    customers.append({
        "email": f"cloud_test_user_{int(time.time())}_{i}@xeno.com",
        "name": f"Cloud Test User {i}",
        "phone": f"+1000{int(time.time())}{i}",
        "preferredChannel": random.choice(["EMAIL", "WHATSAPP", "SMS"]),
        "tags": ["rigorous-test"]
    })

# Use Bulk Ingest
st, res = request("POST", "/customers/bulk", customers)
c_ids = []
if st in (200, 201) and 'data' in res:
    c_ids = [c['id'] for c in res['data']]
print(f"Bulk Ingested {len(c_ids)} customers.")

# 3. Huge Order Ingestion
print("\n[3] Ingesting 200 Orders to trigger Customer 360 metrics computation...")
if c_ids and prod_res:
    orders = []
    for i in range(200):
        c_id = random.choice(c_ids)
        p_id = random.choice(prod_res)
        orders.append({
            "customerId": c_id,
            "orderNumber": f"ORD-CLOUD-{int(time.time())}-{i}",
            "totalAmount": random.uniform(10.0, 500.0),
            "currency": "USD",
            "status": "DELIVERED",
            "items": [{"productId": p_id, "productName": "Rigorous Product", "quantity": 1, "unitPrice": 10.0}]
        })
    # Break into chunks of 100 for bulk orders if needed, or send all
    st, res = request("POST", "/orders/bulk", orders)
    print(f"Bulk Orders Ingest Status: {st}")
    
    time.sleep(3) # Wait for Async Customer 360 metrics calculation

# 4. Sovereign Agent Evaluation
print("\n[4] Testing Sovereign Agent (Gemini)...")
st, res = request("POST", "/agent/chat", {
    "prompt": "Create a segment for customers with the tag 'rigorous-test' and draft an email campaign to them offering a 10% discount."
})
print(f"Agent Chat Status: {st}")
if st in (200, 201):
    data = res.get('data', {})
    print(f"Agent Plan: {data.get('plan')}")
    if data.get('errorMessage'):
        print(f"Agent Error: {data.get('errorMessage')}")
else:
    print("Agent Chat Failed. Check API keys on Render.")

# 5. Segment Creation
print("\n[5] Creating Segment for rigorous test users...")
seg_payload = {
    "name": f"Cloud Rigorous Target {int(time.time())}",
    "description": "Rigorous test segment targeting ALL cloud users",
    "type": "DYNAMIC",
    "filterSql": "SELECT id FROM customers WHERE email LIKE '%@xeno.com' OR email = 'likhithchowdary_vasireddy@srmap.edu.in'"
}
st, res = request("POST", "/segments", seg_payload)
seg_id = res['data']['id'] if st in (200, 201) else None

if seg_id:
    # Evaluate segment
    request("POST", f"/segments/{seg_id}/evaluate")
    time.sleep(10)
    st, mem_res = request("GET", f"/segments/{seg_id}/members")
    data = mem_res.get('data')
    if isinstance(data, list):
        members_count = len(data)
    elif isinstance(data, dict):
        members_count = len(data.get('content', []))
    else:
        members_count = 0
    print(f"Segment created: {seg_id}. Evaluated members: {members_count}")

# 6. Campaign & Variants Creation
if seg_id:
    print("\n[6] Creating Campaign and Variants...")
    camp_payload = {"name": f"Cloud Rigorous Campaign {int(time.time())}", "segmentId": seg_id, "goal": "Rigorous Test"}
    st, res = request("POST", "/campaigns", camp_payload)
    camp_id = res['data']['id']
    
    request("POST", "/variants", {
        "campaignId": camp_id, "name": "Cloud Variant 1", "channel": "email",
        "subjectLine": "Xeno Huge Cloud Test 🎉", 
        "bodyHtml": "<h1>Massive Cloud Verification!</h1><p>This is a rigorous test.</p>",
        "generatedByAi": False
    })
    
    # 7. Execute Campaign
    print("\n[7] Executing Campaign...")
    st, ex_res = request("POST", f"/campaigns/{camp_id}/execute")
    print(f"Execute response: {st}")
    
    # 8. Poll for Webhooks
    print("\n[8] Waiting for Channel Stub Webhooks over the internet...")
    for i in range(15):
        time.sleep(2)
        _, perf = request("GET", f"/campaigns/{camp_id}/performance")
        data = perf.get('data', {})
        sent = data.get('totalSent', 0)
        dlv = data.get('totalDelivered', 0)
        opn = data.get('totalOpened', 0)
        clk = data.get('totalClicked', 0)
        cnv = data.get('totalConverted', 0)
        print(f"Polling {i+1}/15... Sent: {sent}, Delivered: {dlv}, Opened: {opn}, Clicked: {clk}, Converted: {cnv}")
        
        if dlv > 0 and opn > 0:
            print("\n[SUCCESS] Cloud Deployment perfectly functional! The Webhooks are arriving from the Channel Stub over the internet.")
            break
    else:
        print("\n[FAIL] Webhooks didn't arrive fast enough, or Stub isn't connected correctly.")

print("\n=== CLOUD TEST COMPLETE ===")
