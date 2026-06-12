import requests
import time
import json

BASE_URL = "http://localhost:8080/api/v1"
HEADERS = {"Content-Type": "application/json", "X-API-KEY": "likhit@178926a"}

def run_test():
    print("--- Starting Final End-to-End Campaign Test ---")

    REAL_EMAILS = [
        "nikitha7865@gmail.com",
        "likhithchowdary_vasireddy@srmap.edu.in",
        "n54547330@gmail.com"
    ]
    
    print("\n0. Seeding 3 real high-value test customers...")
    for i, email in enumerate(REAL_EMAILS):
        cust = {
            "email": email,
            "name": f"Real VIP User {i+1}",
            "phone": f"+9199{str(int(time.time()))[-8:]}{i}",
            "preferredChannel": "email",
            "tags": ["real_vip"]
        }
        res = requests.post(f"{BASE_URL}/customers", json=cust, headers=HEADERS)
        if res.status_code == 201:
            cid = res.json()["data"]["id"]
            # Seed an order to give them a monetary total > 500
            order = {
                "customerId": cid,
                "orderNumber": f"ORD-REAL-{int(time.time())}-{i}",
                "totalAmount": 5000,
                "currency": "INR",
                "status": "CONFIRMED",
                "items": [{"productId": "dummy", "productName": "Luxury Item", "quantity": 1, "unitPrice": 5000}]
            }
            requests.post(f"{BASE_URL}/orders", json=order, headers=HEADERS)
    print("Seeded 3 real test customers with orders.")

    # 1. Trigger the Agent
    prompt = """
    Create an email campaign targeting high-value customers who spent more than 500 (monetary_total > 500) and have the email ending with gmail.com or srmap.edu.in. 
    Draft exactly 1 email variant. It MUST be an extremely beautiful, professional production-ready marketing email. 
    Include full inline CSS, a vibrant colorful gradient banner at the top, a clean aesthetic body thanking them for their purchases, and a prominent beautiful CTA button with hover effects if possible. 
    Do NOT use placeholder text like 'testing main'. The content must feel like a premium luxury brand actual CAMPAIGN.
    """
    
    print("\n1. Asking Agent to draft the campaign...")
    res = requests.post(f"{BASE_URL}/agent/chat", json={"prompt": prompt}, headers=HEADERS)
    
    if res.status_code != 200:
        print(f"Failed to start agent session: {res.text}")
        return

    session_id = res.json()["data"]["sessionId"]
    print(f"Agent Session started: {session_id}")

    # 2. Wait for Agent to finish drafting
    print("\n2. Waiting for Agent to complete drafting...")
    campaign_id = None
    while True:
        res = requests.get(f"{BASE_URL}/agent/sessions/{session_id}", headers=HEADERS)
        json_data = res.json()
        if not json_data.get("data"):
            print("Failed to get data. Response: ", json_data)
            time.sleep(2)
            continue
            
        status = json_data["data"]["status"]
        print(f"  Status: {status}")
        
        if status == "COMPLETED" or status == "AWAITING_HUMAN_APPROVAL":
            campaign_id = json_data["data"]["createdCampaignId"]
            break
        if status == "FAILED":
            print("Agent failed!")
            print(json_data)
            return
            
        time.sleep(2)

    # 3. Approve Execution
    print(f"\n3. Agent drafting complete. Approving execution of Campaign {campaign_id}...")
    approval_res = requests.post(f"{BASE_URL}/campaigns/{campaign_id}/execute", headers=HEADERS)
    
    if approval_res.status_code not in (200, 202):
        print(f"Failed to approve execution: {approval_res.text}")
        return
        
    print(f"Execution Approved! Status: {approval_res.status_code}")
    
    # 4. Monitor Campaign Performance
    print("\n4. Monitoring Live Campaign Metrics (waiting for async callbacks)...")
    
    for i in range(5):
        time.sleep(4)
        perf_res = requests.get(f"{BASE_URL}/campaigns/{campaign_id}/performance", headers=HEADERS)
        if perf_res.status_code == 200:
            perf = perf_res.json()["data"]
            print(f"  [T+{i*4+4}s] Sent: {perf.get('totalSent')}, Delivered: {perf.get('totalDelivered')}, Opened: {perf.get('totalOpened')}, Clicked: {perf.get('totalClicked')}")
        else:
            print("Failed to fetch performance")
            
    print("\n5. Checking Multi-Armed Bandit Stats for the 3 beautiful variants...")
    mab_res = requests.get(f"{BASE_URL}/variants/{campaign_id}/mab-stats", headers=HEADERS)
    if mab_res.status_code == 200:
        print(json.dumps(mab_res.json()["data"], indent=2))
        
    print("\n--- Final Test Complete! ---")

if __name__ == "__main__":
    run_test()
