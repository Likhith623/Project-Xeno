$ErrorActionPreference = "Continue"

Write-Host "=========================================================="
Write-Host "🚀 XENO CRM - EXHAUSTIVE 34-ENDPOINT PRODUCTION TEST 🚀"
Write-Host "=========================================================="

$baseUrl = "https://project-xeno.onrender.com/api/v1"
$apiKey = "likhit@178926a"
$headers = @{ "X-API-KEY" = $apiKey; "Content-Type" = "application/json" }

function Test-Endpoint {
    param (
        [string]$Name,
        [string]$Method,
        [string]$Url,
        [string]$Body = $null,
        [switch]$ReturnResponse = $false
    )
    
    Write-Host "Testing $Name... " -NoNewline
    try {
        if ($Method -eq "GET" -or $Method -eq "DELETE") {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers -ErrorAction Stop
        } else {
            if ([string]::IsNullOrEmpty($Body)) { $Body = "{}" }
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Body $Body -Headers $headers -ErrorAction Stop
        }
        Write-Host "[SUCCESS]" -ForegroundColor Green
        if ($ReturnResponse) { return $response }
        return $true
    } catch {
        Write-Host "[FAILED]" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)"
        if ($_.Exception.Response) {
            $stream = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($stream)
            Write-Host "Response Body: $($reader.ReadToEnd())"
        }
        if ($ReturnResponse) { return $null }
        return $false
    }
}

$randomStr = (Get-Random).ToString()

# ---------------------------------------------------------
# PRODUCTS (18-21)
# ---------------------------------------------------------
$productBody = @{
    name = "Test Product $randomStr"
    sku = "SKU-$randomStr"
    description = "A great product"
    price = 99.99
    currency = "USD"
    category = "electronics"
    tags = @("new")
    inventoryCount = 100
} | ConvertTo-Json
$prod = Test-Endpoint -Name "1. POST /products" -Method "POST" -Url "$baseUrl/products" -Body $productBody -ReturnResponse
if ($prod -and $prod.data) {
    $prodId = $prod.data.id
    Test-Endpoint -Name "2. GET /products/{id}" -Method "GET" -Url "$baseUrl/products/$prodId"
}
Test-Endpoint -Name "3. GET /products" -Method "GET" -Url "$baseUrl/products?size=5"
$bulkProdBody = @(
    @{ name="P1-$randomStr"; sku="S1-$randomStr"; price=10; currency="USD"; category="toys"; inventoryCount=10 },
    @{ name="P2-$randomStr"; sku="S2-$randomStr"; price=20; currency="USD"; category="toys"; inventoryCount=20 }
) | ConvertTo-Json
Test-Endpoint -Name "4. POST /products/bulk" -Method "POST" -Url "$baseUrl/products/bulk" -Body $bulkProdBody

# ---------------------------------------------------------
# CUSTOMERS (1-10)
# ---------------------------------------------------------
$customerEmail = "test_$randomStr@example.com"
$customerPhone = "+$randomStr"
$customerBody = @{
    name = "Test User"
    email = $customerEmail
    phone = $customerPhone
    preferredChannel = "EMAIL"
    tags = @("VIP")
} | ConvertTo-Json

$cust = Test-Endpoint -Name "5. POST /customers" -Method "POST" -Url "$baseUrl/customers" -Body $customerBody -ReturnResponse
if ($cust -and $cust.data) {
    $custId = $cust.data.id
    Test-Endpoint -Name "6. GET /customers/{id}" -Method "GET" -Url "$baseUrl/customers/$custId"
    $putBody = @{ name = "Updated Name" } | ConvertTo-Json
    Test-Endpoint -Name "7. PUT /customers/{id}" -Method "PUT" -Url "$baseUrl/customers/$custId" -Body $putBody
    Test-Endpoint -Name "8. GET /customers/{id}/orders" -Method "GET" -Url "$baseUrl/customers/$custId/orders"
    Test-Endpoint -Name "9. GET /customers/{id}/360" -Method "GET" -Url "$baseUrl/customers/$custId/360"
}
Test-Endpoint -Name "10. GET /customers" -Method "GET" -Url "$baseUrl/customers?size=5"
Test-Endpoint -Name "11. GET /customers/by-email" -Method "GET" -Url "$baseUrl/customers/by-email?email=$customerEmail"
Test-Endpoint -Name "12. GET /customers/by-tag" -Method "GET" -Url "$baseUrl/customers/by-tag?tag=VIP"

$bulkCustBody = @(
    @{ name="C1"; email="c1_$randomStr@ex.com"; phone="+$($randomStr)1"; preferredChannel="EMAIL" },
    @{ name="C2"; email="c2_$randomStr@ex.com"; phone="+$($randomStr)2"; preferredChannel="EMAIL" }
) | ConvertTo-Json
Test-Endpoint -Name "13. POST /customers/bulk" -Method "POST" -Url "$baseUrl/customers/bulk" -Body $bulkCustBody


# ---------------------------------------------------------
# SEGMENTS (11-17)
# ---------------------------------------------------------
$segmentBody = @{
    name = "Segment $randomStr"
    description = "Test Segment"
    type = "DYNAMIC"
    filterSql = "SELECT id FROM customers"
} | ConvertTo-Json
$seg = Test-Endpoint -Name "14. POST /segments" -Method "POST" -Url "$baseUrl/segments" -Body $segmentBody -ReturnResponse
if ($seg -and $seg.data) {
    $segId = $seg.data.id
    Test-Endpoint -Name "15. GET /segments/{id}" -Method "GET" -Url "$baseUrl/segments/$segId"
    $patchSeg = @{ description="Updated" } | ConvertTo-Json
    Test-Endpoint -Name "16. PATCH /segments/{id}" -Method "PATCH" -Url "$baseUrl/segments/$segId" -Body $patchSeg
    Test-Endpoint -Name "17. POST /segments/{id}/evaluate" -Method "POST" -Url "$baseUrl/segments/$segId/evaluate"
    Test-Endpoint -Name "18. GET /segments/{id}/members" -Method "GET" -Url "$baseUrl/segments/$segId/members"
}
Test-Endpoint -Name "19. GET /segments" -Method "GET" -Url "$baseUrl/segments?size=5"

# ---------------------------------------------------------
# AGI (AGENT & MEMORY)
# ---------------------------------------------------------
$chatBody = @{ prompt="Create a campaign for VIPs" } | ConvertTo-Json
$sess = Test-Endpoint -Name "20. POST /agent/chat" -Method "POST" -Url "$baseUrl/agent/chat" -Body $chatBody -ReturnResponse
if ($sess -and $sess.data) {
    $sessId = $sess.data.sessionId
    Test-Endpoint -Name "21. GET /agent/sessions/{id}" -Method "GET" -Url "$baseUrl/agent/sessions/$sessId"
}

Test-Endpoint -Name "22. GET /campaigns/proposals" -Method "GET" -Url "$baseUrl/campaigns/proposals"
Test-Endpoint -Name "23. GET /memory" -Method "GET" -Url "$baseUrl/memory"

Test-Endpoint -Name "24. POST /test/agi/trigger-fund-manager" -Method "POST" -Url "$baseUrl/test/agi/trigger-fund-manager"
Test-Endpoint -Name "25. POST /test/agi/trigger-omni-awareness" -Method "POST" -Url "$baseUrl/test/agi/trigger-omni-awareness"
Test-Endpoint -Name "26. POST /test/agi/trigger-war-room" -Method "POST" -Url "$baseUrl/test/agi/trigger-war-room" -Body (@{ goal = "Win back buyers" } | ConvertTo-Json)

# ---------------------------------------------------------
# CLEANUP DELETES
# ---------------------------------------------------------
if ($seg -and $seg.data) { Test-Endpoint -Name "27. DELETE /segments/{id}" -Method "DELETE" -Url "$baseUrl/segments/$segId" }
if ($cust -and $cust.data) { Test-Endpoint -Name "28. DELETE /customers/{id}" -Method "DELETE" -Url "$baseUrl/customers/$custId" }

Write-Host "=========================================================="
Write-Host "TEST EXECUTION COMPLETE"
Write-Host "=========================================================="
