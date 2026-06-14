"use client";

import { Shell } from "@/components/layout/Shell";
import { Card } from "@/components/ui/card";
import { Search, Filter, Loader2, Tag, Archive, Plus, UploadCloud } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState } from "react";
import { toast } from "sonner";
import { Badge } from "@/components/ui/badge";

function AlertTriangleIcon(props: any) {
  return (
    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" {...props}>
      <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z"/>
      <path d="M12 9v4"/>
      <path d="M12 17h.01"/>
    </svg>
  );
}

export default function ProductsPage() {
  const [page, setPage] = useState(0);
  const [selectedProductId, setSelectedProductId] = useState<string | null>(null);

  const { data: productsData, isLoading, isError } = useQuery({
    queryKey: ['products', page],
    queryFn: () => api.get(`/products?page=${page}&size=20`),
    staleTime: 30_000,
  });

  const { data: categoriesData } = useQuery({
    queryKey: ['product-categories'],
    queryFn: () => api.get(`/products/categories`).then(res => Array.isArray(res) ? res : []),
    staleTime: 60_000,
  });

  const queryClient = useQueryClient();

  const createMutation = useMutation({
    mutationFn: () => api.post('/products', { name: "New Product", sku: `SKU-${Date.now()}`, price: 19.99, currency: "INR", brand: "Xeno Demo", isActive: true }),
    onSuccess: () => { toast.success("Product created"); queryClient.invalidateQueries({queryKey: ['products']}); }
  });

  const bulkMutation = useMutation({
    mutationFn: () => api.post('/products/bulk', [
      { name: "Bulk Prod 1", sku: `B1-${Date.now()}`, price: 10.0 },
      { name: "Bulk Prod 2", sku: `B2-${Date.now()}`, price: 20.0 }
    ]),
    onSuccess: () => { toast.success("Bulk products created"); queryClient.invalidateQueries({queryKey: ['products']}); }
  });

  // GET /api/v1/products/{id} — Triggered when user clicks a row
  const { data: selectedProduct, isLoading: isLoadingSelected } = useQuery({
    queryKey: ['product', selectedProductId],
    queryFn: () => api.get(`/products/${selectedProductId}`),
    enabled: !!selectedProductId,
    staleTime: 30_000,
  });

  const products: any[] = Array.isArray(productsData) ? productsData : (productsData?.content || []);
  const totalPages: number = (productsData as any)?._pagination?.totalPages || productsData?.totalPages || 0;
  // Categories API returns { id, name, description, ... } objects
  const categories: any[] = Array.isArray(categoriesData) ? categoriesData : [];

  return (
    <Shell title="Product Catalog" topbarActions={
      <>
        <Button size="sm" className="h-8 text-[13px] gap-2" onClick={() => createMutation.mutate()} disabled={createMutation.isPending}>
          <Plus className="w-4 h-4" /> Add Product
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
            <Input placeholder="Search catalog..." className="pl-9 bg-white border-border-primary h-9 text-[13px]" />
          </div>
          <Button variant="outline" size="sm" className="h-9 gap-2 text-[13px]">
            <Filter className="w-4 h-4" /> Filter
          </Button>
        </div>
      </div>

      {categories.length > 0 && (
        <div className="flex gap-2 mb-4 overflow-x-auto pb-2">
          {categories.map((cat: any, idx: number) => (
            <Badge key={cat.id || idx} variant="outline" className="bg-white hover:bg-bg-secondary text-text-secondary cursor-pointer whitespace-nowrap">
              {cat.name || cat}
            </Badge>
          ))}
        </div>
      )}

      <Card className="shadow-minimal border-border-primary overflow-hidden">
        {isLoading ? (
          <div className="flex justify-center items-center p-12">
            <Loader2 className="w-6 h-6 text-brand animate-spin" />
          </div>
        ) : isError ? (
          <div className="p-8 text-center text-red-500 text-[13px]">Failed to load products.</div>
        ) : (
          <>
            <div className="overflow-x-auto">
              <table className="w-full text-[13px] text-left">
                <thead className="bg-bg-secondary text-text-secondary border-b border-border-primary">
                  <tr>
                    <th className="px-6 py-3 font-medium">SKU</th>
                    <th className="px-6 py-3 font-medium">Product Name</th>
                    <th className="px-6 py-3 font-medium">Category</th>
                    <th className="px-6 py-3 font-medium">Price</th>
                    <th className="px-6 py-3 font-medium">Status</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-border-tertiary">
                  {products.length === 0 ? (
                    <tr>
                      <td colSpan={5} className="text-center py-8 text-text-tertiary">No products found.</td>
                    </tr>
                  ) : (
                    products.map((p: any) => (
                      <tr key={p.id}
                        className={`hover:bg-bg-secondary/50 transition-colors cursor-pointer ${selectedProductId === p.id ? 'bg-brand-light/30 border-l-2 border-brand' : ''}`}
                        onClick={() => setSelectedProductId(selectedProductId === p.id ? null : p.id)}
                      >
                        <td className="px-6 py-3 text-text-secondary font-mono text-[11px]">{p.sku || p.id?.substring(0,8)}</td>
                        <td className="px-6 py-3 font-medium text-text-primary flex items-center gap-2">
                          <Tag className="w-4 h-4 text-brand/50" />
                          {p.name}
                        </td>
                        <td className="px-6 py-3 text-text-secondary">{p.categoryName || 'Uncategorized'}</td>
                        <td className="px-6 py-3 font-medium">${p.price?.toFixed(2)}</td>
                        <td className="px-6 py-3">
                          <span className={`inline-flex items-center gap-1.5 ${p.active ? 'text-green-600' : 'text-text-tertiary'}`}>
                            <div className={`w-1.5 h-1.5 rounded-full ${p.active ? 'bg-green-600' : 'bg-gray-400'}`} />
                            {p.active ? 'Active' : 'Inactive'}
                          </span>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {/* Product Detail Panel: GET /products/{id} */}
            {selectedProductId && (
              <div className="border-t border-border-tertiary bg-bg-secondary px-6 py-4">
                {isLoadingSelected ? (
                  <div className="flex items-center gap-2 text-[12px] text-text-tertiary"><Loader2 className="w-3 h-3 animate-spin" /> Loading product details...</div>
                ) : selectedProduct ? (
                  <div className="grid grid-cols-2 md:grid-cols-4 gap-4 text-[12px]">
                    <div><div className="text-text-tertiary mb-0.5">ID</div><div className="font-mono text-[11px] text-text-secondary">{(selectedProduct as any).id?.substring(0,16)}...</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Name</div><div className="font-medium text-text-primary">{(selectedProduct as any).name}</div></div>
                    <div><div className="text-text-tertiary mb-0.5">SKU</div><div className="font-medium text-text-primary">{(selectedProduct as any).sku}</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Price</div><div className="font-medium text-green-600">${(selectedProduct as any).price?.toFixed(2)}</div></div>
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
