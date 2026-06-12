import requests
import time
import json

BASE_URL = "http://localhost:8080/api/v1"
HEADERS = {"Content-Type": "application/json", "X-API-KEY": "likhit@178926a"}

def run_test():
    print("--- Starting Final End-to-End Campaign Test ---")

    print("\n0. Seeding 3 high-value test customers...")
    for i in range(1, 4):
        cust = {
            "email": f"vip_test_{i}@example.com",
            "name": f"VIP User {i}",
            "preferredChannel": "email",
            "tags": ["vip"]
        }
        requests.post(f"{BASE_URL}/customers", json=cust, headers=HEADERS)
    print("Seeded 3 test customers.")

    # 1. Trigger the Agent
    prompt = """
    Create an email campaign targeting high-value customers (monetary_total > 500). 
    Draft exactly 3 email variants. They must be extremely beautiful, professional marketing emails. 
    Include full inline CSS, vibrant colorful banners at the top, a clean body, and a clear CTA button. 
    Use modern gradients and micro-aesthetics.
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
