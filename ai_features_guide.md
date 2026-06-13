
---

# 🧠 Core AI Features & Business Logic Guide
*This section provides the conceptual frontend integration logic for all AI Features.*

## 1. The Sovereign AI Agent (`AgentController`)
The Sovereign Agent allows users to input natural language goals (e.g., "Win back churned VIP users"). 
- **Endpoint:** `POST /api/v1/agent/chat`
- **Frontend Flow:** The frontend should display a chat-like interface. When the user submits a goal, call this endpoint. It returns a `sessionId`.
- **Polling:** Use `GET /api/v1/agent/sessions/{sessionId}` to poll the agent's progress. Display a loading animation. Once `status` becomes `COMPLETED`, display the `plan` JSON which contains the IDs of the newly created Segments and Campaigns.

## 2. Multi-Armed Bandit (MAB) & Thompson Sampling (`CampaignController`)
- **Endpoint:** `GET /api/v1/campaigns/{id}/variants/mab-stats`
- **Frontend Flow:** When viewing an active campaign, the frontend should display a dashboard showing real-time variant performance. Use this endpoint to fetch the `conversionRate`, `weight`, and `impressions` of each variant (A/B/C/D). Render this as a dynamic pie chart or bar chart showing the AI dynamically shifting traffic to the winning variant.

## 3. Autonomous Campaign Proposals ("Tinder-style UI")
- **Endpoint:** `GET /api/v1/campaigns/proposals`
- **Frontend Flow:** The backend proactively generates highly optimized campaigns at midnight. The frontend should fetch these proposals and display them in a card-stack UI. The user can review the AI's proposal, and click "Approve". 
- **Action:** Clicking "Approve" calls `POST /api/v1/campaigns/{id}/approve` to instantly activate the campaign.

## 4. The Sleep Agent (Omni-Awareness Fatigue)
- **Frontend Flow:** When creating a campaign, the frontend doesn't need to manually check if users are fatigued. The backend automatically queries `channel_cooldown_until` in the `CustomerEntity`. The frontend just needs to display a stat on the Campaign Dashboard: "Users Suppressed by Fatigue AI: X". 

## 5. Organizational Memory (`MemoryController`)
- **Endpoint:** `GET /api/v1/memory`
- **Frontend Flow:** The backend learns from past campaign failures and successes. The frontend should have a "Brain" or "Learnings" tab. Fetch the memory logs and display them as insights (e.g., "Urgency CTAs perform 22% better on Friday afternoons"). 

## 6. Real-Time AGI Testing Suite (`AITestingController`)
- **Frontend Flow:** Build a "Developer / Test" panel in the frontend where the user can click buttons to instantly trigger the backend cron jobs:
  - Trigger War Room (Multi-Agent Debate)
  - Trigger Fund Manager (Budget Reallocation)
  - Trigger Omni-Awareness (Fatigue Rules)
- Use `POST /api/v1/test/agi/*` endpoints for these buttons. 

---
**End of Ultimate Guide**
