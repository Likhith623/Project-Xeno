"use client";

import { Shell } from "@/components/layout/Shell";
import { Card } from "@/components/ui/card";
import { Search, Loader2 } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Badge } from "@/components/ui/badge";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState } from "react";
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Plus, UploadCloud } from "lucide-react";

export default function OrdersPage() {
  const [page, setPage] = useState(0);
  const [selectedOrderId, setSelectedOrderId] = useState<string | null>(null);

  // The interceptor now returns the inner data directly.
  // For paginated endpoints the backend wraps data in { content, totalPages, ... }
  const { data: ordersData, isLoading, isError } = useQuery({
    queryKey: ['orders', page],
    queryFn: () => api.get(`/orders?page=${page}&size=20`),
    staleTime: 30_000,
  });

  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: () => api.post('/orders', { customerId: "00000000-0000-0000-0000-000000000000", totalAmount: 99.99, status: "PENDING" }),
    onSuccess: () => { toast.success("Order created"); queryClient.invalidateQueries({queryKey: ['orders']}); }
  });

  const bulkMutation = useMutation({
    mutationFn: () => api.post('/orders/bulk', [
      { customerId: "00000000-0000-0000-0000-000000000000", totalAmount: 10.0, status: "PENDING" },
      { customerId: "00000000-0000-0000-0000-000000000000", totalAmount: 20.0, status: "CONFIRMED" }
    ]),
    onSuccess: () => { toast.success("Bulk orders created"); queryClient.invalidateQueries({queryKey: ['orders']}); }
  });

  // GET /api/v1/orders/{id} — Triggered when user clicks a row
  const { data: selectedOrder, isLoading: isLoadingSelected } = useQuery({
    queryKey: ['order', selectedOrderId],
    queryFn: () => api.get(`/orders/${selectedOrderId}`),
    enabled: !!selectedOrderId,
    staleTime: 30_000,
  });

  // Backend may return page object {content, totalPages} or plain array
  const orders: any[] = Array.isArray(ordersData) ? ordersData : (ordersData?.content || []);
  const totalPages: number = (ordersData as any)?._pagination?.totalPages || ordersData?.totalPages || 0;

  return (
    <Shell title="Orders" topbarActions={
      <>
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => createMutation.mutate()} disabled={createMutation.isPending}>
          <Plus className="w-4 h-4" /> New Order
        </Button>
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => bulkMutation.mutate()} disabled={bulkMutation.isPending}>
          <UploadCloud className="w-4 h-4" /> Bulk Sync
        </Button>
      </>
    }>
      <div className="flex flex-col sm:flex-row justify-between gap-4 mb-6">
        <div className="flex gap-2 w-full sm:w-auto">
          <div className="relative w-full sm:w-[300px]">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-text-tertiary" />
            <Input placeholder="Search order ID or customer..." className="pl-9 bg-white border-border-primary h-9 text-[13px]" />
          </div>
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
                  </tr>
                </thead>
                <tbody className="divide-y divide-border-tertiary">
                  {orders.length === 0 ? (
                    <tr>
                      <td colSpan={6} className="text-center py-8 text-text-tertiary">No orders found.</td>
                    </tr>
                  ) : (
                    orders.map((o: any) => (
                      <tr key={o.id}
                        className={`hover:bg-bg-secondary/50 transition-colors cursor-pointer ${selectedOrderId === o.id ? 'bg-brand-light/30 border-l-2 border-brand' : ''}`}
                        onClick={() => setSelectedOrderId(selectedOrderId === o.id ? null : o.id)}
                      >
                        <td className="px-6 py-3 font-medium text-text-primary">{o.orderNumber || o.id?.substring(0,8)}</td>
                        <td className="px-6 py-3 text-text-secondary">
                           <span className="font-mono text-[11px] bg-bg-secondary border border-border-secondary px-1.5 py-0.5 rounded text-text-secondary">
                             {o.customerId?.substring(0,8)}...
                           </span>
                        </td>
                        <td className="px-6 py-3 text-text-secondary">{new Date(o.createdAt).toLocaleDateString()}</td>
                        <td className="px-6 py-3">
                           <Badge variant="secondary" className={`text-[10px] h-5 px-2 font-medium ${
                              o.status === 'DELIVERED' ? 'bg-green-50 text-green-700 hover:bg-green-50' :
                              o.status === 'CONFIRMED' || o.status === 'SHIPPED' ? 'bg-blue-50 text-blue-700 hover:bg-blue-50' :
                              o.status === 'PENDING' ? 'bg-orange-50 text-orange-700 hover:bg-orange-50' :
                              o.status === 'RETURNED' ? 'bg-purple-50 text-purple-700 hover:bg-purple-50' :
                              'bg-red-50 text-red-700 hover:bg-red-50'
                            }`}>
                            {o.status}
                          </Badge>
                        </td>
                        <td className="px-6 py-3 font-medium text-right text-text-primary">${o.totalAmount?.toFixed(2)}</td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {/* Order Detail Panel: GET /orders/{id} */}
            {selectedOrderId && (
              <div className="border-t border-border-tertiary bg-bg-secondary px-6 py-4">
                {isLoadingSelected ? (
                  <div className="flex items-center gap-2 text-[12px] text-text-tertiary"><Loader2 className="w-3 h-3 animate-spin" /> Loading order details...</div>
                ) : selectedOrder ? (
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-[12px]">
                    <div><div className="text-text-tertiary mb-0.5">ID</div><div className="font-mono text-[11px] text-text-secondary">{selectedOrder.id?.substring(0,16)}...</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Order Number</div><div className="font-medium text-text-primary">{selectedOrder.orderNumber || 'N/A'}</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Date</div><div className="font-medium text-text-primary">{new Date(selectedOrder.createdAt).toLocaleString()}</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Total</div><div className="font-medium text-green-600">${selectedOrder.totalAmount?.toFixed(2)}</div></div>
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
    </Shell>
  );
}
