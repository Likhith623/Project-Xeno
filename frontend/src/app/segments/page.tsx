"use client";

import { Shell } from "@/components/layout/Shell";
import { Button } from "@/components/ui/button";
import { Plus, Bot, Users, UserCircle, FlaskConical, Sword, DollarSign, Moon, Loader2, RefreshCcw } from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { toast } from "sonner";
import { useState } from "react";

export default function SegmentsPage() {
  const [selectedSegmentId, setSelectedSegmentId] = useState<string | null>(null);

  const { data: segmentsData, isLoading, isError } = useQuery({
    queryKey: ['segments'],
    queryFn: () => api.get(`/segments`),
    staleTime: 30_000,
  });

  const segments: any[] = Array.isArray(segmentsData) ? segmentsData : (segmentsData?.content || []);

  const { data: personaData, isLoading: isLoadingPersona, refetch: refetchPersona, isFetching: isFetchingPersona } = useQuery({
    queryKey: ['segment', selectedSegmentId, 'persona'],
    queryFn: () => api.get(`/segments/${selectedSegmentId}/persona`),
    enabled: false // Only fetch when button is clicked
  });

  const { data: membersData, isLoading: isLoadingMembers } = useQuery({
    queryKey: ['segment', selectedSegmentId, 'members'],
    queryFn: () => api.get(`/segments/${selectedSegmentId}/members`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!selectedSegmentId,
    staleTime: 30_000,
  });

  const members: any[] = Array.isArray(membersData) ? membersData : [];

  const warRoomMutation = useMutation({
    mutationFn: () => api.post("/test/ai/trigger-war-room", { goal: "Win back churned users with maximum urgency across all channels" }),
    onSuccess: () => toast.success("AI War Room triggered successfully. Check Agent feed."),
    onError: () => toast.error("Failed to trigger AI War Room.")
  });

  const fundManagerMutation = useMutation({
    mutationFn: () => api.post("/test/ai/trigger-fund-manager"),
    onSuccess: () => toast.success("Autonomous Fund Manager triggered. Budgets re-allocating."),
    onError: () => toast.error("Failed to trigger Fund Manager.")
  });

  const omniAwarenessMutation = useMutation({
    mutationFn: () => api.post("/test/ai/trigger-omni-awareness"),
    onSuccess: () => toast.success("Fatigue Engine triggered. Cooldowns applied."),
    onError: () => toast.error("Failed to trigger Omni-Awareness.")
  });

  const queryClient = useQueryClient();

  const createSegmentMutation = useMutation({
    mutationFn: () => api.post("/segments", { name: "New High Value Segment", filterSql: "monetary_total > 1000", description: "Customers with total spend above ₹1000", type: "DYNAMIC" }),
    onSuccess: () => { toast.success("Segment created"); queryClient.invalidateQueries({queryKey: ['segments']}); }
  });

  const deleteSegmentMutation = useMutation({
    mutationFn: () => api.delete(`/segments/${selectedSegmentId}`),
    onSuccess: () => {
      toast.success("Segment deleted");
      setSelectedSegmentId(null);
      queryClient.invalidateQueries({queryKey: ['segments']});
    }
  });

  const evaluateSegmentMutation = useMutation({
    mutationFn: () => api.post(`/segments/${selectedSegmentId}/evaluate`),
    onSuccess: () => { toast.success("Segment evaluation triggered"); queryClient.invalidateQueries({queryKey: ['segment', selectedSegmentId, 'members']}); }
  });

  const patchSegmentMutation = useMutation({
    mutationFn: () => api.patch(`/segments/${selectedSegmentId}`, { name: "Updated Segment Name", description: "Updated via frontend" }),
    onSuccess: () => { toast.success("Segment updated"); queryClient.invalidateQueries({queryKey: ['segments']}); }
  });

  const handleGeneratePersona = () => {
    if (!selectedSegmentId) {
      toast.error("Please select a segment first");
      return;
    }
    refetchPersona();
  };

  return (
    <Shell title="Segments & AI Personas" topbarActions={
      <>
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2">
          <Bot className="w-4 h-4" /> AI create segment
        </Button>
        <Button size="sm" className="h-8 text-[13px] gap-2" onClick={() => createSegmentMutation.mutate()} disabled={createSegmentMutation.isPending}>
          <Plus className="w-4 h-4" /> New segment
        </Button>
      </>
    }>
      <div className="grid grid-cols-1 lg:grid-cols-[1fr_340px] gap-6">

        {/* Segments List */}
        <div className="flex flex-col gap-6">
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Users className="w-4 h-4" /> All segments <span className="text-[13px] font-normal text-text-secondary ml-1">{segments.length} total</span>
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-2 flex flex-col gap-0">
              {isLoading ? (
                <div className="flex justify-center items-center py-8">
                  <Loader2 className="w-5 h-5 text-brand animate-spin" />
                </div>
              ) : isError ? (
                <div className="text-red-500 text-[13px] py-4 text-center">Failed to load segments</div>
              ) : segments.length === 0 ? (
                <div className="text-text-tertiary text-[13px] py-4 text-center">No segments found</div>
              ) : (
                segments.map((segment: any) => (
                  <SegmentRow
                    key={segment.id}
                    icon={Users} iconColor="text-brand bg-brand-light"
                    name={segment.name}
                    isDynamic={segment.type === 'DYNAMIC' || !!segment.filterSql}
                    meta={segment.description || segment.filterSql || "No description"}
                    count={segment.customerCount ?? '~'}
                    time={`Created ${new Date(segment.createdAt).toLocaleDateString()}`}
                    isSelected={selectedSegmentId === segment.id}
                    onClick={() => setSelectedSegmentId(segment.id)}
                  />
                ))
              )}
            </CardContent>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="flex flex-col gap-6">

          {/* AI Persona */}
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <UserCircle className="w-4 h-4 text-brand" /> AI Persona Generator
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5">
              {!personaData && !isFetchingPersona ? (
                <>
                  <div className="text-[12px] text-text-secondary mb-4">
                    Select a segment on the left to dynamically generate a Buyer Persona using LLM analysis.
                  </div>
                  <Button
                    variant="outline"
                    className="w-full h-8 text-[12px]"
                    onClick={handleGeneratePersona}
                    disabled={!selectedSegmentId}
                  >
                    Generate Persona
                  </Button>
                </>
              ) : isFetchingPersona ? (
                 <div className="flex flex-col items-center justify-center py-6 gap-3">
                   <Loader2 className="w-6 h-6 text-brand animate-spin" />
                   <span className="text-[12px] text-text-secondary animate-pulse">Analyzing segment behavior...</span>
                 </div>
              ) : personaData && (
                <div className="flex flex-col gap-3">
                  <div className="flex items-center gap-3 border-b border-border-tertiary pb-3">
                    <div className="w-10 h-10 rounded-full bg-brand-light text-brand flex items-center justify-center text-lg font-medium shrink-0">
                      {(personaData as any).name ? (personaData as any).name[0] : 'P'}
                    </div>
                    <div>
                      <div className="text-[14px] font-medium text-text-primary">{(personaData as any).name || "Unknown"}</div>
                      <div className="text-[12px] text-text-secondary">{(personaData as any).ageRange || "Age N/A"} · {(personaData as any).incomeBracket || "Income N/A"}</div>
                    </div>
                  </div>
                  <div className="text-[12px] text-text-primary leading-relaxed">
                    {(personaData as any).bio || (personaData as any).description || "No bio available for this persona."}
                  </div>
                  <div className="bg-bg-secondary rounded-lg p-3">
                    <div className="text-[11px] font-medium text-text-secondary uppercase tracking-wider mb-2">Key Motivations</div>
                    <ul className="text-[12px] text-text-primary flex flex-col gap-1.5 list-disc pl-4">
                      {((personaData as any).motivations || ['Exclusivity', 'Quality']).map((m: string, i: number) => <li key={i}>{m}</li>)}
                    </ul>
                  </div>
                  <Button variant="outline" className="w-full h-8 text-[12px] mt-2" onClick={handleGeneratePersona}>
                    <RefreshCcw className="w-3 h-3 mr-2" /> Regenerate
                  </Button>
                </div>
              )}
            </CardContent>
          </Card>

          {/* Segment Members Preview */}
          {selectedSegmentId && (
            <Card className="shadow-minimal border-border-primary">
              <CardHeader className="pb-2 pt-4 px-5">
                <div className="flex justify-between items-start">
                  <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                    <Users className="w-4 h-4 text-blue-600" /> Member Preview
                    <Badge className="bg-blue-50 text-blue-700 hover:bg-blue-50 text-[10px] px-1.5 h-5 ml-2">{members.length} sample</Badge>
                  </CardTitle>
                  <div className="flex gap-2">
                    <Button variant="outline" size="sm" className="h-7 text-[11px] px-2" onClick={() => evaluateSegmentMutation.mutate()} disabled={evaluateSegmentMutation.isPending}>
                      <RefreshCcw className="w-3 h-3 mr-1" /> Force Evaluate
                    </Button>
                    <Button variant="outline" size="sm" className="h-7 text-[11px] px-2 text-blue-600 border-blue-200 hover:bg-blue-50" onClick={() => patchSegmentMutation.mutate()} disabled={patchSegmentMutation.isPending}>
                       Edit
                    </Button>
                    <Button variant="outline" size="sm" className="h-7 text-[11px] px-2 text-red-600 border-red-200 hover:bg-red-50" onClick={() => deleteSegmentMutation.mutate()} disabled={deleteSegmentMutation.isPending}>
                       Delete
                    </Button>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="px-5 pb-2 text-[13px] overflow-x-auto">
                 {isLoadingMembers ? (
                   <div className="flex justify-center py-4"><Loader2 className="w-4 h-4 text-brand animate-spin" /></div>
                 ) : members.length === 0 ? (
                   <div className="text-text-tertiary text-center py-4 text-[12px]">No members found in this segment.</div>
                 ) : (
                   <table className="w-full text-left text-[12px]">
                     <thead>
                       <tr className="text-text-secondary border-b border-border-tertiary">
                         <th className="font-medium pb-2">Customer ID</th>
                         <th className="font-medium pb-2">Added At</th>
                       </tr>
                     </thead>
                     <tbody className="divide-y divide-border-tertiary">
                       {members.slice(0, 5).map((m: any) => (
                         <tr key={m.customerId || m.segmentId}>
                           <td className="py-2 font-mono text-text-secondary text-[11px]">{m.customerId?.substring(0,12) || 'Unknown'}...</td>
                           <td className="py-2 text-text-tertiary">{m.addedAt ? new Date(m.addedAt).toLocaleDateString() : 'N/A'}</td>
                         </tr>
                       ))}
                     </tbody>
                   </table>
                 )}
              </CardContent>
            </Card>
          )}

          {/* Dev Panel */}
          <Card className="shadow-minimal border-orange-200 bg-orange-50/30">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[13px] font-medium flex items-center gap-2 text-orange-600">
                <FlaskConical className="w-4 h-4" /> AI Developer Panel
                <Badge className="bg-orange-100 text-orange-800 font-normal hover:bg-orange-100 px-1.5 py-0 text-[10px]">Internal testing</Badge>
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5 flex flex-col gap-2">
              <DevBtn
                icon={Sword} title="Trigger AI War Room" desc="Two LLMs debate campaign strategy and produce a compromise"
                onClick={() => warRoomMutation.mutate()} isLoading={warRoomMutation.isPending}
              />
              <DevBtn
                icon={DollarSign} title="Trigger Fund Manager" desc="Re-allocate live campaign budgets via MAB confidence scores"
                onClick={() => fundManagerMutation.mutate()} isLoading={fundManagerMutation.isPending}
              />
              <DevBtn
                icon={Moon} title="Trigger Omni-Awareness" desc="Run fatigue engine — apply 14-day channel cooldowns across all users"
                onClick={() => omniAwarenessMutation.mutate()} isLoading={omniAwarenessMutation.isPending}
              />
            </CardContent>
          </Card>

        </div>
      </div>
    </Shell>
  );
}

function SegmentRow({ icon: Icon, iconColor, name, meta, count, time, isDynamic, isSelected, onClick }: any) {
  return (
    <div
      onClick={onClick}
      className={`flex items-center gap-3 py-3 border-b border-border-tertiary last:border-0 cursor-pointer transition-colors ${isSelected ? 'bg-brand-light/30 border-l-2 border-l-brand pl-4 -ml-5' : 'hover:bg-bg-secondary/50'}`}
    >
      <div className={`w-8 h-8 rounded-lg flex items-center justify-center shrink-0 ${iconColor}`}>
        <Icon className="w-4 h-4" />
      </div>
      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-2 mb-1 flex-wrap">
          <span className="text-[13px] font-medium text-text-primary truncate">{name}</span>
          {isDynamic && <Badge className="text-[10px] h-5 px-2 bg-brand-light text-brand hover:bg-brand-light font-medium">Dynamic</Badge>}
        </div>
        <div className="text-[12px] text-text-secondary">{meta}</div>
      </div>
      <div className="text-right shrink-0">
        <div className="text-[11px] text-text-tertiary mt-1">{time}</div>
      </div>
    </div>
  );
}

function DevBtn({ icon: Icon, title, desc, onClick, isLoading }: any) {
  return (
    <button onClick={onClick} disabled={isLoading} className="w-full p-2.5 rounded-md border border-border-secondary bg-white hover:bg-bg-secondary transition-colors flex items-center gap-3 text-left">
      {isLoading ? <Loader2 className="w-4 h-4 text-brand shrink-0 animate-spin" /> : <Icon className="w-4 h-4 text-brand shrink-0" />}
      <div>
        <div className="text-[13px] text-text-primary font-medium leading-snug">{title}</div>
        <div className="text-[11px] text-text-secondary mt-0.5 leading-snug">{desc}</div>
      </div>
    </button>
  );
}
