import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export interface AgentChatMessage {
  role: 'user' | 'agent';
  content: string;
}

interface AgentState {
  sessionId: string | null;
  setSessionId: (id: string | null) => void;
  messages: AgentChatMessage[];
  addMessage: (msg: AgentChatMessage) => void;
  clearMessages: () => void;
  lastCreatedCampaignId: string | null;
  setLastCreatedCampaignId: (id: string | null) => void;
}

export const useAgentStore = create<AgentState>()(
  persist(
    (set) => ({
      sessionId: null,
      setSessionId: (id) => set({ sessionId: id }),
      messages: [],
      addMessage: (msg) => set((state) => ({ messages: [...state.messages, msg] })),
      clearMessages: () => set({ messages: [] }),
      lastCreatedCampaignId: null,
      setLastCreatedCampaignId: (id) => set({ lastCreatedCampaignId: id }),
    }),
    { name: 'xeno-agent-store' }
  )
);
