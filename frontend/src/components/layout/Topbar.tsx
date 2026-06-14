"use client";

import { Bell, Plus } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api";
import Link from "next/link";

export function Topbar({ title, children }: { title: string, children?: React.ReactNode }) {
  const { data: campaigns } = useQuery({
    queryKey: ['campaigns'],
    queryFn: () => api.get('/campaigns').then(res => Array.isArray(res) ? res : res?.content || []),
    staleTime: 30_000,
  });

  const { data: optOutAlerts } = useQuery({
    queryKey: ['campaigns', 'opt-out-alerts'],
    queryFn: () => api.get('/campaigns/opt-out-alerts').then(res => Array.isArray(res) ? res : res?.content || []),
    staleTime: 30_000,
  });

  const campaignsArr = Array.isArray(campaigns) ? campaigns : [];
  const activeCount = campaignsArr.filter((c: any) => c.status === 'RUNNING' || c.status === 'SCHEDULED').length;
  const alertCount = Array.isArray(optOutAlerts) ? optOutAlerts.length : 0;

  return (
    <header className="h-[56px] bg-white border-b border-border-primary flex items-center justify-between px-6 shrink-0">
      <div className="flex items-center gap-3">
        <h1 className="text-[15px] font-medium text-text-primary">{title}</h1>
      </div>

      <div className="flex items-center gap-3">
        {children || (
          <>
            <div className="flex items-center gap-1.5 text-[12px] text-green-600 mr-2">
              <div className="w-2 h-2 rounded-full bg-green-500" />
              <span>{activeCount} campaigns live</span>
            </div>
            <Link href="/campaigns">
              <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2">
                <Bell className="w-4 h-4" />
                Alerts
                {alertCount > 0 && (
                  <span className="bg-red-500 text-white text-[10px] px-1.5 py-0.5 rounded-md leading-none">{alertCount}</span>
                )}
              </Button>
            </Link>
            <Link href="/campaigns" onClick={() => {
              const { setCampaignModalOpen } = require("@/store/useCampaignStore").useCampaignStore.getState();
              setCampaignModalOpen(true);
            }}>
              <Button size="sm" className="h-8 text-[13px] gap-2">
                <Plus className="w-4 h-4" />
                New Campaign
              </Button>
            </Link>
          </>
        )}
      </div>
    </header>
  );
}
