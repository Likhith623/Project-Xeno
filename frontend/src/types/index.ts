export interface Campaign {
  id: string;
  name: string;
  description?: string;
  status: 'DRAFT' | 'SIMULATING' | 'APPROVED' | 'SCHEDULED' | 'RUNNING' | 'PAUSED' | 'COMPLETED' | 'CANCELLED' | 'FAILED';
  goal?: string;
  segmentId?: string;
  scheduledAt?: string;
  startedAt?: string;
  completedAt?: string;
  timezone?: string;
  maxSendCount?: number;
  optOutRateThreshold?: number;
  createdByAgent: boolean;
  agentSessionId?: string;
  parentCampaignId?: string;
  totalSent: number;
  totalDelivered: number;
  totalFailed: number;
  totalOpened: number;
  totalRead: number;
  totalClicked: number;
  totalConverted: number;
  revenueAttributed: number;
  createdAt: string;
  updatedAt: string;
  segmentName?: string;
  segmentSize?: number;
}

export interface Variant {
  id: string;
  campaignId: string;
  name: string;
  channel: 'EMAIL' | 'WHATSAPP' | 'SMS' | 'RCS';
  subjectLine?: string;
  previewText?: string;
  bodyText?: string;
  bodyHtml?: string;
  ctaText?: string;
  ctaUrl?: string;
  mediaUrl?: string;
  templateId?: string;
  templateParams?: Record<string, any>;
  mabAlpha: number;
  mabBeta: number;
  mabImpressions: number;
  mabConversions: number;
  mabIsActive: boolean;
  generatedByAi: boolean;
  generationPrompt?: string;
  expectedConversionRate?: number;
  ciHalfWidth95?: number;
  createdAt: string;
  updatedAt: string;
}

export interface Simulation {
  id: string;
  campaignId: string;
  status: 'PENDING' | 'RUNNING' | 'COMPLETED' | 'FAILED';
  syntheticAudienceSize: number;
  predictedOpenRate?: number;
  predictedCtr?: number;
  predictedConversionRate?: number;
  predictedRevenue?: number;
  confidenceIntervalLow?: number;
  confidenceIntervalHigh?: number;
  winningVariantId?: string;
  personaDistribution?: Record<string, number>;
  startedAt?: string;
  completedAt?: string;
  createdAt: string;
}

export interface Segment {
  id: string;
  name: string;
  description?: string;
  type: 'STATIC' | 'DYNAMIC' | 'AI_GENERATED';
  status: 'DRAFT' | 'BUILDING' | 'READY' | 'ARCHIVED';
  filterSql?: string;
  filterJson?: Record<string, any>;
  isPinned: boolean;
  createdByAgent: boolean;
  agentGoal?: string;
  customerCount: number;
  lastEvaluatedAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface OptOutAlert {
  campaignId: string;
  campaignName: string;
  optOutRateThreshold: number;
  currentOptOutRatePct: number;
  alertLevel: 'OK' | 'WARNING' | 'EXCEEDED';
}

export interface Order {
  id: string;
  customerId: string;
  orderNumber?: string;
  status: 'PENDING' | 'CONFIRMED' | 'SHIPPED' | 'DELIVERED' | 'CANCELLED' | 'RETURNED';
  channel?: string;
  totalAmount: number;
  currency: string;
  discountAmount?: number;
  couponCode?: string;
  placedAt: string;
  deliveredAt?: string;
  metadata?: Record<string, any>;
  createdAt: string;
}

export interface AuditLog {
  id: string;
  actor: string;
  action: string;
  target?: string;
  targetId?: string;
  details?: string;
  createdAt: string;
}

export interface MemoryInsight {
  id: string;
  segmentTag?: string;
  channel?: string;
  dayOfWeek?: number;
  timeOfDay?: 'MORNING' | 'AFTERNOON' | 'EVENING' | 'NIGHT';
  learningType: string;
  learningSummary: string;
  confidence: number;
  sourceCampaignIds: string[];
  evidenceCount: number;
  avgLift?: number;
  winningCopySignals?: Record<string, any>;
  isActive: boolean;
  expiresAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface PageResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    unpaged: boolean;
    paged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
