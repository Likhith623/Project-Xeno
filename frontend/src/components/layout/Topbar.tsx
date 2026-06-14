"use client";

import { Bell, Plus, Menu } from "lucide-react";
import { Button } from "@/components/ui/button";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api";
import Link from "next/link";

interface TopbarProps {
  title: string;
  children?: React.ReactNode;
  onMenuClick?: () => void;
}

export function Topbar({ title, children, onMenuClick }: TopbarProps) {
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
    <header className="min-h-[56px] bg-white border-b border-border-primary flex items-center justify-between px-3 sm:px-6 shrink-0 gap-2 flex-wrap py-2 sm:py-0 sm:flex-nowrap">
      {/* Left: Hamburger (mobile) + Title */}
      <div className="flex items-center gap-2 sm:gap-3 min-w-0">
        {/* Hamburger — only shown on mobile */}
        <button
          className="lg:hidden flex items-center justify-center w-8 h-8 rounded-md hover:bg-bg-secondary transition-colors shrink-0"
          onClick={onMenuClick}
          aria-label="Open sidebar"
        >
          <Menu className="w-5 h-5 text-text-primary" />
        </button>
        <h1 className="text-[14px] sm:text-[15px] font-medium text-text-primary truncate max-w-[160px] sm:max-w-none">
          {title}
        </h1>
      </div>

      {/* Right: Actions */}
      <div className="flex items-center gap-1.5 sm:gap-3 flex-wrap justify-end">
        {children || (
          <>
            <div className="hidden sm:flex items-center gap-1.5 text-[12px] text-green-600 mr-1">
              <div className="w-2 h-2 rounded-full bg-green-500 shrink-0" />
              <span>{activeCount} live</span>
            </div>
            <Link href="/campaigns">
              <Button variant="outline" size="sm" className="h-8 text-[12px] sm:text-[13px] gap-1.5 px-2 sm:px-3">
                <Bell className="w-3.5 h-3.5" />
                <span className="hidden sm:inline">Alerts</span>
                {alertCount > 0 && (
                  <span className="bg-red-500 text-white text-[10px] px-1.5 py-0.5 rounded-md leading-none">
                    {alertCount}
                  </span>
                )}
              </Button>
            </Link>
            <Link href="/campaigns" onClick={() => {
              const { setCampaignModalOpen } = require("@/store/useCampaignStore").useCampaignStore.getState();
              setCampaignModalOpen(true);
            }}>
              <Button size="sm" className="h-8 text-[12px] sm:text-[13px] gap-1.5 px-2 sm:px-3">
                <Plus className="w-3.5 h-3.5" />
                <span className="hidden sm:inline">New Campaign</span>
              </Button>
            </Link>
          </>
        )}
      </div>
    </header>
  );
}
