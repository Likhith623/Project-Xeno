$ErrorActionPreference = "Continue"

Write-Host "=========================================================="
Write-Host "🚀 XENO CRM - COMPREHENSIVE ENDPOINT INTEGRATION TEST 🚀"
Write-Host "=========================================================="

$baseUrl = "http://localhost:8080/api/v1"
$results = @()

function Test-Endpoint {
    param (
        [string]$Name,
        [string]$Method,
        [string]$Url,
        [string]$Body = $null
    )
    
    Write-Host "Testing $Name... " -NoNewline
    try {
        $headers = @{ "X-API-KEY" = "likhit@178926a" }
        if ($Method -eq "GET") {
            $response = Invoke-RestMethod -Uri $Url -Method GET -Headers $headers -ErrorAction Stop
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Body $Body -ContentType "application/json" -Headers $headers -ErrorAction Stop
        }
        Write-Host "[SUCCESS]" -ForegroundColor Green
        $global:results += "$($Name): PASS"
    } catch {
        Write-Host "[FAILED]" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)"
        $global:results += "$($Name): FAIL - $($_.Exception.Message)"
    }
}

# 1. Test Products
Test-Endpoint -Name "Get All Products" -Method "GET" -Url "$baseUrl/products"

# 2. Test Customers
Test-Endpoint -Name "Get All Customers" -Method "GET" -Url "$baseUrl/customers"

# 3. Test Segments
Test-Endpoint -Name "Get All Segments" -Method "GET" -Url "$baseUrl/segments"

# 4. Test Campaigns
Test-Endpoint -Name "Get All Campaigns" -Method "GET" -Url "$baseUrl/campaigns"

# 5. Test AI Proposals
Test-Endpoint -Name "Get AI Campaign Proposals" -Method "GET" -Url "$baseUrl/campaigns/proposals"

# 6. Test Memory
Test-Endpoint -Name "Get AI Memory" -Method "GET" -Url "$baseUrl/memory"

# 7. Create a DUMMY Customer
$customerBody = @"
{
    "name": "Test VIP User",
    "email": "testvip@example.com",
    "phone": "+919000000000",
    "preferredChannel": "EMAIL",
    "tags": ["VIP"]
}
"@
Test-Endpoint -Name "Create Customer" -Method "POST" -Url "$baseUrl/customers" -Body $customerBody

# 8. Create a Segment
$segmentBody = @"
{
    "name": "API Test Segment",
    "description": "Integration Test Segment",
    "type": "DYNAMIC",
    "filterSql": "SELECT id FROM customers WHERE email LIKE '%@example.com'"
}
"@
Test-Endpoint -Name "Create Segment" -Method "POST" -Url "$baseUrl/segments" -Body $segmentBody

# 9. Test The War Room (Multi-Agent Debate)
$warRoomBody = @"
{
    "goal": "Win back churned winter buyers"
}
"@
Test-Endpoint -Name "Trigger The War Room" -Method "POST" -Url "$baseUrl/test/agi/trigger-war-room" -Body $warRoomBody

# 10. Test The Fund Manager
Test-Endpoint -Name "Trigger The Fund Manager" -Method "POST" -Url "$baseUrl/test/agi/trigger-fund-manager"

# 11. Test Omni-Awareness (Sleep Agent & Whisperer)
Test-Endpoint -Name "Trigger Omni-Awareness" -Method "POST" -Url "$baseUrl/test/agi/trigger-omni-awareness"

Write-Host "=========================================================="
Write-Host "TEST EXECUTION COMPLETE"
Write-Host "=========================================================="
$results | Out-File "C:\Users\Sarishma\Project-Xeno\test_results.txt"
