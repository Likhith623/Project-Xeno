# Project Xeno: Exhaustive Frontend Integration Guide

This file contains the **100% rigorous** analysis of every single endpoint and AI feature for frontend implementation.

## 🌍 Base URL & Authentication
**Live Production URL:** `https://project-xeno.onrender.com/api/v1`
**Authentication Header:** `X-API-KEY: <your-api-key>`

---

## Audience Simulator

### `POST /api/v1/simulations`
**Summary:** Trigger a new audience simulation

**Request Payload (`application/json`):**
```json
{
  "campaignId": "123e4567-e89b-12d3-a456-426614174000",
  "syntheticAudienceSize": 0,
  "personaDistribution": {
    "key": {}
  }
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "campaignId": "123e4567-e89b-12d3-a456-426614174000",
    "status": "PENDING",
    "syntheticAudienceSize": 0,
    "predictedOpenRate": 0.0,
    "predictedCtr": 0.0,
    "predictedConversionRate": 0.0,
    "predictedRevenue": 0.0,
    "confidenceIntervalLow": 0.0,
    "confidenceIntervalHigh": 0.0,
    "winningVariantId": "123e4567-e89b-12d3-a456-426614174000",
    "personaDistribution": {
      "key": {}
    },
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/simulations/campaigns/{id}/simulate`
**Summary:** Trigger a simulation for a specific campaign

**Parameters:**
- `id`* (path): `string`

**Request Payload (`application/json`):**
```json
{
  "campaignId": "123e4567-e89b-12d3-a456-426614174000",
  "syntheticAudienceSize": 0,
  "personaDistribution": {
    "key": {}
  }
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "campaignId": "123e4567-e89b-12d3-a456-426614174000",
    "status": "PENDING",
    "syntheticAudienceSize": 0,
    "predictedOpenRate": 0.0,
    "predictedCtr": 0.0,
    "predictedConversionRate": 0.0,
    "predictedRevenue": 0.0,
    "confidenceIntervalLow": 0.0,
    "confidenceIntervalHigh": 0.0,
    "winningVariantId": "123e4567-e89b-12d3-a456-426614174000",
    "personaDistribution": {
      "key": {}
    },
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/simulations/campaigns/{id}/counterfactual`
**Summary:** Run a counterfactual simulation for a campaign

**Parameters:**
- `id`* (path): `string`
- `channel`* (query): `string`
- `counterfactualService`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "channel": "string",
    "predictedOpenRate": 0.0,
    "predictedCtr": 0.0,
    "predictedConversionRate": 0.0,
    "predictedRevenue": 0.0,
    "reasoning": "string"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/simulations/{id}`
**Summary:** Get simulation run result

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "campaignId": "123e4567-e89b-12d3-a456-426614174000",
    "status": "PENDING",
    "syntheticAudienceSize": 0,
    "predictedOpenRate": 0.0,
    "predictedCtr": 0.0,
    "predictedConversionRate": 0.0,
    "predictedRevenue": 0.0,
    "confidenceIntervalLow": 0.0,
    "confidenceIntervalHigh": 0.0,
    "winningVariantId": "123e4567-e89b-12d3-a456-426614174000",
    "personaDistribution": {
      "key": {}
    },
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Audit Logs

### `GET /api/v1/audit-logs/trace/{traceId}`
**Summary:** Get audit logs by trace ID

**Parameters:**
- `traceId`* (path): `string`
- `pageable`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "size": 0,
    "content": [
      {
        "id": 0,
        "traceId": "123e4567-e89b-12d3-a456-426614174000",
        "entityType": "string",
        "entityId": 0,
        "action": "string",
        "actorType": "USER",
        "actorId": "string",
        "oldValue": {
          "key": {}
        },
        "newValue": {
          "key": {}
        },
        "description": "string",
        "createdAt": "2026-06-14T12:00:00Z"
      }
    ],
    "number": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": false,
        "property": "string",
        "ignoreCase": false
      }
    ],
    "last": false,
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": false,
          "property": "string",
          "ignoreCase": false
        }
      ],
      "unpaged": false,
      "paged": false,
      "pageNumber": 0,
      "pageSize": 0
    },
    "first": false,
    "empty": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/audit-logs/entity/{entityType}/{entityId}`
**Summary:** Get audit logs by entity type and ID

**Parameters:**
- `entityType`* (path): `string`
- `entityId`* (path): `string`
- `pageable`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "size": 0,
    "content": [
      {
        "id": 0,
        "traceId": "123e4567-e89b-12d3-a456-426614174000",
        "entityType": "string",
        "entityId": 0,
        "action": "string",
        "actorType": "USER",
        "actorId": "string",
        "oldValue": {
          "key": {}
        },
        "newValue": {
          "key": {}
        },
        "description": "string",
        "createdAt": "2026-06-14T12:00:00Z"
      }
    ],
    "number": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": false,
        "property": "string",
        "ignoreCase": false
      }
    ],
    "last": false,
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": false,
          "property": "string",
          "ignoreCase": false
        }
      ],
      "unpaged": false,
      "paged": false,
      "pageNumber": 0,
      "pageSize": 0
    },
    "first": false,
    "empty": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/audit-logs/actor/{actorId}`
**Summary:** Get audit logs by actor ID

**Parameters:**
- `actorId`* (path): `string`
- `pageable`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "size": 0,
    "content": [
      {
        "id": 0,
        "traceId": "123e4567-e89b-12d3-a456-426614174000",
        "entityType": "string",
        "entityId": 0,
        "action": "string",
        "actorType": "USER",
        "actorId": "string",
        "oldValue": {
          "key": {}
        },
        "newValue": {
          "key": {}
        },
        "description": "string",
        "createdAt": "2026-06-14T12:00:00Z"
      }
    ],
    "number": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": false,
        "property": "string",
        "ignoreCase": false
      }
    ],
    "last": false,
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": false,
          "property": "string",
          "ignoreCase": false
        }
      ],
      "unpaged": false,
      "paged": false,
      "pageNumber": 0,
      "pageSize": 0
    },
    "first": false,
    "empty": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Campaign

### `GET /api/v1/campaigns`
**Summary:** Get all campaigns with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "string",
      "description": "string",
      "status": "DRAFT",
      "goal": "string",
      "segmentId": "123e4567-e89b-12d3-a456-426614174000",
      "segmentName": "string",
      "scheduledAt": "2026-06-14T12:00:00Z",
      "startedAt": "2026-06-14T12:00:00Z",
      "completedAt": "2026-06-14T12:00:00Z",
      "timezone": "string",
      "maxSendCount": 0,
      "optOutRateThreshold": 0.0,
      "createdByAgent": false,
      "agentSessionId": "string",
      "parentCampaignId": "123e4567-e89b-12d3-a456-426614174000",
      "totalSent": 0,
      "totalDelivered": 0,
      "totalFailed": 0,
      "totalOpened": 0,
      "totalRead": 0,
      "totalClicked": 0,
      "totalConverted": 0,
      "revenueAttributed": 0.0,
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/campaigns`
**Summary:** Create a new campaign manually

**Request Payload (`application/json`):**
```json
{
  "name": "string",
  "description": "string",
  "segmentId": "123e4567-e89b-12d3-a456-426614174000",
  "status": "DRAFT",
  "goal": "string",
  "scheduledAt": "2026-06-14T12:00:00Z",
  "timezone": "string",
  "maxSendCount": 0,
  "optOutRateThreshold": 0.0
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "description": "string",
    "status": "DRAFT",
    "goal": "string",
    "segmentId": "123e4567-e89b-12d3-a456-426614174000",
    "segmentName": "string",
    "scheduledAt": "2026-06-14T12:00:00Z",
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z",
    "timezone": "string",
    "maxSendCount": 0,
    "optOutRateThreshold": 0.0,
    "createdByAgent": false,
    "agentSessionId": "string",
    "parentCampaignId": "123e4567-e89b-12d3-a456-426614174000",
    "totalSent": 0,
    "totalDelivered": 0,
    "totalFailed": 0,
    "totalOpened": 0,
    "totalRead": 0,
    "totalClicked": 0,
    "totalConverted": 0,
    "revenueAttributed": 0.0,
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/campaigns/{id}/simulate`
**Summary:** Trigger a simulation for a specific campaign

**Parameters:**
- `id`* (path): `string`

**Request Payload (`application/json`):**
```json
{
  "campaignId": "123e4567-e89b-12d3-a456-426614174000",
  "syntheticAudienceSize": 0,
  "personaDistribution": {
    "key": {}
  }
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "campaignId": "123e4567-e89b-12d3-a456-426614174000",
    "status": "PENDING",
    "syntheticAudienceSize": 0,
    "predictedOpenRate": 0.0,
    "predictedCtr": 0.0,
    "predictedConversionRate": 0.0,
    "predictedRevenue": 0.0,
    "confidenceIntervalLow": 0.0,
    "confidenceIntervalHigh": 0.0,
    "winningVariantId": "123e4567-e89b-12d3-a456-426614174000",
    "personaDistribution": {
      "key": {}
    },
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/campaigns/{id}/execute`
**Summary:** Trigger async execution of a campaign

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {},
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/campaigns/{id}/approve`
**Summary:** Approve an AI proposed campaign and execute it

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "description": "string",
    "status": "DRAFT",
    "goal": "string",
    "segmentId": "123e4567-e89b-12d3-a456-426614174000",
    "segmentName": "string",
    "scheduledAt": "2026-06-14T12:00:00Z",
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z",
    "timezone": "string",
    "maxSendCount": 0,
    "optOutRateThreshold": 0.0,
    "createdByAgent": false,
    "agentSessionId": "string",
    "parentCampaignId": "123e4567-e89b-12d3-a456-426614174000",
    "totalSent": 0,
    "totalDelivered": 0,
    "totalFailed": 0,
    "totalOpened": 0,
    "totalRead": 0,
    "totalClicked": 0,
    "totalConverted": 0,
    "revenueAttributed": 0.0,
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `PATCH /api/v1/campaigns/{id}/status`
**Summary:** Update campaign status (pause, cancel, etc.)

**Parameters:**
- `id`* (path): `string`

**Request Payload (`application/json`):**
```json
{
  "status": "DRAFT"
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "description": "string",
    "status": "DRAFT",
    "goal": "string",
    "segmentId": "123e4567-e89b-12d3-a456-426614174000",
    "segmentName": "string",
    "scheduledAt": "2026-06-14T12:00:00Z",
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z",
    "timezone": "string",
    "maxSendCount": 0,
    "optOutRateThreshold": 0.0,
    "createdByAgent": false,
    "agentSessionId": "string",
    "parentCampaignId": "123e4567-e89b-12d3-a456-426614174000",
    "totalSent": 0,
    "totalDelivered": 0,
    "totalFailed": 0,
    "totalOpened": 0,
    "totalRead": 0,
    "totalClicked": 0,
    "totalConverted": 0,
    "revenueAttributed": 0.0,
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/campaigns/{id}`
**Summary:** Get campaign details and metrics

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "description": "string",
    "status": "DRAFT",
    "goal": "string",
    "segmentId": "123e4567-e89b-12d3-a456-426614174000",
    "segmentName": "string",
    "scheduledAt": "2026-06-14T12:00:00Z",
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z",
    "timezone": "string",
    "maxSendCount": 0,
    "optOutRateThreshold": 0.0,
    "createdByAgent": false,
    "agentSessionId": "string",
    "parentCampaignId": "123e4567-e89b-12d3-a456-426614174000",
    "totalSent": 0,
    "totalDelivered": 0,
    "totalFailed": 0,
    "totalOpened": 0,
    "totalRead": 0,
    "totalClicked": 0,
    "totalConverted": 0,
    "revenueAttributed": 0.0,
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/campaigns/{id}/variants/mab-stats`
**Summary:** Get Thompson Sampling (MAB) statistics for campaign variants

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "variantId": "123e4567-e89b-12d3-a456-426614174000",
      "campaignId": "123e4567-e89b-12d3-a456-426614174000",
      "variantName": "string",
      "channel": "string",
      "mabAlpha": 0.0,
      "mabBeta": 0.0,
      "mabImpressions": 0,
      "mabConversions": 0,
      "expectedConversionRate": 0.0,
      "ciHalfWidth95": 0.0,
      "mabIsActive": false,
      "campaignName": "string"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/campaigns/{id}/timeline`
**Summary:** Get a chronological narrative timeline of the campaign

**Parameters:**
- `id`* (path): `string`
- `timelineService`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    "string"
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/campaigns/{id}/performance`
**Summary:** Get campaign performance metrics including delivery, open, and conversion rates

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "status": "string",
    "goal": "string",
    "scheduledAt": "2026-06-14T12:00:00Z",
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z",
    "createdByAgent": false,
    "totalSent": 0,
    "totalDelivered": 0,
    "totalFailed": 0,
    "totalOpened": 0,
    "totalRead": 0,
    "totalClicked": 0,
    "totalConverted": 0,
    "revenueAttributed": 0.0,
    "deliveryRatePct": 0.0,
    "failureRatePct": 0.0,
    "openRatePct": 0.0,
    "ctrPct": 0.0,
    "conversionRatePct": 0.0,
    "optOutRatePct": 0.0,
    "segmentName": "string",
    "segmentSize": 0
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/campaigns/{id}/corrections`
**Summary:** Get self-correction events for a specific campaign

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "campaignId": "123e4567-e89b-12d3-a456-426614174000",
      "triggerType": "HIGH_FAILURE_RATE",
      "triggerThreshold": 0.0,
      "observedValue": 0.0,
      "cohortSize": 0,
      "actionTaken": "SWITCH_CHANNEL",
      "oldChannel": "email",
      "newChannel": "email",
      "oldVariantId": "123e4567-e89b-12d3-a456-426614174000",
      "newVariantId": "123e4567-e89b-12d3-a456-426614174000",
      "aiReasoning": "string",
      "correctionOutcome": "IMPROVED",
      "outcomeDelta": 0.0,
      "createdAt": "2026-06-14T12:00:00Z",
      "evaluatedAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/campaigns/{id}/analytics/narrative`
**Summary:** Get a natural language narrative analysis of the campaign's performance

**Parameters:**
- `id`* (path): `string`
- `analyticsService`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    "string"
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/campaigns/proposals`
**Summary:** Get all autonomous AI campaign proposals awaiting human approval (Tinder Swipe UI)

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "string",
      "description": "string",
      "status": "DRAFT",
      "goal": "string",
      "segmentId": "123e4567-e89b-12d3-a456-426614174000",
      "segmentName": "string",
      "scheduledAt": "2026-06-14T12:00:00Z",
      "startedAt": "2026-06-14T12:00:00Z",
      "completedAt": "2026-06-14T12:00:00Z",
      "timezone": "string",
      "maxSendCount": 0,
      "optOutRateThreshold": 0.0,
      "createdByAgent": false,
      "agentSessionId": "string",
      "parentCampaignId": "123e4567-e89b-12d3-a456-426614174000",
      "totalSent": 0,
      "totalDelivered": 0,
      "totalFailed": 0,
      "totalOpened": 0,
      "totalRead": 0,
      "totalClicked": 0,
      "totalConverted": 0,
      "revenueAttributed": 0.0,
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/campaigns/opt-out-alerts`
**Summary:** Get opt-out alerts for running campaigns exceeding safety thresholds

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "campaignId": "123e4567-e89b-12d3-a456-426614174000",
      "campaignName": "string",
      "optOutRateThreshold": 0.0,
      "currentOptOutRatePct": 0.0,
      "alertLevel": "string"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Channel Callbacks

### `POST /api/v1/callbacks/channel`
**Summary:** Receive channel callback webhook

**Request Payload (`application/json`):**
```json
{
  "channelMessageId": "string",
  "communicationId": "123e4567-e89b-12d3-a456-426614174000",
  "eventType": "DELIVERED",
  "payload": {
    "key": {}
  }
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {},
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Communication

### `PATCH /api/v1/communications/{id}/status`
**Summary:** Update status of a communication

**Parameters:**
- `id`* (path): `string`
- `status`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {},
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/communications/customer/{customerId}`
**Summary:** Get communications for a customer

**Parameters:**
- `customerId`* (path): `string`
- `pageable`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "campaignId": "123e4567-e89b-12d3-a456-426614174000",
      "variantId": "123e4567-e89b-12d3-a456-426614174000",
      "customerId": "123e4567-e89b-12d3-a456-426614174000",
      "channel": "email",
      "status": "PENDING",
      "channelMessageId": "string",
      "recipientAddress": "string",
      "personalisedSubject": "string",
      "personalisedBody": "string",
      "sentAt": "2026-06-14T12:00:00Z",
      "deliveredAt": "2026-06-14T12:00:00Z",
      "failedAt": "2026-06-14T12:00:00Z",
      "openedAt": "2026-06-14T12:00:00Z",
      "readAt": "2026-06-14T12:00:00Z",
      "clickedAt": "2026-06-14T12:00:00Z",
      "convertedAt": "2026-06-14T12:00:00Z",
      "unsubscribedAt": "2026-06-14T12:00:00Z",
      "failureReason": "string",
      "failureCode": "string",
      "retryCount": 0,
      "nextRetryAt": "2026-06-14T12:00:00Z",
      "attributedOrderId": "123e4567-e89b-12d3-a456-426614174000",
      "attributionWindowHours": 0,
      "spawnedFollowupId": "123e4567-e89b-12d3-a456-426614174000",
      "mabSampleValue": 0.0,
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/communications/campaign/{campaignId}`
**Summary:** Get communications for a campaign

**Parameters:**
- `campaignId`* (path): `string`
- `pageable`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "campaignId": "123e4567-e89b-12d3-a456-426614174000",
      "variantId": "123e4567-e89b-12d3-a456-426614174000",
      "customerId": "123e4567-e89b-12d3-a456-426614174000",
      "channel": "email",
      "status": "PENDING",
      "channelMessageId": "string",
      "recipientAddress": "string",
      "personalisedSubject": "string",
      "personalisedBody": "string",
      "sentAt": "2026-06-14T12:00:00Z",
      "deliveredAt": "2026-06-14T12:00:00Z",
      "failedAt": "2026-06-14T12:00:00Z",
      "openedAt": "2026-06-14T12:00:00Z",
      "readAt": "2026-06-14T12:00:00Z",
      "clickedAt": "2026-06-14T12:00:00Z",
      "convertedAt": "2026-06-14T12:00:00Z",
      "unsubscribedAt": "2026-06-14T12:00:00Z",
      "failureReason": "string",
      "failureCode": "string",
      "retryCount": 0,
      "nextRetryAt": "2026-06-14T12:00:00Z",
      "attributedOrderId": "123e4567-e89b-12d3-a456-426614174000",
      "attributionWindowHours": 0,
      "spawnedFollowupId": "123e4567-e89b-12d3-a456-426614174000",
      "mabSampleValue": 0.0,
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Customer

### `GET /api/v1/customers/{id}`
**Summary:** Get basic customer details

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "externalId": "string",
    "email": "string",
    "phone": "string",
    "whatsappNumber": "string",
    "name": "string",
    "gender": "MALE",
    "dateOfBirth": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "tags": [
      "string"
    ],
    "customAttributes": {
      "key": {}
    },
    "preferredChannel": "EMAIL",
    "optOutChannels": [
      "string"
    ],
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z",
    "globallyOptedOut": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `PUT /api/v1/customers/{id}`
**Summary:** Update an existing customer

**Parameters:**
- `id`* (path): `string`

**Request Payload (`application/json`):**
```json
{
  "email": "string",
  "phone": "string",
  "whatsappNumber": "string",
  "name": "string",
  "gender": "MALE",
  "dateOfBirth": "string",
  "city": "string",
  "state": "string",
  "country": "string",
  "tags": [
    "string"
  ],
  "customAttributes": {
    "key": {}
  },
  "preferredChannel": "EMAIL",
  "optOutChannels": [
    "string"
  ],
  "isGloballyOptedOut": false
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "externalId": "string",
    "email": "string",
    "phone": "string",
    "whatsappNumber": "string",
    "name": "string",
    "gender": "MALE",
    "dateOfBirth": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "tags": [
      "string"
    ],
    "customAttributes": {
      "key": {}
    },
    "preferredChannel": "EMAIL",
    "optOutChannels": [
      "string"
    ],
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z",
    "globallyOptedOut": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `DELETE /api/v1/customers/{id}`
**Summary:** Delete a customer

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {},
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/customers`
**Summary:** Get all customers with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "externalId": "string",
      "email": "string",
      "phone": "string",
      "whatsappNumber": "string",
      "name": "string",
      "gender": "MALE",
      "dateOfBirth": "string",
      "city": "string",
      "state": "string",
      "country": "string",
      "tags": [
        "string"
      ],
      "customAttributes": {
        "key": {}
      },
      "preferredChannel": "EMAIL",
      "optOutChannels": [
        "string"
      ],
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z",
      "globallyOptedOut": false
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/customers`
**Summary:** Create a new customer

**Request Payload (`application/json`):**
```json
{
  "externalId": "string",
  "email": "string",
  "phone": "string",
  "whatsappNumber": "string",
  "name": "string",
  "gender": "MALE",
  "dateOfBirth": "string",
  "city": "string",
  "state": "string",
  "country": "string",
  "tags": [
    "string"
  ],
  "customAttributes": {
    "key": {}
  },
  "preferredChannel": "EMAIL",
  "optOutChannels": [
    "string"
  ],
  "globallyOptedOut": false
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "externalId": "string",
    "email": "string",
    "phone": "string",
    "whatsappNumber": "string",
    "name": "string",
    "gender": "MALE",
    "dateOfBirth": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "tags": [
      "string"
    ],
    "customAttributes": {
      "key": {}
    },
    "preferredChannel": "EMAIL",
    "optOutChannels": [
      "string"
    ],
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z",
    "globallyOptedOut": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/customers/bulk`
**Summary:** Create multiple customers in bulk

**Request Payload (`application/json`):**
```json
[
  {
    "externalId": "string",
    "email": "string",
    "phone": "string",
    "whatsappNumber": "string",
    "name": "string",
    "gender": "MALE",
    "dateOfBirth": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "tags": [
      "string"
    ],
    "customAttributes": {
      "key": {}
    },
    "preferredChannel": "EMAIL",
    "optOutChannels": [
      "string"
    ],
    "globallyOptedOut": false
  }
]
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "externalId": "string",
      "email": "string",
      "phone": "string",
      "whatsappNumber": "string",
      "name": "string",
      "gender": "MALE",
      "dateOfBirth": "string",
      "city": "string",
      "state": "string",
      "country": "string",
      "tags": [
        "string"
      ],
      "customAttributes": {
        "key": {}
      },
      "preferredChannel": "EMAIL",
      "optOutChannels": [
        "string"
      ],
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z",
      "globallyOptedOut": false
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/customers/{id}/orders`
**Summary:** Get customer orders

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "customerId": "123e4567-e89b-12d3-a456-426614174000",
      "orderNumber": "string",
      "status": "PENDING",
      "channel": "string",
      "totalAmount": 0.0,
      "currency": "string",
      "discountAmount": 0.0,
      "couponCode": "string",
      "placedAt": "2026-06-14T12:00:00Z",
      "deliveredAt": "2026-06-14T12:00:00Z",
      "createdAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/customers/{id}/360`
**Summary:** Get customer 360 view including metrics

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "externalId": "string",
    "email": "string",
    "phone": "string",
    "whatsappNumber": "string",
    "name": "string",
    "gender": "MALE",
    "dateOfBirth": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "tags": [
      "string"
    ],
    "customAttributes": {
      "key": {}
    },
    "preferredChannel": "EMAIL",
    "optOutChannels": [
      "string"
    ],
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z",
    "recencyDays": 0,
    "frequency": 0,
    "monetaryTotal": 0.0,
    "monetaryAvgOrder": 0.0,
    "rfmScore": 0.0,
    "totalOrdersLast30d": 0,
    "totalOrdersLast90d": 0,
    "avgDaysBetweenOrders": 0.0,
    "favouriteCategoryId": "123e4567-e89b-12d3-a456-426614174000",
    "favouriteChannel": "string",
    "clvPredicted": 0.0,
    "churnProbability": 0.0,
    "emailOpenRate": 0.0,
    "emailClickRate": 0.0,
    "whatsappReadRate": 0.0,
    "smsClickRate": 0.0,
    "lastComputedAt": "2026-06-14T12:00:00Z",
    "globallyOptedOut": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/customers/by-tag`
**Summary:** Get customers by tag

**Parameters:**
- `tag`* (query): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "externalId": "string",
      "email": "string",
      "phone": "string",
      "whatsappNumber": "string",
      "name": "string",
      "gender": "MALE",
      "dateOfBirth": "string",
      "city": "string",
      "state": "string",
      "country": "string",
      "tags": [
        "string"
      ],
      "customAttributes": {
        "key": {}
      },
      "preferredChannel": "EMAIL",
      "optOutChannels": [
        "string"
      ],
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z",
      "globallyOptedOut": false
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/customers/by-email`
**Summary:** No summary

**Parameters:**
- `email`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "externalId": "string",
    "email": "string",
    "phone": "string",
    "whatsappNumber": "string",
    "name": "string",
    "gender": "MALE",
    "dateOfBirth": "string",
    "city": "string",
    "state": "string",
    "country": "string",
    "tags": [
      "string"
    ],
    "customAttributes": {
      "key": {}
    },
    "preferredChannel": "EMAIL",
    "optOutChannels": [
      "string"
    ],
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z",
    "globallyOptedOut": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Order

### `GET /api/v1/orders`
**Summary:** Get all orders with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "customerId": "123e4567-e89b-12d3-a456-426614174000",
      "orderNumber": "string",
      "status": "PENDING",
      "channel": "string",
      "totalAmount": 0.0,
      "currency": "string",
      "discountAmount": 0.0,
      "couponCode": "string",
      "placedAt": "2026-06-14T12:00:00Z",
      "deliveredAt": "2026-06-14T12:00:00Z",
      "createdAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/orders`
**Summary:** Create a new order

**Request Payload (`application/json`):**
```json
{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "orderNumber": "string",
  "status": "PENDING",
  "channel": "string",
  "totalAmount": 0.0,
  "currency": "string",
  "discountAmount": 0.0,
  "couponCode": "string",
  "placedAt": "2026-06-14T12:00:00Z",
  "metadata": {
    "key": {}
  },
  "items": [
    {
      "productId": "123e4567-e89b-12d3-a456-426614174000",
      "productSku": "string",
      "productName": "string",
      "quantity": 0,
      "unitPrice": 0.0,
      "discountAmount": 0.0,
      "lineTotal": 0.0
    }
  ]
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "customerId": "123e4567-e89b-12d3-a456-426614174000",
    "orderNumber": "string",
    "status": "PENDING",
    "channel": "string",
    "totalAmount": 0.0,
    "currency": "string",
    "discountAmount": 0.0,
    "couponCode": "string",
    "placedAt": "2026-06-14T12:00:00Z",
    "deliveredAt": "2026-06-14T12:00:00Z",
    "createdAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/orders/bulk`
**Summary:** Create multiple orders in bulk

**Request Payload (`application/json`):**
```json
[
  {
    "customerId": "123e4567-e89b-12d3-a456-426614174000",
    "orderNumber": "string",
    "status": "PENDING",
    "channel": "string",
    "totalAmount": 0.0,
    "currency": "string",
    "discountAmount": 0.0,
    "couponCode": "string",
    "placedAt": "2026-06-14T12:00:00Z",
    "metadata": {
      "key": {}
    },
    "items": [
      {
        "productId": "123e4567-e89b-12d3-a456-426614174000",
        "productSku": "string",
        "productName": "string",
        "quantity": 0,
        "unitPrice": 0.0,
        "discountAmount": 0.0,
        "lineTotal": 0.0
      }
    ]
  }
]
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "customerId": "123e4567-e89b-12d3-a456-426614174000",
      "orderNumber": "string",
      "status": "PENDING",
      "channel": "string",
      "totalAmount": 0.0,
      "currency": "string",
      "discountAmount": 0.0,
      "couponCode": "string",
      "placedAt": "2026-06-14T12:00:00Z",
      "deliveredAt": "2026-06-14T12:00:00Z",
      "createdAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/orders/{id}`
**Summary:** Get order details

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "customerId": "123e4567-e89b-12d3-a456-426614174000",
    "orderNumber": "string",
    "status": "PENDING",
    "channel": "string",
    "totalAmount": 0.0,
    "currency": "string",
    "discountAmount": 0.0,
    "couponCode": "string",
    "placedAt": "2026-06-14T12:00:00Z",
    "deliveredAt": "2026-06-14T12:00:00Z",
    "createdAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Organizational Memory

### `GET /api/v1/memory`
**Summary:** Get all organizational memory entries

**Parameters:**
- `pageable`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "size": 0,
    "content": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "segmentTag": "string",
        "channel": "email",
        "learningType": "COPY_STYLE",
        "learningSummary": "string",
        "confidence": 0.0,
        "evidenceCount": 0,
        "avgLift": 0.0,
        "winningCopySignals": {
          "key": {}
        },
        "dayOfWeek": 0,
        "timeOfDay": "MORNING",
        "sourceCampaignIds": [
          "123e4567-e89b-12d3-a456-426614174000"
        ],
        "expiresAt": "2026-06-14T12:00:00Z",
        "createdAt": "2026-06-14T12:00:00Z",
        "updatedAt": "2026-06-14T12:00:00Z",
        "active": false
      }
    ],
    "number": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": false,
        "property": "string",
        "ignoreCase": false
      }
    ],
    "last": false,
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": false,
          "property": "string",
          "ignoreCase": false
        }
      ],
      "unpaged": false,
      "paged": false,
      "pageNumber": 0,
      "pageSize": 0
    },
    "first": false,
    "empty": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/memory/query`
**Summary:** Query organizational memory by segment tag and channel

**Parameters:**
- `segmentTag` (query): `string`
- `channel` (query): `string`
- `pageable`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "size": 0,
    "content": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "segmentTag": "string",
        "channel": "email",
        "learningType": "COPY_STYLE",
        "learningSummary": "string",
        "confidence": 0.0,
        "evidenceCount": 0,
        "avgLift": 0.0,
        "winningCopySignals": {
          "key": {}
        },
        "dayOfWeek": 0,
        "timeOfDay": "MORNING",
        "sourceCampaignIds": [
          "123e4567-e89b-12d3-a456-426614174000"
        ],
        "expiresAt": "2026-06-14T12:00:00Z",
        "createdAt": "2026-06-14T12:00:00Z",
        "updatedAt": "2026-06-14T12:00:00Z",
        "active": false
      }
    ],
    "number": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": false,
        "property": "string",
        "ignoreCase": false
      }
    ],
    "last": false,
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": false,
          "property": "string",
          "ignoreCase": false
        }
      ],
      "unpaged": false,
      "paged": false,
      "pageNumber": 0,
      "pageSize": 0
    },
    "first": false,
    "empty": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/memory/ask`
**Summary:** Ask the organizational memory a question using natural language

**Parameters:**
- `query`* (query): `string`
- `llmGatewayService`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": "string",
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Product

### `GET /api/v1/products`
**Summary:** Get all products with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "sku": "string",
      "name": "string",
      "categoryId": "123e4567-e89b-12d3-a456-426614174000",
      "categoryName": "string",
      "price": 0.0,
      "currency": "string",
      "brand": "string",
      "tags": [
        "string"
      ],
      "attributes": {
        "key": {}
      },
      "createdAt": "2026-06-14T12:00:00Z",
      "active": false
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/products`
**Summary:** Create a new product

**Request Payload (`application/json`):**
```json
{
  "sku": "string",
  "name": "string",
  "categoryId": "123e4567-e89b-12d3-a456-426614174000",
  "price": 0.0,
  "currency": "string",
  "brand": "string",
  "tags": [
    "string"
  ],
  "attributes": {
    "key": {}
  },
  "active": false
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "sku": "string",
    "name": "string",
    "categoryId": "123e4567-e89b-12d3-a456-426614174000",
    "categoryName": "string",
    "price": 0.0,
    "currency": "string",
    "brand": "string",
    "tags": [
      "string"
    ],
    "attributes": {
      "key": {}
    },
    "createdAt": "2026-06-14T12:00:00Z",
    "active": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/products/bulk`
**Summary:** Create multiple products in bulk

**Request Payload (`application/json`):**
```json
[
  {
    "sku": "string",
    "name": "string",
    "categoryId": "123e4567-e89b-12d3-a456-426614174000",
    "price": 0.0,
    "currency": "string",
    "brand": "string",
    "tags": [
      "string"
    ],
    "attributes": {
      "key": {}
    },
    "active": false
  }
]
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "sku": "string",
      "name": "string",
      "categoryId": "123e4567-e89b-12d3-a456-426614174000",
      "categoryName": "string",
      "price": 0.0,
      "currency": "string",
      "brand": "string",
      "tags": [
        "string"
      ],
      "attributes": {
        "key": {}
      },
      "createdAt": "2026-06-14T12:00:00Z",
      "active": false
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/products/{id}`
**Summary:** Get product details

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "sku": "string",
    "name": "string",
    "categoryId": "123e4567-e89b-12d3-a456-426614174000",
    "categoryName": "string",
    "price": 0.0,
    "currency": "string",
    "brand": "string",
    "tags": [
      "string"
    ],
    "attributes": {
      "key": {}
    },
    "createdAt": "2026-06-14T12:00:00Z",
    "active": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/products/categories`
**Summary:** Get all product categories

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "externalId": "string",
      "name": "string",
      "description": "string",
      "parentCategoryId": "123e4567-e89b-12d3-a456-426614174000",
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Segment

### `GET /api/v1/segments`
**Summary:** Get all segments with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "name": "string",
      "description": "string",
      "type": "STATIC",
      "status": "DRAFT",
      "filterSql": "string",
      "filterJson": {
        "key": {}
      },
      "createdByAgent": false,
      "agentGoal": "string",
      "customerCount": 0,
      "lastEvaluatedAt": "2026-06-14T12:00:00Z",
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z",
      "pinned": false
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/segments`
**Summary:** Create a new segment

**Request Payload (`application/json`):**
```json
{
  "name": "string",
  "description": "string",
  "type": "STATIC",
  "status": "DRAFT",
  "filterSql": "string",
  "filterJson": {
    "key": {}
  },
  "isPinned": false,
  "createdByAgent": false,
  "agentGoal": "string"
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "description": "string",
    "type": "STATIC",
    "status": "DRAFT",
    "filterSql": "string",
    "filterJson": {
      "key": {}
    },
    "createdByAgent": false,
    "agentGoal": "string",
    "customerCount": 0,
    "lastEvaluatedAt": "2026-06-14T12:00:00Z",
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z",
    "pinned": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `POST /api/v1/segments/{id}/evaluate`
**Summary:** Trigger async evaluation of a segment

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {},
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/segments/{id}`
**Summary:** Get segment details

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "description": "string",
    "type": "STATIC",
    "status": "DRAFT",
    "filterSql": "string",
    "filterJson": {
      "key": {}
    },
    "createdByAgent": false,
    "agentGoal": "string",
    "customerCount": 0,
    "lastEvaluatedAt": "2026-06-14T12:00:00Z",
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z",
    "pinned": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `DELETE /api/v1/segments/{id}`
**Summary:** Delete a segment

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {},
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `PATCH /api/v1/segments/{id}`
**Summary:** Update segment details

**Parameters:**
- `id`* (path): `string`

**Request Payload (`application/json`):**
```json
{
  "name": "string",
  "description": "string",
  "filterSql": "string",
  "filterJson": {
    "key": {}
  },
  "isPinned": false
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "description": "string",
    "type": "STATIC",
    "status": "DRAFT",
    "filterSql": "string",
    "filterJson": {
      "key": {}
    },
    "createdByAgent": false,
    "agentGoal": "string",
    "customerCount": 0,
    "lastEvaluatedAt": "2026-06-14T12:00:00Z",
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z",
    "pinned": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/segments/{id}/persona`
**Summary:** Generate an AI persona for this segment

**Parameters:**
- `id`* (path): `string`
- `personaGenerationService`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "key": {}
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/segments/{id}/members`
**Summary:** Get paginated list of customer IDs in a segment

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "segmentId": "123e4567-e89b-12d3-a456-426614174000",
      "customerId": "123e4567-e89b-12d3-a456-426614174000",
      "addedAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Self-Correction Engine

### `GET /api/v1/corrections`
**Summary:** Get all correction events

**Parameters:**
- `pageable`* (query): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "totalElements": 0,
    "totalPages": 0,
    "size": 0,
    "content": [
      {
        "id": "123e4567-e89b-12d3-a456-426614174000",
        "campaignId": "123e4567-e89b-12d3-a456-426614174000",
        "triggerType": "HIGH_FAILURE_RATE",
        "triggerThreshold": 0.0,
        "observedValue": 0.0,
        "cohortSize": 0,
        "actionTaken": "SWITCH_CHANNEL",
        "oldChannel": "email",
        "newChannel": "email",
        "oldVariantId": "123e4567-e89b-12d3-a456-426614174000",
        "newVariantId": "123e4567-e89b-12d3-a456-426614174000",
        "aiReasoning": "string",
        "correctionOutcome": "IMPROVED",
        "outcomeDelta": 0.0,
        "createdAt": "2026-06-14T12:00:00Z",
        "evaluatedAt": "2026-06-14T12:00:00Z"
      }
    ],
    "number": 0,
    "sort": [
      {
        "direction": "string",
        "nullHandling": "string",
        "ascending": false,
        "property": "string",
        "ignoreCase": false
      }
    ],
    "last": false,
    "numberOfElements": 0,
    "pageable": {
      "offset": 0,
      "sort": [
        {
          "direction": "string",
          "nullHandling": "string",
          "ascending": false,
          "property": "string",
          "ignoreCase": false
        }
      ],
      "unpaged": false,
      "paged": false,
      "pageNumber": 0,
      "pageSize": 0
    },
    "first": false,
    "empty": false
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Sovereign Agent

### `POST /api/v1/agent/chat`
**Summary:** Send a prompt to the sovereign agent

**Request Payload (`application/json`):**
```json
{
  "prompt": "string",
  "sessionId": "123e4567-e89b-12d3-a456-426614174000"
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "sessionId": "123e4567-e89b-12d3-a456-426614174000",
    "textReply": "string",
    "actionTaken": "SEGMENT_QUERY"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/agent/sessions/{id}`
**Summary:** Poll the status and plan of a sovereign agent session

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "goal": "string",
    "status": "RUNNING",
    "modelUsed": "string",
    "plan": {
      "key": {}
    },
    "createdSegmentId": "123e4567-e89b-12d3-a456-426614174000",
    "createdCampaignId": "123e4567-e89b-12d3-a456-426614174000",
    "errorMessage": "string",
    "startedAt": "2026-06-14T12:00:00Z",
    "completedAt": "2026-06-14T12:00:00Z",
    "tokensUsedIn": 0,
    "tokensUsedOut": 0,
    "conversationLog": [
      {
        "key": {}
      }
    ]
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/agent/sessions/{id}/decisions`
**Summary:** Get the full ReAct reasoning chain (decision audit trail) for a session

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "sessionId": "123e4567-e89b-12d3-a456-426614174000",
      "stepNumber": 0,
      "decisionType": "SEGMENT_QUERY",
      "inputContext": {
        "key": {}
      },
      "outputAction": {
        "key": {}
      },
      "reasoning": "string",
      "createdAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## Variant

### `POST /api/v1/variants`
**Summary:** Create a new variant for a campaign

**Request Payload (`application/json`):**
```json
{
  "campaignId": "123e4567-e89b-12d3-a456-426614174000",
  "name": "string",
  "channel": "email",
  "subjectLine": "string",
  "previewText": "string",
  "bodyText": "string",
  "bodyHtml": "string",
  "ctaText": "string",
  "ctaUrl": "string",
  "mediaUrl": "string",
  "templateId": "string",
  "templateParams": {
    "key": {}
  },
  "generatedByAi": false,
  "generationPrompt": "string"
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "campaignId": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "channel": "email",
    "subjectLine": "string",
    "previewText": "string",
    "bodyText": "string",
    "bodyHtml": "string",
    "ctaText": "string",
    "ctaUrl": "string",
    "mediaUrl": "string",
    "templateId": "string",
    "templateParams": {
      "key": {}
    },
    "mabAlpha": 0.0,
    "mabBeta": 0.0,
    "mabImpressions": 0,
    "mabConversions": 0,
    "mabIsActive": false,
    "generatedByAi": false,
    "generationPrompt": "string",
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/variants/{id}`
**Summary:** Get variant by ID

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "campaignId": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "channel": "email",
    "subjectLine": "string",
    "previewText": "string",
    "bodyText": "string",
    "bodyHtml": "string",
    "ctaText": "string",
    "ctaUrl": "string",
    "mediaUrl": "string",
    "templateId": "string",
    "templateParams": {
      "key": {}
    },
    "mabAlpha": 0.0,
    "mabBeta": 0.0,
    "mabImpressions": 0,
    "mabConversions": 0,
    "mabIsActive": false,
    "generatedByAi": false,
    "generationPrompt": "string",
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `DELETE /api/v1/variants/{id}`
**Summary:** Soft delete variant

**Parameters:**
- `id`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {},
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `PATCH /api/v1/variants/{id}`
**Summary:** Update variant details

**Parameters:**
- `id`* (path): `string`

**Request Payload (`application/json`):**
```json
{
  "name": "string",
  "channel": "string",
  "subjectLine": "string",
  "previewText": "string",
  "bodyText": "string",
  "bodyHtml": "string",
  "ctaText": "string",
  "ctaUrl": "string",
  "mediaUrl": "string",
  "templateId": "string",
  "templateParams": {
    "key": {}
  },
  "mabIsActive": false
}
```

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "campaignId": "123e4567-e89b-12d3-a456-426614174000",
    "name": "string",
    "channel": "email",
    "subjectLine": "string",
    "previewText": "string",
    "bodyText": "string",
    "bodyHtml": "string",
    "ctaText": "string",
    "ctaUrl": "string",
    "mediaUrl": "string",
    "templateId": "string",
    "templateParams": {
      "key": {}
    },
    "mabAlpha": 0.0,
    "mabBeta": 0.0,
    "mabImpressions": 0,
    "mabConversions": 0,
    "mabIsActive": false,
    "generatedByAi": false,
    "generationPrompt": "string",
    "createdAt": "2026-06-14T12:00:00Z",
    "updatedAt": "2026-06-14T12:00:00Z"
  },
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/variants/{campaignId}/mab-stats`
**Summary:** Get Thompson Sampling (MAB) statistics for campaign variants

**Parameters:**
- `campaignId`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "variantId": "123e4567-e89b-12d3-a456-426614174000",
      "campaignId": "123e4567-e89b-12d3-a456-426614174000",
      "variantName": "string",
      "channel": "string",
      "mabAlpha": 0.0,
      "mabBeta": 0.0,
      "mabImpressions": 0,
      "mabConversions": 0,
      "expectedConversionRate": 0.0,
      "ciHalfWidth95": 0.0,
      "mabIsActive": false,
      "campaignName": "string"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

### `GET /api/v1/variants/campaign/{campaignId}`
**Summary:** Get all variants for a campaign

**Parameters:**
- `campaignId`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
{
  "success": false,
  "data": [
    {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "campaignId": "123e4567-e89b-12d3-a456-426614174000",
      "name": "string",
      "channel": "email",
      "subjectLine": "string",
      "previewText": "string",
      "bodyText": "string",
      "bodyHtml": "string",
      "ctaText": "string",
      "ctaUrl": "string",
      "mediaUrl": "string",
      "templateId": "string",
      "templateParams": {
        "key": {}
      },
      "mabAlpha": 0.0,
      "mabBeta": 0.0,
      "mabImpressions": 0,
      "mabConversions": 0,
      "mabIsActive": false,
      "generatedByAi": false,
      "generationPrompt": "string",
      "createdAt": "2026-06-14T12:00:00Z",
      "updatedAt": "2026-06-14T12:00:00Z"
    }
  ],
  "message": "string",
  "errorCode": "string",
  "errorMessage": "string",
  "pagination": {
    "pageNumber": 0,
    "pageSize": 0,
    "totalElements": 0,
    "totalPages": 0,
    "last": false
  }
}
```

---

## agent-decision-controller

### `GET /api/v1/agent/sessions/{sessionId}/decisions`
**Summary:** No summary

**Parameters:**
- `sessionId`* (path): `string`

**Response:**
**`200 OK` Payload:**
```json
[
  {
    "id": "123e4567-e89b-12d3-a456-426614174000",
    "session": {
      "id": "123e4567-e89b-12d3-a456-426614174000",
      "goal": "string",
      "status": "RUNNING",
      "modelUsed": "string",
      "conversationLog": [
        {
          "key": {}
        }
      ],
      "plan": {
        "key": {}
      },
      "createdSegmentId": "123e4567-e89b-12d3-a456-426614174000",
      "createdCampaignId": "123e4567-e89b-12d3-a456-426614174000",
      "errorMessage": "string",
      "startedAt": "2026-06-14T12:00:00Z",
      "completedAt": "2026-06-14T12:00:00Z",
      "tokensUsedIn": 0,
      "tokensUsedOut": 0
    },
    "stepNumber": 0,
    "decisionType": "SEGMENT_QUERY",
    "inputContext": {
      "key": {}
    },
    "outputAction": {
      "key": {}
    },
    "reasoning": "string",
    "createdAt": "2026-06-14T12:00:00Z"
  }
]
```

---

## ai-testing-controller

### `POST /api/v1/test/agi/trigger-war-room`
**Summary:** No summary

**Request Payload (`application/json`):**
```json
{
  "key": "string"
}
```

**Response:**
**`200 OK` Payload:**
```json
"string"
```

---

### `POST /api/v1/test/agi/trigger-omni-awareness`
**Summary:** No summary

**Response:**
**`200 OK` Payload:**
```json
"string"
```

---

### `POST /api/v1/test/agi/trigger-fund-manager`
**Summary:** No summary

**Response:**
**`200 OK` Payload:**
```json
"string"
```

---

# 🧠 Core AI Features & Frontend Integration Logic

## 1. The Sovereign AI Agent
- **Endpoint:** `POST /api/v1/agent/chat` -> Returns `sessionId`.
- **Polling:** Use `GET /api/v1/agent/sessions/{sessionId}` to poll until `status == COMPLETED`.
- **Decisions Audit:** Use `GET /api/v1/agent/sessions/{sessionId}/decisions` to show the Agent's reasoning.

## 2. Multi-Armed Bandit (MAB) Dashboards
- **Endpoint:** `GET /api/v1/campaigns/{id}/variants/mab-stats`
- **UI:** Display real-time variant performance, impressions, and shifting traffic weights.

## 3. Autonomous Campaign Proposals (Tinder-UI)
- **Endpoint:** `GET /api/v1/campaigns/proposals`
- **UI:** Show cards of AI-generated campaigns. Click "Approve" to call `POST /api/v1/campaigns/{id}/approve`.

## 4. Omni-Awareness Fatigue (Sleep Agent)
- **UI:** Automatically enforced by the backend (`channel_cooldown_until`). Display suppressed users in campaign stats.

## 5. Organizational Memory
- **Endpoint:** `GET /api/v1/memory`
- **UI:** Show actionable insights learned from past campaigns.

## 6. AGI Testing Panel
- **Endpoints:** `POST /api/v1/test/agi/*`
- **UI:** Create buttons to trigger War Room, Fund Manager, and Omni-Awareness background jobs instantly.

