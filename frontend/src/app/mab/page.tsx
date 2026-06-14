"use client";

import { Shell } from "@/components/layout/Shell";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, BarChart, Bar, Cell } from 'recharts';
import { Bot, TrendingUp, Loader2, ChevronDown } from "lucide-react";
import { Badge } from "@/components/ui/badge";
import { useQuery } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState, useMemo } from "react";
import Link from "next/link";

const VARIANT_COLORS = ['#4f46e5', '#0ea5e9', '#10b981', '#f59e0b', '#ef4444', '#8b5cf6'];

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
  const [selectedCampaignId, setSelectedCampaignId] = useState<string | null>(null);
  const [showDropdown, setShowDropdown] = useState(false);

  // Fetch all campaigns
  const { data: campaigns } = useQuery({
    queryKey: ['campaigns'],
    queryFn: () => api.get(`/campaigns`).then(res => Array.isArray(res) ? res : res?.content || []),
    staleTime: 30_000,
  });

  const campaignsList: any[] = Array.isArray(campaigns) ? campaigns : [];

  // Use selected or first campaign
  const activeCampaignId = selectedCampaignId || campaignsList[0]?.id;
  const activeCampaign = campaignsList.find(c => c.id === activeCampaignId) || campaignsList[0];

  // Fetch MAB stats for the selected campaign
  const { data: mabStats, isLoading: isLoadingMab } = useQuery({
    queryKey: ['mab-stats', activeCampaignId],
    queryFn: () => api.get(`/campaigns/${activeCampaignId}/variants/mab-stats`),
    enabled: !!activeCampaignId,
    staleTime: 15_000,
  });

  // Fetch variants for more detail
  const { data: variantsList } = useQuery({
    queryKey: ['variants', 'campaign', activeCampaignId],
    queryFn: () => api.get(`/variants/campaign/${activeCampaignId}`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!activeCampaignId,
    staleTime: 30_000,
  });

  // Also aggregate stats across ALL campaigns to show global summary
  const { data: allMabData } = useQuery({
    queryKey: ['mab-stats-all', campaignsList.map(c => c.id).join(',')],
    queryFn: async () => {
      const results = await Promise.allSettled(
        campaignsList.slice(0, 5).map(c => api.get(`/campaigns/${c.id}/variants/mab-stats`))
      );
      return results
        .filter(r => r.status === 'fulfilled')
        .flatMap(r => Array.isArray((r as any).value) ? (r as any).value : []);
    },
    enabled: campaignsList.length > 0,
    staleTime: 60_000,
  });

  const mabArray: any[] = Array.isArray(mabStats) ? mabStats : [];
  const allMabArray: any[] = Array.isArray(allMabData) ? allMabData : [];
  const variantsArray: any[] = Array.isArray(variantsList) ? variantsList : [];

  const allocationData = mabArray.map((v: any, index: number) => ({
    name: v.variantName || v.name || `Variant ${String.fromCharCode(65 + index)}`,
    value: Number(((v.expectedConversionRate || 0) * 100).toFixed(2)),
    alpha: v.mabAlpha || 1,
    beta: v.mabBeta || 1,
    impressions: v.mabImpressions || 0,
    conversions: v.mabConversions || 0,
    fill: VARIANT_COLORS[index % VARIANT_COLORS.length],
  }));

  const topVariant = useMemo(() => {
    if (allocationData.length === 0) return null;
    return allocationData.reduce((best, v) => v.value > best.value ? v : best, allocationData[0]);
  }, [allocationData]);

  const totalImpressions = allocationData.reduce((s, v) => s + v.impressions, 0);
  const totalConversions = allocationData.reduce((s, v) => s + v.conversions, 0);

  return (
    <Shell title="Multi-Armed Bandit Dashboard">
      <div className="flex items-center gap-2 mb-6 flex-wrap">
        <Badge className="bg-brand-light text-brand hover:bg-brand-light flex items-center gap-1">
          <Bot className="w-3 h-3" /> Thompson Sampling Active
        </Badge>
        <span className="text-[12px] text-text-secondary">AI is routing traffic to highest performing variants in real-time.</span>
      </div>

      {/* Campaign Selector */}
      {campaignsList.length > 0 && (
        <div className="mb-6 relative inline-block">
          <button
            onClick={() => setShowDropdown(v => !v)}
            className="flex items-center gap-2 bg-white border border-border-primary rounded-lg px-4 py-2 text-[13px] text-text-primary hover:bg-bg-secondary transition-colors"
          >
            <span className="font-medium">{activeCampaign?.name || 'Select Campaign'}</span>
            <ChevronDown className="w-4 h-4 text-text-tertiary" />
          </button>
          {showDropdown && (
            <div className="absolute z-50 mt-1 bg-white border border-border-primary rounded-lg shadow-lg w-64 max-h-60 overflow-y-auto">
              {campaignsList.map(c => (
                <button
                  key={c.id}
                  className={`w-full text-left px-4 py-2.5 text-[13px] hover:bg-bg-secondary transition-colors flex items-center gap-2 ${c.id === activeCampaignId ? 'text-brand font-medium bg-brand-light/30' : 'text-text-primary'}`}
                  onClick={() => { setSelectedCampaignId(c.id); setShowDropdown(false); }}
                >
                  <div className={`w-1.5 h-1.5 rounded-full ${c.status === 'RUNNING' ? 'bg-green-500' : 'bg-gray-300'}`} />
                  {c.name}
                </button>
              ))}
            </div>
          )}
        </div>
      )}

      {/* KPI Strip */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-6">
        <div className="bg-white border border-border-primary rounded-xl p-4 text-center">
          <div className="text-[22px] font-semibold text-text-primary">{mabArray.length}</div>
          <div className="text-[11px] text-text-secondary mt-1">Active Variants</div>
        </div>
        <div className="bg-white border border-border-primary rounded-xl p-4 text-center">
          <div className="text-[22px] font-semibold text-brand">{totalImpressions.toLocaleString()}</div>
          <div className="text-[11px] text-text-secondary mt-1">Total Impressions</div>
        </div>
        <div className="bg-white border border-border-primary rounded-xl p-4 text-center">
          <div className="text-[22px] font-semibold text-green-600">{totalConversions.toLocaleString()}</div>
          <div className="text-[11px] text-text-secondary mt-1">Total Conversions</div>
        </div>
        <div className="bg-white border border-border-primary rounded-xl p-4 text-center">
          <div className="text-[22px] font-semibold text-orange-600">
            {totalImpressions > 0 ? ((totalConversions / totalImpressions) * 100).toFixed(1) : '0.0'}%
          </div>
          <div className="text-[11px] text-text-secondary mt-1">Overall Conv. Rate</div>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
        <div className="lg:col-span-2">
           <Card className="shadow-minimal border-border-primary h-full">
            <CardHeader className="pb-2 pt-4 px-6">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                Variant Performance — Conversion Rate (%)
              </CardTitle>
              <CardDescription className="text-[12px]">
                Campaign: {activeCampaign?.name || 'Loading...'} · {variantsArray.length > 0 ? `${variantsArray.length} variants tracked` : 'Loading variants...'}
              </CardDescription>
            </CardHeader>
            <CardContent className="px-2 pb-4">
              <div style={{ width: '100%', height: 300 }}>
                <ResponsiveContainer width="100%" height={300}>
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
                    <Area type="monotone" dataKey="variantC" name="Variant C (Conservative)" stroke="#10b981" strokeWidth={2} fillOpacity={0} />
                  </AreaChart>
                </ResponsiveContainer>
              </div>
            </CardContent>
          </Card>
        </div>

        <div>
           <Card className="shadow-minimal border-border-primary h-full">
            <CardHeader className="pb-2 pt-4 px-6">
              <CardTitle className="text-[14px] font-medium">Live Traffic Allocation</CardTitle>
              {topVariant && (
                <div className="text-[12px] text-green-600 font-medium mt-1">
                  🏆 Winner: {topVariant.name} ({topVariant.value.toFixed(1)}% conv.)
                </div>
              )}
            </CardHeader>
            <CardContent className="px-4 pt-2 pb-4">
              {isLoadingMab ? (
                <div className="flex items-center justify-center" style={{height: 250}}>
                  <Loader2 className="w-6 h-6 animate-spin text-brand" />
                </div>
              ) : allocationData.length === 0 ? (
                <div className="flex flex-col items-center justify-center text-center gap-3" style={{height: 250}}>
                  <div className="text-[12px] text-text-secondary">No variants found for this campaign.</div>
                  <Link href={`/campaigns/${activeCampaignId}`}>
                    <button className="text-[12px] text-brand underline">Create a variant →</button>
                  </Link>
                </div>
              ) : (
                <>
                  <div style={{ width: '100%', height: Math.max(180, allocationData.length * 48) }}>
                    <ResponsiveContainer width="100%" height="100%">
                       <BarChart data={allocationData} layout="vertical" margin={{ top: 0, right: 16, left: 0, bottom: 0 }}>
                         <XAxis type="number" hide domain={[0, 'dataMax']} />
                         <YAxis dataKey="name" type="category" axisLine={false} tickLine={false} tick={{ fontSize: 12, fill: '#0f172a' }} width={90} />
                         <Tooltip
                           cursor={{fill: 'rgba(79,70,229,0.05)'}}
                           contentStyle={{ borderRadius: '8px', border: '1px solid #e2e8f0', boxShadow: '0 2px 4px rgba(0,0,0,0.05)' }}
                           itemStyle={{ fontSize: '12px' }}
                           formatter={(val: any) => [`${Number(val).toFixed(2)}%`, 'Conv. Rate']}
                         />
                         <Bar dataKey="value" radius={[0, 4, 4, 0]} barSize={24}>
                           {allocationData.map((entry, index) => (
                             <Cell key={`cell-${index}`} fill={entry.fill} />
                           ))}
                         </Bar>
                       </BarChart>
                    </ResponsiveContainer>
                  </div>
                  <div className="mt-4 flex items-start gap-2 bg-brand-light/30 p-3 rounded-md text-[12px] text-brand">
                    <TrendingUp className="w-4 h-4 shrink-0 mt-0.5" />
                    <div>AI is routing traffic using Bayesian Thompson Sampling. {allocationData.length} variants tracked.</div>
                  </div>
                </>
              )}
            </CardContent>
          </Card>
        </div>
      </div>

      {/* Variant Detail Table */}
      {allocationData.length > 0 && (
        <Card className="shadow-minimal border-border-primary">
          <CardHeader className="pb-3 pt-5 px-6">
            <CardTitle className="text-[14px] font-medium">Variant Stats — {activeCampaign?.name}</CardTitle>
          </CardHeader>
          <CardContent className="px-0 pb-0">
            <div className="overflow-x-auto">
              <table className="w-full text-[13px] text-left">
                <thead className="bg-bg-secondary text-text-secondary border-b border-border-primary">
                  <tr>
                    <th className="px-6 py-3 font-medium">Variant</th>
                    <th className="px-6 py-3 font-medium">Alpha (α)</th>
                    <th className="px-6 py-3 font-medium">Beta (β)</th>
                    <th className="px-6 py-3 font-medium">Impressions</th>
                    <th className="px-6 py-3 font-medium">Conversions</th>
                    <th className="px-6 py-3 font-medium">Conv. Rate</th>
                    <th className="px-6 py-3 font-medium">Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-border-tertiary">
                  {allocationData.map((v, idx) => {
                    const isWinner = topVariant?.name === v.name;
                    return (
                      <tr key={idx} className={`hover:bg-bg-secondary/50 ${isWinner ? 'bg-green-50/50' : ''}`}>
                        <td className="px-6 py-3 font-medium">
                          <div className="flex items-center gap-2">
                            <div className="w-2.5 h-2.5 rounded-full shrink-0" style={{ backgroundColor: v.fill }} />
                            {v.name}
                            {isWinner && <span className="text-[10px] bg-green-100 text-green-700 px-1.5 py-0.5 rounded-md font-medium">Winning</span>}
                          </div>
                        </td>
                        <td className="px-6 py-3 text-text-secondary">{v.alpha.toFixed(2)}</td>
                        <td className="px-6 py-3 text-text-secondary">{v.beta.toFixed(2)}</td>
                        <td className="px-6 py-3 text-text-secondary">{v.impressions.toLocaleString()}</td>
                        <td className="px-6 py-3 text-text-secondary">{v.conversions.toLocaleString()}</td>
                        <td className="px-6 py-3 font-medium text-brand">{v.value.toFixed(2)}%</td>
                        <td className="px-6 py-3">
                          <div className="w-full bg-border-tertiary rounded-full h-1.5 w-24">
                            <div className="h-1.5 rounded-full" style={{ width: `${Math.min(100, v.value * 10)}%`, backgroundColor: v.fill }} />
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Cross-campaign summary */}
      {allMabArray.length > 0 && (
        <Card className="shadow-minimal border-border-primary mt-6">
          <CardHeader className="pb-3 pt-5 px-6">
            <CardTitle className="text-[14px] font-medium flex items-center gap-2">
              <Bot className="w-4 h-4 text-brand" /> Cross-Campaign MAB Summary
              <Badge className="bg-brand-light text-brand hover:bg-brand-light text-[10px] px-1.5 h-5">{allMabArray.length} variants across {Math.min(5, campaignsList.length)} campaigns</Badge>
            </CardTitle>
          </CardHeader>
          <CardContent className="px-6 pb-5">
            <div className="grid grid-cols-2 md:grid-cols-4 gap-3">
              <div className="bg-bg-secondary rounded-lg p-3 text-center">
                <div className="text-[18px] font-semibold text-text-primary">{allMabArray.length}</div>
                <div className="text-[11px] text-text-secondary mt-1">Total Variants Tracked</div>
              </div>
              <div className="bg-bg-secondary rounded-lg p-3 text-center">
                <div className="text-[18px] font-semibold text-brand">
                  {(allMabArray.reduce((s: number, v: any) => s + (v.expectedConversionRate || 0), 0) / Math.max(1, allMabArray.length) * 100).toFixed(1)}%
                </div>
                <div className="text-[11px] text-text-secondary mt-1">Avg Conv. Rate</div>
              </div>
              <div className="bg-bg-secondary rounded-lg p-3 text-center">
                <div className="text-[18px] font-semibold text-green-600">
                  {allMabArray.reduce((s: number, v: any) => s + (v.mabConversions || 0), 0).toLocaleString()}
                </div>
                <div className="text-[11px] text-text-secondary mt-1">Total Conversions</div>
              </div>
              <div className="bg-bg-secondary rounded-lg p-3 text-center">
                <div className="text-[18px] font-semibold text-orange-600">
                  {allMabArray.reduce((s: number, v: any) => s + (v.mabImpressions || 0), 0).toLocaleString()}
                </div>
                <div className="text-[11px] text-text-secondary mt-1">Total Impressions</div>
              </div>
            </div>
          </CardContent>
        </Card>
      )}
    </Shell>
  );
}
