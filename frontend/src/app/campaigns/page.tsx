"use client";

import { Shell } from "@/components/layout/Shell";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import Link from "next/link";
import { Bot, Search, Filter, Loader2, Plus, AlertTriangle, Bell } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { toast } from "sonner";
import { useState } from "react";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger, DialogFooter } from "@/components/ui/dialog";
import { Campaign, OptOutAlert } from "@/types";

import { useCampaignStore } from "@/store/useCampaignStore";

export default function CampaignsPage() {
  const queryClient = useQueryClient();
  const { isCampaignModalOpen, setCampaignModalOpen } = useCampaignStore();
  const [newCampaignName, setNewCampaignName] = useState("");
  const [newCampaignGoal, setNewCampaignGoal] = useState("");
  const [newCampaignSegmentId, setNewCampaignSegmentId] = useState("");

  const { data: campaignsData, isLoading, isError } = useQuery({
    queryKey: ['campaigns'],
    queryFn: () => api.get(`/campaigns`),
    staleTime: 30_000,
  });

  // Fetch segments to get a valid segmentId for campaign creation
  const { data: segmentsForCreate } = useQuery({
    queryKey: ['segments-minimal'],
    queryFn: () => api.get('/segments?page=0&size=100').then(res => Array.isArray(res) ? res : res?.content || []),
    staleTime: 60_000,
  });

  const firstSegmentId = Array.isArray(segmentsForCreate) ? segmentsForCreate[0]?.id : undefined;

  const createMutation = useMutation({
    mutationFn: () => {
      if (!newCampaignSegmentId) { toast.error("Please select a segment"); return Promise.reject("No segment"); }
      return api.post('/campaigns', { name: newCampaignName, goal: newCampaignGoal, segmentId: newCampaignSegmentId });
    },
    onSuccess: () => { 
        toast.success("Campaign created"); 
        queryClient.invalidateQueries({queryKey: ['campaigns']}); 
        setCampaignModalOpen(false);
        setNewCampaignName("");
        setNewCampaignGoal("");
        setNewCampaignSegmentId("");
    }
  });

  // GET /api/v1/campaigns/opt-out-alerts — Safety threshold breach alerts
  const { data: optOutAlerts } = useQuery({
    queryKey: ['campaigns', 'opt-out-alerts'],
    queryFn: () => api.get('/campaigns/opt-out-alerts').then(res => Array.isArray(res) ? res : res?.content || []),
    staleTime: 30_000,
  });

  // Campaigns may be a plain array or a paginated { content: [...] }
  const campaignsList = Array.isArray(campaignsData) ? campaignsData : (campaignsData?.content || []);
  const campaigns: Campaign[] = campaignsList;
  const alerts: OptOutAlert[] = Array.isArray(optOutAlerts) ? optOutAlerts : [];

  return (
    <Shell title="All Campaigns">
      <div className="flex flex-col sm:flex-row justify-between gap-4 mb-6">
        <div className="flex gap-2 w-full sm:w-auto">
          <div className="relative w-full sm:w-[300px]">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-text-tertiary" />
            <Input placeholder="Search campaigns..." className="pl-9 bg-white border-border-primary h-9 text-[13px]" />
          </div>
          <Button variant="outline" size="sm" className="h-9 gap-2 text-[13px]">
            <Filter className="w-4 h-4" /> Filter
          </Button>
        </div>
        <Dialog open={isCampaignModalOpen} onOpenChange={setCampaignModalOpen}>
          <DialogTrigger
            render={
              <Button size="sm" className="h-9 text-[13px] gap-2 bg-text-primary text-white hover:bg-text-secondary" onClick={() => setCampaignModalOpen(true)}>
                <Plus className="w-4 h-4" /> New campaign
              </Button>
            }
          />
          <DialogContent className="sm:max-w-md">
            <DialogHeader>
              <DialogTitle>Create New Campaign</DialogTitle>
            </DialogHeader>
            <div className="flex flex-col gap-4 py-4">
              <div className="flex flex-col gap-2">
                <label className="text-[13px] font-medium">Campaign Name</label>
                <Input value={newCampaignName} onChange={e => setNewCampaignName(e.target.value)} placeholder="e.g. Summer Clearance Sale" />
              </div>
              <div className="flex flex-col gap-2">
                <label className="text-[13px] font-medium">Goal</label>
                <textarea 
                  className="flex min-h-[80px] w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:cursor-not-allowed disabled:opacity-50" 
                  value={newCampaignGoal} 
                  onChange={e => setNewCampaignGoal(e.target.value)} 
                  placeholder="e.g. Clear out electronics dead stock and drive 5% conversion."
                />
              </div>
              <div className="flex flex-col gap-2">
                <label className="text-[13px] font-medium">Target Segment</label>
                <select 
                  className="flex h-9 w-full items-center justify-between whitespace-nowrap rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-1 focus:ring-ring disabled:cursor-not-allowed disabled:opacity-50"
                  value={newCampaignSegmentId} 
                  onChange={e => setNewCampaignSegmentId(e.target.value)}
                >
                  <option value="" disabled>Select a segment...</option>
                  {(Array.isArray(segmentsForCreate) ? segmentsForCreate : []).map((seg: any) => (
                    <option key={seg.id} value={seg.id}>{seg.name}</option>
                  ))}
                </select>
              </div>
            </div>
            <DialogFooter>
              <Button onClick={() => createMutation.mutate()} disabled={!newCampaignName || !newCampaignSegmentId || createMutation.isPending} className="w-full">
                {createMutation.isPending ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
                Create Campaign
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* Opt-Out Alerts: GET /campaigns/opt-out-alerts */}
      {alerts.length > 0 && (
        <Card className="shadow-minimal border-red-200 bg-red-50/30 mb-6">
          <CardHeader className="pb-2 pt-4 px-5">
            <CardTitle className="text-[14px] font-medium flex items-center gap-2 text-red-700">
              <AlertTriangle className="w-4 h-4" /> Opt-Out Safety Alerts
              <Badge className="bg-red-100 text-red-800 hover:bg-red-100 text-[10px] px-1.5 h-5">{alerts.length} active</Badge>
            </CardTitle>
          </CardHeader>
          <CardContent className="px-5 pb-4">
            {alerts.map((alert: any, i: number) => (
              <div key={alert.campaignId || i} className="flex justify-between items-center py-2 border-b border-red-100 last:border-0 text-[12px]">
                <div className="flex items-center gap-2">
                  <Bell className="w-3.5 h-3.5 text-red-600" />
                  <span className="text-text-primary font-medium">{alert.campaignName || `Campaign ${alert.campaignId?.substring(0,8)}`}</span>
                </div>
                <span className="text-red-700 font-medium">{alert.currentOptOutRatePct?.toFixed(1) || '?'}% opt-out rate</span>
              </div>
            ))}
          </CardContent>
        </Card>
      )}

      <Card className="shadow-minimal border-border-primary overflow-hidden">
        {isLoading ? (
          <div className="flex justify-center items-center py-12">
            <Loader2 className="w-6 h-6 text-brand animate-spin" />
          </div>
        ) : isError ? (
          <div className="text-center py-8 text-red-500 text-[13px]">Failed to load campaigns</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-[13px] text-left">
              <thead className="bg-bg-secondary text-text-secondary border-b border-border-primary">
                <tr>
                  <th className="px-6 py-3 font-medium">Name</th>
                  <th className="px-6 py-3 font-medium">Status</th>
                  <th className="px-6 py-3 font-medium">Sent</th>
                  <th className="px-6 py-3 font-medium">Created</th>
                  <th className="px-6 py-3 font-medium text-right">Action</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-border-tertiary">
                {campaigns.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="text-center py-8 text-text-tertiary">No campaigns found.</td>
                  </tr>
                ) : (
                  campaigns.map((c: any) => (
                    <tr key={c.id} className="hover:bg-bg-secondary/50 transition-colors">
                      <td className="px-6 py-3 font-medium text-text-primary">
                        <Link href={`/campaigns/${c.id}`} className="hover:underline flex items-center gap-2">
                          {c.name}
                          {c.createdByAgent && <Badge className="text-[10px] h-4 px-1.5 bg-brand-light text-brand hover:bg-brand-light gap-1"><Bot className="w-3 h-3"/></Badge>}
                        </Link>
                      </td>
                      <td className="px-6 py-3">
                        <span className={`inline-flex items-center gap-1.5 ${
                          c.status === 'RUNNING' ? 'text-green-600' :
                          c.status === 'COMPLETED' ? 'text-blue-600' :
                          c.status === 'DRAFT' ? 'text-orange-600' :
                          c.status === 'PAUSED' ? 'text-yellow-600' :
                          c.status === 'FAILED' ? 'text-red-600' : 'text-text-tertiary'
                        }`}>
                          <div className={`w-1.5 h-1.5 rounded-full ${
                            c.status === 'RUNNING' ? 'bg-green-600 animate-pulse' :
                            c.status === 'COMPLETED' ? 'bg-blue-600' :
                            c.status === 'DRAFT' ? 'bg-orange-600' :
                            c.status === 'PAUSED' ? 'bg-yellow-600' :
                            c.status === 'FAILED' ? 'bg-red-600' : 'bg-text-tertiary'
                          }`} />
                          {c.status}
                        </span>
                      </td>
                      <td className="px-6 py-3 text-text-secondary">{c.totalSent || 0} sent</td>
                      <td className="px-6 py-3 text-text-secondary">{new Date(c.createdAt).toLocaleDateString()}</td>
                      <td className="px-6 py-3 text-right">
                        <Link href={`/campaigns/${c.id}`}>
                          <Button variant="ghost" size="sm" className="h-8 text-text-secondary hover:text-text-primary">View</Button>
                        </Link>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}
      </Card>
    </Shell>
  );
}
