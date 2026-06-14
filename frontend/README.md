# Project Xeno: Frontend Application

Welcome to the frontend repository for **Project Xeno**, the world's first multi-agent autonomous CRM. This application provides the interactive dashboard and control center for marketers to oversee and guide the Sovereign AI Agent.

## 🛠️ Technology Stack

- **Framework**: Next.js 16.2.9 (App Router)
- **Language**: TypeScript 5
- **Styling**: Tailwind CSS 4
- **UI Primitives**: shadcn/ui & @base-ui/react
- **Server State**: TanStack React Query 5
- **Client State**: Zustand 5 (with localStorage persistence)
- **Charts**: Recharts 3.8
- **HTTP Client**: Axios 1.17

## 📂 Application Architecture

The frontend is structured using the Next.js App Router paradigm, organized by feature domains:

```text
frontend/src/
├── app/                             # Next.js App Router
│   ├── page.tsx                     # Dashboard — KPI cards, charts, active campaigns
│   ├── campaigns/                   # Campaign list & details (variants, simulate, timeline)
│   ├── proposals/                   # Tinder swipe UI — AI proposals inbox
│   ├── agent/                       # Sovereign AI Agent chat + reasoning trace
│   ├── segments/                    # Segment management + AI War Room + Persona Gen
│   ├── customers/                   # Customer table + Create/Edit modals
│   ├── orders/                      # Order table + Create + status
│   ├── products/                    # Product catalog + Add/Edit modals
│   ├── mab/                         # MAB Dashboard — Thompson Sampling live stats
│   ├── memory/                      # Org Memory viewer + AI memory query
│   ├── audit-logs/                  # Audit trail log viewer
│   └── corrections/                 # Human feedback corrections
├── components/                      # Reusable UI components (layout, shadcn primitives)
├── lib/                             # Utility functions and API client (Axios)
└── store/                           # Zustand stores (Agent, Campaign, App state)
```

## 🧠 State Management Philosophy

Project Xeno uses a hybrid state management approach to ensure optimal performance and developer experience:

1. **Server State (React Query)**: Handles asynchronous data fetching, caching, and invalidation for all API endpoints (e.g., fetching campaigns, polling agent sessions, real-time MAB stats).
2. **Client State (Zustand)**: Manages UI-specific transient state (e.g., sidebar toggles, modal open/close states) and persistent client-side data (e.g., the active Agent session ID via `localStorage`).

## 🚀 Setup & Installation

1. **Prerequisites**: Node.js 20+ and `npm`.
2. **Install Dependencies**:
   ```bash
   cd frontend
   npm install
   ```
3. **Environment Variables**: Create a `.env.local` file in the `frontend` root and add your backend API URL (or use the proxy configuration in Next.js).
   ```env
   NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
   NEXT_PUBLIC_API_KEY=likhit@178926a
   ```
4. **Run the Development Server**:
   ```bash
   npm run dev
   ```
5. Open [http://localhost:3000](http://localhost:3000) in your browser.

## 💡 Best Practices for Contributors

- Use `lucide-react` for all icons to maintain consistency.
- All new UI components should be built using Tailwind CSS utility classes and `shadcn/ui` patterns.
- Wrap API calls using the configured `axios` instance in `lib/api.ts` to automatically handle authentication envelopes and error toasts (via `sonner`).
