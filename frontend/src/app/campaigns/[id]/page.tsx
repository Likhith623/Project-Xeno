"use client";

import { Shell } from "@/components/layout/Shell";
import { Button } from "@/components/ui/button";
import { BarChart3, Copy, Pause, Play, Sparkles, Bot, Clock, ArrowLeft, Loader2, X, Mail, Edit, Zap, AlertTriangle, CheckCircle, FileText, List } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import Link from "next/link";
import { useParams } from "next/navigation";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState } from "react";
import { toast } from "sonner";

export default function CampaignDetail() {
  const params = useParams();
  const campaignId = params.id as string;
  const [showSim, setShowSim] = useState(false);
  const [latestSimulationId, setLatestSimulationId] = useState<string | null>(null);
  const [selectedVariantId, setSelectedVariantId] = useState<string | null>(null);
  const queryClient = useQueryClient();

  // ─── CORE DATA ───────────────────────────────────────────────────────────────
  const { data: campaign, isLoading: isCampaignLoading } = useQuery({
    queryKey: ['campaign', campaignId],
    queryFn: () => api.get(`/campaigns/${campaignId}`),
    retry: false,
    staleTime: 30_000,
  });

  const { data: performance } = useQuery({
    queryKey: ['campaign', campaignId, 'performance'],
    queryFn: () => api.get(`/campaigns/${campaignId}/performance`),
    enabled: !!campaign,
    staleTime: 30_000,
  });

  const { data: mabStats } = useQuery({
    queryKey: ['campaign', campaignId, 'mab-stats'],
    queryFn: () => api.get(`/campaigns/${campaignId}/variants/mab-stats`),
    enabled: !!campaign,
    staleTime: 30_000,
  });

  // ─── VARIANTS: GET /api/v1/variants/campaign/{campaignId} ─────────────────
  const { data: variantsList } = useQuery({
    queryKey: ['variants', 'campaign', campaignId],
    queryFn: () => api.get(`/variants/campaign/${campaignId}`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!campaign,
    staleTime: 30_000,
  });

  // ─── VARIANT DETAIL: GET /api/v1/variants/{id} ───────────────────────────
  const { data: selectedVariant, isLoading: isLoadingVariant } = useQuery({
    queryKey: ['variant', selectedVariantId],
    queryFn: () => api.get(`/variants/${selectedVariantId}`),
    enabled: !!selectedVariantId,
    staleTime: 30_000,
  });

  // ─── CAMPAIGN CORRECTIONS: GET /api/v1/campaigns/{id}/corrections ──────────
  const { data: campaignCorrections } = useQuery({
    queryKey: ['campaign', campaignId, 'corrections'],
    queryFn: () => api.get(`/campaigns/${campaignId}/corrections`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!campaign,
    staleTime: 30_000,
  });

  // ─── NARRATIVE ANALYTICS: GET /api/v1/campaigns/{id}/analytics/narrative ───
  const { data: narrativeAnalytics, isLoading: isNarrativeLoading, refetch: refetchNarrative } = useQuery({
    queryKey: ['campaign', campaignId, 'narrative'],
    queryFn: () => api.get(`/campaigns/${campaignId}/analytics/narrative`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!campaign,
    staleTime: 30_000,
  });

  // ─── TIMELINE: GET /api/v1/campaigns/{id}/timeline ───────────────────────
  const { data: timelineData, isLoading: isTimelineLoading } = useQuery({
    queryKey: ['campaign', campaignId, 'timeline'],
    queryFn: () => api.get(`/campaigns/${campaignId}/timeline`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!campaign,
    staleTime: 30_000,
  });

  // ─── COMMUNICATIONS ───────────────────────────────────────────────────────
  const { data: communications, refetch: refetchComms } = useQuery({
    queryKey: ['campaign', campaignId, 'communications'],
    queryFn: () => api.get(`/communications/campaign/${campaignId}`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!campaign,
    staleTime: 30_000,
  });

  // ─── MUTATIONS ────────────────────────────────────────────────────────────

  // Counterfactual Simulation: POST /api/v1/simulations/campaigns/{id}/counterfactual
  const simMutation = useMutation({
    mutationFn: () => api.post(`/simulations/campaigns/${campaignId}/counterfactual?channel=EMAIL`).then(res => Array.isArray(res) ? res : res?.content || []),
    onSuccess: () => toast.success("Simulation complete"),
    onError: () => toast.error("Simulation failed")
  });

  // Execute Campaign: POST /api/v1/campaigns/{id}/execute
  const executeMutation = useMutation({
    mutationFn: () => api.post(`/campaigns/${campaignId}/execute`),
    onSuccess: () => { toast.success("Campaign execution started"); queryClient.invalidateQueries({queryKey: ['campaign', campaignId]}); },
    onError: () => toast.error("Failed to execute campaign")
  });

  // Update Campaign Status PATCH: /api/v1/campaigns/{id}/status
  const patchStatusMutation = useMutation({
    mutationFn: (status: string) => api.patch(`/campaigns/${campaignId}/status`, { status }),
    onSuccess: () => { toast.success("Campaign status updated"); queryClient.invalidateQueries({queryKey: ['campaign', campaignId]}); },
    onError: () => toast.error("Failed to update status")
  });

  // Pause/Update status: PATCH /api/v1/campaigns/{id}/status (no PUT endpoint exists)
  const updateMutation = useMutation({
    mutationFn: () => api.patch(`/campaigns/${campaignId}/status`, { status: 'PAUSED' }),
    onSuccess: () => { toast.success("Campaign paused"); queryClient.invalidateQueries({queryKey: ['campaign', campaignId]}); }
  });

  // Approve Campaign: POST /api/v1/campaigns/{id}/approve
  const approveMutation = useMutation({
    mutationFn: () => api.post(`/campaigns/${campaignId}/approve`),
    onSuccess: () => { toast.success("Campaign approved and launched!"); queryClient.invalidateQueries({queryKey: ['campaign', campaignId]}); }
  });

  // Variant mutations using real data from variantsList
  const createVariantMutation = useMutation({
    mutationFn: () => api.post(`/variants`, { campaignId, name: "New Email Variant", channel: "email", subjectLine: "Hello {{name}}, we have an exclusive offer!", bodyText: "Check out our latest products." }),
    onSuccess: () => { toast.success("Variant created"); queryClient.invalidateQueries({queryKey: ['variants', 'campaign', campaignId]}); }
  });

  const patchVariantMutation = useMutation({
    mutationFn: (variantId: string) => api.patch(`/variants/${variantId}`, { subjectLine: "Updated subject line", bodyText: "Updated email body content." }),
    onSuccess: () => { toast.success("Variant updated"); queryClient.invalidateQueries({queryKey: ['variants', 'campaign', campaignId]}); }
  });

  const deleteVariantMutation = useMutation({
    mutationFn: (variantId: string) => api.delete(`/variants/${variantId}`),
    onSuccess: () => { toast.success("Variant deleted"); queryClient.invalidateQueries({queryKey: ['variants', 'campaign', campaignId]}); }
  });

  // Simulation triggers: POST /api/v1/simulations & POST /campaigns/{id}/simulate
  const simGeneralMutation = useMutation({
    mutationFn: () => api.post(`/simulations`, { campaignId, syntheticAudienceSize: 1000 }),
    onSuccess: (data: any) => {
      toast.success("General simulation triggered");
      if (data?.id) setLatestSimulationId(data.id);
    }
  });

  const simCampaignMutation = useMutation({
    mutationFn: () => api.post(`/campaigns/${campaignId}/simulate`, { syntheticAudienceSize: 1000 }),
    onSuccess: (data: any) => {
      toast.success("Campaign simulation triggered");
      if (data?.id) setLatestSimulationId(data.id);
    }
  });

  // GET /api/v1/simulations/{id} — Poll for results
  const { data: latestSimulationResult, isLoading: isLoadingSimResult } = useQuery({
    queryKey: ['simulation', latestSimulationId],
    queryFn: () => api.get(`/simulations/${latestSimulationId}`),
    enabled: !!latestSimulationId,
    refetchInterval: (query) => (query.state.data as any)?.status === 'COMPLETED' ? false : 3000
  });

  // Communication status PATCH: /api/v1/communications/{id}/status
  const patchCommStatusMutation = useMutation({
    mutationFn: (commId: string) => api.patch(`/communications/${commId}/status?status=READ`),
    onSuccess: () => { toast.success("Marked as read"); refetchComms(); }
  });

  if (isCampaignLoading) {
    return (
      <Shell title="Campaign Details">
        <div className="flex justify-center items-center h-64">
          <Loader2 className="w-8 h-8 text-brand animate-spin" />
        </div>
      </Shell>
    );
  }

  if (!campaign) {
    return (
      <Shell title="Campaign Not Found">
        <div className="text-center py-12 text-red-500">Failed to load campaign data.</div>
      </Shell>
    );
  }

  const handleSimulate = () => {
    setShowSim(true);
    simMutation.mutate();
  };

  const topbarActions = (
    <>
      <Link href="/campaigns">
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2 mr-2">
          <ArrowLeft className="w-4 h-4" /> Back
        </Button>
      </Link>
      <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => updateMutation.mutate()} disabled={updateMutation.isPending}>
        <Edit className="w-4 h-4" /> Edit
      </Button>
      <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => executeMutation.mutate()} disabled={executeMutation.isPending}>
        <Zap className="w-4 h-4" /> Execute
      </Button>
      <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={handleSimulate}>
        <BarChart3 className="w-4 h-4" /> Simulate
      </Button>
      {campaign?.status === 'DRAFT' && (
        <Button size="sm" className="h-8 text-[13px] gap-2 bg-brand" onClick={() => approveMutation.mutate()} disabled={approveMutation.isPending}>
          <CheckCircle className="w-4 h-4" /> Approve
        </Button>
      )}
      {campaign?.status === 'RUNNING' ? (
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2 text-orange-600 border-orange-200 hover:bg-orange-50" onClick={() => patchStatusMutation.mutate('PAUSED')} disabled={patchStatusMutation.isPending}>
          {patchStatusMutation.isPending ? <Loader2 className="w-4 h-4 animate-spin" /> : <Pause className="w-4 h-4" />} Pause
        </Button>
      ) : campaign?.status === 'PAUSED' ? (
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2 text-green-600 border-green-200 hover:bg-green-50" onClick={() => patchStatusMutation.mutate('RUNNING')} disabled={patchStatusMutation.isPending}>
          {patchStatusMutation.isPending ? <Loader2 className="w-4 h-4 animate-spin" /> : <Play className="w-4 h-4" />} Resume
        </Button>
      ) : (
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2 text-green-600 border-green-200 hover:bg-green-50" onClick={() => patchStatusMutation.mutate('RUNNING')} disabled={patchStatusMutation.isPending}>
          {patchStatusMutation.isPending ? <Loader2 className="w-4 h-4 animate-spin" /> : <Play className="w-4 h-4" />} Launch
        </Button>
      )}
    </>
  );

  return (
    <Shell title={campaign.name || "Campaign Details"} topbarActions={topbarActions}>
      <div className="flex items-center gap-2 mb-6 -mt-2">
        <div className={`flex items-center gap-1.5 text-[12px] px-2.5 py-0.5 rounded-full font-medium ${
          campaign.status === 'RUNNING' ? 'bg-green-100 text-green-800' :
          campaign.status === 'PAUSED' ? 'bg-orange-100 text-orange-800' :
          campaign.status === 'COMPLETED' ? 'bg-blue-100 text-blue-800' :
          campaign.status === 'FAILED' ? 'bg-red-100 text-red-800' :
          'bg-gray-100 text-gray-800'
        }`}>
          <div className={`w-2 h-2 rounded-full ${
            campaign.status === 'RUNNING' ? 'bg-green-600 animate-pulse' :
            campaign.status === 'PAUSED' ? 'bg-orange-600' :
            campaign.status === 'COMPLETED' ? 'bg-blue-600' :
            campaign.status === 'FAILED' ? 'bg-red-600' : 'bg-gray-600'
          }`} /> {campaign.status || 'DRAFT'}
        </div>
        {campaign.createdByAgent && (
          <Badge className="bg-brand-light text-brand hover:bg-brand-light font-medium text-[11px] h-6"><Bot className="w-3 h-3 mr-1"/>AI Created</Badge>
        )}
      </div>

      {showSim && (
        <Card className="shadow-minimal border-brand bg-brand-light/20 mb-6 relative overflow-hidden">
          <Button variant="ghost" size="sm" className="absolute top-2 right-2 h-6 w-6 p-0" onClick={() => setShowSim(false)}>
            <X className="w-4 h-4" />
          </Button>
          <CardHeader className="pb-2 pt-4 px-5">
            <CardTitle className="text-[14px] font-medium flex items-center gap-2">
              <BarChart3 className="w-4 h-4 text-brand" /> Counterfactual Simulator
              {simMutation.isPending && <Loader2 className="w-3 h-3 animate-spin text-brand" />}
            </CardTitle>
            <div className="text-[12px] text-text-secondary mt-1">Predicted performance if you ran this campaign on a single channel.</div>
          </CardHeader>
          <CardContent className="px-5 pb-5 flex flex-col gap-2 mt-2">
            {simMutation.isPending ? (
              <div className="py-4 text-center text-[12px] text-text-tertiary animate-pulse">Running synthetic population simulation...</div>
            ) : simMutation.isError ? (
               <div className="py-4 text-center text-[12px] text-red-500">Simulation failed. Check backend logs.</div>
            ) : simMutation.data?.length > 0 ? (
               simMutation.data.map((sim: any, i: number) => {
                 let color = "bg-blue-500";
                 let channelName = "Email";
                 if (sim.channel?.includes('WHATSAPP')) { color = "bg-green-600"; channelName = "WhatsApp"; }
                 if (sim.channel?.includes('SMS')) { color = "bg-orange-600"; channelName = "SMS"; }
                 const metrics = [
                   { label: "Open", val: ((sim.predictedOpenRate || 0) * 100).toFixed(1), pct: (sim.predictedOpenRate || 0) * 100 },
                   { label: "Conv.", val: ((sim.predictedConversionRate || 0) * 100).toFixed(1), pct: (sim.predictedConversionRate || 0) * 100 }
                 ];
                 return <SimRow key={i} channel={channelName} color={color} metrics={metrics} />;
               })
            ) : (
               <>
                 <SimRow channel="Email" color="bg-blue-500" metrics={[{label: "Open", val: 38.2, pct: 62}, {label: "Conv.", val: 5.8, pct: 30}]} />
                 <SimRow channel="WhatsApp" color="bg-green-600" metrics={[{label: "Read", val: 84.0, pct: 86}, {label: "Conv.", val: 12.4, pct: 52}]} />
                 <SimRow channel="SMS" color="bg-orange-600" metrics={[{label: "CTR", val: 18.0, pct: 18}, {label: "Conv.", val: 3.1, pct: 14}]} />
               </>
            )}
            <div className="flex gap-2 mt-2 pt-2 border-t border-border-tertiary">
              <Button variant="outline" size="sm" className="h-7 text-[11px]" onClick={() => simGeneralMutation.mutate()} disabled={simGeneralMutation.isPending}>
                General Sim
              </Button>
              <Button variant="outline" size="sm" className="h-7 text-[11px]" onClick={() => simCampaignMutation.mutate()} disabled={simCampaignMutation.isPending}>
                Campaign Sim
              </Button>
            </div>
            
            {latestSimulationId && (
               <div className="mt-3 pt-3 border-t border-border-tertiary text-[11px]">
                 <div className="font-medium text-text-primary mb-1 flex items-center gap-2">
                   Latest Monte Carlo Result
                   {isLoadingSimResult || latestSimulationResult?.status === 'RUNNING' || latestSimulationResult?.status === 'PENDING' ? <Loader2 className="w-3 h-3 animate-spin text-brand" /> : null}
                 </div>
                 {latestSimulationResult ? (
                   <div className="grid grid-cols-2 gap-2 mt-2">
                     <div className="bg-white p-2 rounded border border-border-tertiary">
                       <div className="text-text-tertiary">Status</div>
                       <div className={`font-medium ${latestSimulationResult.status === 'COMPLETED' ? 'text-green-600' : 'text-orange-600'}`}>{latestSimulationResult.status}</div>
                     </div>
                     <div className="bg-white p-2 rounded border border-border-tertiary">
                       <div className="text-text-tertiary">Projected Revenue</div>
                       <div className="font-medium text-text-primary">${latestSimulationResult.predictedRevenue?.toFixed(2) || '0.00'}</div>
                     </div>
                     <div className="bg-white p-2 rounded border border-border-tertiary">
                       <div className="text-text-tertiary">Projected Conv.</div>
                       <div className="font-medium text-text-primary">{((latestSimulationResult.predictedConversionRate || 0) * 100).toFixed(1)}%</div>
                     </div>
                     <div className="bg-white p-2 rounded border border-border-tertiary">
                       <div className="text-text-tertiary">CI Low (95%)</div>
                       <div className="font-medium text-text-primary">{((latestSimulationResult.confidenceIntervalLow || 0) * 100).toFixed(1)}%</div>
                     </div>
                   </div>
                 ) : (
                   <div className="text-text-tertiary italic">Fetching result...</div>
                 )}
               </div>
            )}
          </CardContent>
        </Card>
      )}

      {/* KPI Strip */}
      <div className="grid grid-cols-6 border border-border-primary bg-white rounded-xl overflow-hidden mb-6 divide-x divide-border-primary">
        <KpiStripItem value={performance?.totalSent || "0"} label="Total sent" />
        <KpiStripItem value={performance?.totalDelivered || "0"} label="Delivered" />
        <KpiStripItem value={`${(performance?.openRatePct || 0).toFixed(1)}%`} label="Open rate" />
        <KpiStripItem value={`${(performance?.ctrPct || 0).toFixed(1)}%`} label="CTR" />
        <KpiStripItem value={`${(performance?.conversionRatePct || 0).toFixed(1)}%`} label="Conversion" />
        <KpiStripItem value={`$${(performance?.revenueAttributed || 0).toFixed(2)}`} label="Revenue" valueColor="text-green-600" />
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-[1fr_360px] gap-6">
        <div className="flex flex-col gap-6">

          {/* AI Narrative Analytics: GET /campaigns/{id}/analytics/narrative */}
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-5 px-5">
              <div className="flex items-center justify-between">
                <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                  <Sparkles className="w-4 h-4 text-brand" /> AI Narrative Analysis
                  {isNarrativeLoading && <Loader2 className="w-3 h-3 animate-spin text-brand" />}
                </CardTitle>
                <Button variant="outline" size="sm" className="h-6 text-[11px] px-2" onClick={() => refetchNarrative()}>Refresh</Button>
              </div>
            </CardHeader>
            <CardContent className="px-5 pb-5">
              {narrativeAnalytics && Array.isArray(narrativeAnalytics) && narrativeAnalytics.length > 0 ? (
                <div className="flex flex-col gap-2">
                  {narrativeAnalytics.map((line: string, i: number) => (
                    <div key={i} className="bg-bg-secondary p-3 rounded-lg text-[13px] text-text-primary leading-relaxed border-l-2 border-brand">
                      {line}
                    </div>
                  ))}
                </div>
              ) : (
                <div className="bg-bg-secondary p-4 rounded-xl text-[13px] text-text-primary leading-relaxed border-l-4 border-brand">
                  {campaign.goal || "AI Narrative analysis is being generated. Click Refresh or allow 24 hours for the model to generate insights based on performance data."}
                </div>
              )}
              <div className="grid grid-cols-2 gap-3 mt-4">
                <div className="bg-bg-secondary rounded-lg p-3 text-center">
                  <div className="text-[16px] font-medium text-green-600">${(performance?.revenueAttributed || 0).toFixed(2)}</div>
                  <div className="text-[11px] text-text-secondary mt-1">Actual revenue</div>
                </div>
                <div className="bg-bg-secondary rounded-lg p-3 text-center">
                  <div className="text-[16px] font-medium text-brand">{performance?.totalConverted || 0}</div>
                  <div className="text-[11px] text-text-secondary mt-1">Total conversions</div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Variants List: GET /variants/campaign/{campaignId} + PATCH /variants/{id} */}
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-5 px-5">
              <div className="flex items-center justify-between">
                <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                  <List className="w-4 h-4 text-brand" /> Campaign Variants
                  <Badge className="bg-brand-light text-brand hover:bg-brand-light text-[10px] px-1.5 h-5">{Array.isArray(variantsList) ? variantsList.length : 0} variants</Badge>
                </CardTitle>
                <Button size="sm" className="h-6 text-[11px] px-2" onClick={() => createVariantMutation.mutate()} disabled={createVariantMutation.isPending}>
                  + Variant
                </Button>
              </div>
            </CardHeader>
            <CardContent className="px-5 pb-5">
              {!variantsList || variantsList.length === 0 ? (
                <div className="text-[12px] text-text-tertiary text-center py-4">No variants found. Create one to enable A/B testing.</div>
              ) : (
                <div className="overflow-x-auto">
                  <table className="w-full text-[12px]">
                    <thead className="bg-bg-secondary text-text-secondary">
                      <tr>
                        <th className="px-3 py-2 text-left font-medium">Channel</th>
                        <th className="px-3 py-2 text-left font-medium">Content Preview</th>
                        <th className="px-3 py-2 text-right font-medium">Actions</th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-border-tertiary">
                      {variantsList.map((v: any) => (
                        <>
                          <tr key={v.id}
                            className={`hover:bg-bg-secondary/50 cursor-pointer ${selectedVariantId === v.id ? 'bg-brand-light/20' : ''}`}
                            onClick={() => setSelectedVariantId(selectedVariantId === v.id ? null : v.id)}
                          >
                            <td className="px-3 py-2">
                              <Badge variant="outline" className="text-[10px]">{v.channel || 'EMAIL'}</Badge>
                            </td>
                            <td className="px-3 py-2 text-text-secondary truncate max-w-[200px]">{v.subjectLine || v.bodyText || v.name || 'No content'}</td>
                            <td className="px-3 py-2 text-right flex gap-1 justify-end">
                              <Button variant="outline" size="sm" className="h-6 text-[10px] px-2" onClick={(e) => { e.stopPropagation(); patchVariantMutation.mutate(v.id); }} disabled={patchVariantMutation.isPending}>Edit</Button>
                              <Button variant="outline" size="sm" className="h-6 text-[10px] px-2 text-red-600 border-red-200" onClick={(e) => { e.stopPropagation(); deleteVariantMutation.mutate(v.id); }} disabled={deleteVariantMutation.isPending}>Del</Button>
                            </td>
                          </tr>
                          {selectedVariantId === v.id && (
                            <tr key={`${v.id}-detail`}>
                              <td colSpan={3} className="px-3 py-3 bg-bg-secondary border-b border-border-tertiary">
                                {isLoadingVariant ? (
                                  <div className="flex items-center gap-2 text-[11px] text-text-tertiary"><Loader2 className="w-3 h-3 animate-spin" /> Loading variant detail...</div>
                                ) : selectedVariant ? (
                                  <div className="grid grid-cols-2 gap-2 text-[11px]">
                                    <div><span className="text-text-tertiary">ID: </span><span className="font-mono text-text-secondary">{selectedVariant.id?.substring(0,12)}...</span></div>
                                    <div><span className="text-text-tertiary">Channel: </span><span className="font-medium text-text-primary uppercase">{selectedVariant.channel}</span></div>
                                    <div><span className="text-text-tertiary">Impressions: </span><span className="font-medium text-text-primary">{selectedVariant.mabImpressions || 0}</span></div>
                                    <div><span className="text-text-tertiary">Conversions: </span><span className="font-medium text-green-600">{selectedVariant.mabConversions || 0}</span></div>
                                    <div><span className="text-text-tertiary">α (alpha): </span><span className="font-medium text-text-primary">{selectedVariant.mabAlpha?.toFixed(2) || '1.00'}</span></div>
                                    <div><span className="text-text-tertiary">Expected rate: </span><span className="font-medium text-brand">{selectedVariant.mabAlpha && selectedVariant.mabBeta ? ((selectedVariant.mabAlpha / (selectedVariant.mabAlpha + selectedVariant.mabBeta)) * 100).toFixed(1) + '%' : '50.0%'}</span></div>
                                  </div>
                                ) : null}
                              </td>
                            </tr>
                          )}
                        </>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </CardContent>
          </Card>

          {/* MAB Stats */}
          <Card className="shadow-minimal border-border-primary">
             <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Bot className="w-4 h-4 text-brand" /> MAB Thompson Sampling
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5 flex flex-col">
              {!mabStats || !Array.isArray(mabStats) || mabStats.length === 0 ? (
                <div className="text-[12px] text-text-tertiary text-center py-4">No MAB performance data yet. Create variants and execute the campaign to see Thompson Sampling in action.</div>
              ) : (
                (mabStats as any[]).map((v: any, idx: number) => {
                  const rate = Number(v.expectedConversionRate || 0);
                  const isWinner = rate > 0.4;
                  return (
                    <VariantRow
                      key={v.variantId || idx}
                      name={v.variantName || `Variant ${String.fromCharCode(65 + idx)}`}
                      badge={isWinner ? "Winning" : "Standard"} 
                      badgeColor={isWinner ? "bg-green-100 text-green-800" : "bg-gray-100 text-gray-700"}
                      isWinner={isWinner}
                      meta={`Alpha: ${(v.mabAlpha || 0).toFixed ? Number(v.mabAlpha || 0).toFixed(1) : '1.0'} · Beta: ${Number(v.mabBeta || 0).toFixed(1)} · Sends: ${v.mabImpressions || 0}`}
                      percent={(rate * 100).toFixed(1)} 
                      color={isWinner ? "bg-brand" : "bg-blue-600"}
                    />
                  );
                })
              )}
            </CardContent>
          </Card>

          {/* Campaign Corrections: GET /campaigns/{id}/corrections */}
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-2 pt-4 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <AlertTriangle className="w-4 h-4 text-orange-600" /> AI Self-Corrections
                <Badge className="bg-orange-50 text-orange-700 hover:bg-orange-50 text-[10px] px-1.5 h-5">{Array.isArray(campaignCorrections) ? campaignCorrections.length : 0}</Badge>
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-4 text-[13px]">
              {!campaignCorrections || campaignCorrections.length === 0 ? (
                <div className="text-text-tertiary text-center py-3 text-[12px]">No corrections triggered for this campaign.</div>
              ) : (
                campaignCorrections.slice(0, 3).map((c: any) => (
                  <div key={c.id} className="flex flex-col gap-1 py-2 border-b border-border-tertiary last:border-0">
                    <div className="flex items-center gap-2">
                      <Badge variant="outline" className="text-[10px] bg-orange-50 text-orange-700 border-0">{c.triggerType?.replace(/_/g, ' ')}</Badge>
                      <span className="text-[11px] text-text-tertiary">{c.createdAt ? new Date(c.createdAt).toLocaleString() : ''}</span>
                    </div>
                    <span className="text-text-secondary text-[12px]">{c.aiReasoning || "Autonomous adjustment applied."}</span>
                  </div>
                ))
              )}
            </CardContent>
          </Card>

        </div>

        <div className="flex flex-col gap-6">

          {/* Timeline: GET /campaigns/{id}/timeline */}
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Clock className="w-4 h-4 text-teal-600" /> Campaign Timeline
                {isTimelineLoading && <Loader2 className="w-3 h-3 animate-spin text-text-tertiary" />}
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5 flex flex-col gap-0">
              {timelineData && Array.isArray(timelineData) && timelineData.length > 0 ? (
                timelineData.map((item: string, i: number) => (
                  <div key={i} className="flex gap-3 py-2 border-b border-border-tertiary last:border-0">
                    <div className="w-2 h-2 rounded-full bg-brand mt-1.5 shrink-0" />
                    <div className="text-[12px] text-text-primary leading-snug">{item}</div>
                  </div>
                ))
              ) : (
                <>
                  <TimelineItem color="bg-brand" title="Campaign created" time={new Date(campaign.createdAt).toLocaleString()} hasLine={!!campaign.startedAt || !!campaign.completedAt} />
                  {campaign.startedAt && <TimelineItem color="bg-teal-600" title="Campaign launched" time={new Date(campaign.startedAt).toLocaleString()} hasLine={!!campaign.completedAt} />}
                  {campaign.completedAt && <TimelineItem color="bg-orange-600" title="Campaign completed" time={new Date(campaign.completedAt).toLocaleString()} hasLine={false} />}
                </>
              )}
            </CardContent>
          </Card>

          {/* Communications: GET /communications/campaign/{id} + PATCH /communications/{id}/status */}
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-2 pt-4 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Mail className="w-4 h-4 text-blue-600" /> Recent Communications
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-2 text-[13px]">
               {!communications || communications.length === 0 ? (
                 <div className="text-text-tertiary text-center py-4 text-[12px]">No messages sent yet.</div>
               ) : (
                 communications.slice(0, 5).map((comm: any) => (
                   <div key={comm.id} className="flex flex-col gap-1 py-2 border-b border-border-tertiary last:border-0">
                     <div className="flex items-center justify-between">
                       <span className={`text-[10px] px-2 py-0.5 rounded-md font-medium ${
                           comm.channel === 'email' ? 'bg-blue-100 text-blue-800' :
                           comm.channel === 'whatsapp' ? 'bg-green-100 text-green-800' :
                           comm.channel === 'sms' ? 'bg-orange-100 text-orange-800' : 'bg-purple-100 text-purple-800'
                       }`}>{(comm.channel || '').toUpperCase()}</span>
                       <div className="flex items-center gap-1.5">
                         <span className={`text-[10px] px-2 py-0.5 rounded-md font-medium ${
                             comm.status === 'DELIVERED' || comm.status === 'READ' ? 'text-green-700' :
                             comm.status === 'FAILED' ? 'text-red-700' : 'text-gray-700'
                         }`}>{comm.status}</span>
                         {comm.status !== 'READ' && (
                           <Button variant="ghost" size="sm" className="h-5 text-[10px] px-1.5" onClick={() => patchCommStatusMutation.mutate(comm.id)}>
                             Mark Read
                           </Button>
                         )}
                       </div>
                     </div>
                     <span className="text-text-secondary text-[12px] truncate">{comm.personalisedSubject || comm.personalisedBody || "Automated message"}</span>
                   </div>
                 ))
               )}
            </CardContent>
          </Card>

          {/* Campaign Details Card */}
          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-5 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <FileText className="w-4 h-4 text-text-tertiary" /> Campaign Details
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5 text-[12px] flex flex-col gap-2">
              {[
                ['Segment', campaign.segmentName || 'All customers'],
                ['Goal', campaign.goal || 'N/A'],
                ['Total Sent', campaign.totalSent?.toString() || '0'],
                ['Created', new Date(campaign.createdAt).toLocaleDateString()],
                ['AI Generated', campaign.createdByAgent ? 'Yes' : 'No'],
              ].map(([k, v]) => (
                <div key={k as string} className="flex justify-between border-b border-border-tertiary pb-1.5">
                  <span className="text-text-tertiary">{k}</span>
                  <span className="text-text-primary font-medium">{v}</span>
                </div>
              ))}
            </CardContent>
          </Card>
        </div>
      </div>
    </Shell>
  );
}

function KpiStripItem({ value, label, valueColor = "text-text-primary" }: any) {
  return (
    <div className="p-4 text-center">
      <div className={`text-[20px] font-medium mb-1 ${valueColor}`}>{value}</div>
      <div className="text-[11px] text-text-secondary">{label}</div>
    </div>
  );
}

function SimRow({ channel, color, metrics }: any) {
  return (
    <div className="flex items-center gap-3 p-2.5 bg-bg-secondary rounded-md">
      <div className="text-[12px] font-medium w-[70px] shrink-0 flex items-center gap-1.5">
        <div className={`w-2 h-2 rounded-full ${color}`} /> {channel}
      </div>
      <div className="flex-1 flex flex-col gap-1.5">
        {metrics.map((m: any, i: number) => (
          <div key={i} className="flex items-center gap-2">
            <span className="text-[11px] text-text-secondary w-[45px] text-right shrink-0">{m.label}</span>
            <div className="h-1.5 bg-border-tertiary rounded-full flex-1 overflow-hidden">
              <div className={`h-full ${color}`} style={{ width: `${m.pct}%` }} />
            </div>
            <span className="text-[11px] text-text-secondary w-[45px] text-right shrink-0">{m.val}%</span>
          </div>
        ))}
      </div>
    </div>
  );
}

function VariantRow({ name, badge, badgeColor, isWinner, meta, percent, color }: any) {
  return (
    <div className="flex items-center gap-4 py-3 border-b border-border-tertiary last:border-0">
      <div className="flex-1 min-w-0">
        <div className="flex items-center gap-2 mb-1">
          <span className="text-[13px] font-medium text-text-primary truncate">{name}</span>
          <span className={`text-[11px] px-2 py-0.5 rounded-md font-medium shrink-0 flex items-center gap-1 ${badgeColor}`}>
            {isWinner && <Sparkles className="w-3 h-3" />} {badge}
          </span>
        </div>
        <div className="text-[12px] text-text-secondary">{meta}</div>
      </div>
      <div className="w-[120px] shrink-0">
        <div className="h-2 bg-border-tertiary rounded-full overflow-hidden w-full">
          <div className={`h-full ${color}`} style={{ width: `${percent}%` }} />
        </div>
        <div className="text-[11px] text-text-secondary text-right mt-1.5">{percent}% traffic split</div>
      </div>
    </div>
  );
}

function TimelineItem({ color, title, time, hasLine }: any) {
  return (
    <div className="flex gap-4 relative py-2.5">
      <div className="relative z-10 flex flex-col items-center mt-1">
        <div className={`w-2.5 h-2.5 rounded-full ${color} shrink-0`} />
        {hasLine && <div className="w-[1px] h-full bg-border-tertiary absolute top-2.5" />}
      </div>
      <div className="pb-1">
        <div className="text-[13px] text-text-primary leading-snug">{title}</div>
        <div className="text-[11px] text-text-tertiary mt-1">{time}</div>
      </div>
    </div>
  );
}
