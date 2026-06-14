"use client";

import { Shell } from "@/components/layout/Shell";
import { Button } from "@/components/ui/button";
import { Plus, Bot, Users, UserCircle, FlaskConical, Sword, DollarSign, Moon, Loader2, RefreshCcw, AlertTriangle, ExternalLink, Edit2, Trash2 } from "lucide-react";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { toast } from "sonner";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { useState } from "react";
import { useRouter } from "next/navigation";

// ─── Edit Segment Modal ───────────────────────────────────────────────────────
function EditSegmentModal({ segment, open, onClose }: { segment: any; open: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [name, setName] = useState(segment?.name || "");
  const [description, setDescription] = useState(segment?.description || "");
  const [filterSql, setFilterSql] = useState(segment?.filterSql || "");

  const mutation = useMutation({
    mutationFn: () => api.patch(`/segments/${segment.id}`, { name, description, filterSql }),
    onSuccess: () => {
      toast.success("Segment updated!");
      queryClient.invalidateQueries({ queryKey: ['segments'] });
      onClose();
    },
    onError: (err: any) => toast.error(err.message || "Failed to update segment"),
  });

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader><DialogTitle>Edit Segment</DialogTitle></DialogHeader>
        <div className="flex flex-col gap-4 py-4">
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Name</label>
            <Input value={name} onChange={e => setName(e.target.value)} />
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Description</label>
            <Input value={description} onChange={e => setDescription(e.target.value)} placeholder="What defines this segment?" />
          </div>
          {segment?.filterSql && (
            <div className="flex flex-col gap-2">
              <label className="text-[13px] font-medium">Filter SQL</label>
              <textarea
                className="flex min-h-[80px] w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring"
                value={filterSql}
                onChange={e => setFilterSql(e.target.value)}
              />
            </div>
          )}
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} className="flex-1">Cancel</Button>
          <Button onClick={() => mutation.mutate()} disabled={!name || mutation.isPending} className="flex-1">
            {mutation.isPending ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
            Save Changes
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

// ─── Main Page ────────────────────────────────────────────────────────────────
export default function SegmentsPage() {
  const [selectedSegmentId, setSelectedSegmentId] = useState<string | null>(null);
  const [isSegmentModalOpen, setIsSegmentModalOpen] = useState(false);
  const [editSegment, setEditSegment] = useState<any>(null);
  const [newSegmentName, setNewSegmentName] = useState("");
  const [newSegmentDescription, setNewSegmentDescription] = useState("");
  const [newSegmentType, setNewSegmentType] = useState("DYNAMIC");
  const [newSegmentFilterSql, setNewSegmentFilterSql] = useState("");
  const [warRoomResult, setWarRoomResult] = useState<string | null>(null);
  const [personaError, setPersonaError] = useState<string | null>(null);

  const router = useRouter();
  const queryClient = useQueryClient();

  const { data: segmentsData, isLoading, isError } = useQuery({
    queryKey: ['segments'],
    queryFn: () => api.get(`/segments`),
    staleTime: 30_000,
  });

  const segments: any[] = Array.isArray(segmentsData) ? segmentsData : (segmentsData?.content || []);
  const selectedSegment = segments.find(s => s.id === selectedSegmentId);

  const { data: personaData, isLoading: isLoadingPersona, refetch: refetchPersona, isFetching: isFetchingPersona, error: personaFetchError } = useQuery({
    queryKey: ['segment', selectedSegmentId, 'persona'],
    queryFn: async () => {
      setPersonaError(null);
      try {
        return await api.get(`/segments/${selectedSegmentId}/persona`);
      } catch (e: any) {
        setPersonaError(e.message || "Failed to generate persona");
        throw e;
      }
    },
    enabled: false,
    retry: false,
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
    onSuccess: (data: any) => {
      const result = typeof data === 'string' ? data : (data?.message || data?.result || "AI War Room triggered. Agents are debating strategy.");
      setWarRoomResult(result);
      toast.success("AI War Room triggered!", {
        description: "Agents are debating campaign strategy.",
        action: {
          label: "View Agent",
          onClick: () => router.push('/agent'),
        },
        duration: 8000,
      });
    },
    onError: (err: any) => toast.error(err.message || "Failed to trigger AI War Room.")
  });

  const fundManagerMutation = useMutation({
    mutationFn: () => api.post("/test/ai/trigger-fund-manager"),
    onSuccess: () => {
      toast.success("Autonomous Fund Manager triggered!", {
        description: "Campaign budgets are being re-allocated based on MAB confidence scores.",
        action: {
          label: "View MAB Dashboard",
          onClick: () => router.push('/mab'),
        },
        duration: 8000,
      });
    },
    onError: (err: any) => toast.error(err.message || "Failed to trigger Fund Manager.")
  });

  const omniAwarenessMutation = useMutation({
    mutationFn: () => api.post("/test/ai/trigger-omni-awareness"),
    onSuccess: () => {
      toast.success("Fatigue Engine triggered!", {
        description: "14-day channel cooldowns applied across users. Check Memory for recorded insights.",
        action: {
          label: "View Memory",
          onClick: () => router.push('/memory'),
        },
        duration: 8000,
      });
    },
    onError: (err: any) => toast.error(err.message || "Failed to trigger Omni-Awareness.")
  });

  const createSegmentMutation = useMutation({
    mutationFn: () => api.post("/segments", {
        name: newSegmentName,
        description: newSegmentDescription,
        type: newSegmentType,
        filterSql: newSegmentFilterSql
    }),
    onSuccess: () => {
        toast.success("Segment created");
        queryClient.invalidateQueries({queryKey: ['segments']});
        setIsSegmentModalOpen(false);
        setNewSegmentName(""); setNewSegmentDescription(""); setNewSegmentFilterSql("");
    },
    onError: (err: any) => toast.error(err.message || "Failed to create segment"),
  });

  const deleteSegmentMutation = useMutation({
    mutationFn: (id: string) => api.delete(`/segments/${id}`),
    onSuccess: () => {
      toast.success("Segment deleted");
      setSelectedSegmentId(null);
      queryClient.invalidateQueries({queryKey: ['segments']});
    },
    onError: (err: any) => toast.error(err.message || "Failed to delete segment"),
  });

  const evaluateSegmentMutation = useMutation({
    mutationFn: () => api.post(`/segments/${selectedSegmentId}/evaluate`),
    onSuccess: () => {
      toast.success("Segment evaluation triggered");
      queryClient.invalidateQueries({queryKey: ['segment', selectedSegmentId, 'members']});
    }
  });

  const handleGeneratePersona = () => {
    if (!selectedSegmentId) {
      toast.error("Please select a segment first");
      return;
    }
    setPersonaError(null);
    refetchPersona();
  };

  return (
    <Shell title="Segments & AI Personas" topbarActions={
      <>
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => {
          toast.info("Use the AI Agent to create segments intelligently.", {
            action: { label: "Open Agent", onClick: () => router.push('/agent') }
          });
        }}>
          <Bot className="w-4 h-4" /> AI create segment
        </Button>
        <Button size="sm" className="h-8 text-[13px] gap-2" onClick={() => setIsSegmentModalOpen(true)}>
          <Plus className="w-4 h-4" /> New segment
        </Button>
      </>
    }>
      <div className="grid grid-cols-1 lg:grid-cols-[1fr_340px] gap-4 sm:gap-6">

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
                <div className="text-text-tertiary text-[13px] py-8 text-center flex flex-col items-center gap-3">
                  <Users className="w-8 h-8 text-text-tertiary/50" />
                  No segments found. Create your first segment to get started.
                </div>
              ) : (
                segments.map((segment: any) => (
                  <div
                    key={segment.id}
                    onClick={() => setSelectedSegmentId(segment.id)}
                    className={`flex items-center gap-3 py-3 border-b border-border-tertiary last:border-0 cursor-pointer transition-colors ${selectedSegmentId === segment.id ? 'bg-brand-light/30 border-l-2 border-l-brand pl-4 -ml-5' : 'hover:bg-bg-secondary/50'}`}
                  >
                    <div className="w-8 h-8 rounded-lg flex items-center justify-center shrink-0 text-brand bg-brand-light">
                      <Users className="w-4 h-4" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <div className="flex items-center gap-2 mb-1 flex-wrap">
                        <span className="text-[13px] font-medium text-text-primary truncate">{segment.name}</span>
                        {(segment.type === 'DYNAMIC' || !!segment.filterSql) && (
                          <Badge className="text-[10px] h-5 px-2 bg-brand-light text-brand hover:bg-brand-light font-medium">Dynamic</Badge>
                        )}
                      </div>
                      <div className="text-[12px] text-text-secondary truncate">{segment.description || segment.filterSql || "No description"}</div>
                    </div>
                    <div className="text-right shrink-0 flex flex-col items-end gap-1">
                      {segment.customerCount !== undefined && (
                        <div className="text-[12px] font-medium text-text-primary">{segment.customerCount.toLocaleString()} members</div>
                      )}
                      <div className="text-[11px] text-text-tertiary">{new Date(segment.createdAt).toLocaleDateString()}</div>
                    </div>
                  </div>
                ))
              )}
            </CardContent>
          </Card>

          {/* Segment Members Preview */}
          {selectedSegmentId && (
            <Card className="shadow-minimal border-border-primary">
              <CardHeader className="pb-2 pt-4 px-5">
                <div className="flex justify-between items-start">
                  <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                    <Users className="w-4 h-4 text-blue-600" /> Member Preview — {selectedSegment?.name}
                    <Badge className="bg-blue-50 text-blue-700 hover:bg-blue-50 text-[10px] px-1.5 h-5 ml-2">{members.length} loaded</Badge>
                  </CardTitle>
                  <div className="flex gap-2 flex-wrap">
                    <Button variant="outline" size="sm" className="h-7 text-[11px] px-2" onClick={() => evaluateSegmentMutation.mutate()} disabled={evaluateSegmentMutation.isPending}>
                      <RefreshCcw className="w-3 h-3 mr-1" /> Re-evaluate
                    </Button>
                    <Button variant="outline" size="sm" className="h-7 text-[11px] px-2 text-blue-600 border-blue-200 hover:bg-blue-50" onClick={() => setEditSegment(selectedSegment)}>
                      <Edit2 className="w-3 h-3 mr-1" /> Edit
                    </Button>
                    <Button variant="outline" size="sm" className="h-7 text-[11px] px-2 text-red-600 border-red-200 hover:bg-red-50" onClick={() => deleteSegmentMutation.mutate(selectedSegmentId!)} disabled={deleteSegmentMutation.isPending}>
                      <Trash2 className="w-3 h-3 mr-1" /> Delete
                    </Button>
                  </div>
                </div>
              </CardHeader>
              <CardContent className="px-5 pb-2 text-[13px] overflow-x-auto">
                 {isLoadingMembers ? (
                   <div className="flex justify-center py-4"><Loader2 className="w-4 h-4 text-brand animate-spin" /></div>
                 ) : members.length === 0 ? (
                   <div className="text-text-tertiary text-center py-4 text-[12px]">No members found. Try re-evaluating the segment.</div>
                 ) : (
                   <table className="w-full text-left text-[12px]">
                     <thead>
                       <tr className="text-text-secondary border-b border-border-tertiary">
                         <th className="font-medium pb-2">Customer ID</th>
                         <th className="font-medium pb-2">Added At</th>
                       </tr>
                     </thead>
                     <tbody className="divide-y divide-border-tertiary">
                       {members.slice(0, 10).map((m: any) => (
                         <tr key={m.customerId || m.id}>
                           <td className="py-2 font-mono text-text-secondary text-[11px]">{m.customerId?.substring(0,16) || 'Unknown'}...</td>
                           <td className="py-2 text-text-tertiary">{m.addedAt ? new Date(m.addedAt).toLocaleDateString() : 'N/A'}</td>
                         </tr>
                       ))}
                     </tbody>
                   </table>
                 )}
              </CardContent>
            </Card>
          )}
        </div>

        {/* Sidebar */}
        <div className="flex flex-col gap-6">

          {/* AI Persona Generator */}
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <UserCircle className="w-4 h-4 text-brand" /> AI Persona Generator
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5">
              {!selectedSegmentId ? (
                <div className="text-[12px] text-text-secondary text-center py-4">
                  Select a segment on the left to generate a Buyer Persona.
                </div>
              ) : isFetchingPersona ? (
                 <div className="flex flex-col items-center justify-center py-6 gap-3">
                   <Loader2 className="w-6 h-6 text-brand animate-spin" />
                   <span className="text-[12px] text-text-secondary animate-pulse">Analyzing segment behavior with AI...</span>
                 </div>
              ) : personaError ? (
                <div className="flex flex-col gap-3">
                  <div className="flex items-center gap-2 bg-red-50 border border-red-100 rounded-lg px-3 py-2.5 text-[12px] text-red-700">
                    <AlertTriangle className="w-4 h-4 shrink-0" />
                    <span>{personaError}</span>
                  </div>
                  <Button variant="outline" className="w-full h-8 text-[12px]" onClick={handleGeneratePersona}>
                    <RefreshCcw className="w-3 h-3 mr-2" /> Try Again
                  </Button>
                </div>
              ) : personaData ? (
                <div className="flex flex-col gap-3">
                  <div className="flex items-center gap-3 border-b border-border-tertiary pb-3">
                    <div className="w-10 h-10 rounded-full bg-brand-light text-brand flex items-center justify-center text-lg font-medium shrink-0">
                      {(personaData as any).name ? (personaData as any).name[0] : 'P'}
                    </div>
                    <div>
                      <div className="text-[14px] font-medium text-text-primary">{(personaData as any).name || "Persona"}</div>
                      <div className="text-[12px] text-text-secondary">{(personaData as any).ageRange || "Age N/A"} · {(personaData as any).incomeBracket || "Income N/A"}</div>
                    </div>
                  </div>
                  <div className="text-[12px] text-text-primary leading-relaxed">
                    {(personaData as any).bio || (personaData as any).description || "No bio available for this persona."}
                  </div>
                  {((personaData as any).motivations || []).length > 0 && (
                    <div className="bg-bg-secondary rounded-lg p-3">
                      <div className="text-[11px] font-medium text-text-secondary uppercase tracking-wider mb-2">Key Motivations</div>
                      <ul className="text-[12px] text-text-primary flex flex-col gap-1.5 list-disc pl-4">
                        {((personaData as any).motivations || ['Exclusivity', 'Quality']).map((m: string, i: number) => <li key={i}>{m}</li>)}
                      </ul>
                    </div>
                  )}
                  <Button variant="outline" className="w-full h-8 text-[12px] mt-2" onClick={handleGeneratePersona}>
                    <RefreshCcw className="w-3 h-3 mr-2" /> Regenerate
                  </Button>
                </div>
              ) : (
                <>
                  <div className="text-[12px] text-text-secondary mb-4">
                    Selected: <span className="font-medium text-text-primary">{selectedSegment?.name}</span>. Click below to generate a buyer persona using AI analysis.
                  </div>
                  <Button
                    variant="outline"
                    className="w-full h-8 text-[12px]"
                    onClick={handleGeneratePersona}
                  >
                    <UserCircle className="w-3.5 h-3.5 mr-2" /> Generate Persona
                  </Button>
                </>
              )}
            </CardContent>
          </Card>

          {/* AI Developer Panel */}
          <Card className="shadow-minimal border-orange-200 bg-orange-50/30">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[13px] font-medium flex items-center gap-2 text-orange-600">
                <FlaskConical className="w-4 h-4" /> AI Developer Panel
                <Badge className="bg-orange-100 text-orange-800 font-normal hover:bg-orange-100 px-1.5 py-0 text-[10px]">Internal testing</Badge>
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5 flex flex-col gap-2">
              <DevBtn
                icon={Sword} title="Trigger AI War Room" desc="Two LLMs debate campaign strategy and produce a compromise plan"
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

              {/* War Room result */}
              {warRoomResult && (
                <div className="mt-2 bg-white border border-orange-200 rounded-lg p-3 text-[12px] text-text-primary leading-relaxed">
                  <div className="font-medium text-orange-700 mb-1 text-[11px] uppercase tracking-wider">War Room Output</div>
                  {warRoomResult}
                  <button
                    className="mt-2 flex items-center gap-1 text-[11px] text-brand hover:underline"
                    onClick={() => router.push('/agent')}
                  >
                    <ExternalLink className="w-3 h-3" /> View in Agent workspace
                  </button>
                </div>
              )}

              <div className="mt-2 pt-2 border-t border-orange-100">
                <button
                  className="flex items-center gap-1.5 text-[12px] text-orange-700 hover:text-orange-900 transition-colors"
                  onClick={() => router.push('/agent')}
                >
                  <ExternalLink className="w-3.5 h-3.5" /> Open Agent Workspace to see results
                </button>
              </div>
            </CardContent>
          </Card>

        </div>
      </div>

      {/* Create Segment Modal */}
      <Dialog open={isSegmentModalOpen} onOpenChange={setIsSegmentModalOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader><DialogTitle>Create New Segment</DialogTitle></DialogHeader>
          <div className="flex flex-col gap-4 py-4">
            <div className="flex flex-col gap-2">
              <label className="text-[13px] font-medium">Name *</label>
              <Input value={newSegmentName} onChange={e => setNewSegmentName(e.target.value)} placeholder="e.g. High Value Customers" />
            </div>
            <div className="flex flex-col gap-2">
              <label className="text-[13px] font-medium">Description</label>
              <Input value={newSegmentDescription} onChange={e => setNewSegmentDescription(e.target.value)} placeholder="What defines this segment?" />
            </div>
            <div className="flex flex-col gap-2">
              <label className="text-[13px] font-medium">Type</label>
              <select
                className="flex h-9 w-full items-center justify-between whitespace-nowrap rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-ring"
                value={newSegmentType}
                onChange={e => setNewSegmentType(e.target.value)}
              >
                <option value="DYNAMIC">Dynamic (SQL based)</option>
                <option value="STATIC">Static</option>
              </select>
            </div>
            {newSegmentType === 'DYNAMIC' && (
              <div className="flex flex-col gap-2">
                <label className="text-[13px] font-medium">Filter SQL</label>
                <textarea
                  className="flex min-h-[80px] w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring"
                  value={newSegmentFilterSql}
                  onChange={e => setNewSegmentFilterSql(e.target.value)}
                  placeholder="e.g. monetary_total > 1000 AND status = 'active'"
                />
              </div>
            )}
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsSegmentModalOpen(false)} className="flex-1">Cancel</Button>
            <Button onClick={() => createSegmentMutation.mutate()} disabled={!newSegmentName || createSegmentMutation.isPending} className="flex-1">
              {createSegmentMutation.isPending ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
              Create Segment
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Edit Segment Modal */}
      {editSegment && (
        <EditSegmentModal segment={editSegment} open={!!editSegment} onClose={() => setEditSegment(null)} />
      )}
    </Shell>
  );
}

function DevBtn({ icon: Icon, title, desc, onClick, isLoading }: any) {
  return (
    <button onClick={onClick} disabled={isLoading} className="w-full p-2.5 rounded-md border border-border-secondary bg-white hover:bg-bg-secondary transition-colors flex items-center gap-3 text-left disabled:opacity-50">
      {isLoading ? <Loader2 className="w-4 h-4 text-brand shrink-0 animate-spin" /> : <Icon className="w-4 h-4 text-brand shrink-0" />}
      <div>
        <div className="text-[13px] text-text-primary font-medium leading-snug">{title}</div>
        <div className="text-[11px] text-text-secondary mt-0.5 leading-snug">{desc}</div>
      </div>
    </button>
  );
}
