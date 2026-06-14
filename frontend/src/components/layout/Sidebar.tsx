"use client";

import Link from "next/link";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api";
import {
  LayoutDashboard,
  Bot,
  HeartHandshake,
  Megaphone,
  TestTube,
  LineChart,
  Users,
  Box,
  ShoppingBag,
  Receipt,
  Brain,
  Wrench,
  FileSearch,
  FlaskConical,
  Settings,
  Activity
} from "lucide-react";

interface SidebarProps {
  onClose?: () => void;
}

export function Sidebar({ onClose }: SidebarProps) {
  const pathname = usePathname();

  const { data: proposalsData } = useQuery({
    queryKey: ['campaigns', 'proposals'],
    queryFn: () => api.get('/campaigns/proposals').then(res => Array.isArray(res) ? res : res?.content || []),
    staleTime: 30_000,
  });

  const proposalsCount = Array.isArray(proposalsData) ? proposalsData.length : 0;

  const NAV_GROUPS = [
    {
      title: "Overview",
      items: [
        { name: "Dashboard", href: "/", icon: LayoutDashboard },
        { name: "AI Agent", href: "/agent", icon: Bot, badge: "Live", badgeClass: "bg-brand-light text-brand" },
        { name: "Proposals", href: "/proposals", icon: HeartHandshake, badge: proposalsCount > 0 ? String(proposalsCount) : undefined, badgeClass: "bg-red-100 text-red-700" },
      ]
    },
    {
      title: "Campaigns",
      items: [
        { name: "All Campaigns", href: "/campaigns", icon: Megaphone },
        { name: "MAB Dashboard", href: "/mab", icon: LineChart },
      ]
    },
    {
      title: "Data",
      items: [
        { name: "Customers", href: "/customers", icon: Users },
        { name: "Segments", href: "/segments", icon: Box },
        { name: "Orders", href: "/orders", icon: Receipt },
        { name: "Products", href: "/products", icon: ShoppingBag },
      ]
    },
    {
      title: "Intelligence",
      items: [
        { name: "Org Memory", href: "/memory", icon: Brain },
        { name: "Audit Logs", href: "/audit-logs", icon: FileSearch },
        { name: "Corrections", href: "/corrections", icon: Wrench },
      ]
    }
  ];

  return (
    <aside className="w-60 min-w-[240px] bg-white border-r border-border-primary flex flex-col h-full shrink-0 overflow-y-auto">
      {/* Logo */}
      <div className="p-4 sm:p-5 border-b border-border-primary flex items-center gap-3 shrink-0">
        <div className="w-7 h-7 bg-text-primary rounded-md flex items-center justify-center shrink-0">
          <Activity className="w-4 h-4 text-white" />
        </div>
        <span className="font-medium text-[15px] text-text-primary truncate">Project Xeno</span>
      </div>

      {/* Nav groups */}
      <div className="flex-1 py-3 sm:py-4 flex flex-col gap-5 sm:gap-6 overflow-y-auto">
        {NAV_GROUPS.map((group) => (
          <div key={group.title} className="px-2 sm:px-3">
            <div className="px-3 mb-1.5 sm:mb-2 text-[11px] font-medium text-text-tertiary uppercase tracking-wider">
              {group.title}
            </div>
            <div className="flex flex-col gap-0.5 sm:gap-1">
              {group.items.map((item) => {
                const isActive = pathname === item.href || (item.href !== "/" && pathname.startsWith(item.href));
                return (
                  <Link
                    key={item.name}
                    href={item.href}
                    onClick={onClose}
                    className={cn(
                      "flex items-center gap-3 px-3 py-2 rounded-md text-[13px] transition-colors",
                      isActive
                        ? "bg-bg-secondary text-text-primary font-medium"
                        : "text-text-secondary hover:bg-bg-secondary hover:text-text-primary"
                    )}
                  >
                    <item.icon className="w-4 h-4 shrink-0" />
                    <span className="truncate">{item.name}</span>
                    {item.badge && (
                      <span className={cn("ml-auto text-[10px] font-medium px-2 py-0.5 rounded-full shrink-0", item.badgeClass)}>
                        {item.badge}
                      </span>
                    )}
                  </Link>
                );
              })}
            </div>
          </div>
        ))}
      </div>

      {/* Settings */}
      <div className="p-2 sm:p-3 border-t border-border-primary mt-auto shrink-0">
        <Link
          href="/settings"
          onClick={onClose}
          className="flex items-center gap-3 px-3 py-2 rounded-md text-[13px] text-text-secondary hover:bg-bg-secondary hover:text-text-primary transition-colors"
        >
          <Settings className="w-4 h-4 shrink-0" />
          <span>Settings</span>
        </Link>
      </div>
    </aside>
  );
}
