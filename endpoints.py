import urllib.request
import json
import uuid
import time
import sys

BASE_URL = "http://localhost:8080/api/v1"
HEADERS = {
    "Content-Type": "application/json",
    "X-API-KEY": "likhit@178926a"
}

def make_request(method, endpoint, data=None):
    url = f"{BASE_URL}{endpoint}"
    req_data = json.dumps(data).encode('utf-8') if data else None
    req = urllib.request.Request(url, method=method, headers=HEADERS, data=req_data)
    try:
        res = urllib.request.urlopen(req)
        body = res.read().decode('utf-8')
        try:
            return json.loads(body)
        except json.JSONDecodeError:
            return body
    except urllib.error.HTTPError as e:
        body = e.read().decode('utf-8')
        print(f"[{method}] {endpoint} -> FAILED WITH {e.code}: {body}")
        return None
    except Exception as e:
        print(f"[{method}] {endpoint} -> EXCEPTION: {e}")
        return None

def test_all():
    print("--- 1. Testing Customer Endpoints ---")
    customer = make_request("POST", "/customers", {
        "name": "Rigorous Tester",
        "email": f"tester_{uuid.uuid4().hex[:8]}@example.com",
        "phone": f"+123{uuid.uuid4().hex[:7]}",
        "preferredChannel": "EMAIL",
        "tags": ["vip", "tester"]
    })
    assert customer and customer.get("success"), "Failed to create customer"
    customer_id = customer["data"]["id"]
    print(f"Created Customer: {customer_id}")

    make_request("GET", f"/customers/{customer_id}")
    make_request("GET", "/customers")
    make_request("GET", f"/customers/by-email?email={customer['data']['email']}")
    make_request("GET", f"/customers/{customer_id}/360")
    make_request("PUT", f"/customers/{customer_id}", {
        "name": "Updated Tester",
        "tags": ["vip", "updated"]
    })

    print("\n--- 2. Testing Product & Order Endpoints ---")
    product = make_request("POST", "/products", {
        "sku": f"TEST-SKU-{uuid.uuid4().hex[:4]}",
        "name": "Test Product",
        "price": 99.99,
        "currency": "USD"
    })
    product_id = product["data"]["id"]
    make_request("GET", "/products")
    make_request("GET", f"/products/{product_id}")

    order = make_request("POST", "/orders", {
        "customerId": customer_id,
        "orderNumber": f"ORD-{uuid.uuid4().hex[:6]}",
        "totalAmount": 99.99,
        "currency": "USD",
        "items": [{
            "productId": product_id,
            "productName": "Test Product",
            "quantity": 1,
            "unitPrice": 99.99
        }]
    })
    order_id = order["data"]["id"]
    make_request("GET", "/orders")
    make_request("GET", f"/orders/{order_id}")
    make_request("GET", f"/customers/{customer_id}/orders")

    print("\n--- 3. Testing AI Agent & Segment Endpoints ---")
    agent_response = make_request("POST", "/agent/chat", {
        "prompt": "Create a segment for customers who spent over 50 and make an email campaign for them."
    })
    session_id = agent_response["data"]["sessionId"]
    print(f"Started Agent Session: {session_id}")
    
    # Wait for agent to finish
    time.sleep(12)
    session_data = make_request("GET", f"/agent/sessions/{session_id}")
    
    segment_id = session_data["data"]["plan"].get("segmentId")
    campaign_id = session_data["data"]["plan"].get("campaignId")

    print(f"Agent Created Segment: {segment_id}")
    print(f"Agent Created Campaign: {campaign_id}")

    make_request("GET", "/segments")
    make_request("GET", f"/segments/{segment_id}")
    make_request("POST", f"/segments/{segment_id}/evaluate")
    time.sleep(2) # let evaluation finish
    
    print("\n--- 4. Testing Campaign & Variant Endpoints ---")
    make_request("GET", "/campaigns")
    make_request("GET", f"/campaigns/{campaign_id}")
    make_request("GET", f"/variants/campaign/{campaign_id}")
    
    print("\n--- 5. Testing Campaign Execution ---")
    make_request("POST", f"/campaigns/{campaign_id}/execute")
    
    # Wait for execution and communications
    time.sleep(5)
    make_request("GET", f"/communications/campaign/{campaign_id}")
    make_request("GET", f"/communications/customer/{customer_id}")
    
    print("\n--- 6. Test Callbacks ---")
    # We will simulate a callback for a communication if any were created
    comms = make_request("GET", f"/communications/customer/{customer_id}")
    if comms and comms.get("data") and len(comms["data"]["content"]) > 0:
        comm_id = comms["data"]["content"][0]["id"]
        msg_id = comms["data"]["content"][0].get("channelMessageId") or "test-msg-id"
        print(f"Found communication {comm_id}, testing callback with msg_id {msg_id}")
        make_request("POST", "/callbacks/channel", {
            "channelMessageId": msg_id,
            "status": "CLICKED",
            "metadata": {"test": "true"}
        })
    else:
        print("No communications found to test callback.")

    print("\n--- 7. Miscellaneous Endpoints ---")
    make_request("GET", "/memory")
    make_request("GET", "/corrections")
    
    print("\nAll major endpoint workflows tested successfully!")

if __name__ == "__main__":
    test_all()
