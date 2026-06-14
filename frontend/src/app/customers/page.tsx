"use client";

import { Shell } from "@/components/layout/Shell";
import { Card, CardHeader, CardTitle, CardContent } from "@/components/ui/card";
import { Search, Loader2, Plus, UploadCloud, Mail, Edit, Trash2, X } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import Link from "next/link";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState, useMemo } from "react";
import { toast } from "sonner";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";

// ─── Modals ───────────────────────────────────────────────────────────────────

function CreateCustomerModal({ open, onClose }: { open: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [externalId, setExternalId] = useState("");

  const mutation = useMutation({
    mutationFn: () => api.post('/customers', {
      name,
      email,
      phone: phone || undefined,
      externalId: externalId || `ext-${Date.now()}`,
    }),
    onSuccess: () => {
      toast.success("Customer created successfully!");
      queryClient.invalidateQueries({ queryKey: ['customers'] });
      onClose();
      setName(""); setEmail(""); setPhone(""); setExternalId("");
    },
    onError: (err: any) => toast.error(err.message || "Failed to create customer"),
  });

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader><DialogTitle>Add New Customer</DialogTitle></DialogHeader>
        <div className="flex flex-col gap-4 py-4">
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Full Name *</label>
            <Input value={name} onChange={e => setName(e.target.value)} placeholder="e.g. Jane Smith" />
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Email Address *</label>
            <Input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder="e.g. jane@example.com" />
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Phone Number</label>
            <Input value={phone} onChange={e => setPhone(e.target.value)} placeholder="e.g. +91 98765 43210" />
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">External ID</label>
            <Input value={externalId} onChange={e => setExternalId(e.target.value)} placeholder="Your CRM / ERP ID (optional)" />
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} className="flex-1">Cancel</Button>
          <Button onClick={() => mutation.mutate()} disabled={!name || !email || mutation.isPending} className="flex-1">
            {mutation.isPending ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
            Create Customer
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

function EditCustomerModal({ customer, open, onClose }: { customer: any; open: boolean; onClose: () => void }) {
  const queryClient = useQueryClient();
  const [name, setName] = useState(customer?.name || "");
  const [phone, setPhone] = useState(customer?.phone || "");

  const mutation = useMutation({
    mutationFn: () => api.patch(`/customers/${customer.id}`, { name, phone }),
    onSuccess: () => {
      toast.success("Customer updated!");
      queryClient.invalidateQueries({ queryKey: ['customers'] });
      onClose();
    },
    onError: (err: any) => toast.error(err.message || "Failed to update customer"),
  });

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader><DialogTitle>Edit Customer</DialogTitle></DialogHeader>
        <div className="flex flex-col gap-4 py-4">
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Full Name</label>
            <Input value={name} onChange={e => setName(e.target.value)} />
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Email (read-only)</label>
            <Input value={customer?.email || ''} readOnly className="bg-bg-secondary text-text-tertiary cursor-not-allowed" />
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Phone Number</label>
            <Input value={phone} onChange={e => setPhone(e.target.value)} placeholder="+91 98765 43210" />
          </div>
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

function SendEmailModal({ customer, open, onClose }: { customer: any; open: boolean; onClose: () => void }) {
  const [subject, setSubject] = useState("");
  const [body, setBody] = useState("");

  const mutation = useMutation({
    mutationFn: () => api.post('/communications/send', {
      customerId: customer.id,
      channel: 'EMAIL',
      subject,
      body,
    }),
    onSuccess: () => {
      toast.success(`Email sent to ${customer.email}!`);
      onClose();
      setSubject(""); setBody("");
    },
    onError: (err: any) => toast.error(err.message || "Failed to send email"),
  });

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader><DialogTitle>Send Email to {customer?.name}</DialogTitle></DialogHeader>
        <div className="flex flex-col gap-4 py-4">
          <div className="text-[12px] text-text-secondary bg-bg-secondary px-3 py-2 rounded-lg">
            To: <span className="font-medium text-text-primary">{customer?.email}</span>
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Subject *</label>
            <Input value={subject} onChange={e => setSubject(e.target.value)} placeholder="e.g. Special offer just for you!" />
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Message *</label>
            <textarea
              className="flex min-h-[120px] w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring"
              value={body}
              onChange={e => setBody(e.target.value)}
              placeholder="Write your message here..."
            />
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} className="flex-1">Cancel</Button>
          <Button onClick={() => mutation.mutate()} disabled={!subject || !body || mutation.isPending} className="flex-1">
            {mutation.isPending ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : <Mail className="w-4 h-4 mr-2" />}
            Send Email
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

// ─── Main Page ────────────────────────────────────────────────────────────────

export default function CustomersPage() {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [createOpen, setCreateOpen] = useState(false);
  const [editCustomer, setEditCustomer] = useState<any>(null);
  const [emailCustomer, setEmailCustomer] = useState<any>(null);

  const { data: customersData, isLoading, isError } = useQuery({
    queryKey: ['customers', page],
    queryFn: () => api.get(`/customers?page=${page}&size=50`),
    staleTime: 30_000,
  });

  const queryClient = useQueryClient();

  const bulkMutation = useMutation({
    mutationFn: () => api.post('/customers/bulk', [
      { name: "Bulk User 1", email: `bulk1-${Date.now()}@example.com`, externalId: `ext-b1-${Date.now()}` },
      { name: "Bulk User 2", email: `bulk2-${Date.now()}@example.com`, externalId: `ext-b2-${Date.now()}` }
    ]),
    onSuccess: () => { toast.success("Bulk customers created"); queryClient.invalidateQueries({ queryKey: ['customers'] }); }
  });

  const customers: any[] = Array.isArray(customersData) ? customersData : (customersData?.content || []);
  const totalPages: number = (customersData as any)?._pagination?.totalPages || (customersData as any)?.totalPages || 0;

  // Client-side search filter
  const filtered = useMemo(() => {
    if (!search.trim()) return customers;
    const q = search.toLowerCase();
    return customers.filter(c =>
      (c.name || '').toLowerCase().includes(q) ||
      (c.email || '').toLowerCase().includes(q) ||
      (c.phone || '').toLowerCase().includes(q)
    );
  }, [customers, search]);

  return (
    <Shell title="Customers" topbarActions={
      <>
        <Button size="sm" className="h-8 text-[13px] gap-2" onClick={() => setCreateOpen(true)}>
          <Plus className="w-4 h-4" /> Add Customer
        </Button>
        <Button variant="outline" size="sm" className="h-8 text-[13px] gap-2" onClick={() => bulkMutation.mutate()} disabled={bulkMutation.isPending}>
          <UploadCloud className="w-4 h-4" /> Bulk Sync
        </Button>
      </>
    }>
      <div className="flex flex-col sm:flex-row justify-between gap-4 mb-6">
        <div className="flex gap-2 w-full sm:w-auto">
          <div className="relative w-full sm:w-[320px]">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-text-tertiary" />
            <Input
              placeholder="Search by name, email or phone..."
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
          {search && (
            <span className="text-[12px] text-text-secondary self-center whitespace-nowrap">
              {filtered.length} result{filtered.length !== 1 ? 's' : ''}
            </span>
          )}
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
                  <th className="px-6 py-3 font-medium">Status</th>
                  <th className="px-6 py-3 font-medium text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-border-tertiary">
                {filtered.length === 0 ? (
                  <tr>
                    <td colSpan={5} className="text-center py-8 text-text-tertiary">
                      {search ? `No customers match "${search}"` : 'No customers found.'}
                    </td>
                  </tr>
                ) : (
                  filtered.map((c: any) => (
                    <tr key={c.id} className="hover:bg-bg-secondary/50 transition-colors">
                      <td className="px-6 py-3">
                        <div className="flex items-center gap-3">
                          <Avatar className="h-8 w-8">
                            <AvatarFallback className="bg-brand-light text-brand text-[10px] font-medium">
                              {(c.name || 'U').split(' ').map((n: string) => n[0]).join('').substring(0, 2).toUpperCase()}
                            </AvatarFallback>
                          </Avatar>
                          <div>
                            <div className="font-medium text-text-primary">{c.name || "Unknown"}</div>
                            {c.externalId && <div className="text-[11px] text-text-tertiary font-mono">{c.externalId}</div>}
                          </div>
                        </div>
                      </td>
                      <td className="px-6 py-3 text-text-secondary">{c.email}</td>
                      <td className="px-6 py-3 text-text-secondary">{c.phone || "—"}</td>
                      <td className="px-6 py-3">
                        <span className={`inline-flex items-center gap-1.5 ${c.globallyOptedOut ? 'text-red-600' : 'text-green-600'}`}>
                          <div className={`w-1.5 h-1.5 rounded-full ${c.globallyOptedOut ? 'bg-red-600' : 'bg-green-600'}`} />
                          {c.globallyOptedOut ? 'Opted Out' : 'Subscribed'}
                        </span>
                      </td>
                      <td className="px-6 py-3 text-right">
                        <div className="flex items-center gap-1 justify-end">
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-7 text-[11px] px-2 text-blue-600 hover:text-blue-700 hover:bg-blue-50"
                            onClick={() => setEmailCustomer(c)}
                            title="Send Email"
                          >
                            <Mail className="w-3.5 h-3.5" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-7 text-[11px] px-2 text-text-secondary hover:text-text-primary"
                            onClick={() => setEditCustomer(c)}
                            title="Edit Customer"
                          >
                            <Edit className="w-3.5 h-3.5" />
                          </Button>
                          <Link href={`/customers/${c.id}`}>
                            <Button variant="ghost" size="sm" className="h-7 text-[11px] px-2 text-text-secondary hover:text-text-primary">
                              View →
                            </Button>
                          </Link>
                        </div>
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

      {/* Modals */}
      <CreateCustomerModal open={createOpen} onClose={() => setCreateOpen(false)} />
      {editCustomer && (
        <EditCustomerModal customer={editCustomer} open={!!editCustomer} onClose={() => setEditCustomer(null)} />
      )}
      {emailCustomer && (
        <SendEmailModal customer={emailCustomer} open={!!emailCustomer} onClose={() => setEmailCustomer(null)} />
      )}
    </Shell>
  );
}
