"use client";

import { Shell } from "@/components/layout/Shell";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, BarChart, Bar } from 'recharts';
import { Bot, TrendingUp, Loader2 } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api";

const performanceData = [
  { time: '00:00', variantA: 2.4, variantB: 1.8, variantC: 1.2 },
  { time: '04:00', variantA: 2.8, variantB: 2.1, variantC: 1.4 },
  { time: '08:00', variantA: 3.5, variantB: 2.9, variantC: 1.8 },
  { time: '12:00', variantA: 4.2, variantB: 4.5, variantC: 2.1 },
  { time: '16:00', variantA: 4.8, variantB: 6.2, variantC: 2.4 },
  { time: '20:00', variantA: 5.1, variantB: 8.4, variantC: 2.5 },
  { time: '24:00', variantA: 5.2, variantB: 9.1, variantC: 2.6 },
];

export default function MABDashboard() {
  // 1. Fetch campaigns to get an ID
  const { data: campaigns } = useQuery({
    queryKey: ['campaigns'],
    queryFn: () => api.get(`/campaigns`).then(res => Array.isArray(res) ? res : res?.content || []),
    staleTime: 30_000,
  });

  const campaignId = Array.isArray(campaigns) ? campaigns[0]?.id : undefined;

  // 2. Fetch MAB stats for that campaign
  const { data: mabStats, isLoading } = useQuery({
    queryKey: ['mab-stats', campaignId],
    queryFn: () => api.get(`/campaigns/${campaignId}/variants/mab-stats`),
    enabled: !!campaignId,
    staleTime: 30_000,
  });

  // 3. Map to BarChart — mabStats is a plain List<MabStatsDto> (array)
  const mabArray: any[] = Array.isArray(mabStats) ? mabStats : [];
  const allocationData = mabArray.map((v: any, index: number) => ({
    name: v.variantName || v.name || `Variant ${index + 1}`,
    value: Number(((v.expectedConversionRate || 0) * 100).toFixed(2)),
    fill: index === 0 ? '#94a3b8' : index === 1 ? '#4f46e5' : '#cbd5e1'
  }));

  const firstCampaign = Array.isArray(campaigns) ? campaigns[0] : null;

  return (
    <Shell title="Multi-Armed Bandit Dashboard">
      <div className="flex items-center gap-2 mb-6">
        <Badge className="bg-brand-light text-brand hover:bg-brand-light flex items-center gap-1">
          <Bot className="w-3 h-3" /> Auto-optimisation active
        </Badge>
        <span className="text-[12px] text-text-secondary">AI is actively routing traffic to highest performing variants based on live Thompson Sampling.</span>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
        <div className="lg:col-span-2">
           <Card className="shadow-minimal border-border-primary h-full">
            <CardHeader className="pb-2 pt-4 px-6">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                Simulated Variant Performance (Conversion Rate %)
              </CardTitle>
              <CardDescription className="text-[12px]">Campaign: {firstCampaign?.name || 'Loading...'}</CardDescription>
            </CardHeader>
            <CardContent className="h-[300px] px-2">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={performanceData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                  <XAxis dataKey="time" axisLine={false} tickLine={false} tick={{ fontSize: 11, fill: '#64748b' }} />
                  <YAxis axisLine={false} tickLine={false} tick={{ fontSize: 11, fill: '#64748b' }} tickFormatter={(val) => `${val}%`} />
                  <Tooltip
                    contentStyle={{ borderRadius: '8px', border: '1px solid #e2e8f0', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}
                    labelStyle={{ color: '#0f172a', fontWeight: 500, marginBottom: '4px' }}
                    itemStyle={{ fontSize: '12px', padding: '2px 0' }}
                  />
                  <Area type="monotone" dataKey="variantA" name="Variant A (Control)" stroke="#94a3b8" strokeWidth={2} fillOpacity={0} />
                  <Area type="monotone" dataKey="variantB" name="Variant B (Aggressive)" stroke="#4f46e5" strokeWidth={2} fillOpacity={0.1} fill="#4f46e5" />
                  <Area type="monotone" dataKey="variantC" name="Variant C (Conservative)" stroke="#cbd5e1" strokeWidth={2} fillOpacity={0} />
                </AreaChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </div>

        <div>
           <Card className="shadow-minimal border-border-primary h-full">
            <CardHeader className="pb-2 pt-4 px-6">
              <CardTitle className="text-[14px] font-medium">Live Traffic Allocation</CardTitle>
            </CardHeader>
            <CardContent className="h-[250px] px-4 pt-6">
              {isLoading ? (
                <div className="flex h-full items-center justify-center">
                  <Loader2 className="w-6 h-6 animate-spin text-brand" />
                </div>
              ) : mabArray.length === 0 ? (
                <div className="flex h-full items-center justify-center text-[12px] text-text-secondary text-center">
                  No active variants found for this campaign.
                </div>
              ) : (
                 <>
                   <ResponsiveContainer width="100%" height="100%">
                      <BarChart data={allocationData} layout="vertical" margin={{ top: 0, right: 0, left: 0, bottom: 0 }}>
                        <XAxis type="number" hide />
                        <YAxis dataKey="name" type="category" axisLine={false} tickLine={false} tick={{ fontSize: 12, fill: '#0f172a' }} width={80} />
                        <Tooltip
                          cursor={{fill: 'transparent'}}
                          contentStyle={{ borderRadius: '8px', border: '1px solid #e2e8f0', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}
                          itemStyle={{ fontSize: '12px' }}
                          formatter={(val: any) => [`${val.toFixed(1)}%`, 'Allocation']}
                        />
                        <Bar dataKey="value" radius={[0, 4, 4, 0]} barSize={24} />
                      </BarChart>
                   </ResponsiveContainer>
                   <div className="mt-4 flex items-start gap-2 bg-brand-light/30 p-3 rounded-md text-[12px] text-brand">
                     <TrendingUp className="w-4 h-4 shrink-0 mt-0.5" />
                     <div>AI is actively optimizing traffic based on Bayesian Thompson Sampling. {mabArray.length} variants tracked.</div>
                   </div>
                 </>
              )}
            </CardContent>
          </Card>
        </div>
      </div>
    </Shell>
  );
}
