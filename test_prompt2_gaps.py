import requests
import json
import uuid

BASE_URL = "http://localhost:8080/api/v1"
HEADERS = {
    "X-API-KEY": "likhit@178926a",
    "Content-Type": "application/json"
}

def print_result(name, res):
    print(f"=== {name} ===")
    print(f"Status: {res.status_code}")
    if res.status_code in [200, 201, 202]:
        try:
            print(json.dumps(res.json(), indent=2))
        except:
            pass
    else:
        print(res.text)
    print("\n")

def test_endpoints():
    print("Testing PROMPT2 GAP Endpoints...")
    
    # 1. Bulk Ingest Customers
    uid = str(uuid.uuid4())[:8]
    customers = [
        {"email": f"nikitha_{uid}@test.com", "name": "Nikitha", "phone": f"+9188888{uid[:5]}", "tags": ["VIP"], "optOutChannels": []},
        {"email": f"padma_{uid}@test.com", "name": "Padma", "phone": f"+9188888{uid[1:6]}", "tags": ["New"], "optOutChannels": []},
        {"email": f"other_{uid}@test.com", "name": "Other", "phone": f"+9188888{uid[2:7]}", "tags": [], "optOutChannels": []}
    ]
    res = requests.post(f"{BASE_URL}/customers/bulk", json=customers, headers=HEADERS)
    print_result("Bulk Ingest Customers", res)
    
    if res.status_code != 201:
        print("Failed to ingest customers. Exiting.")
        return
        
    created_customers = res.json()["data"]
    
    # 2. Bulk Ingest Orders
    orders = [
        {"customerId": created_customers[0]["id"], "orderNumber": f"ORD-{uid}-1", "totalAmount": 35000, "currency": "INR", "channel": "WEB"},
        {"customerId": created_customers[1]["id"], "orderNumber": f"ORD-{uid}-2", "totalAmount": 25000, "currency": "INR", "channel": "STORE"},
        {"customerId": created_customers[2]["id"], "orderNumber": f"ORD-{uid}-3", "totalAmount": 20000, "currency": "INR", "channel": "APP"}
    ]
    res = requests.post(f"{BASE_URL}/orders/bulk", json=orders, headers=HEADERS)
    print_result("Bulk Ingest Orders", res)
    
    # 3. Create a Segment
    segment_req = {
        "name": "High Value Customers",
        "description": "Customers who spent > 20000",
        "filterSql": "SELECT customer_id FROM customer_metrics WHERE monetary_total > 20000",
        "filterJson": {"condition": "AND", "rules": []}
    }
    res = requests.post(f"{BASE_URL}/segments", json=segment_req, headers=HEADERS)
    print_result("Create Segment", res)
    segment_id = res.json()["data"]["id"]
    
    # Evaluate Segment
    res = requests.post(f"{BASE_URL}/segments/{segment_id}/evaluate", headers=HEADERS)
    print_result("Evaluate Segment", res)
    
    # Get Segment Members
    res = requests.get(f"{BASE_URL}/segments/{segment_id}/members", headers=HEADERS)
    print_result("Get Segment Members", res)
    
    # 4. Create Campaign
    campaign_req = {
        "name": "Luxury Promo",
        "description": "Promo for high value customers",
        "goal": "Increase retention",
        "segmentId": segment_id,
        "maxSendCount": 1000,
        "optOutRateThreshold": 0.05
    }
    res = requests.post(f"{BASE_URL}/campaigns", json=campaign_req, headers=HEADERS)
    print_result("Create Campaign", res)
    campaign_id = res.json()["data"]["id"]
    
    # Add Variant
    variant_req = {
        "campaignId": campaign_id,
        "name": "Luxury Variant A",
        "channel": "email",
        "templateId": "tmpl_luxury_01",
        "subjectLine": "Exclusive Offer for You",
        "contentBody": "Here is a 10% discount on your next purchase.",
        "callToAction": "Shop Now"
    }
    res = requests.post(f"{BASE_URL}/variants", json=variant_req, headers=HEADERS)
    print_result("Create Variant", res)
    
    # Trigger Campaign Execution
    res = requests.post(f"{BASE_URL}/campaigns/{campaign_id}/execute", headers=HEADERS)
    print_result("Execute Campaign", res)
    
    # 5. Patch Campaign Status
    res = requests.patch(f"{BASE_URL}/campaigns/{campaign_id}/status", json={"status": "PAUSED"}, headers=HEADERS)
    print_result("Patch Campaign Status (PAUSED)", res)
    
    # 6. Campaign Performance
    res = requests.get(f"{BASE_URL}/campaigns/{campaign_id}/performance", headers=HEADERS)
    print_result("Get Campaign Performance", res)
    
    # 7. MAB Stats
    res = requests.get(f"{BASE_URL}/campaigns/{campaign_id}/variants/mab-stats", headers=HEADERS)
    print_result("Get MAB Stats", res)
    
    # 8. Opt Out Alerts
    res = requests.get(f"{BASE_URL}/campaigns/opt-out-alerts", headers=HEADERS)
    print_result("Get Opt-Out Alerts", res)
    
    # 9. Campaign Corrections
    res = requests.get(f"{BASE_URL}/campaigns/{campaign_id}/corrections", headers=HEADERS)
    print_result("Get Campaign Corrections", res)
    
    # 10. Agent Chat
    chat_req = {
        "prompt": "Create a campaign for our high value customers."
    }
    res = requests.post(f"{BASE_URL}/agent/chat", json=chat_req, headers=HEADERS)
    print_result("Agent Chat", res)
    session_id = res.json()["data"]["sessionId"]
    
    # 11. Agent Session
    res = requests.get(f"{BASE_URL}/agent/sessions/{session_id}", headers=HEADERS)
    print_result("Get Agent Session", res)
    
    # 12. Agent Decisions
    res = requests.get(f"{BASE_URL}/agent/sessions/{session_id}/decisions", headers=HEADERS)
    print_result("Get Agent Decisions", res)
    
    # 13. Memory Query
    res = requests.get(f"{BASE_URL}/memory/query?segmentTag=VIP&channel=email", headers=HEADERS)
    print_result("Get Memory Query", res)
    
    # 14. Customers by Tag
    res = requests.get(f"{BASE_URL}/customers/by-tag?tag=VIP", headers=HEADERS)
    print_result("Customers By Tag", res)

    # 15. Customer Orders
    res = requests.get(f"{BASE_URL}/customers/{created_customers[0]['id']}/orders", headers=HEADERS)
    print_result("Customer Orders", res)

    # 16. Products Bulk
    products = [
        {"sku": f"SKU-{uid}-1", "name": "Premium Coffee", "category": "Coffee", "price": 1500, "currency": "INR"},
        {"sku": f"SKU-{uid}-2", "name": "Espresso Machine", "category": "Equipment", "price": 25000, "currency": "INR"}
    ]
    res = requests.post(f"{BASE_URL}/products/bulk", json=products, headers=HEADERS)
    print_result("Bulk Ingest Products", res)

    # 17. Product Categories
    res = requests.get(f"{BASE_URL}/products/categories", headers=HEADERS)
    print_result("Product Categories", res)

    # 18. Patch Segment
    res = requests.patch(f"{BASE_URL}/segments/{segment_id}", json={"description": "Updated description"}, headers=HEADERS)
    print_result("Patch Segment", res)

    # 19. Patch Variant
    variant_id = res.json().get("data", {}).get("id") if res.json().get("data") else None # Actually we didn't save variant ID earlier
    
    # Let's get variants for campaign to grab variant ID
    res = requests.get(f"{BASE_URL}/variants/campaign/{campaign_id}", headers=HEADERS)
    if res.status_code == 200 and len(res.json().get("data", [])) > 0:
        variant_id = res.json()["data"][0]["id"]
        res = requests.patch(f"{BASE_URL}/variants/{variant_id}", json={"bodyText": "Updated body text"}, headers=HEADERS)
        print_result("Patch Variant", res)
        
        # 20. Soft Delete Variant
        res = requests.delete(f"{BASE_URL}/variants/{variant_id}", headers=HEADERS)
        print_result("Delete Variant", res)

    # 21. Delete Segment (Create a dummy one first)
    dummy_seg = requests.post(f"{BASE_URL}/segments", json={"name": "To Delete"}, headers=HEADERS)
    if dummy_seg.status_code == 201:
        del_seg_id = dummy_seg.json()["data"]["id"]
        res = requests.delete(f"{BASE_URL}/segments/{del_seg_id}", headers=HEADERS)
        print_result("Delete Segment", res)

    # 22. Campaign Simulate
    sim_req = {"campaignId": campaign_id, "audienceSize": 500}
    res = requests.post(f"{BASE_URL}/campaigns/{campaign_id}/simulate", json=sim_req, headers=HEADERS)
    print_result("Campaign Simulate", res)

    print("All tests completed.")

if __name__ == "__main__":
    test_endpoints()
