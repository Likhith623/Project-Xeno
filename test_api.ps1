$ErrorActionPreference = "Continue"

Write-Host "=========================================================="
Write-Host "🚀 XENO CRM - COMPREHENSIVE E2E PRODUCTION INTEGRATION TEST 🚀"
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

# ---------------------------------------------------------
# 1. CUSTOMER LIFECYCLE
# ---------------------------------------------------------
$randomStr = (Get-Random).ToString()
$customerEmail = "testvip_$randomStr@example.com"
$customerBody = @{
    name = "Test VIP User"
    email = $customerEmail
    phone = "+$randomStr"
    preferredChannel = "EMAIL"
    tags = @("VIP")
} | ConvertTo-Json

$cust = Test-Endpoint -Name "POST /customers" -Method "POST" -Url "$baseUrl/customers" -Body $customerBody -ReturnResponse
if ($cust -and $cust.data) {
    $custId = $cust.data.id
    Test-Endpoint -Name "GET /customers/{id}" -Method "GET" -Url "$baseUrl/customers/$custId"
    Test-Endpoint -Name "GET /customers/{id}/360" -Method "GET" -Url "$baseUrl/customers/$custId/360"
    Test-Endpoint -Name "GET /customers/by-email" -Method "GET" -Url "$baseUrl/customers/by-email?email=$customerEmail"
}

Test-Endpoint -Name "GET /customers (Paginated)" -Method "GET" -Url "$baseUrl/customers?size=5"

# ---------------------------------------------------------
# 2. PRODUCT LIFECYCLE
# ---------------------------------------------------------
Test-Endpoint -Name "GET /products" -Method "GET" -Url "$baseUrl/products?size=5"
Test-Endpoint -Name "GET /products/categories" -Method "GET" -Url "$baseUrl/products/categories"

# ---------------------------------------------------------
# 3. AUDIENCE SEGMENTS
# ---------------------------------------------------------
$segmentBody = @{
    name = "API Test Segment $randomStr"
    description = "Integration Test Segment"
    type = "DYNAMIC"
    filterSql = "SELECT id FROM customers WHERE email LIKE '%@example.com'"
} | ConvertTo-Json

$seg = Test-Endpoint -Name "POST /segments" -Method "POST" -Url "$baseUrl/segments" -Body $segmentBody -ReturnResponse
if ($seg -and $seg.data) {
    $segId = $seg.data.id
    Test-Endpoint -Name "GET /segments/{id}" -Method "GET" -Url "$baseUrl/segments/$segId"
}

Test-Endpoint -Name "GET /segments" -Method "GET" -Url "$baseUrl/segments?size=5"

# ---------------------------------------------------------
# 4. CAMPAIGNS & VARIANTS
# ---------------------------------------------------------
Test-Endpoint -Name "GET /campaigns" -Method "GET" -Url "$baseUrl/campaigns?size=5"
Test-Endpoint -Name "GET /campaigns/proposals" -Method "GET" -Url "$baseUrl/campaigns/proposals"

# ---------------------------------------------------------
# 5. AGI & MEMORY
# ---------------------------------------------------------
Test-Endpoint -Name "GET /memory" -Method "GET" -Url "$baseUrl/memory"

# Trigger AGI tests
$warRoomBody = @{ goal = "Win back churned winter buyers" } | ConvertTo-Json
Test-Endpoint -Name "POST /test/agi/trigger-war-room" -Method "POST" -Url "$baseUrl/test/agi/trigger-war-room" -Body $warRoomBody
Test-Endpoint -Name "POST /test/agi/trigger-fund-manager" -Method "POST" -Url "$baseUrl/test/agi/trigger-fund-manager" -Body "{}"
Test-Endpoint -Name "POST /test/agi/trigger-omni-awareness" -Method "POST" -Url "$baseUrl/test/agi/trigger-omni-awareness" -Body "{}"

Write-Host "=========================================================="
Write-Host "TEST EXECUTION COMPLETE"
Write-Host "=========================================================="
