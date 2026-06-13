# Xeno CRM - Frontend Integration Guide

This document outlines every single endpoint available in the Xeno CRM backend along with its exact input fields, output fields, and paths.

## Customer

### `GET /api/v1/customers/{id}`
**Summary:** Get basic customer details

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    externalId: string,
    email: string,
    phone: string,
    whatsappNumber: string,
    name: string,
    gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
    dateOfBirth: string,
    city: string,
    state: string,
    country: string,
    tags: Array<string>,
    customAttributes: Record<string, Record<string, any>>,
    preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
    optOutChannels: Array<string>,
    createdAt: string (date-time),
    updatedAt: string (date-time),
    globallyOptedOut: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `PUT /api/v1/customers/{id}`
**Summary:** Update an existing customer

**Parameters:**
- `id`* (path): `string`

**Request Body (`application/json`):**
```typescript
{
  email: string,
  phone: string,
  whatsappNumber: string,
  name: string,
  gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
  dateOfBirth: string,
  city: string,
  state: string,
  country: string,
  tags: Array<string>,
  customAttributes: Record<string, Record<string, any>>,
  preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
  optOutChannels: Array<string>,
  isGloballyOptedOut: boolean,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    externalId: string,
    email: string,
    phone: string,
    whatsappNumber: string,
    name: string,
    gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
    dateOfBirth: string,
    city: string,
    state: string,
    country: string,
    tags: Array<string>,
    customAttributes: Record<string, Record<string, any>>,
    preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
    optOutChannels: Array<string>,
    createdAt: string (date-time),
    updatedAt: string (date-time),
    globallyOptedOut: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `DELETE /api/v1/customers/{id}`
**Summary:** Delete a customer

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Record<string, any>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/customers`
**Summary:** Get all customers with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    externalId: string,
    email: string,
    phone: string,
    whatsappNumber: string,
    name: string,
    gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
    dateOfBirth: string,
    city: string,
    state: string,
    country: string,
    tags: Array<string>,
    customAttributes: Record<string, Record<string, any>>,
    preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
    optOutChannels: Array<string>,
    createdAt: string (date-time),
    updatedAt: string (date-time),
    globallyOptedOut: boolean,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/customers`
**Summary:** Create a new customer

**Request Body (`application/json`):**
```typescript
{
  externalId: string,
  email: string,
  phone: string,
  whatsappNumber: string,
  name: string,
  gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
  dateOfBirth: string,
  city: string,
  state: string,
  country: string,
  tags: Array<string>,
  customAttributes: Record<string, Record<string, any>>,
  preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
  optOutChannels: Array<string>,
  globallyOptedOut: boolean,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    externalId: string,
    email: string,
    phone: string,
    whatsappNumber: string,
    name: string,
    gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
    dateOfBirth: string,
    city: string,
    state: string,
    country: string,
    tags: Array<string>,
    customAttributes: Record<string, Record<string, any>>,
    preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
    optOutChannels: Array<string>,
    createdAt: string (date-time),
    updatedAt: string (date-time),
    globallyOptedOut: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/customers/bulk`
**Summary:** Create multiple customers in bulk

**Request Body (`application/json`):**
```typescript
Array<{
  externalId: string,
  email: string,
  phone: string,
  whatsappNumber: string,
  name: string,
  gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
  dateOfBirth: string,
  city: string,
  state: string,
  country: string,
  tags: Array<string>,
  customAttributes: Record<string, Record<string, any>>,
  preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
  optOutChannels: Array<string>,
  globallyOptedOut: boolean,
}>
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    externalId: string,
    email: string,
    phone: string,
    whatsappNumber: string,
    name: string,
    gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
    dateOfBirth: string,
    city: string,
    state: string,
    country: string,
    tags: Array<string>,
    customAttributes: Record<string, Record<string, any>>,
    preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
    optOutChannels: Array<string>,
    createdAt: string (date-time),
    updatedAt: string (date-time),
    globallyOptedOut: boolean,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/customers/{id}/orders`
**Summary:** Get customer orders

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    customerId: string (uuid),
    orderNumber: string,
    status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED',
    channel: string,
    totalAmount: number,
    currency: string,
    discountAmount: number,
    couponCode: string,
    placedAt: string (date-time),
    deliveredAt: string (date-time),
    createdAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/customers/{id}/360`
**Summary:** Get customer 360 view including metrics

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    externalId: string,
    email: string,
    phone: string,
    whatsappNumber: string,
    name: string,
    gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
    dateOfBirth: string,
    city: string,
    state: string,
    country: string,
    tags: Array<string>,
    customAttributes: Record<string, Record<string, any>>,
    preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
    optOutChannels: Array<string>,
    createdAt: string (date-time),
    updatedAt: string (date-time),
    recencyDays: number,
    frequency: number,
    monetaryTotal: number,
    monetaryAvgOrder: number,
    rfmScore: number,
    totalOrdersLast30d: number,
    totalOrdersLast90d: number,
    avgDaysBetweenOrders: number,
    favouriteCategoryId: string (uuid),
    favouriteChannel: string,
    clvPredicted: number,
    churnProbability: number,
    emailOpenRate: number,
    emailClickRate: number,
    whatsappReadRate: number,
    smsClickRate: number,
    lastComputedAt: string (date-time),
    globallyOptedOut: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/customers/by-tag`
**Summary:** Get customers by tag

**Parameters:**
- `tag`* (query): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    externalId: string,
    email: string,
    phone: string,
    whatsappNumber: string,
    name: string,
    gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
    dateOfBirth: string,
    city: string,
    state: string,
    country: string,
    tags: Array<string>,
    customAttributes: Record<string, Record<string, any>>,
    preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
    optOutChannels: Array<string>,
    createdAt: string (date-time),
    updatedAt: string (date-time),
    globallyOptedOut: boolean,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/customers/by-email`
**Summary:** No summary provided.

**Parameters:**
- `email`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    externalId: string,
    email: string,
    phone: string,
    whatsappNumber: string,
    name: string,
    gender: 'MALE' | 'FEMALE' | 'OTHER' | 'UNKNOWN',
    dateOfBirth: string,
    city: string,
    state: string,
    country: string,
    tags: Array<string>,
    customAttributes: Record<string, Record<string, any>>,
    preferredChannel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS',
    optOutChannels: Array<string>,
    createdAt: string (date-time),
    updatedAt: string (date-time),
    globallyOptedOut: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Variant

### `POST /api/v1/variants`
**Summary:** Create a new variant for a campaign

**Request Body (`application/json`):**
```typescript
{
  campaignId: string (uuid),
  name: string,
  channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
  subjectLine: string,
  previewText: string,
  bodyText: string,
  bodyHtml: string,
  ctaText: string,
  ctaUrl: string,
  mediaUrl: string,
  templateId: string,
  templateParams: Record<string, Record<string, any>>,
  generatedByAi: boolean,
  generationPrompt: string,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    campaignId: string (uuid),
    name: string,
    channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
    subjectLine: string,
    previewText: string,
    bodyText: string,
    bodyHtml: string,
    ctaText: string,
    ctaUrl: string,
    mediaUrl: string,
    templateId: string,
    templateParams: Record<string, Record<string, any>>,
    mabAlpha: number,
    mabBeta: number,
    mabImpressions: number,
    mabConversions: number,
    mabIsActive: boolean,
    generatedByAi: boolean,
    generationPrompt: string,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/variants/{id}`
**Summary:** Get variant by ID

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    campaignId: string (uuid),
    name: string,
    channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
    subjectLine: string,
    previewText: string,
    bodyText: string,
    bodyHtml: string,
    ctaText: string,
    ctaUrl: string,
    mediaUrl: string,
    templateId: string,
    templateParams: Record<string, Record<string, any>>,
    mabAlpha: number,
    mabBeta: number,
    mabImpressions: number,
    mabConversions: number,
    mabIsActive: boolean,
    generatedByAi: boolean,
    generationPrompt: string,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `DELETE /api/v1/variants/{id}`
**Summary:** Soft delete variant

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Record<string, any>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `PATCH /api/v1/variants/{id}`
**Summary:** Update variant details

**Parameters:**
- `id`* (path): `string`

**Request Body (`application/json`):**
```typescript
{
  name: string,
  channel: string,
  subjectLine: string,
  previewText: string,
  bodyText: string,
  bodyHtml: string,
  ctaText: string,
  ctaUrl: string,
  mediaUrl: string,
  templateId: string,
  templateParams: Record<string, Record<string, any>>,
  mabIsActive: boolean,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    campaignId: string (uuid),
    name: string,
    channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
    subjectLine: string,
    previewText: string,
    bodyText: string,
    bodyHtml: string,
    ctaText: string,
    ctaUrl: string,
    mediaUrl: string,
    templateId: string,
    templateParams: Record<string, Record<string, any>>,
    mabAlpha: number,
    mabBeta: number,
    mabImpressions: number,
    mabConversions: number,
    mabIsActive: boolean,
    generatedByAi: boolean,
    generationPrompt: string,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/variants/{campaignId}/mab-stats`
**Summary:** Get Thompson Sampling (MAB) statistics for campaign variants

**Parameters:**
- `campaignId`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    variantId: string (uuid),
    campaignId: string (uuid),
    variantName: string,
    channel: string,
    mabAlpha: number,
    mabBeta: number,
    mabImpressions: number,
    mabConversions: number,
    expectedConversionRate: number,
    ciHalfWidth95: number,
    mabIsActive: boolean,
    campaignName: string,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/variants/campaign/{campaignId}`
**Summary:** Get all variants for a campaign

**Parameters:**
- `campaignId`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    campaignId: string (uuid),
    name: string,
    channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
    subjectLine: string,
    previewText: string,
    bodyText: string,
    bodyHtml: string,
    ctaText: string,
    ctaUrl: string,
    mediaUrl: string,
    templateId: string,
    templateParams: Record<string, Record<string, any>>,
    mabAlpha: number,
    mabBeta: number,
    mabImpressions: number,
    mabConversions: number,
    mabIsActive: boolean,
    generatedByAi: boolean,
    generationPrompt: string,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Audience Simulator

### `POST /api/v1/simulations`
**Summary:** Trigger a new audience simulation

**Request Body (`application/json`):**
```typescript
{
  campaignId: string (uuid),
  syntheticAudienceSize: number,
  personaDistribution: Record<string, Record<string, any>>,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    campaignId: string (uuid),
    status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED',
    syntheticAudienceSize: number,
    predictedOpenRate: number,
    predictedCtr: number,
    predictedConversionRate: number,
    predictedRevenue: number,
    confidenceIntervalLow: number,
    confidenceIntervalHigh: number,
    winningVariantId: string (uuid),
    personaDistribution: Record<string, Record<string, any>>,
    startedAt: string (date-time),
    completedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/simulations/campaigns/{id}/simulate`
**Summary:** Trigger a simulation for a specific campaign

**Parameters:**
- `id`* (path): `string`

**Request Body (`application/json`):**
```typescript
{
  campaignId: string (uuid),
  syntheticAudienceSize: number,
  personaDistribution: Record<string, Record<string, any>>,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    campaignId: string (uuid),
    status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED',
    syntheticAudienceSize: number,
    predictedOpenRate: number,
    predictedCtr: number,
    predictedConversionRate: number,
    predictedRevenue: number,
    confidenceIntervalLow: number,
    confidenceIntervalHigh: number,
    winningVariantId: string (uuid),
    personaDistribution: Record<string, Record<string, any>>,
    startedAt: string (date-time),
    completedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/simulations/{id}`
**Summary:** Get simulation run result

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    campaignId: string (uuid),
    status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED',
    syntheticAudienceSize: number,
    predictedOpenRate: number,
    predictedCtr: number,
    predictedConversionRate: number,
    predictedRevenue: number,
    confidenceIntervalLow: number,
    confidenceIntervalHigh: number,
    winningVariantId: string (uuid),
    personaDistribution: Record<string, Record<string, any>>,
    startedAt: string (date-time),
    completedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Segment

### `GET /api/v1/segments`
**Summary:** Get all segments with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    name: string,
    description: string,
    type: 'STATIC' | 'DYNAMIC' | 'AI_GENERATED',
    status: 'DRAFT' | 'BUILDING' | 'READY' | 'ARCHIVED',
    filterSql: string,
    filterJson: Record<string, Record<string, any>>,
    createdByAgent: boolean,
    agentGoal: string,
    customerCount: number,
    lastEvaluatedAt: string (date-time),
    createdAt: string (date-time),
    updatedAt: string (date-time),
    pinned: boolean,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/segments`
**Summary:** Create a new segment

**Request Body (`application/json`):**
```typescript
{
  name: string,
  description: string,
  type: 'STATIC' | 'DYNAMIC' | 'AI_GENERATED',
  status: 'DRAFT' | 'BUILDING' | 'READY' | 'ARCHIVED',
  filterSql: string,
  filterJson: Record<string, Record<string, any>>,
  isPinned: boolean,
  createdByAgent: boolean,
  agentGoal: string,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    name: string,
    description: string,
    type: 'STATIC' | 'DYNAMIC' | 'AI_GENERATED',
    status: 'DRAFT' | 'BUILDING' | 'READY' | 'ARCHIVED',
    filterSql: string,
    filterJson: Record<string, Record<string, any>>,
    createdByAgent: boolean,
    agentGoal: string,
    customerCount: number,
    lastEvaluatedAt: string (date-time),
    createdAt: string (date-time),
    updatedAt: string (date-time),
    pinned: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/segments/{id}/evaluate`
**Summary:** Trigger async evaluation of a segment

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Record<string, any>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/segments/{id}`
**Summary:** Get segment details

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    name: string,
    description: string,
    type: 'STATIC' | 'DYNAMIC' | 'AI_GENERATED',
    status: 'DRAFT' | 'BUILDING' | 'READY' | 'ARCHIVED',
    filterSql: string,
    filterJson: Record<string, Record<string, any>>,
    createdByAgent: boolean,
    agentGoal: string,
    customerCount: number,
    lastEvaluatedAt: string (date-time),
    createdAt: string (date-time),
    updatedAt: string (date-time),
    pinned: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `DELETE /api/v1/segments/{id}`
**Summary:** Delete a segment

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Record<string, any>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `PATCH /api/v1/segments/{id}`
**Summary:** Update segment details

**Parameters:**
- `id`* (path): `string`

**Request Body (`application/json`):**
```typescript
{
  name: string,
  description: string,
  filterSql: string,
  filterJson: Record<string, Record<string, any>>,
  isPinned: boolean,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    name: string,
    description: string,
    type: 'STATIC' | 'DYNAMIC' | 'AI_GENERATED',
    status: 'DRAFT' | 'BUILDING' | 'READY' | 'ARCHIVED',
    filterSql: string,
    filterJson: Record<string, Record<string, any>>,
    createdByAgent: boolean,
    agentGoal: string,
    customerCount: number,
    lastEvaluatedAt: string (date-time),
    createdAt: string (date-time),
    updatedAt: string (date-time),
    pinned: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/segments/{id}/members`
**Summary:** Get paginated list of customer IDs in a segment

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    segmentId: string (uuid),
    customerId: string (uuid),
    addedAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Product

### `GET /api/v1/products`
**Summary:** Get all products with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    sku: string,
    name: string,
    categoryId: string (uuid),
    categoryName: string,
    price: number,
    currency: string,
    brand: string,
    tags: Array<string>,
    attributes: Record<string, Record<string, any>>,
    createdAt: string (date-time),
    active: boolean,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/products`
**Summary:** Create a new product

**Request Body (`application/json`):**
```typescript
{
  sku: string,
  name: string,
  categoryId: string (uuid),
  price: number,
  currency: string,
  brand: string,
  tags: Array<string>,
  attributes: Record<string, Record<string, any>>,
  active: boolean,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    sku: string,
    name: string,
    categoryId: string (uuid),
    categoryName: string,
    price: number,
    currency: string,
    brand: string,
    tags: Array<string>,
    attributes: Record<string, Record<string, any>>,
    createdAt: string (date-time),
    active: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/products/bulk`
**Summary:** Create multiple products in bulk

**Request Body (`application/json`):**
```typescript
Array<{
  sku: string,
  name: string,
  categoryId: string (uuid),
  price: number,
  currency: string,
  brand: string,
  tags: Array<string>,
  attributes: Record<string, Record<string, any>>,
  active: boolean,
}>
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    sku: string,
    name: string,
    categoryId: string (uuid),
    categoryName: string,
    price: number,
    currency: string,
    brand: string,
    tags: Array<string>,
    attributes: Record<string, Record<string, any>>,
    createdAt: string (date-time),
    active: boolean,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/products/{id}`
**Summary:** Get product details

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    sku: string,
    name: string,
    categoryId: string (uuid),
    categoryName: string,
    price: number,
    currency: string,
    brand: string,
    tags: Array<string>,
    attributes: Record<string, Record<string, any>>,
    createdAt: string (date-time),
    active: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/products/categories`
**Summary:** Get all product categories

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    externalId: string,
    name: string,
    description: string,
    parentCategoryId: string (uuid),
    createdAt: string (date-time),
    updatedAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Order

### `GET /api/v1/orders`
**Summary:** Get all orders with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    customerId: string (uuid),
    orderNumber: string,
    status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED',
    channel: string,
    totalAmount: number,
    currency: string,
    discountAmount: number,
    couponCode: string,
    placedAt: string (date-time),
    deliveredAt: string (date-time),
    createdAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/orders`
**Summary:** Create a new order

**Request Body (`application/json`):**
```typescript
{
  customerId: string (uuid),
  orderNumber: string,
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED',
  channel: string,
  totalAmount: number,
  currency: string,
  discountAmount: number,
  couponCode: string,
  placedAt: string (date-time),
  metadata: Record<string, Record<string, any>>,
  items: Array<{
    productId: string (uuid),
    productSku: string,
    productName: string,
    quantity: number,
    unitPrice: number,
    discountAmount: number,
    lineTotal: number,
  }>,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    customerId: string (uuid),
    orderNumber: string,
    status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED',
    channel: string,
    totalAmount: number,
    currency: string,
    discountAmount: number,
    couponCode: string,
    placedAt: string (date-time),
    deliveredAt: string (date-time),
    createdAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/orders/bulk`
**Summary:** Create multiple orders in bulk

**Request Body (`application/json`):**
```typescript
Array<{
  customerId: string (uuid),
  orderNumber: string,
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED',
  channel: string,
  totalAmount: number,
  currency: string,
  discountAmount: number,
  couponCode: string,
  placedAt: string (date-time),
  metadata: Record<string, Record<string, any>>,
  items: Array<{
    productId: string (uuid),
    productSku: string,
    productName: string,
    quantity: number,
    unitPrice: number,
    discountAmount: number,
    lineTotal: number,
  }>,
}>
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    customerId: string (uuid),
    orderNumber: string,
    status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED',
    channel: string,
    totalAmount: number,
    currency: string,
    discountAmount: number,
    couponCode: string,
    placedAt: string (date-time),
    deliveredAt: string (date-time),
    createdAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/orders/{id}`
**Summary:** Get order details

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    customerId: string (uuid),
    orderNumber: string,
    status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED',
    channel: string,
    totalAmount: number,
    currency: string,
    discountAmount: number,
    couponCode: string,
    placedAt: string (date-time),
    deliveredAt: string (date-time),
    createdAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Campaign

### `GET /api/v1/campaigns`
**Summary:** Get all campaigns with pagination

**Parameters:**
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    name: string,
    description: string,
    status: 'DRAFT' | 'SIMULATING' | 'SCHEDULED' | 'RUNNING' | 'PAUSED' | 'COMPLETED' | 'CANCELLED' | 'FAILED',
    goal: string,
    segmentId: string (uuid),
    segmentName: string,
    scheduledAt: string (date-time),
    startedAt: string (date-time),
    completedAt: string (date-time),
    timezone: string,
    maxSendCount: number,
    optOutRateThreshold: number,
    createdByAgent: boolean,
    agentSessionId: string,
    parentCampaignId: string (uuid),
    totalSent: number,
    totalDelivered: number,
    totalFailed: number,
    totalOpened: number,
    totalRead: number,
    totalClicked: number,
    totalConverted: number,
    revenueAttributed: number,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/campaigns`
**Summary:** Create a new campaign manually

**Request Body (`application/json`):**
```typescript
{
  name: string,
  description: string,
  segmentId: string (uuid),
  status: 'DRAFT' | 'SIMULATING' | 'SCHEDULED' | 'RUNNING' | 'PAUSED' | 'COMPLETED' | 'CANCELLED' | 'FAILED',
  goal: string,
  scheduledAt: string (date-time),
  timezone: string,
  maxSendCount: number,
  optOutRateThreshold: number,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    name: string,
    description: string,
    status: 'DRAFT' | 'SIMULATING' | 'SCHEDULED' | 'RUNNING' | 'PAUSED' | 'COMPLETED' | 'CANCELLED' | 'FAILED',
    goal: string,
    segmentId: string (uuid),
    segmentName: string,
    scheduledAt: string (date-time),
    startedAt: string (date-time),
    completedAt: string (date-time),
    timezone: string,
    maxSendCount: number,
    optOutRateThreshold: number,
    createdByAgent: boolean,
    agentSessionId: string,
    parentCampaignId: string (uuid),
    totalSent: number,
    totalDelivered: number,
    totalFailed: number,
    totalOpened: number,
    totalRead: number,
    totalClicked: number,
    totalConverted: number,
    revenueAttributed: number,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/campaigns/{id}/simulate`
**Summary:** Trigger a simulation for a specific campaign

**Parameters:**
- `id`* (path): `string`

**Request Body (`application/json`):**
```typescript
{
  campaignId: string (uuid),
  syntheticAudienceSize: number,
  personaDistribution: Record<string, Record<string, any>>,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    campaignId: string (uuid),
    status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED',
    syntheticAudienceSize: number,
    predictedOpenRate: number,
    predictedCtr: number,
    predictedConversionRate: number,
    predictedRevenue: number,
    confidenceIntervalLow: number,
    confidenceIntervalHigh: number,
    winningVariantId: string (uuid),
    personaDistribution: Record<string, Record<string, any>>,
    startedAt: string (date-time),
    completedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `POST /api/v1/campaigns/{id}/execute`
**Summary:** Trigger async execution of a campaign

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Record<string, any>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `PATCH /api/v1/campaigns/{id}/status`
**Summary:** Update campaign status (pause, cancel, etc.)

**Parameters:**
- `id`* (path): `string`

**Request Body (`application/json`):**
```typescript
{
  status: 'DRAFT' | 'SIMULATING' | 'SCHEDULED' | 'RUNNING' | 'PAUSED' | 'COMPLETED' | 'CANCELLED' | 'FAILED',
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    name: string,
    description: string,
    status: 'DRAFT' | 'SIMULATING' | 'SCHEDULED' | 'RUNNING' | 'PAUSED' | 'COMPLETED' | 'CANCELLED' | 'FAILED',
    goal: string,
    segmentId: string (uuid),
    segmentName: string,
    scheduledAt: string (date-time),
    startedAt: string (date-time),
    completedAt: string (date-time),
    timezone: string,
    maxSendCount: number,
    optOutRateThreshold: number,
    createdByAgent: boolean,
    agentSessionId: string,
    parentCampaignId: string (uuid),
    totalSent: number,
    totalDelivered: number,
    totalFailed: number,
    totalOpened: number,
    totalRead: number,
    totalClicked: number,
    totalConverted: number,
    revenueAttributed: number,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/campaigns/{id}`
**Summary:** Get campaign details and metrics

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    name: string,
    description: string,
    status: 'DRAFT' | 'SIMULATING' | 'SCHEDULED' | 'RUNNING' | 'PAUSED' | 'COMPLETED' | 'CANCELLED' | 'FAILED',
    goal: string,
    segmentId: string (uuid),
    segmentName: string,
    scheduledAt: string (date-time),
    startedAt: string (date-time),
    completedAt: string (date-time),
    timezone: string,
    maxSendCount: number,
    optOutRateThreshold: number,
    createdByAgent: boolean,
    agentSessionId: string,
    parentCampaignId: string (uuid),
    totalSent: number,
    totalDelivered: number,
    totalFailed: number,
    totalOpened: number,
    totalRead: number,
    totalClicked: number,
    totalConverted: number,
    revenueAttributed: number,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/campaigns/{id}/variants/mab-stats`
**Summary:** Get Thompson Sampling (MAB) statistics for campaign variants

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    variantId: string (uuid),
    campaignId: string (uuid),
    variantName: string,
    channel: string,
    mabAlpha: number,
    mabBeta: number,
    mabImpressions: number,
    mabConversions: number,
    expectedConversionRate: number,
    ciHalfWidth95: number,
    mabIsActive: boolean,
    campaignName: string,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/campaigns/{id}/performance`
**Summary:** Get campaign performance metrics including delivery, open, and conversion rates

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    name: string,
    status: string,
    goal: string,
    scheduledAt: string (date-time),
    startedAt: string (date-time),
    completedAt: string (date-time),
    createdByAgent: boolean,
    totalSent: number,
    totalDelivered: number,
    totalFailed: number,
    totalOpened: number,
    totalRead: number,
    totalClicked: number,
    totalConverted: number,
    revenueAttributed: number,
    deliveryRatePct: number,
    failureRatePct: number,
    openRatePct: number,
    ctrPct: number,
    conversionRatePct: number,
    optOutRatePct: number,
    segmentName: string,
    segmentSize: number,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/campaigns/{id}/corrections`
**Summary:** Get self-correction events for a specific campaign

**Parameters:**
- `id`* (path): `string`
- `page` (query): `integer`
- `size` (query): `integer`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    campaignId: string (uuid),
    triggerType: 'HIGH_FAILURE_RATE' | 'LOW_OPEN_RATE' | 'LOW_CTR' | 'CHANNEL_TIMEOUT' | 'BOUNCE_SPIKE' | 'OPT_OUT_SPIKE',
    triggerThreshold: number,
    observedValue: number,
    cohortSize: number,
    actionTaken: 'SWITCH_CHANNEL' | 'REWRITE_COPY' | 'PAUSE_CAMPAIGN' | 'REDUCE_FREQUENCY' | 'ADD_FALLBACK' | 'NO_ACTION',
    oldChannel: 'email' | 'whatsapp' | 'sms' | 'rcs',
    newChannel: 'email' | 'whatsapp' | 'sms' | 'rcs',
    oldVariantId: string (uuid),
    newVariantId: string (uuid),
    aiReasoning: string,
    correctionOutcome: 'IMPROVED' | 'NEUTRAL' | 'WORSENED' | 'INSUFFICIENT_DATA',
    outcomeDelta: number,
    createdAt: string (date-time),
    evaluatedAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/campaigns/opt-out-alerts`
**Summary:** Get opt-out alerts for running campaigns exceeding safety thresholds

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    campaignId: string (uuid),
    campaignName: string,
    optOutRateThreshold: number,
    currentOptOutRatePct: number,
    alertLevel: string,
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Channel Callbacks

### `POST /api/v1/callbacks/channel`
**Summary:** Receive channel callback webhook

**Request Body (`application/json`):**
```typescript
{
  channelMessageId: string,
  communicationId: string (uuid),
  eventType: 'DELIVERED' | 'FAILED' | 'OPENED' | 'READ' | 'CLICKED' | 'CONVERTED' | 'UNSUBSCRIBED' | 'BOUNCED' | 'EXPIRED',
  payload: Record<string, Record<string, any>>,
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Record<string, any>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Sovereign Agent

### `POST /api/v1/agent/chat`
**Summary:** Send a prompt to the sovereign agent

**Request Body (`application/json`):**
```typescript
{
  prompt: string,
  sessionId: string (uuid),
}
```

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    sessionId: string (uuid),
    textReply: string,
    actionTaken: 'SEGMENT_QUERY' | 'VARIANT_GENERATION' | 'CHANNEL_SELECTION' | 'SCHEDULE_DECISION' | 'SEND_COMMAND' | 'ABORT' | 'MEMORY_LOOKUP' | 'SIMULATION_TRIGGER' | 'CORRECTION_TRIGGER',
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/agent/sessions/{id}`
**Summary:** Poll the status and plan of a sovereign agent session

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    id: string (uuid),
    goal: string,
    status: 'RUNNING' | 'COMPLETED' | 'FAILED' | 'PAUSED',
    modelUsed: string,
    plan: Record<string, Record<string, any>>,
    createdSegmentId: string (uuid),
    createdCampaignId: string (uuid),
    errorMessage: string,
    startedAt: string (date-time),
    completedAt: string (date-time),
    tokensUsedIn: number,
    tokensUsedOut: number,
    conversationLog: Array<Record<string, Record<string, any>>>,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/agent/sessions/{id}/decisions`
**Summary:** Get the full ReAct reasoning chain (decision audit trail) for a session

**Parameters:**
- `id`* (path): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    sessionId: string (uuid),
    stepNumber: number,
    decisionType: 'SEGMENT_QUERY' | 'VARIANT_GENERATION' | 'CHANNEL_SELECTION' | 'SCHEDULE_DECISION' | 'SEND_COMMAND' | 'ABORT' | 'MEMORY_LOOKUP' | 'SIMULATION_TRIGGER' | 'CORRECTION_TRIGGER',
    inputContext: Record<string, Record<string, any>>,
    outputAction: Record<string, Record<string, any>>,
    reasoning: string,
    createdAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Communication

### `PATCH /api/v1/communications/{id}/status`
**Summary:** Update status of a communication

**Parameters:**
- `id`* (path): `string`
- `status`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Record<string, any>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/communications/customer/{customerId}`
**Summary:** Get communications for a customer

**Parameters:**
- `customerId`* (path): `string`
- `pageable`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    campaignId: string (uuid),
    variantId: string (uuid),
    customerId: string (uuid),
    channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
    status: 'PENDING' | 'SENT' | 'DELIVERED' | 'FAILED' | 'OPENED' | 'READ' | 'CLICKED' | 'CONVERTED' | 'UNSUBSCRIBED' | 'BOUNCED' | 'EXPIRED',
    channelMessageId: string,
    recipientAddress: string,
    personalisedSubject: string,
    personalisedBody: string,
    sentAt: string (date-time),
    deliveredAt: string (date-time),
    failedAt: string (date-time),
    openedAt: string (date-time),
    readAt: string (date-time),
    clickedAt: string (date-time),
    convertedAt: string (date-time),
    unsubscribedAt: string (date-time),
    failureReason: string,
    failureCode: string,
    retryCount: number,
    nextRetryAt: string (date-time),
    attributedOrderId: string (uuid),
    attributionWindowHours: number,
    spawnedFollowupId: string (uuid),
    mabSampleValue: number,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/communications/campaign/{campaignId}`
**Summary:** Get communications for a campaign

**Parameters:**
- `campaignId`* (path): `string`
- `pageable`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: Array<{
    id: string (uuid),
    campaignId: string (uuid),
    variantId: string (uuid),
    customerId: string (uuid),
    channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
    status: 'PENDING' | 'SENT' | 'DELIVERED' | 'FAILED' | 'OPENED' | 'READ' | 'CLICKED' | 'CONVERTED' | 'UNSUBSCRIBED' | 'BOUNCED' | 'EXPIRED',
    channelMessageId: string,
    recipientAddress: string,
    personalisedSubject: string,
    personalisedBody: string,
    sentAt: string (date-time),
    deliveredAt: string (date-time),
    failedAt: string (date-time),
    openedAt: string (date-time),
    readAt: string (date-time),
    clickedAt: string (date-time),
    convertedAt: string (date-time),
    unsubscribedAt: string (date-time),
    failureReason: string,
    failureCode: string,
    retryCount: number,
    nextRetryAt: string (date-time),
    attributedOrderId: string (uuid),
    attributionWindowHours: number,
    spawnedFollowupId: string (uuid),
    mabSampleValue: number,
    createdAt: string (date-time),
    updatedAt: string (date-time),
  }>,
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Organizational Memory

### `GET /api/v1/memory`
**Summary:** Get all organizational memory entries

**Parameters:**
- `pageable`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    totalElements: number,
    totalPages: number,
    size: number,
    content: Array<{
      id: string (uuid),
      segmentTag: string,
      channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
      learningType: 'COPY_STYLE' | 'SEND_TIME' | 'CHANNEL_PREFERENCE' | 'FREQUENCY' | 'OFFER_TYPE' | 'SUBJECT_PATTERN',
      learningSummary: string,
      confidence: number,
      evidenceCount: number,
      avgLift: number,
      winningCopySignals: Record<string, Record<string, any>>,
      dayOfWeek: number,
      timeOfDay: 'MORNING' | 'AFTERNOON' | 'EVENING' | 'NIGHT',
      sourceCampaignIds: Array<string (uuid)>,
      expiresAt: string (date-time),
      createdAt: string (date-time),
      updatedAt: string (date-time),
      active: boolean,
    }>,
    number: number,
    sort: Array<{
      direction: string,
      nullHandling: string,
      ascending: boolean,
      property: string,
      ignoreCase: boolean,
    }>,
    first: boolean,
    last: boolean,
    numberOfElements: number,
    pageable: {
      offset: number,
      sort: Array<{
        direction: string,
        nullHandling: string,
        ascending: boolean,
        property: string,
        ignoreCase: boolean,
      }>,
      paged: boolean,
      pageSize: number,
      pageNumber: number,
      unpaged: boolean,
    },
    empty: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/memory/query`
**Summary:** Query organizational memory by segment tag and channel

**Parameters:**
- `segmentTag` (query): `string`
- `channel` (query): `string`
- `pageable`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    totalElements: number,
    totalPages: number,
    size: number,
    content: Array<{
      id: string (uuid),
      segmentTag: string,
      channel: 'email' | 'whatsapp' | 'sms' | 'rcs',
      learningType: 'COPY_STYLE' | 'SEND_TIME' | 'CHANNEL_PREFERENCE' | 'FREQUENCY' | 'OFFER_TYPE' | 'SUBJECT_PATTERN',
      learningSummary: string,
      confidence: number,
      evidenceCount: number,
      avgLift: number,
      winningCopySignals: Record<string, Record<string, any>>,
      dayOfWeek: number,
      timeOfDay: 'MORNING' | 'AFTERNOON' | 'EVENING' | 'NIGHT',
      sourceCampaignIds: Array<string (uuid)>,
      expiresAt: string (date-time),
      createdAt: string (date-time),
      updatedAt: string (date-time),
      active: boolean,
    }>,
    number: number,
    sort: Array<{
      direction: string,
      nullHandling: string,
      ascending: boolean,
      property: string,
      ignoreCase: boolean,
    }>,
    first: boolean,
    last: boolean,
    numberOfElements: number,
    pageable: {
      offset: number,
      sort: Array<{
        direction: string,
        nullHandling: string,
        ascending: boolean,
        property: string,
        ignoreCase: boolean,
      }>,
      paged: boolean,
      pageSize: number,
      pageNumber: number,
      unpaged: boolean,
    },
    empty: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Self-Correction Engine

### `GET /api/v1/corrections`
**Summary:** Get all correction events

**Parameters:**
- `pageable`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    totalElements: number,
    totalPages: number,
    size: number,
    content: Array<{
      id: string (uuid),
      campaignId: string (uuid),
      triggerType: 'HIGH_FAILURE_RATE' | 'LOW_OPEN_RATE' | 'LOW_CTR' | 'CHANNEL_TIMEOUT' | 'BOUNCE_SPIKE' | 'OPT_OUT_SPIKE',
      triggerThreshold: number,
      observedValue: number,
      cohortSize: number,
      actionTaken: 'SWITCH_CHANNEL' | 'REWRITE_COPY' | 'PAUSE_CAMPAIGN' | 'REDUCE_FREQUENCY' | 'ADD_FALLBACK' | 'NO_ACTION',
      oldChannel: 'email' | 'whatsapp' | 'sms' | 'rcs',
      newChannel: 'email' | 'whatsapp' | 'sms' | 'rcs',
      oldVariantId: string (uuid),
      newVariantId: string (uuid),
      aiReasoning: string,
      correctionOutcome: 'IMPROVED' | 'NEUTRAL' | 'WORSENED' | 'INSUFFICIENT_DATA',
      outcomeDelta: number,
      createdAt: string (date-time),
      evaluatedAt: string (date-time),
    }>,
    number: number,
    sort: Array<{
      direction: string,
      nullHandling: string,
      ascending: boolean,
      property: string,
      ignoreCase: boolean,
    }>,
    first: boolean,
    last: boolean,
    numberOfElements: number,
    pageable: {
      offset: number,
      sort: Array<{
        direction: string,
        nullHandling: string,
        ascending: boolean,
        property: string,
        ignoreCase: boolean,
      }>,
      paged: boolean,
      pageSize: number,
      pageNumber: number,
      unpaged: boolean,
    },
    empty: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

## Audit Logs

### `GET /api/v1/audit-logs/trace/{traceId}`
**Summary:** Get audit logs by trace ID

**Parameters:**
- `traceId`* (path): `string`
- `pageable`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    totalElements: number,
    totalPages: number,
    size: number,
    content: Array<{
      id: number,
      traceId: string (uuid),
      entityType: string,
      entityId: number,
      action: string,
      actorType: 'USER' | 'AGENT' | 'SYSTEM',
      actorId: string,
      oldValue: Record<string, Record<string, any>>,
      newValue: Record<string, Record<string, any>>,
      description: string,
      createdAt: string (date-time),
    }>,
    number: number,
    sort: Array<{
      direction: string,
      nullHandling: string,
      ascending: boolean,
      property: string,
      ignoreCase: boolean,
    }>,
    first: boolean,
    last: boolean,
    numberOfElements: number,
    pageable: {
      offset: number,
      sort: Array<{
        direction: string,
        nullHandling: string,
        ascending: boolean,
        property: string,
        ignoreCase: boolean,
      }>,
      paged: boolean,
      pageSize: number,
      pageNumber: number,
      unpaged: boolean,
    },
    empty: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/audit-logs/entity/{entityType}/{entityId}`
**Summary:** Get audit logs by entity type and ID

**Parameters:**
- `entityType`* (path): `string`
- `entityId`* (path): `string`
- `pageable`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    totalElements: number,
    totalPages: number,
    size: number,
    content: Array<{
      id: number,
      traceId: string (uuid),
      entityType: string,
      entityId: number,
      action: string,
      actorType: 'USER' | 'AGENT' | 'SYSTEM',
      actorId: string,
      oldValue: Record<string, Record<string, any>>,
      newValue: Record<string, Record<string, any>>,
      description: string,
      createdAt: string (date-time),
    }>,
    number: number,
    sort: Array<{
      direction: string,
      nullHandling: string,
      ascending: boolean,
      property: string,
      ignoreCase: boolean,
    }>,
    first: boolean,
    last: boolean,
    numberOfElements: number,
    pageable: {
      offset: number,
      sort: Array<{
        direction: string,
        nullHandling: string,
        ascending: boolean,
        property: string,
        ignoreCase: boolean,
      }>,
      paged: boolean,
      pageSize: number,
      pageNumber: number,
      unpaged: boolean,
    },
    empty: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---

### `GET /api/v1/audit-logs/actor/{actorId}`
**Summary:** Get audit logs by actor ID

**Parameters:**
- `actorId`* (path): `string`
- `pageable`* (query): `string`

**Response (`200 OK / 201 Created`):**
```typescript
{
  success: boolean,
  data: {
    totalElements: number,
    totalPages: number,
    size: number,
    content: Array<{
      id: number,
      traceId: string (uuid),
      entityType: string,
      entityId: number,
      action: string,
      actorType: 'USER' | 'AGENT' | 'SYSTEM',
      actorId: string,
      oldValue: Record<string, Record<string, any>>,
      newValue: Record<string, Record<string, any>>,
      description: string,
      createdAt: string (date-time),
    }>,
    number: number,
    sort: Array<{
      direction: string,
      nullHandling: string,
      ascending: boolean,
      property: string,
      ignoreCase: boolean,
    }>,
    first: boolean,
    last: boolean,
    numberOfElements: number,
    pageable: {
      offset: number,
      sort: Array<{
        direction: string,
        nullHandling: string,
        ascending: boolean,
        property: string,
        ignoreCase: boolean,
      }>,
      paged: boolean,
      pageSize: number,
      pageNumber: number,
      unpaged: boolean,
    },
    empty: boolean,
  },
  message: string,
  errorCode: string,
  errorMessage: string,
  pagination: {
    pageNumber: number,
    pageSize: number,
    totalElements: number,
    totalPages: number,
    last: boolean,
  },
}
```

---


---

## 15. The Sovereign AI Agent (`/api/v1/agent`)

### `POST /api/v1/agent/chat`
**Summary:** Submit natural language goal to the Sovereign Agent
**Payload:**
```json
{
  "prompt": "Send a 20% discount to all VIP users in California"
}
```
**Response:**
```json
{
  "sessionId": "b42b9c...",
  "status": "IN_PROGRESS",
  "textReply": "I have created the segment and am drafting the campaign."
}
```

### `GET /api/v1/agent/sessions/{sessionId}`
**Summary:** Poll Agent Session Status
**Response:**
```json
{
  "id": "b42b9c...",
  "goal": "Send 20% discount...",
  "status": "COMPLETED",
  "plan": {
    "segmentId": "UUID",
    "campaignId": "UUID",
    "channelRecommendation": "EMAIL"
  }
}
```

---

## 16. AI Campaign Proposals (`/api/v1/campaigns/proposals`)

### `GET /api/v1/campaigns/proposals`
**Summary:** Get Autonomous Campaigns awaiting human approval
**Response:**
```json
[
  {
    "id": "UUID",
    "name": "Predictive Clearance: Blue Coats",
    "description": "Autonomously generated by AI to clear dead stock.",
    "status": "DRAFT",
    "createdByAgent": true
  }
]
```

### `POST /api/v1/campaigns/{id}/approve`
**Summary:** Approve an autonomous campaign to start execution immediately.
**Payload:** None
**Response:**
```json
{
  "id": "UUID",
  "status": "RUNNING"
}
```

---

## 17. AI Memory Layer (`/api/v1/memory`)

### `GET /api/v1/memory`
**Summary:** Get all long-term organizational learnings.
**Response:**
```json
[
  {
    "learningType": "copy_style",
    "learningSummary": "Urgency CTAs perform 22% better on Friday afternoons",
    "confidence": 0.85
  }
]
```

---

## 18. AGI Real-Time Testing (`/api/v1/test/agi`)

### `POST /api/v1/test/agi/trigger-war-room`
**Summary:** Triggers Multi-Agent Debate (Persona A vs B)
**Payload:**
```json
{
  "goal": "Win back churned winter buyers"
}
```

### `POST /api/v1/test/agi/trigger-fund-manager`
**Summary:** Triggers the budget reallocation AI

### `POST /api/v1/test/agi/trigger-omni-awareness`
**Summary:** Triggers The Sleep Agent (Fatigue Detection) and Whisperer (Micro-Churn)
