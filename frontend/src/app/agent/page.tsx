"use client";

import { Shell } from "@/components/layout/Shell";
import { Button } from "@/components/ui/button";
import { Bot, Send, Search, LayoutDashboard, Brain, MessageSquarePlus, RefreshCcw, Loader2 } from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { useState } from "react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { toast } from "sonner";
import { Badge } from "@/components/ui/badge";

export default function AgentPage() {
  const [input, setInput] = useState("");
  const [sessionId, setSessionId] = useState<string | null>(null);

  const chatMutation = useMutation({
    mutationFn: (prompt: string) => api.post("/agent/chat", { prompt }),
    onSuccess: (data: any) => {
      // Backend returns: { sessionId, textReply, actionTaken }
      const sid = data?.sessionId;
      if (sid) setSessionId(sid);
      toast.success(data?.textReply || "Agent started working on your request.");
    },
    onError: (err: any) => {
      toast.error(err.message || "Failed to start agent session.");
    }
  });

  const { data: sessionInfo } = useQuery({
    queryKey: ['agent-session', sessionId],
    queryFn: () => api.get(`/agent/sessions/${sessionId}`),
    enabled: !!sessionId,
    refetchInterval: (query) => (query.state.data?.status === 'COMPLETED' ? false : 3000),
  });

  const { data: decisionsData } = useQuery({
    queryKey: ['agent-decisions', sessionId],
    queryFn: () => api.get(`/agent/sessions/${sessionId}/decisions`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!sessionId,
    refetchInterval: (query) => ((sessionInfo as any)?.status === 'COMPLETED' ? false : 3000),
  });

  const decisions: any[] = Array.isArray(decisionsData) ? decisionsData : [];

  const handleSend = () => {
    if (!input.trim()) return;
    chatMutation.mutate(input);
    setInput("");
  };

  const getIconForDecision = (type: string) => {
    if (type?.includes('SEGMENT')) return Search;
    if (type?.includes('MEMORY')) return Brain;
    if (type?.includes('SIMULAT')) return RefreshCcw;
    return MessageSquarePlus;
  };

  return (
    <Shell title="Sovereign AI Agent Workspace">
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 h-[calc(100vh-140px)]">

        {/* Main Chat Interface */}
        <Card className="col-span-2 shadow-minimal border-border-primary flex flex-col h-full">
           <CardHeader className="pb-3 pt-5 px-5 shrink-0 border-b border-border-tertiary">
            <CardTitle className="text-[15px] font-medium flex items-center gap-2">
              <Bot className="w-5 h-5 text-brand" /> Sovereign AI Agent
              <Badge className="bg-green-100 text-green-800 hover:bg-green-100 font-medium text-[11px] px-2 h-5 flex items-center gap-1 ml-2">
                <div className="w-1.5 h-1.5 rounded-full bg-green-600" /> Live
              </Badge>
            </CardTitle>
          </CardHeader>
          <CardContent className="px-0 pb-0 flex-1 flex flex-col justify-between overflow-hidden">

            <div className="flex-1 overflow-y-auto p-6 flex flex-col gap-6">
              <AgentMessage>
                Hello! I&apos;m your Sovereign AI Agent. I can autonomously create segments, generate campaigns, run simulations, and analyse your CRM data. What would you like me to work on?
              </AgentMessage>

              {chatMutation.isPending && (
                <AgentMessage isThinking>
                  <div className="flex items-center gap-1.5">
                    <span className="w-2 h-2 bg-text-tertiary rounded-full animate-bounce [animation-delay:-0.3s]"></span>
                    <span className="w-2 h-2 bg-text-tertiary rounded-full animate-bounce [animation-delay:-0.15s]"></span>
                    <span className="w-2 h-2 bg-text-tertiary rounded-full animate-bounce"></span>
                  </div>
                </AgentMessage>
              )}

              {sessionInfo && (
                <AgentMessage>
                  <div className="mb-2"><strong>Status:</strong> {(sessionInfo as any).status}</div>
                  {(sessionInfo as any).status === 'COMPLETED' ? (
                    <>
                      I&apos;ve completed the task. Here&apos;s my plan:<br/><br/>
                      <pre className="bg-white p-3 rounded-md text-[11px] border border-border-tertiary overflow-x-auto whitespace-pre-wrap">
                        {typeof (sessionInfo as any).plan === 'object' ? JSON.stringify((sessionInfo as any).plan, null, 2) : (sessionInfo as any).plan || "Task completed."}
                      </pre>
                    </>
                  ) : (
                    <div className="flex items-center gap-2">
                      <div className="w-4 h-4 border-2 border-brand border-t-transparent rounded-full animate-spin"></div>
                      <span className="animate-pulse bg-gradient-to-r from-text-primary to-text-tertiary bg-clip-text text-transparent font-medium text-[13px]">Working on it...</span>
                    </div>
                  )}
                </AgentMessage>
              )}

            </div>

            <div className="p-4 border-t border-border-tertiary bg-bg-secondary shrink-0">
              <div className="flex items-center gap-3 bg-white border border-border-secondary rounded-xl p-2 shadow-sm">
                <input
                  type="text"
                  value={input}
                  onChange={(e) => setInput(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                  placeholder="Ask the agent to create a win-back campaign for VIPs..."
                  className="flex-1 bg-transparent px-3 py-1.5 text-[14px] outline-none"
                  disabled={chatMutation.isPending || (sessionInfo as any)?.status === 'RUNNING'}
                />
                <Button
                  onClick={handleSend}
                  disabled={!input.trim() || chatMutation.isPending || (sessionInfo as any)?.status === 'RUNNING'}
                  className="shrink-0 h-10 w-10 p-0 rounded-lg bg-brand hover:bg-brand/90 text-white"
                >
                  <Send className="w-4 h-4" />
                </Button>
              </div>
              <div className="flex items-center gap-3 mt-3 px-2 flex-wrap">
                <span className="text-[11px] text-text-tertiary font-medium uppercase tracking-wider">Suggested:</span>
                <Badge variant="outline" className="font-normal text-[11px] cursor-pointer hover:bg-bg-tertiary text-text-secondary border-border-secondary bg-white" onClick={() => setInput("Create a win-back campaign for VIPs")}>Win-back VIPs</Badge>
                <Badge variant="outline" className="font-normal text-[11px] cursor-pointer hover:bg-bg-tertiary text-text-secondary border-border-secondary bg-white" onClick={() => setInput("Clear dead stock for electronics")}>Clear dead stock</Badge>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Audit Trail & Session Info */}
        <div className="flex flex-col gap-6 overflow-y-auto">
          <Card className="shadow-minimal border-border-primary shrink-0">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <LayoutDashboard className="w-4 h-4 text-teal-600" /> Current session context
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5 text-[12px] flex flex-col gap-3">
              {sessionId ? (
                <>
                  <div className="flex justify-between border-b border-border-tertiary pb-2">
                    <span className="text-text-secondary">Session ID</span>
                    <span className="font-mono text-[10px] text-text-primary">{sessionId.substring(0,8)}...</span>
                  </div>
                   <div className="flex justify-between border-b border-border-tertiary pb-2">
                    <span className="text-text-secondary">Goal</span>
                    <span className="text-text-primary truncate max-w-[150px]">{(sessionInfo as any)?.goal || "Analyzing..."}</span>
                  </div>
                  <div className="flex justify-between">
                    <span className="text-text-secondary">Tokens used</span>
                    <span className="text-text-primary font-medium">{(sessionInfo as any)?.tokensUsedIn || 0} IN / {(sessionInfo as any)?.tokensUsedOut || 0} OUT</span>
                  </div>
                </>
              ) : (
                <div className="text-text-tertiary text-center py-4">No active session. Send a message to start.</div>
              )}
            </CardContent>
          </Card>

          <Card className="shadow-minimal border-border-primary flex-1">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Search className="w-4 h-4 text-orange-600" /> Agent reasoning trace
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5">
               <div className="text-[11px] text-text-secondary mb-4">Real-time audit log of the ReAct decisions taken by the agent.</div>

               <div className="flex flex-col gap-3 relative before:absolute before:inset-0 before:ml-2.5 before:-translate-x-px md:before:mx-auto md:before:translate-x-0 before:h-full before:w-0.5 before:bg-gradient-to-b before:from-transparent before:via-border-tertiary before:to-transparent">
                  {decisions.length === 0 ? (
                    <div className="text-text-tertiary text-[12px] text-center italic py-4">Awaiting decisions...</div>
                  ) : (
                    decisions.map((d: any, idx: number) => (
                      <TraceItem
                        key={d.id || idx}
                        icon={getIconForDecision(d.decisionType)}
                        title={d.decisionType}
                        color="text-brand bg-brand-light"
                        desc={d.reasoning}
                      />
                    ))
                  )}
               </div>
            </CardContent>
          </Card>
        </div>

      </div>
    </Shell>
  );
}

function AgentMessage({ children, isThinking = false }: { children: React.ReactNode, isThinking?: boolean }) {
  return (
    <div className="flex gap-4">
      <div className={`w-8 h-8 rounded-lg flex items-center justify-center shrink-0 mt-1 ${isThinking ? 'bg-bg-tertiary text-text-tertiary animate-pulse' : 'bg-brand-light text-brand'}`}>
        <Bot className="w-5 h-5" />
      </div>
      <div className="flex-1">
        <div className="text-[12px] font-medium text-text-tertiary mb-1">
          Sovereign AI Agent {isThinking && "· Thinking..."}
        </div>
        <div className={`p-4 rounded-2xl rounded-tl-sm text-[13px] leading-relaxed max-w-[85%] shadow-sm ${isThinking ? 'bg-bg-tertiary text-text-tertiary border border-border-primary' : 'bg-white border border-border-primary text-text-primary'}`}>
          {children}
        </div>
      </div>
    </div>
  );
}

function TraceItem({ icon: Icon, title, color, desc }: any) {
  return (
    <div className="relative flex items-center justify-between md:justify-normal md:odd:flex-row-reverse group is-active">
      <div className={`flex items-center justify-center w-6 h-6 rounded-full border-2 border-white shadow shrink-0 md:order-1 md:group-odd:-translate-x-1/2 md:group-even:translate-x-1/2 z-10 ${color}`}>
        <Icon className="w-3 h-3" />
      </div>
      <div className="w-[calc(100%-2.5rem)] md:w-[calc(50%-1.5rem)] bg-bg-secondary p-2.5 rounded border border-border-secondary shadow-sm">
        <div className="text-[11px] font-medium text-text-primary mb-1">{title}</div>
        <div className="text-[10px] text-text-secondary leading-tight line-clamp-3" title={desc}>{desc || 'Completed'}</div>
      </div>
    </div>
  );
}
