import { create } from 'zustand';

interface AppState {
  isAgentSidebarOpen: boolean;
  toggleAgentSidebar: () => void;
  setAgentSidebarOpen: (isOpen: boolean) => void;
}

export const useAppStore = create<AppState>((set) => ({
  isAgentSidebarOpen: false,
  toggleAgentSidebar: () => set((state) => ({ isAgentSidebarOpen: !state.isAgentSidebarOpen })),
  setAgentSidebarOpen: (isOpen) => set({ isAgentSidebarOpen: isOpen }),
}));
