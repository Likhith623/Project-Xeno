import { Bell, Plus } from "lucide-react";
import { Button } from "@/components/ui/button";

export function Topbar({ title, children }: { title: string, children?: React.ReactNode }) {
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
              <span>3 campaigns live</span>
            </div>
            <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2">
              <Bell className="w-4 h-4" />
              Alerts
              <span className="bg-red-500 text-white text-[10px] px-1.5 py-0.5 rounded-md leading-none">2</span>
            </Button>
            <Button size="sm" className="h-8 text-[13px] gap-2">
              <Plus className="w-4 h-4" />
              New Campaign
            </Button>
          </>
        )}
      </div>
    </header>
  );
}
