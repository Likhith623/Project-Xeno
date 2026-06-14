"use client";

import { Shell } from "@/components/layout/Shell";
import { Card } from "@/components/ui/card";
import { Search, Loader2, Plus, X, ChevronDown } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState, useMemo } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";

// ─── Create Order Modal ───────────────────────────────────────────────────────
function CreateOrderModal({ open, onClose }: { open: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [amount, setAmount] = useState("99.99");
  const [status, setStatus] = useState("PENDING");
  const [orderNumber, setOrderNumber] = useState("");
  const [selectedCustomerId, setSelectedCustomerId] = useState<string>("");
  const [customerSearch, setCustomerSearch] = useState("");

  const { data: customersData } = useQuery({
    queryKey: ['customers-for-order'],
    queryFn: () => api.get('/customers?page=0&size=100').then(res => Array.isArray(res) ? res : res?.content || []),
    staleTime: 60_000,
  });

  const customersList: any[] = Array.isArray(customersData) ? customersData : [];
  const filteredCustomers = useMemo(() => {
    if (!customerSearch.trim()) return customersList;
    const q = customerSearch.toLowerCase();
    return customersList.filter(c => (c.name || '').toLowerCase().includes(q) || (c.email || '').toLowerCase().includes(q));
  }, [customersList, customerSearch]);

  const selectedCustomer = customersList.find(c => c.id === selectedCustomerId);

  const mutation = useMutation({
    mutationFn: () => api.post('/orders', {
      customerId: selectedCustomerId,
      totalAmount: parseFloat(amount) || 0,
      status,
      orderNumber: orderNumber || `ORD-${Date.now()}`,
    }),
    onSuccess: () => {
      toast.success("Order created successfully!");
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      onClose();
      setAmount("99.99"); setStatus("PENDING"); setOrderNumber(""); setSelectedCustomerId(""); setCustomerSearch("");
    },
    onError: (err: any) => toast.error(err.message || "Failed to create order"),
  });

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader><DialogTitle>Create New Order</DialogTitle></DialogHeader>
        <div className="flex flex-col gap-4 py-4">
          {/* Customer picker */}
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Customer *</label>
            {selectedCustomer ? (
              <div className="flex items-center justify-between bg-brand-light/30 border border-brand/20 rounded-lg px-3 py-2">
                <div>
                  <div className="text-[13px] font-medium text-text-primary">{selectedCustomer.name}</div>
                  <div className="text-[11px] text-text-secondary">{selectedCustomer.email}</div>
                </div>
                <button onClick={() => setSelectedCustomerId("")} className="text-text-tertiary hover:text-text-primary">
                  <X className="w-4 h-4" />
                </button>
              </div>
            ) : (
              <div className="flex flex-col gap-1">
                <div className="relative">
                  <Search className="absolute left-2.5 top-2.5 w-4 h-4 text-text-tertiary" />
                  <input
                    className="w-full pl-9 pr-3 py-2 border border-input rounded-md text-[13px] outline-none focus:ring-1 focus:ring-ring"
                    placeholder="Search customer by name or email..."
                    value={customerSearch}
                    onChange={e => setCustomerSearch(e.target.value)}
                  />
                </div>
                {filteredCustomers.length > 0 && (
                  <div className="border border-border-primary rounded-lg max-h-40 overflow-y-auto bg-white shadow-sm">
                    {filteredCustomers.slice(0, 8).map(c => (
                      <button
                        key={c.id}
                        className="w-full text-left px-3 py-2 hover:bg-bg-secondary text-[13px] border-b border-border-tertiary last:border-0 transition-colors"
                        onClick={() => { setSelectedCustomerId(c.id); setCustomerSearch(""); }}
                      >
                        <div className="font-medium text-text-primary">{c.name}</div>
                        <div className="text-[11px] text-text-secondary">{c.email}</div>
                      </button>
                    ))}
                  </div>
                )}
                {customerSearch && filteredCustomers.length === 0 && (
                  <div className="text-[12px] text-text-tertiary text-center py-2">No customers found</div>
                )}
              </div>
            )}
          </div>
          <div className="flex gap-3">
            <div className="flex flex-col gap-2 flex-1">
              <label className="text-[13px] font-medium">Total Amount ($) *</label>
              <Input type="number" value={amount} onChange={e => setAmount(e.target.value)} min="0" step="0.01" placeholder="99.99" />
            </div>
            <div className="flex flex-col gap-2 flex-1">
              <label className="text-[13px] font-medium">Status</label>
              <select
                className="flex h-9 w-full items-center justify-between whitespace-nowrap rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-ring"
                value={status}
                onChange={e => setStatus(e.target.value)}
              >
                <option value="PENDING">Pending</option>
                <option value="CONFIRMED">Confirmed</option>
                <option value="SHIPPED">Shipped</option>
                <option value="DELIVERED">Delivered</option>
                <option value="CANCELLED">Cancelled</option>
              </select>
            </div>
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Order Number (optional)</label>
            <Input value={orderNumber} onChange={e => setOrderNumber(e.target.value)} placeholder="e.g. ORD-2024-001 (auto-generated if blank)" />
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} className="flex-1">Cancel</Button>
          <Button
            onClick={() => mutation.mutate()}
            disabled={!selectedCustomerId || !amount || mutation.isPending}
            className="flex-1"
          >
            {mutation.isPending ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
            Create Order
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

// ─── Update Order Status Modal ────────────────────────────────────────────────
function UpdateStatusModal({ order, open, onClose }: { order: any; open: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [status, setStatus] = useState(order?.status || "PENDING");

  const mutation = useMutation({
    mutationFn: () => api.patch(`/orders/${order.id}/status`, { status }),
    onSuccess: () => {
      toast.success("Order status updated!");
      queryClient.invalidateQueries({ queryKey: ['orders'] });
      queryClient.invalidateQueries({ queryKey: ['order', order.id] });
      onClose();
    },
    onError: (err: any) => toast.error(err.message || "Failed to update status"),
  });

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-sm">
        <DialogHeader><DialogTitle>Update Order Status</DialogTitle></DialogHeader>
        <div className="flex flex-col gap-4 py-4">
          <div className="text-[12px] text-text-secondary">Order: <span className="font-medium text-text-primary">{order?.orderNumber || order?.id?.substring(0, 8)}</span></div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">New Status</label>
            <select
              className="flex h-9 w-full items-center justify-between whitespace-nowrap rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-ring"
              value={status}
              onChange={e => setStatus(e.target.value)}
            >
              <option value="PENDING">Pending</option>
              <option value="CONFIRMED">Confirmed</option>
              <option value="SHIPPED">Shipped</option>
              <option value="DELIVERED">Delivered</option>
              <option value="RETURNED">Returned</option>
              <option value="CANCELLED">Cancelled</option>
            </select>
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} className="flex-1">Cancel</Button>
          <Button onClick={() => mutation.mutate()} disabled={mutation.isPending} className="flex-1">
            {mutation.isPending ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
            Update Status
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

// ─── Main Page ────────────────────────────────────────────────────────────────
export default function OrdersPage() {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [selectedOrderId, setSelectedOrderId] = useState<string | null>(null);
  const [createOpen, setCreateOpen] = useState(false);
  const [updateStatusOrder, setUpdateStatusOrder] = useState<any>(null);

  const { data: ordersData, isLoading, isError } = useQuery({
    queryKey: ['orders', page],
    queryFn: () => api.get(`/orders?page=${page}&size=20`),
    staleTime: 30_000,
  });

  const queryClient = useQueryClient();

  const { data: selectedOrder, isLoading: isLoadingSelected } = useQuery({
    queryKey: ['order', selectedOrderId],
    queryFn: () => api.get(`/orders/${selectedOrderId}`),
    enabled: !!selectedOrderId,
    staleTime: 30_000,
  });

  const orders: any[] = Array.isArray(ordersData) ? ordersData : (ordersData?.content || []);
  const totalPages: number = (ordersData as any)?._pagination?.totalPages || ordersData?.totalPages || 0;

  // Client-side search
  const filtered = useMemo(() => {
    if (!search.trim()) return orders;
    const q = search.toLowerCase();
    return orders.filter(o =>
      (o.orderNumber || '').toLowerCase().includes(q) ||
      (o.id || '').toLowerCase().includes(q) ||
      (o.customerId || '').toLowerCase().includes(q) ||
      (o.status || '').toLowerCase().includes(q)
    );
  }, [orders, search]);

  const statusColor = (s: string) => {
    switch (s) {
      case 'DELIVERED': return 'bg-green-50 text-green-700 hover:bg-green-50';
      case 'CONFIRMED': case 'SHIPPED': return 'bg-blue-50 text-blue-700 hover:bg-blue-50';
      case 'PENDING': return 'bg-orange-50 text-orange-700 hover:bg-orange-50';
      case 'RETURNED': return 'bg-purple-50 text-purple-700 hover:bg-purple-50';
      case 'CANCELLED': return 'bg-red-50 text-red-700 hover:bg-red-50';
      default: return 'bg-gray-50 text-gray-700 hover:bg-gray-50';
    }
  };

  return (
    <Shell title="Orders" topbarActions={
      <>
        <Button size="sm" className="h-8 text-[13px] gap-2" onClick={() => setCreateOpen(true)}>
          <Plus className="w-4 h-4" /> New Order
        </Button>
      </>
    }>
      <div className="flex flex-col sm:flex-row justify-between gap-4 mb-6">
        <div className="flex gap-2 w-full sm:w-auto">
          <div className="relative w-full sm:w-[320px]">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-text-tertiary" />
            <Input
              placeholder="Search by order ID, customer or status..."
              className="pl-9 bg-white border-border-primary h-9 text-[13px]"
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
            {search && (
              <button className="absolute right-2.5 top-2.5" onClick={() => setSearch("")}>
                <X className="w-4 h-4 text-text-tertiary hover:text-text-primary" />
              </button>
            )}
          </div>
          {search && <span className="text-[12px] text-text-secondary self-center whitespace-nowrap">{filtered.length} result{filtered.length !== 1 ? 's' : ''}</span>}
        </div>
      </div>

      <Card className="shadow-minimal border-border-primary overflow-hidden">
        {isLoading ? (
          <div className="flex justify-center items-center py-12">
            <Loader2 className="w-6 h-6 text-brand animate-spin" />
          </div>
        ) : isError ? (
          <div className="text-center py-8 text-red-500 text-[13px]">Failed to load orders</div>
        ) : (
          <>
            <div className="overflow-x-auto">
              <table className="w-full text-[13px] text-left">
                <thead className="bg-bg-secondary text-text-secondary border-b border-border-primary">
                  <tr>
                    <th className="px-6 py-3 font-medium">Order ID</th>
                    <th className="px-6 py-3 font-medium">Customer ID</th>
                    <th className="px-6 py-3 font-medium">Date</th>
                    <th className="px-6 py-3 font-medium">Status</th>
                    <th className="px-6 py-3 font-medium text-right">Total</th>
                    <th className="px-6 py-3 font-medium text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-border-tertiary">
                  {filtered.length === 0 ? (
                    <tr>
                      <td colSpan={6} className="text-center py-8 text-text-tertiary">
                        {search ? `No orders match "${search}"` : 'No orders found.'}
                      </td>
                    </tr>
                  ) : (
                    filtered.map((o: any) => (
                      <tr
                        key={o.id}
                        className={`hover:bg-bg-secondary/50 transition-colors cursor-pointer ${selectedOrderId === o.id ? 'bg-brand-light/30' : ''}`}
                        onClick={() => setSelectedOrderId(selectedOrderId === o.id ? null : o.id)}
                      >
                        <td className="px-6 py-3 font-medium text-text-primary">{o.orderNumber || o.id?.substring(0, 8)}</td>
                        <td className="px-6 py-3 text-text-secondary">
                           <span className="font-mono text-[11px] bg-bg-secondary border border-border-secondary px-1.5 py-0.5 rounded text-text-secondary">
                             {o.customerId?.substring(0, 8)}...
                           </span>
                        </td>
                        <td className="px-6 py-3 text-text-secondary">{new Date(o.createdAt).toLocaleDateString()}</td>
                        <td className="px-6 py-3">
                           <Badge variant="secondary" className={`text-[10px] h-5 px-2 font-medium ${statusColor(o.status)}`}>
                             {o.status}
                           </Badge>
                        </td>
                        <td className="px-6 py-3 font-medium text-right text-text-primary">${o.totalAmount?.toFixed(2)}</td>
                        <td className="px-6 py-3 text-right" onClick={e => e.stopPropagation()}>
                          <Button
                            variant="outline"
                            size="sm"
                            className="h-6 text-[10px] px-2"
                            onClick={() => setUpdateStatusOrder(o)}
                          >
                            <ChevronDown className="w-3 h-3 mr-1" /> Status
                          </Button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {/* Order Detail Panel */}
            {selectedOrderId && (
              <div className="border-t border-border-tertiary bg-bg-secondary px-6 py-4">
                <div className="text-[12px] font-medium text-text-primary mb-3 flex items-center gap-2">
                  Order Details
                  <button className="text-text-tertiary hover:text-text-primary ml-auto" onClick={() => setSelectedOrderId(null)}>
                    <X className="w-4 h-4" />
                  </button>
                </div>
                {isLoadingSelected ? (
                  <div className="flex items-center gap-2 text-[12px] text-text-tertiary"><Loader2 className="w-3 h-3 animate-spin" /> Loading order details...</div>
                ) : selectedOrder ? (
                  <div className="grid grid-cols-2 md:grid-cols-5 gap-4 text-[12px]">
                    <div><div className="text-text-tertiary mb-0.5">Order ID</div><div className="font-mono text-[11px] text-text-secondary">{(selectedOrder as any).id?.substring(0, 16)}...</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Order Number</div><div className="font-medium text-text-primary">{(selectedOrder as any).orderNumber || 'N/A'}</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Date</div><div className="font-medium text-text-primary">{new Date((selectedOrder as any).createdAt).toLocaleString()}</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Status</div><div><Badge className={`text-[10px] px-2 h-5 font-medium ${statusColor((selectedOrder as any).status)}`}>{(selectedOrder as any).status}</Badge></div></div>
                    <div><div className="text-text-tertiary mb-0.5">Total</div><div className="font-medium text-green-600">${(selectedOrder as any).totalAmount?.toFixed(2)}</div></div>
                  </div>
                ) : null}
              </div>
            )}
          </>
        )}
      </Card>

      {!isLoading && !isError && totalPages > 1 && (
        <div className="flex justify-between items-center mt-4 text-[13px]">
           <span className="text-text-secondary">Page {page + 1} of {totalPages}</span>
           <div className="flex gap-2">
              <Button variant="outline" size="sm" disabled={page === 0} onClick={() => setPage(p => p - 1)}>Previous</Button>
              <Button variant="outline" size="sm" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Next</Button>
           </div>
        </div>
      )}

      <CreateOrderModal open={createOpen} onClose={() => setCreateOpen(false)} />
      {updateStatusOrder && (
        <UpdateStatusModal order={updateStatusOrder} open={!!updateStatusOrder} onClose={() => setUpdateStatusOrder(null)} />
      )}
    </Shell>
  );
}
