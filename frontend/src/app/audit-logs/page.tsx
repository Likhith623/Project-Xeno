"use client";

import { Shell } from "@/components/layout/Shell";
import { Card } from "@/components/ui/card";
import { Search, Bot, User, Loader2, Activity, Box } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState } from "react";

export default function AuditLogsPage() {
  const [activeFilter, setActiveFilter] = useState<'ACTOR'|'ENTITY'|'TRACE'>('ACTOR');

  const { data: actorLogs, isLoading: isLoadingActor, isError: isErrorActor } = useQuery({
    queryKey: ['audit-logs', 'actor'],
    queryFn: () => api.get(`/audit-logs/actor/AI_AGENT`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: activeFilter === 'ACTOR',
    staleTime: 30_000,
  });

  const { data: entityLogs, isLoading: isLoadingEntity, isError: isErrorEntity } = useQuery({
    queryKey: ['audit-logs', 'entity'],
    queryFn: () => api.get(`/audit-logs/entity/CAMPAIGN/00000000-0000-0000-0000-000000000000`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: activeFilter === 'ENTITY',
    staleTime: 30_000,
  });

  const { data: traceLogs, isLoading: isLoadingTrace, isError: isErrorTrace } = useQuery({
    queryKey: ['audit-logs', 'trace'],
    queryFn: () => api.get(`/audit-logs/trace/00000000-0000-0000-0000-000000000001`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: activeFilter === 'TRACE',
    staleTime: 30_000,
  });

  const logs: any[] = activeFilter === 'ACTOR' ? (Array.isArray(actorLogs) ? actorLogs : []) :
               activeFilter === 'ENTITY' ? (Array.isArray(entityLogs) ? entityLogs : []) :
               (Array.isArray(traceLogs) ? traceLogs : []);
  
  const isError = isErrorActor || isErrorEntity || isErrorTrace;
  const isLoading = isLoadingActor || isLoadingEntity || isLoadingTrace;

  return (
    <Shell title="Audit Logs" topbarActions={
      <div className="flex gap-2">
         <Button variant={activeFilter === 'ACTOR' ? 'default' : 'outline'} size="sm" onClick={() => setActiveFilter('ACTOR')} className="h-8 text-[12px] gap-2">
           <User className="w-3.5 h-3.5" /> By Actor
         </Button>
         <Button variant={activeFilter === 'ENTITY' ? 'default' : 'outline'} size="sm" onClick={() => setActiveFilter('ENTITY')} className="h-8 text-[12px] gap-2">
           <Box className="w-3.5 h-3.5" /> By Entity
         </Button>
         <Button variant={activeFilter === 'TRACE' ? 'default' : 'outline'} size="sm" onClick={() => setActiveFilter('TRACE')} className="h-8 text-[12px] gap-2">
           <Activity className="w-3.5 h-3.5" /> By Trace
         </Button>
      </div>
    }>
      <div className="flex flex-col sm:flex-row justify-between gap-4 mb-6">
        <div className="flex gap-2 w-full sm:w-auto">
          <div className="relative w-full sm:w-[300px]">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-text-tertiary" />
            <Input placeholder="Search logs..." className="pl-9 bg-white border-border-primary h-9 text-[13px]" />
          </div>
        </div>
      </div>

      <Card className="shadow-minimal border-border-primary overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-[13px] text-left">
            <thead className="bg-bg-secondary text-text-secondary border-b border-border-primary">
              <tr>
                <th className="px-6 py-3 font-medium">Time</th>
                <th className="px-6 py-3 font-medium">Actor</th>
                <th className="px-6 py-3 font-medium">Type</th>
                <th className="px-6 py-3 font-medium">Action</th>
                <th className="px-6 py-3 font-medium text-right">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border-tertiary">
              {isLoading ? (
                <tr>
                  <td colSpan={5} className="px-6 py-12 text-center">
                    <div className="flex justify-center items-center">
                      <Loader2 className="w-6 h-6 text-brand animate-spin" />
                    </div>
                  </td>
                </tr>
              ) : isError ? (
                <tr>
                  <td colSpan={5} className="px-6 py-12 text-center text-red-500">
                    Failed to load audit logs.
                  </td>
                </tr>
              ) : logs.length === 0 ? (
                <tr>
                  <td colSpan={5} className="px-6 py-12 text-center text-text-tertiary">
                    No AI Agent logs found in the system yet. Run a Sovereign AI session to generate logs.
                  </td>
                </tr>
              ) : (
                logs.map((log: any) => {
                  const isAi = log.actorType === 'AI_AGENT' || log.actorId === 'AI_AGENT';
                  return (
                    <tr key={log.id} className="hover:bg-bg-secondary/50 transition-colors">
                      <td className="px-6 py-3 text-text-secondary whitespace-nowrap">{new Date(log.createdAt).toLocaleString()}</td>
                      <td className="px-6 py-3">
                        <div className="flex items-center gap-2">
                          {isAi ? <Bot className="w-4 h-4 text-brand" /> : <User className="w-4 h-4 text-text-tertiary" />}
                          <span className={isAi ? "font-medium text-brand" : "text-text-secondary"}>{log.actorId || "AI_AGENT"}</span>
                        </div>
                      </td>
                      <td className="px-6 py-3 text-text-secondary">{log.entityType || "SYSTEM"}</td>
                      <td className="px-6 py-3 text-text-primary">{log.description || `${log.action || 'ACTION'} on ${log.entityType || 'ENTITY'}`}</td>
                      <td className="px-6 py-3 text-right">
                        <span className={`inline-flex items-center gap-1.5 text-green-600`}>
                          <div className={`w-1.5 h-1.5 rounded-full bg-green-600`} />
                          Success
                        </span>
                      </td>
                    </tr>
                  );
                })
              )}
            </tbody>
          </table>
        </div>
      </Card>
    </Shell>
  );
}
