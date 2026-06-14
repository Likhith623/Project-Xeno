"use client";

import { Shell } from "@/components/layout/Shell";
import { Card } from "@/components/ui/card";
import { Loader2, Activity, ArrowRight } from "lucide-react";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState } from "react";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";

export default function CorrectionsPage() {
  const [page, setPage] = useState(0);

  const { data: correctionsData, isLoading, isError } = useQuery({
    queryKey: ['corrections', page],
    queryFn: () => api.get(`/corrections?page=${page}&size=20`),
    staleTime: 30_000,
  });

  const corrections: any[] = Array.isArray(correctionsData) ? correctionsData : (correctionsData?.content || []);
  const totalPages: number = correctionsData?.totalPages || 0;

  return (
    <Shell title="Self-Correction Engine" topbarActions={
       <div className="flex items-center gap-2 text-[12px] text-text-secondary bg-orange-50 px-3 py-1.5 rounded-full border border-orange-100">
         <Activity className="w-3.5 h-3.5 text-orange-600" /> Autonomous adjustments running
       </div>
    }>
      <div className="mb-6 max-w-3xl">
        <h2 className="text-[14px] font-medium text-text-primary mb-1">AI Intervention Log</h2>
        <p className="text-[13px] text-text-secondary leading-relaxed">
          This log displays real-time course corrections made by the Autonomous Fund Manager and Fatigue Engine.
          When an AI agent detects poor performance or channel fatigue, it intercepts the workflow and rewrites parameters automatically.
        </p>
      </div>

      <div className="flex flex-col gap-4">
        {isLoading ? (
          <div className="flex justify-center py-12"><Loader2 className="w-6 h-6 text-brand animate-spin" /></div>
        ) : isError ? (
          <div className="text-red-500 text-[13px] py-4 text-center bg-red-50 rounded-lg">Failed to load corrections.</div>
        ) : corrections.length === 0 ? (
          <div className="text-text-tertiary text-center py-12 text-[13px] bg-white rounded-xl border border-border-primary">
            No self-corrections have occurred yet.
          </div>
        ) : (
          corrections.map((c: any) => (
             <Card key={c.id} className="shadow-minimal border-border-primary overflow-hidden">
               <div className="flex flex-col md:flex-row md:items-stretch">
                  <div className={`w-1.5 shrink-0 ${c.triggerType === 'HIGH_FAILURE_RATE' ? 'bg-red-500' : c.triggerType === 'LOW_OPEN_RATE' ? 'bg-orange-500' : 'bg-brand'}`} />
                  <div className="p-5 flex-1 flex flex-col gap-3">
                     <div className="flex justify-between items-start">
                       <div className="flex items-center gap-2">
                          <Badge variant="outline" className={`text-[10px] uppercase font-medium tracking-wider px-2 py-0.5 rounded-sm border-0 ${
                            c.triggerType === 'HIGH_FAILURE_RATE' ? 'bg-red-100 text-red-800' :
                            c.triggerType === 'LOW_OPEN_RATE' ? 'bg-orange-100 text-orange-800' : 'bg-brand-light text-brand'
                          }`}>
                            {c.triggerType?.replace(/_/g, ' ')}
                          </Badge>
                          <span className="text-[12px] text-text-secondary">{new Date(c.createdAt).toLocaleString()}</span>
                       </div>
                       <div className="text-[12px] font-mono text-text-tertiary bg-bg-secondary px-2 py-0.5 rounded">Campaign: {c.campaignId?.substring(0,8)}</div>
                     </div>
                     
                     <div className="text-[13px] text-text-primary leading-relaxed">
                       {c.aiReasoning || "Autonomous intervention triggered based on statistical significance thresholds."}
                     </div>

                     {(c.oldChannel || c.newChannel) && (
                       <div className="flex items-center gap-4 mt-2 p-3 bg-bg-secondary rounded-lg border border-border-tertiary">
                          {c.oldChannel && (
                            <div className="flex-1">
                              <div className="text-[11px] text-text-tertiary uppercase tracking-wider mb-1">Previous Channel</div>
                              <div className="text-[13px] font-medium text-text-secondary line-through opacity-70">{c.oldChannel}</div>
                            </div>
                          )}
                          <ArrowRight className="w-4 h-4 text-text-tertiary shrink-0" />
                          {c.newChannel && (
                            <div className="flex-1">
                              <div className="text-[11px] text-brand uppercase tracking-wider mb-1">New Channel</div>
                              <div className="text-[13px] font-medium text-brand">{c.newChannel}</div>
                            </div>
                          )}
                       </div>
                     )}

                     <div className="flex items-center gap-4 mt-1 flex-wrap">
                       {c.actionTaken && (
                         <div className="text-[11px] text-text-secondary">Action: <span className="font-medium text-text-primary">{c.actionTaken?.replace(/_/g, ' ')}</span></div>
                       )}
                       {c.observedValue != null && (
                         <div className="text-[11px] text-text-secondary">Observed: <span className="font-mono">{(c.observedValue * 100).toFixed(1)}%</span></div>
                       )}
                       {c.triggerThreshold != null && (
                         <div className="text-[11px] text-text-secondary">Threshold: <span className="font-mono">{(c.triggerThreshold * 100).toFixed(1)}%</span></div>
                       )}
                       {c.correctionOutcome && (
                         <Badge className={`text-[10px] h-5 px-2 ${c.correctionOutcome === 'IMPROVED' ? 'bg-green-100 text-green-800 hover:bg-green-100' : 'bg-gray-100 text-gray-600 hover:bg-gray-100'}`}>
                           {c.correctionOutcome}
                         </Badge>
                       )}
                     </div>
                  </div>
               </div>
             </Card>
          ))
        )}
      </div>

      {!isLoading && !isError && totalPages > 1 && (
        <div className="flex justify-between items-center mt-6 text-[13px]">
           <span className="text-text-secondary">Page {page + 1} of {totalPages}</span>
           <div className="flex gap-2">
              <Button variant="outline" size="sm" disabled={page === 0} onClick={() => setPage(p => p - 1)}>Previous</Button>
              <Button variant="outline" size="sm" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next</Button>
           </div>
        </div>
      )}
    </Shell>
  );
}
