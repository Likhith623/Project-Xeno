"use client";

import { Shell } from "@/components/layout/Shell";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Search, Filter, Loader2, Plus, UploadCloud, Mail, Tag } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState } from "react";
import { toast } from "sonner";

export default function CustomersPage() {
  const [page, setPage] = useState(0);

  const { data: customersData, isLoading, isError } = useQuery({
    queryKey: ['customers', page],
    queryFn: () => api.get(`/customers?page=${page}&size=20`),
    staleTime: 30_000,
  });

  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: () => api.post('/customers', {
      name: "Sample User", email: `sample-${Date.now()}@example.com`, externalId: `ext-${Date.now()}`
    }),
    onSuccess: () => { toast.success("Customer created"); queryClient.invalidateQueries({queryKey: ['customers']}); }
  });

  const bulkMutation = useMutation({
    mutationFn: () => api.post('/customers/bulk', [
      { name: "Bulk User 1", email: `bulk1-${Date.now()}@example.com`, externalId: `ext-b1-${Date.now()}` },
      { name: "Bulk User 2", email: `bulk2-${Date.now()}@example.com`, externalId: `ext-b2-${Date.now()}` }
    ]),
    onSuccess: () => { toast.success("Bulk customers created"); queryClient.invalidateQueries({queryKey: ['customers']}); }
  });

  const searchEmailMutation = useMutation({
    mutationFn: () => api.get('/customers/by-email?email=sample@example.com'),
    onSuccess: () => toast.success("Search by email executed"),
    onError: () => toast.error("Email not found")
  });

  const searchTagMutation = useMutation({
    mutationFn: () => api.get('/customers/by-tag?tag=VIP'),
    onSuccess: () => toast.success("Search by tag executed")
  });

  const customers: any[] = Array.isArray(customersData) ? customersData : [];
  const totalPages: number = (customersData as any)?._pagination?.totalPages || 0;

  return (
    <Shell title="Customers" topbarActions={
      <>
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => createMutation.mutate()} disabled={createMutation.isPending}>
          <Plus className="w-4 h-4" /> New Customer
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
            <Input placeholder="Search by name or email..." className="pl-9 bg-white border-border-primary h-9 text-[13px]" />
          </div>
          <Button variant="outline" size="sm" className="h-9 gap-2 text-[13px]" onClick={() => searchEmailMutation.mutate()} disabled={searchEmailMutation.isPending}>
            <Mail className="w-4 h-4" /> Test /by-email
          </Button>
          <Button variant="outline" size="sm" className="h-9 gap-2 text-[13px]" onClick={() => searchTagMutation.mutate()} disabled={searchTagMutation.isPending}>
            <Tag className="w-4 h-4" /> Test /by-tag
          </Button>
        </div>
      </div>

      <Card className="shadow-minimal border-border-primary overflow-hidden">
        {isLoading ? (
          <div className="flex justify-center items-center p-12">
            <Loader2 className="w-6 h-6 text-brand animate-spin" />
          </div>
        ) : isError ? (
          <div className="p-8 text-center text-red-500 text-[13px]">Failed to load customers.</div>
        ) : (
          <div className="overflow-x-auto">
            <table className="w-full text-[13px] text-left">
              <thead className="bg-bg-secondary text-text-secondary border-b border-border-primary">
                <tr>
                  <th className="px-6 py-3 font-medium">Customer</th>
                  <th className="px-6 py-3 font-medium">Email</th>
                  <th className="px-6 py-3 font-medium">Phone</th>
                  <th className="px-6 py-3 font-medium">Opt-Out</th>
                  <th className="px-6 py-3 font-medium text-right">Profile</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-border-tertiary">
                {customers.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="text-center py-8 text-text-tertiary">No customers found.</td>
                  </tr>
                ) : (
                  customers.map((c: any) => (
                    <tr key={c.id} className="hover:bg-bg-secondary/50 transition-colors">
                      <td className="px-6 py-3">
                        <div className="flex items-center gap-3">
                          <Avatar className="h-8 w-8">
                            <AvatarFallback className="bg-brand-light text-brand text-[10px] font-medium">
                              {(c.name || 'U').split(' ').map((n: string) => n[0]).join('')}
                            </AvatarFallback>
                          </Avatar>
                          <div className="font-medium text-text-primary">{c.name || "Unknown"}</div>
                        </div>
                      </td>
                      <td className="px-6 py-3 text-text-secondary">{c.email}</td>
                      <td className="px-6 py-3 text-text-secondary">{c.phone || "N/A"}</td>
                      <td className="px-6 py-3">
                        <span className={`inline-flex items-center gap-1.5 ${c.globallyOptedOut ? 'text-red-600' : 'text-green-600'}`}>
                          <div className={`w-1.5 h-1.5 rounded-full ${c.globallyOptedOut ? 'bg-red-600' : 'bg-green-600'}`} />
                          {c.globallyOptedOut ? 'Opted Out' : 'Subscribed'}
                        </span>
                      </td>
                      <td className="px-6 py-3 text-right">
                        <Link href={`/customers/${c.id}`}>
                          <Button variant="ghost" size="sm" className="h-8 text-text-secondary hover:text-text-primary">View 360 &rarr;</Button>
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
