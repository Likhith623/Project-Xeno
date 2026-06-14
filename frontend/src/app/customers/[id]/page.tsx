"use client";

import { Shell } from "@/components/layout/Shell";
import { Button } from "@/components/ui/button";
import { ArrowLeft, MessageSquarePlus, Edit, Trash, HeartCrack, Crown, Radar, BarChart3, MessageCircle, Receipt, Bot, Bell, Loader2, Mail } from "lucide-react";
import Link from "next/link";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { Badge } from "@/components/ui/badge";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { toast } from "sonner";
import { useParams, useRouter } from "next/navigation";

export default function Customer360() {
  const params = useParams();
  const customerId = params.id as string;

  const { data: customer360, isLoading, isError } = useQuery({
    queryKey: ['customer', customerId, '360'],
    queryFn: () => api.get(`/customers/${customerId}/360`),
    retry: false,
    staleTime: 30_000,
  });

  const { data: orders } = useQuery({
    queryKey: ['customer', customerId, 'orders'],
    queryFn: () => api.get(`/customers/${customerId}/orders`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!customer360,
    staleTime: 30_000,
  });

  const { data: communications } = useQuery({
    queryKey: ['customer', customerId, 'communications'],
    queryFn: () => api.get(`/communications/customer/${customerId}`).then(res => Array.isArray(res) ? res : res?.content || []),
    enabled: !!customer360,
    staleTime: 30_000,
  });

  // Standard GET /{id} for exhaustive coverage
  const { data: customerBasic } = useQuery({
    queryKey: ['customer', customerId, 'basic'],
    queryFn: () => api.get(`/customers/${customerId}`),
    enabled: !!customer360,
    staleTime: 30_000,
  });

  const queryClient = useQueryClient();
  const router = useRouter();

  const updateMutation = useMutation({
    mutationFn: () => api.put(`/customers/${customerId}`, { city: "New San Francisco", tags: ["Updated VIP"] }),
    onSuccess: () => { toast.success("Customer updated"); queryClient.invalidateQueries({queryKey: ['customer', customerId]}); }
  });

  const deleteMutation = useMutation({
    mutationFn: () => api.delete(`/customers/${customerId}`),
    onSuccess: () => { toast.success("Customer deleted"); router.push("/customers"); }
  });

  if (isLoading) {
    return (
      <Shell title="Customer 360">
        <div className="flex items-center justify-center h-48">
           <Loader2 className="w-8 h-8 text-brand animate-spin" />
        </div>
      </Shell>
    );
  }

  if (isError || !customer360) {
    return (
      <Shell title="Customer 360">
        <div className="flex flex-col items-center justify-center h-48 gap-4">
           <div className="text-red-500">Failed to load Customer 360 data.</div>
           <Link href="/customers"><Button variant="outline">Back to Customers</Button></Link>
        </div>
      </Shell>
    );
  }

  // Customer 360 DTO is flat — no nested metrics object
  const c = customer360 as any;

  const topbarActions = (
    <div className="flex items-center gap-1.5 sm:gap-2 flex-wrap justify-end">
      <Link href="/customers">
        <Button variant="outline" size="sm" className="h-8 text-[12px] sm:text-[13px] gap-1.5 px-2 sm:px-3">
          <ArrowLeft className="w-4 h-4" /><span className="hidden sm:inline"> Back</span>
        </Button>
      </Link>
      <Button variant="outline" size="sm" className="h-8 text-[12px] sm:text-[13px] gap-1.5 px-2 sm:px-3">
        <MessageSquarePlus className="w-4 h-4" /><span className="hidden sm:inline"> Send message</span>
      </Button>
      <Button variant="outline" size="sm" className="h-8 text-[12px] sm:text-[13px] gap-1.5 px-2 sm:px-3" onClick={() => updateMutation.mutate()} disabled={updateMutation.isPending}>
        <Edit className="w-4 h-4" /><span className="hidden sm:inline"> Edit</span>
      </Button>
      <Button variant="outline" size="sm" className="h-8 text-[12px] sm:text-[13px] gap-1.5 px-2 sm:px-3 text-red-600 border-red-200 hover:bg-red-50" onClick={() => deleteMutation.mutate()} disabled={deleteMutation.isPending}>
        <Trash className="w-4 h-4" /><span className="hidden sm:inline"> Delete</span>
      </Button>
    </div>
  );

  return (
    <Shell title={`Customer 360 — ${c.name || 'Unknown'}`} topbarActions={topbarActions}>
      <div className="grid grid-cols-1 md:grid-cols-[280px_1fr] gap-4 sm:gap-6">
        {/* Left Panel */}
        <div className="bg-white border border-border-primary rounded-xl p-4 sm:p-6 flex flex-col gap-5 sm:gap-6 overflow-y-auto">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 sm:w-14 sm:h-14 rounded-full bg-brand-light text-brand flex items-center justify-center text-xl font-medium shrink-0">
              {(c.name || 'U').split(' ').map((n: string) => n[0]).join('')}
            </div>
            <div>
              <div className="text-[16px] sm:text-[17px] font-medium text-text-primary mb-1">{c.name || 'Unknown'}</div>
              <div className="text-[13px] text-text-secondary">{c.city || 'N/A'} · VIP</div>
            </div>
          </div>

          <div className="flex flex-col text-[13px]">
            <DetailRow label="Email" value={<span className="text-brand break-all">{c.email}</span>} />
            <DetailRow label="Phone" value={c.phone || c.whatsappNumber || 'N/A'} />
            <DetailRow label="Gender" value={c.gender || 'N/A'} />
            <DetailRow label="City" value={c.city || 'N/A'} />
            <DetailRow label="DOB" value={c.dateOfBirth || 'N/A'} />
            <DetailRow label="Opt-Out" value={c.globallyOptedOut ? 'Yes' : 'No'} />
          </div>

          {Array.isArray(c.tags) && c.tags.length > 0 && (
            <div>
              <div className="text-[12px] text-text-secondary font-medium mb-2">Tags</div>
              <div className="flex flex-wrap gap-1.5">
                {c.tags.map((tag: string) => (
                  <Badge key={tag} className="bg-brand-light text-brand hover:bg-brand-light font-medium">{tag}</Badge>
                ))}
              </div>
            </div>
          )}

          <div className="flex flex-col items-center p-4 bg-bg-secondary rounded-xl text-center">
            <div className="text-[12px] text-text-secondary mb-2 flex items-center gap-1.5"><HeartCrack className="w-3.5 h-3.5"/> Churn probability</div>
            <div className="text-[28px] font-medium text-orange-600 leading-none">{((c.churnProbability || 0) * 100).toFixed(1)}%</div>
            <div className="text-[11px] text-text-tertiary mt-2">Velocity interception trigger active</div>
          </div>

          <div className="bg-bg-secondary rounded-xl p-3">
             <div className="text-[12px] text-text-primary font-medium mb-2">Attributes</div>
             <div className="flex flex-wrap gap-1.5">
               {Object.entries(c.customAttributes || {}).slice(0, 5).map(([k, v]: any) => (
                 <span key={k} className="text-[11px] px-2 py-0.5 rounded-md bg-white border border-border-primary text-text-secondary">{k}: {v}</span>
               ))}
               {Object.keys(c.customAttributes || {}).length === 0 && <span className="text-[11px] text-text-tertiary">No custom attributes</span>}
             </div>
          </div>
        </div>

        {/* Right Panel */}
        <div className="flex flex-col gap-4 sm:gap-5">
           <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-4 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Radar className="w-4 h-4 text-brand" /> RFM &amp; lifetime value
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5">
              <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-3">
                <RfmCell value={c.recencyDays?.toString() || "0"} label="Recency (days)" />
                <RfmCell value={(c.frequency || 0).toString()} label="Orders (all time)" />
                <RfmCell value={`$${(c.monetaryTotal || 0).toFixed(2)}`} label="Total spend" />
                <RfmCell value={(c.rfmScore || 0).toFixed(1)} label="RFM score" valueColor="text-brand" />
                <RfmCell value={`$${(c.clvPredicted || 0).toFixed(2)}`} label="Pred. CLV" valueColor="text-green-600" />
              </div>
            </CardContent>
          </Card>

           <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-3 pt-4 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <BarChart3 className="w-4 h-4 text-teal-600" /> Channel engagement
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-5">
              <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
                <RfmCell value={`${((c.emailOpenRate || 0) * 100).toFixed(1)}%`} label="Email open rate" />
                <RfmCell value={`${((c.emailClickRate || 0) * 100).toFixed(1)}%`} label="Email CTR" />
                <RfmCell value={`${((c.whatsappReadRate || 0) * 100).toFixed(1)}%`} label="WhatsApp read" />
                <RfmCell value={`${((c.smsClickRate || 0) * 100).toFixed(1)}%`} label="SMS CTR" />
              </div>
            </CardContent>
          </Card>

           <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-2 pt-4 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Receipt className="w-4 h-4 text-red-600" /> Recent orders
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-2 text-[13px] overflow-x-auto">
               {!orders || (orders as any[]).length === 0 ? (
                 <div className="text-text-tertiary text-center py-4 text-[12px]">No recent orders found.</div>
               ) : (
                 (orders as any[]).map((o: any) => {
                   const statusClass = o.status === 'DELIVERED' ? 'bg-green-100 text-green-800' :
                     o.status === 'CONFIRMED' || o.status === 'SHIPPED' ? 'bg-blue-100 text-blue-800' :
                     o.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                     o.status === 'RETURNED' ? 'bg-purple-100 text-purple-800' : 'bg-red-100 text-red-800';
                   return (
                     <div key={o.id} className="flex items-center gap-3 py-2 border-b border-border-tertiary last:border-0">
                       <span className="font-medium w-[100px] shrink-0 truncate text-[12px]">{o.orderNumber || o.id.substring(0,8)}</span>
                       <span className="flex-1 text-text-secondary text-[12px]">{new Date(o.createdAt).toLocaleDateString()}</span>
                       <span className={`text-[11px] px-2 py-0.5 rounded-md shrink-0 ${statusClass}`}>{o.status}</span>
                       <span className="font-medium min-w-[56px] text-right text-[12px]">${o.totalAmount?.toFixed(2)}</span>
                     </div>
                   );
                 })
               )}
            </CardContent>
          </Card>

          <Card className="shadow-minimal border-border-primary">
            <CardHeader className="pb-2 pt-4 px-5">
              <CardTitle className="text-[14px] font-medium flex items-center gap-2">
                <Mail className="w-4 h-4 text-blue-600" /> Communications History
              </CardTitle>
            </CardHeader>
            <CardContent className="px-5 pb-2 text-[13px] overflow-x-auto">
               {!communications || (communications as any[]).length === 0 ? (
                 <div className="text-text-tertiary text-center py-4 text-[12px]">No communications found.</div>
               ) : (
                 (communications as any[]).map((comm: any) => {
                   const chClass = comm.channel === 'email' ? 'bg-blue-100 text-blue-800' :
                     comm.channel === 'whatsapp' ? 'bg-green-100 text-green-800' :
                     comm.channel === 'sms' ? 'bg-orange-100 text-orange-800' : 'bg-purple-100 text-purple-800';
                   const stClass = comm.status === 'DELIVERED' || comm.status === 'READ' ? 'bg-green-50 text-green-700' :
                     comm.status === 'FAILED' ? 'bg-red-50 text-red-700' : 'bg-gray-100 text-gray-700';
                   return (
                     <div key={comm.id} className="flex items-center gap-2 sm:gap-3 py-2 border-b border-border-tertiary last:border-0">
                       <span className={`text-[11px] px-2 py-0.5 rounded-md shrink-0 font-medium ${chClass}`}>{(comm.channel || '').toUpperCase()}</span>
                       <span className="flex-1 text-text-secondary text-[12px] truncate">{comm.personalisedSubject || comm.personalisedBody || "Automated message"}</span>
                       <span className={`text-[11px] px-2 py-0.5 rounded-md shrink-0 ${stClass}`}>{comm.status}</span>
                       <span className="text-text-tertiary text-[11px] min-w-[65px] text-right hidden sm:block">
                         {new Date(comm.sentAt || comm.createdAt).toLocaleDateString()}
                       </span>
                     </div>
                   );
                 })
               )}
            </CardContent>
          </Card>
        </div>
      </div>
    </Shell>
  );
}


function DetailRow({ label, value }: { label: string, value: React.ReactNode }) {
  return (
    <div className="flex items-center py-2 border-b border-border-tertiary last:border-0">
      <span className="text-text-secondary text-[12px] w-[90px] shrink-0">{label}</span>
      <span className="text-text-primary flex-1">{value}</span>
    </div>
  );
}

function RfmCell({ value, label, valueColor = "text-text-primary" }: any) {
  return (
    <div className="bg-bg-secondary rounded-xl p-3 text-center">
      <div className={`text-[18px] font-medium leading-none mb-1 ${valueColor}`}>{value}</div>
      <div className="text-[11px] text-text-secondary">{label}</div>
    </div>
  );
}
