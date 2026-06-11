import urllib.request
import urllib.error
import json
import uuid

API_KEY = "likhit@178926a"
BASE_URL = "http://localhost:8080/api/v1"

headers = {
    "X-API-KEY": API_KEY,
    "Content-Type": "application/json"
}

def make_request(endpoint, method="GET", data=None):
    url = f"{BASE_URL}{endpoint}"
    req = urllib.request.Request(url, method=method, headers=headers)
    if data:
        req.data = json.dumps(data).encode("utf-8")
    
    try:
        with urllib.request.urlopen(req) as response:
            res_data = response.read().decode("utf-8")
            if res_data:
                return json.loads(res_data), response.status
            return None, response.status
    except urllib.error.HTTPError as e:
        res_data = e.read().decode("utf-8")
        try:
            return json.loads(res_data), e.code
        except:
            return res_data, e.code
    except Exception as e:
        print(f"Error connecting to {url}: {e}")
        return None, 500

def test_customers():
    print("--- Testing Customers ---")
    mock_email = f"mock_{uuid.uuid4().hex[:8]}@example.com"
    data = {
        "name": "Mock User",
        "email": mock_email,
        "phone": f"+1{uuid.uuid4().hex[:10]}",
        "city": "Test City",
        "tags": ["test"],
        "isGloballyOptedOut": False
    }
    res, status = make_request("/customers", method="POST", data=data)
    print(f"POST /customers: {status} -> {res.get('success') if isinstance(res, dict) else res}")
    
    if status == 201 and res and res.get('data'):
        cust_id = res['data']['id']
        res, status = make_request(f"/customers/{cust_id}", method="GET")
        print(f"GET /customers/{{id}}: {status} -> {res.get('success')}")

        res, status = make_request(f"/customers/{cust_id}/360", method="GET")
        print(f"GET /customers/{{id}}/360: {status} -> {res.get('success')}")
        
        update_data = {"name": "Mock User Updated"}
        res, status = make_request(f"/customers/{cust_id}", method="PUT", data=update_data)
        print(f"PUT /customers/{{id}}: {status} -> {res.get('success')}")
        return cust_id
    else:
        print(f"Failed to create customer: {res}")
        return None

def test_products():
    print("--- Testing Products ---")
    data = {
        "name": "Test Product",
        "description": "Mock Product",
        "productType": "SHIRT",
        "brand": "MockBrand",
        "sku": f"PROD-SKU-{uuid.uuid4().hex[:8]}",
        "price": 199.99,
        "currency": "INR"
    }
    res, status = make_request("/products", method="POST", data=data)
    print(f"POST /products: {status} -> {res.get('success') if isinstance(res, dict) else res}")
    if status == 201 and res and res.get('data'):
        prod_id = res['data']['id']
        res, status = make_request(f"/products/{prod_id}", method="GET")
        print(f"GET /products/{{id}}: {status} -> {res.get('success')}")
        return prod_id
    return None

def test_variants(camp_id):
    if not camp_id: return None
    print("--- Testing Variants ---")
    data = {
        "campaignId": camp_id,
        "channel": "email",
        "copyText": "Test Copy Text",
        "name": "Test Variant"
    }
    res, status = make_request("/variants", method="POST", data=data)
    print(f"POST /variants: {status} -> {res.get('success') if isinstance(res, dict) else res}")
    if status == 201 and res and res.get('data'):
        var_id = res['data']['id']
        res, status = make_request(f"/variants/{var_id}", method="GET")
        print(f"GET /variants/{{id}}: {status} -> {res.get('success')}")
        return var_id
    return None

def test_orders(cust_id, prod_id):
    if not cust_id or not prod_id: return None
    print("--- Testing Orders ---")
    data = {
        "customerId": cust_id,
        "totalAmount": 100.0,
        "currency": "USD",
        "orderStatus": "CONFIRMED",
        "items": [
            {
                "productId": prod_id,
                "productName": "Test Product",
                "quantity": 1,
                "unitPrice": 100.0
            }
        ]
    }
    res, status = make_request("/orders", method="POST", data=data)
    print(f"POST /orders: {status} -> {res.get('success') if isinstance(res, dict) else res}")
    if status == 201 and res and res.get('data'):
        order_id = res['data']['id']
        res, status = make_request(f"/orders/{order_id}", method="GET")
        print(f"GET /orders/{{id}}: {status} -> {res.get('success')}")
        return order_id
    return None

def test_segments():
    print("--- Testing Segments ---")
    data = {
        "name": "Test Segment",
        "description": "Mock segment",
        "filterLogic": "{}",
        "estimatedSize": 10
    }
    res, status = make_request("/segments", method="POST", data=data)
    print(f"POST /segments: {status} -> {res.get('success') if isinstance(res, dict) else res}")
    if status == 201 and res and res.get('data'):
        seg_id = res['data']['id']
        res, status = make_request(f"/segments/{seg_id}", method="GET")
        print(f"GET /segments/{{id}}: {status} -> {res.get('success')}")
        return seg_id
    return None

def test_campaigns(seg_id):
    if not seg_id: return None
    print("--- Testing Campaigns ---")
    data = {
        "name": "Test Campaign",
        "goal": "Test goal",
        "segmentId": seg_id
    }
    res, status = make_request("/campaigns", method="POST", data=data)
    print(f"POST /campaigns: {status} -> {res.get('success') if isinstance(res, dict) else res}")
    if status == 201 and res and res.get('data'):
        camp_id = res['data']['id']
        res, status = make_request(f"/campaigns/{camp_id}", method="GET")
        print(f"GET /campaigns/{{id}}: {status} -> {res.get('success')}")
        return camp_id
    return None

def test_ai_features(camp_id):
    if not camp_id: return
    print("--- Testing AI Features ---")
    
    # 1. Sovereign Agent Chat
    chat_data = {
        "prompt": "Create a campaign for VIP customers"
    }
    res, status = make_request("/agent/chat", method="POST", data=chat_data)
    print(f"POST /agent/chat: {status} -> {res.get('success') if isinstance(res, dict) else res}")

    # 2. Audience Simulator
    sim_data = {
        "campaignId": camp_id,
        "syntheticAudienceSize": 100,
        "personaDistribution": {"vip": 0.5, "new": 0.5}
    }
    res, status = make_request("/simulations", method="POST", data=sim_data)
    print(f"POST /simulations: {status} -> {res.get('success') if isinstance(res, dict) else res}")
    if status == 200 and res and res.get('data'):
        sim_id = res['data']['id']
        res, status = make_request(f"/simulations/{sim_id}", method="GET")
        print(f"GET /simulations/{{id}}: {status} -> {res.get('success')}")

    # 3. Correction Engine (GET only)
    res, status = make_request("/corrections", method="GET")
    print(f"GET /corrections: {status} -> {res.get('success') if isinstance(res, dict) else res}")

def main():
    cust_id = test_customers()
    prod_id = test_products()
    test_orders(cust_id, prod_id)
    
    seg_id = test_segments()
    camp_id = test_campaigns(seg_id)
    test_variants(camp_id)
    
    test_ai_features(camp_id)

if __name__ == "__main__":
    main()
