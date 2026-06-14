import { create } from 'zustand';

interface CampaignState {
  isCampaignModalOpen: boolean;
  setCampaignModalOpen: (open: boolean) => void;
}

export const useCampaignStore = create<CampaignState>((set) => ({
  isCampaignModalOpen: false,
  setCampaignModalOpen: (open) => set({ isCampaignModalOpen: open }),
}));
