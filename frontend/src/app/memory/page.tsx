"use client";

import { Shell } from "@/components/layout/Shell";
import { Button } from "@/components/ui/button";
import { Brain, RefreshCcw, Bot, Search, Send, Loader2, Sparkles, ExternalLink } from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { useQuery, useMutation } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState } from "react";
import { toast } from "sonner";
import { useAgentStore } from "@/store/useAgentStore";
import { useRouter } from "next/navigation";

export default function MemoryPage() {
  const [searchQuery, setSearchQuery] = useState("");
  const [channelFilter, setChannelFilter] = useState("");
  const [askQuery, setAskQuery] = useState("");
  const [chatInput, setChatInput] = useState("");

  // Use persisted messages from Zustand store
  const { messages: chatHistory, addMessage, setSessionId } = useAgentStore();
  const router = useRouter();

  // GET /memory — All memories
  const { data: memoryData, isLoading: isLoadingMemory, refetch: refetchMemory } = useQuery({
    queryKey: ['memory', searchQuery, channelFilter],
    queryFn: () => {
      const params = new URLSearchParams();
      if (searchQuery) params.set('segmentTag', searchQuery);
      if (channelFilter) params.set('channel', channelFilter);
      const url = (searchQuery || channelFilter) ? `/memory/query?${params.toString()}` : '/memory';
      return api.get(url).then(res => Array.isArray(res) ? res : res?.content || []);
    },
    staleTime: 30_000,
  });

  // GET /memory/ask?query= — Natural language query
  const { data: askData, isLoading: isLoadingAsk, refetch: refetchAsk, isFetching: isFetchingAsk } = useQuery({
    queryKey: ['memory-ask', askQuery],
    queryFn: () => api.get(`/memory/ask?query=${encodeURIComponent(askQuery)}`),
    enabled: false
  });

  const memories: any[] = Array.isArray(memoryData) ? memoryData : [];

  // Chat Mutation — stores replies in Zustand for persistence
  const chatMutation = useMutation({
    mutationFn: (prompt: string) => api.post("/agent/chat", { prompt }),
    onSuccess: (data: any, variables) => {
      const sid = data?.sessionId;
      if (sid) setSessionId(sid);
      addMessage({ role: 'user', content: variables });
      addMessage({
        role: 'agent',
        content: data?.textReply || `Agent started session. Check the Agent Workspace for real-time tracking.`
      });
      setChatInput("");
    },
    onError: (err: any) => {
      toast.error(err.message || "Failed to start agent session.");
    }
  });

  const handleSend = () => {
    if (!chatInput.trim()) return;
    chatMutation.mutate(chatInput);
  };

  // Filter only user+agent messages relevant to current context (all from store)
  const chatMessages = chatHistory;

  return (
    <Shell title="Org Memory & AI Agent" topbarActions={
      <>
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => refetchMemory()}>
          <RefreshCcw className="w-4 h-4" /> Refresh memory
        </Button>
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => router.push('/agent')}>
          <ExternalLink className="w-4 h-4" /> Agent Workspace
        </Button>
      </>
    }>
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4 sm:gap-6">

        {/* Left Column: Memory */}
        <div className="flex flex-col gap-6">
          <Card className="shadow-minimal border-border-primary">
             <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Brain className="w-4 h-4 text-brand" /> CRM Brain — Org Memory
                <Badge className="bg-brand-light text-brand hover:bg-brand-light font-medium text-[11px] px-2 h-5">{memories.length} learnings</Badge>
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5">
              <div className="flex gap-2 mb-3">
                <div className="flex-1 flex items-center gap-2 bg-bg-secondary border border-border-secondary rounded-lg px-3 py-2">
                  <Search className="w-4 h-4 text-text-tertiary shrink-0" />
                  <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    placeholder='Filter by segment tag...'
                    className="bg-transparent border-none outline-none text-[13px] text-text-primary w-full placeholder:text-text-tertiary"
                  />
                </div>
                <select
                  value={channelFilter}
                  onChange={(e) => setChannelFilter(e.target.value)}
                  className="bg-bg-secondary border border-border-secondary rounded-lg px-3 py-2 text-[13px] text-text-primary outline-none"
                >
                  <option value="">All Channels</option>
                  <option value="email">EMAIL</option>
                  <option value="whatsapp">WHATSAPP</option>
                  <option value="sms">SMS</option>
                  <option value="rcs">RCS</option>
                </select>
              </div>
              <div className="flex gap-2 mb-3 border-b border-border-tertiary pb-4">
                <div className="flex-1 flex items-center gap-2 bg-brand-light/30 border border-brand/20 rounded-lg px-3 py-2">
                  <Bot className="w-4 h-4 text-brand shrink-0" />
                  <input
                    type="text"
                    value={askQuery}
                    onChange={(e) => setAskQuery(e.target.value)}
                    onKeyDown={(e) => e.key === 'Enter' && refetchAsk()}
                    placeholder='Ask memory a question...'
                    className="bg-transparent border-none outline-none text-[13px] text-brand w-full placeholder:text-brand/60"
                  />
                </div>
                <Button onClick={() => refetchAsk()} disabled={!askQuery || isFetchingAsk} size="sm" className="h-[38px]">
                  {isFetchingAsk ? <Loader2 className="w-4 h-4 animate-spin" /> : "Ask AI"}
                </Button>
              </div>

              {askData && (
                 <div className="bg-brand text-white p-4 rounded-xl mb-4 text-[13px] leading-relaxed">
                   <div className="font-medium text-[11px] uppercase tracking-wider mb-2 opacity-80 flex items-center gap-1.5"><Sparkles className="w-3 h-3"/> AI Memory Answer</div>
                   {String(askData)}
                 </div>
              )}

              <div className="text-[11px] text-text-tertiary mb-4">
                {searchQuery || channelFilter ? `Filtered: ${searchQuery || 'all segments'} · ${channelFilter || 'all channels'}` : 'Showing: all channels · all segments'}
              </div>

              <div className="flex flex-col gap-2 max-h-[500px] overflow-y-auto pr-2">
                {isLoadingMemory ? (
                  <div className="flex justify-center items-center py-8">
                    <Loader2 className="w-5 h-5 text-brand animate-spin" />
                  </div>
                ) : memories.length === 0 ? (
                  <div className="text-center py-8 text-text-tertiary text-[13px]">
                    No insights found. {(searchQuery || channelFilter) && (
                      <button className="text-brand underline ml-1" onClick={() => { setSearchQuery(""); setChannelFilter(""); }}>
                        Clear filters
                      </button>
                    )}
                  </div>
                ) : (
                  memories.map((m: any) => (
                    <MemoryCard
                      key={m.id}
                      channel={m.channel || 'Unknown'}
                      chColor={
                        m.channel === 'whatsapp' ? 'bg-green-50 text-green-700' :
                        m.channel === 'sms' ? 'bg-purple-50 text-purple-700' :
                        m.channel === 'rcs' ? 'bg-teal-50 text-teal-700' :
                        'bg-blue-50 text-blue-700'
                      }
                      type={m.learningType || 'INSIGHT'}
                      conf={Math.round((m.confidence || 0) * 100).toString()}
                      text={m.learningSummary}
                      meta={`Segment: ${m.segmentTag || 'Global'} · Lift: +${((m.avgLift || 0) * 100).toFixed(0)}% · Evidence: ${m.evidenceCount || 0} campaigns`}
                    />
                  ))
                )}
              </div>
            </CardContent>
          </Card>
        </div>

        {/* Right Column: Agent Chat */}
        <div className="flex flex-col gap-6">
          <Card className="shadow-minimal border-border-primary flex-1 flex flex-col min-h-[500px]">
             <CardHeader className="pb-3 pt-5 px-5 shrink-0 border-b border-border-tertiary">
              <div className="flex items-center justify-between">
                <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                  <Bot className="w-4 h-4 text-brand" /> Sovereign AI Agent
                  <Badge className="bg-green-100 text-green-800 hover:bg-green-100 font-medium text-[11px] px-2 h-5">Live</Badge>
                </CardTitle>
                <button
                  className="text-[11px] text-text-tertiary hover:text-text-secondary underline"
                  onClick={() => router.push('/agent')}
                >
                  Full Workspace →
                </button>
              </div>
            </CardHeader>
            <CardContent className="px-5 pb-5 flex-1 flex flex-col justify-between overflow-hidden">
              <div className="flex flex-col gap-4 overflow-y-auto mb-4 p-1 max-h-[400px] flex-1">
                {/* Intro Msg */}
                {chatMessages.length === 0 && (
                  <div>
                    <div className="text-[11px] text-text-tertiary font-medium mb-1">Sovereign AI Agent</div>
                    <div className="bg-bg-secondary border border-border-tertiary text-text-primary p-3 rounded-2xl rounded-tl-sm text-[13px] leading-relaxed w-11/12">
                      Hello! I&apos;m your Sovereign AI Agent. I can autonomously create segments, generate campaigns, run simulations, and analyse your CRM data. What would you like me to work on?
                    </div>
                  </div>
                )}

                {chatMessages.map((msg, idx) => (
                  <div key={idx} className={msg.role === 'user' ? "self-end w-11/12 flex justify-end" : ""}>
                    {msg.role === 'agent' && <div className="text-[11px] text-text-tertiary font-medium mb-1">Sovereign AI Agent</div>}
                    <div className={msg.role === 'user'
                      ? "bg-brand text-white p-3 rounded-2xl rounded-tr-sm text-[13px] leading-relaxed inline-block"
                      : "bg-bg-secondary border border-border-tertiary text-text-primary p-3 rounded-2xl rounded-tl-sm text-[13px] leading-relaxed w-11/12"
                    }>
                      {msg.content}
                    </div>
                  </div>
                ))}

                {chatMutation.isPending && (
                  <div>
                    <div className="text-[11px] text-text-tertiary font-medium mb-1">Sovereign AI Agent · Thinking...</div>
                    <div className="bg-bg-secondary border border-border-tertiary text-text-primary p-3 rounded-2xl rounded-tl-sm flex items-center gap-2 w-16">
                      <div className="w-2 h-2 bg-text-tertiary rounded-full animate-bounce [animation-delay:-0.3s]"></div>
                      <div className="w-2 h-2 bg-text-tertiary rounded-full animate-bounce [animation-delay:-0.15s]"></div>
                      <div className="w-2 h-2 bg-text-tertiary rounded-full animate-bounce"></div>
                    </div>
                  </div>
                )}
              </div>

              <div className="flex items-center gap-2 pt-3 border-t border-border-tertiary shrink-0">
                <input
                  type="text"
                  value={chatInput}
                  onChange={(e) => setChatInput(e.target.value)}
                  onKeyDown={(e) => e.key === 'Enter' && handleSend()}
                  placeholder="Ask the agent to do something…"
                  className="flex-1 bg-bg-secondary border border-border-secondary rounded-lg px-4 py-2 text-[13px] outline-none"
                  disabled={chatMutation.isPending}
                />
                <Button onClick={handleSend} disabled={!chatInput.trim() || chatMutation.isPending} className="shrink-0 h-[38px] w-[38px] p-0">
                  <Send className="w-4 h-4" />
                </Button>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </Shell>
  );
}

function MemoryCard({ channel, chColor, type, conf, text, meta }: any) {
  return (
    <div className="bg-bg-secondary rounded-xl p-3.5 hover:bg-[#ebedef] transition-colors cursor-pointer">
      <div className="flex items-center gap-2 mb-2">
        <Badge className={`${chColor} hover:${chColor} font-medium text-[10px] px-2 h-5`}>{channel.toUpperCase()}</Badge>
        <Badge className="bg-brand-light text-brand hover:bg-brand-light font-medium text-[10px] px-2 h-5">{type}</Badge>
        <div className="ml-auto text-[12px] font-medium text-green-600">{conf}%</div>
      </div>
      <div className="text-[13px] text-text-primary leading-relaxed mb-1.5">{text}</div>
      <div className="text-[11px] text-text-tertiary">{meta}</div>
    </div>
  );
}
