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

# The target users as requested
USERS = [
    {"email": "nikitha7865@gmail.com", "name": "Nikitha", "spend": 35000, "product": "Shoes"},
    {"email": "apparipadmasri@gmail.com", "name": "Padma", "spend": 25000, "product": "Handbag"},
    {"email": "n54547330@gmail.com", "name": "User3", "spend": 20000, "product": "Watch"}
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

def run_production_e2e():
    print("=== XENO CRM: FINAL PRODUCTION E2E TEST ===\n")
    ts = str(int(time.time()))

    print("--- 1. Fetching / Creating Customers ---")
    customer_ids = {}
    for u in USERS:
        c_data = {
            "name": u["name"],
            "email": u["email"],
            "phone": f"+9199{ts[-8:]}{len(customer_ids)}", # Random unique phone
            "preferredChannel": "EMAIL",
            "tags": ["vip", "production_test"]
        }
        st, res = make_request("POST", "/customers", c_data)
        if st in [201]:
            customer_ids[u["email"]] = res["data"]["id"]
            print(f"[SUCCESS] Created {u['email']} -> ID: {res['data']['id']}")
        elif st == 400 and "already exists" in res.get("errorMessage", ""):
            # Fetch existing customer
            gst, gres = make_request("GET", f"/customers/by-email?email={u['email']}")
            if gst == 200:
                customer_ids[u["email"]] = gres["data"]["id"]
                print(f"[INFO] Fetched existing {u['email']} -> ID: {gres['data']['id']}")
            else:
                print(f"[ERROR] Failed to fetch {u['email']}: {gres}")
        else:
            print(f"[ERROR] Failed to create {u['email']}: {res}")

    print("\n--- 2. Creating Products & Orders ---")
    for u in USERS:
        cid = customer_ids.get(u['email'])
        if not cid:
            continue
        
        # Check current monetary total BEFORE order
        st, res360_before = make_request("GET", f"/customers/{cid}/360")
        monetary_before = float(res360_before.get("data", {}).get("monetaryTotal") or 0) if st == 200 else 0.0
        print(f"[{u['email']}] Current Spend: {monetary_before}")

        # Create Product
        p_data = {"sku": f"PRD-{u['product'].upper()}-{ts}", "name": u['product'], "price": u['spend'], "currency": "INR"}
        st, res = make_request("POST", "/products", p_data)
        pid = res['data']['id'] if st == 201 else None

        if pid:
            # Create Order
            o_data = {
                "customerId": cid,
                "orderNumber": f"ORD-{u['product'][:3]}-{ts}",
                "totalAmount": u['spend'],
                "currency": "INR",
                "status": "CONFIRMED",
                "items": [{"productId": pid, "productName": u['product'], "quantity": 1, "unitPrice": u['spend']}]
            }
            st, res = make_request("POST", "/orders", o_data)
            if st == 201:
                print(f"[SUCCESS] Order Placed for {u['email']}: {u['product']} at {u['spend']}")
            else:
                print(f"[ERROR] Order Failed for {u['email']}: {res}")
                
            # Wait for async metric computation
            time.sleep(1)
            st, res360_after = make_request("GET", f"/customers/{cid}/360")
            monetary_after = float(res360_after.get("data", {}).get("monetaryTotal") or 0) if st == 200 else 0.0
            print(f"[{u['email']}] Updated Spend: {monetary_after} (+{monetary_after - monetary_before})")


    print("\n--- 3. Sovereign AI Agent Invocation ---")
    ai_prompt = {
        "prompt": "We want to reward our high spenders Nikitha, Padma, and User3. Generate a segment targeting exactly these emails: nikitha7865@gmail.com, apparipadmasri@gmail.com, n54547330@gmail.com and draft an HTML email variant thanking them for their purchases."
    }
    print(f"Sending prompt to AI: {ai_prompt['prompt']}")
    st, res = make_request("POST", "/agent/chat", ai_prompt)
    if st == 200:
        print(f"[SUCCESS] AI Response Received: {res['data']['textReply']}")
    else:
        print(f"[ERROR] AI Chat Failed: {st} - {res}")

    print("\n--- 4. Executing AI Plan: Creating Segment ---")
    emails_csv = "','".join([u['email'] for u in USERS])
    sql_filter = f"SELECT id FROM customers WHERE email IN ('{emails_csv}')"
    seg_data = {
        "name": f"Production VIP Segment {ts}",
        "description": "Targeting 3 specific buyers for final test",
        "type": "DYNAMIC",
        "filterSql": sql_filter
    }
    st, res = make_request("POST", "/segments", seg_data)
    if st == 201:
        segment_id = res['data']['id']
        print(f"[SUCCESS] Segment Created -> ID: {segment_id}")
    else:
        print(f"[ERROR] Segment Creation Failed: {res}")
        return

    print("\n--- 5. Creating Campaign & Variant ---")
    camp_data = {"name": f"Production VIP Rewards {ts}", "segmentId": segment_id, "goal": "Reward top 3 users"}
    st, res = make_request("POST", "/campaigns", camp_data)
    if st == 201:
        campaign_id = res['data']['id']
        print(f"[SUCCESS] Campaign Created -> ID: {campaign_id}")
    else:
        print(f"[ERROR] Campaign Creation Failed: {res}")
        return

    var_data = {
        "campaignId": campaign_id,
        "name": "Production Email Variant",
        "channel": "email",
        "subjectLine": "Exclusive VIP Reward - Thank You from Xeno CRM!",
        "bodyHtml": "<h2>You are a VIP!</h2><p>Dear customer, thank you for your recent high-value purchases (Shoes, Handbags, Watches). We are thrilled to have you! Here is a 30% off coupon for your next purchase: <b>VIP30</b></p>"
    }
    st, res = make_request("POST", "/variants", var_data)
    if st == 201:
        print(f"[SUCCESS] Email Variant Created -> ID: {res['data']['id']}")
    else:
        print(f"[ERROR] Variant Creation Failed: {res}")
        return

    print("\n--- 6. Triggering Live SMTP Dispatch ---")
    st, res = make_request("POST", f"/campaigns/{campaign_id}/execute")
    if st == 202:
        print("[SUCCESS] Execution Triggered Successfully (202 Accepted). Dispatching emails asynchronously...")
    else:
        print(f"[ERROR] Execution Failed: {res}")
        return

    print("\n Waiting 8 seconds for JavaMailSender to dispatch...")
    time.sleep(8)

    st, res = make_request("GET", f"/campaigns/{campaign_id}")
    if st == 200:
        print(f"[SUCCESS] Final Campaign Status: {res['data']['status']}")
        print(f"   Total Sent: {res['data']['totalSent']}")
    else:
        print(f"[ERROR] Failed to verify status: {res}")

    print("\n=== PRODUCTION TEST COMPLETE ===")
    print("Please check the 3 inboxes to verify the live SMTP dispatch.")

if __name__ == "__main__":
    run_production_e2e()
