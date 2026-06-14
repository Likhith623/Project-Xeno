"use client";

import { Shell } from "@/components/layout/Shell";
import { Card } from "@/components/ui/card";
import { Search, Loader2, Tag, Plus, X, Edit, ToggleLeft, ToggleRight } from "lucide-react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { api } from "@/lib/api";
import { useState, useMemo } from "react";
import { toast } from "sonner";
import { Badge } from "@/components/ui/badge";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from "@/components/ui/dialog";

// ─── Add Product Modal ─────────────────────────────────────────────────────────
function AddProductModal({ open, onClose, categories }: { open: boolean; onClose: () => void; categories: any[] }) {
  const queryClient = useQueryClient();
  const [name, setName] = useState("");
  const [sku, setSku] = useState("");
  const [price, setPrice] = useState("19.99");
  const [brand, setBrand] = useState("");
  const [categoryId, setCategoryId] = useState("");
  const [description, setDescription] = useState("");

  const mutation = useMutation({
    mutationFn: () => api.post('/products', {
      name,
      sku: sku || `SKU-${Date.now()}`,
      price: parseFloat(price) || 0,
      currency: "INR",
      brand: brand || undefined,
      categoryId: categoryId || undefined,
      description: description || undefined,
      active: true,
    }),
    onSuccess: () => {
      toast.success("Product added!");
      queryClient.invalidateQueries({ queryKey: ['products'] });
      onClose();
      setName(""); setSku(""); setPrice("19.99"); setBrand(""); setCategoryId(""); setDescription("");
    },
    onError: (err: any) => toast.error(err.message || "Failed to add product"),
  });

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader><DialogTitle>Add New Product</DialogTitle></DialogHeader>
        <div className="flex flex-col gap-4 py-4">
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Product Name *</label>
            <Input value={name} onChange={e => setName(e.target.value)} placeholder="e.g. Premium Wireless Headphones" />
          </div>
          <div className="flex gap-3">
            <div className="flex flex-col gap-2 flex-1">
              <label className="text-[13px] font-medium">SKU</label>
              <Input value={sku} onChange={e => setSku(e.target.value)} placeholder="e.g. SKU-2024-001" />
            </div>
            <div className="flex flex-col gap-2 flex-1">
              <label className="text-[13px] font-medium">Price *</label>
              <Input type="number" value={price} onChange={e => setPrice(e.target.value)} min="0" step="0.01" />
            </div>
          </div>
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Brand</label>
            <Input value={brand} onChange={e => setBrand(e.target.value)} placeholder="e.g. Sony, Samsung, Xeno" />
          </div>
          {categories.length > 0 && (
            <div className="flex flex-col gap-2">
              <label className="text-[13px] font-medium">Category</label>
              <select
                className="flex h-9 w-full items-center justify-between whitespace-nowrap rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm focus:outline-none focus:ring-1 focus:ring-ring"
                value={categoryId}
                onChange={e => setCategoryId(e.target.value)}
              >
                <option value="">No Category</option>
                {categories.map(c => (
                  <option key={c.id} value={c.id}>{c.name || c}</option>
                ))}
              </select>
            </div>
          )}
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Description</label>
            <textarea
              className="flex min-h-[60px] w-full rounded-md border border-input bg-transparent px-3 py-2 text-sm shadow-sm placeholder:text-muted-foreground focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring"
              value={description}
              onChange={e => setDescription(e.target.value)}
              placeholder="Product description (optional)"
            />
          </div>
        </div>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} className="flex-1">Cancel</Button>
          <Button onClick={() => mutation.mutate()} disabled={!name || !price || mutation.isPending} className="flex-1">
            {mutation.isPending ? <Loader2 className="w-4 h-4 animate-spin mr-2" /> : null}
            Add Product
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}

// ─── Edit Product Modal ────────────────────────────────────────────────────────
function EditProductModal({ product, open, onClose, categories }: { product: any; open: boolean; onClose: () => void; categories: any[] }) {
  const queryClient = useQueryClient();
  const [name, setName] = useState(product?.name || "");
  const [price, setPrice] = useState(String(product?.price || ""));
  const [brand, setBrand] = useState(product?.brand || "");
  const [active, setActive] = useState(product?.active !== false);

  const mutation = useMutation({
    mutationFn: () => api.put(`/products/${product.id}`, { name, price: parseFloat(price), brand, active }),
    onSuccess: () => {
      toast.success("Product updated!");
      queryClient.invalidateQueries({ queryKey: ['products'] });
      queryClient.invalidateQueries({ queryKey: ['product', product.id] });
      onClose();
    },
    onError: (err: any) => toast.error(err.message || "Failed to update product"),
  });

  return (
    <Dialog open={open} onOpenChange={onClose}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader><DialogTitle>Edit Product</DialogTitle></DialogHeader>
        <div className="flex flex-col gap-4 py-4">
          <div className="flex flex-col gap-2">
            <label className="text-[13px] font-medium">Product Name</label>
            <Input value={name} onChange={e => setName(e.target.value)} />
          </div>
          <div className="flex gap-3">
            <div className="flex flex-col gap-2 flex-1">
              <label className="text-[13px] font-medium">Price</label>
              <Input type="number" value={price} onChange={e => setPrice(e.target.value)} min="0" step="0.01" />
            </div>
            <div className="flex flex-col gap-2 flex-1">
              <label className="text-[13px] font-medium">Brand</label>
              <Input value={brand} onChange={e => setBrand(e.target.value)} />
            </div>
          </div>
          <div className="flex items-center justify-between">
            <label className="text-[13px] font-medium">Active Status</label>
            <button
              onClick={() => setActive(!active)}
              className={`flex items-center gap-2 px-3 py-1.5 rounded-lg text-[12px] font-medium transition-colors ${active ? 'bg-green-50 text-green-700 border border-green-200' : 'bg-gray-50 text-gray-600 border border-gray-200'}`}
            >
              {active ? <ToggleRight className="w-4 h-4" /> : <ToggleLeft className="w-4 h-4" />}
              {active ? 'Active' : 'Inactive'}
            </button>
          </div>
          <div className="text-[12px] text-text-secondary bg-bg-secondary px-3 py-2 rounded-lg">SKU: {product?.sku}</div>
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

// ─── Main Page ────────────────────────────────────────────────────────────────
export default function ProductsPage() {
  const [page, setPage] = useState(0);
  const [search, setSearch] = useState("");
  const [selectedCategoryId, setSelectedCategoryId] = useState<string | null>(null);
  const [statusFilter, setStatusFilter] = useState<'all' | 'active' | 'inactive'>('all');
  const [addOpen, setAddOpen] = useState(false);
  const [editProduct, setEditProduct] = useState<any>(null);
  const [selectedProductId, setSelectedProductId] = useState<string | null>(null);

  const { data: productsData, isLoading, isError } = useQuery({
    queryKey: ['products', page],
    queryFn: () => api.get(`/products?page=${page}&size=50`),
    staleTime: 30_000,
  });

  const { data: categoriesData } = useQuery({
    queryKey: ['product-categories'],
    queryFn: () => api.get(`/products/categories`).then(res => Array.isArray(res) ? res : []),
    staleTime: 60_000,
  });

  const { data: selectedProduct, isLoading: isLoadingSelected } = useQuery({
    queryKey: ['product', selectedProductId],
    queryFn: () => api.get(`/products/${selectedProductId}`),
    enabled: !!selectedProductId,
    staleTime: 30_000,
  });

  const products: any[] = Array.isArray(productsData) ? productsData : (productsData?.content || []);
  const totalPages: number = (productsData as any)?._pagination?.totalPages || productsData?.totalPages || 0;
  const categories: any[] = Array.isArray(categoriesData) ? categoriesData : [];

  // Client-side filtering
  const filtered = useMemo(() => {
    let list = products;
    if (search.trim()) {
      const q = search.toLowerCase();
      list = list.filter(p =>
        (p.name || '').toLowerCase().includes(q) ||
        (p.sku || '').toLowerCase().includes(q) ||
        (p.brand || '').toLowerCase().includes(q)
      );
    }
    if (selectedCategoryId) {
      list = list.filter(p => p.categoryId === selectedCategoryId || p.categoryName === categories.find(c => c.id === selectedCategoryId)?.name);
    }
    if (statusFilter === 'active') list = list.filter(p => p.active);
    if (statusFilter === 'inactive') list = list.filter(p => !p.active);
    return list;
  }, [products, search, selectedCategoryId, statusFilter, categories]);

  const activeCount = products.filter(p => p.active).length;
  const inactiveCount = products.filter(p => !p.active).length;

  return (
    <Shell title="Product Catalog" topbarActions={
      <>
        <Button size="sm" className="h-8 text-[13px] gap-2" onClick={() => setAddOpen(true)}>
          <Plus className="w-4 h-4" /> Add Product
        </Button>
      </>
    }>
      {/* Stats Bar */}
      <div className="flex gap-4 mb-6 flex-wrap">
        <button
          onClick={() => setStatusFilter('all')}
          className={`px-3 py-1.5 rounded-lg text-[12px] font-medium transition-colors border ${statusFilter === 'all' ? 'bg-text-primary text-white border-text-primary' : 'border-border-primary text-text-secondary hover:bg-bg-secondary'}`}
        >
          All Products ({products.length})
        </button>
        <button
          onClick={() => setStatusFilter('active')}
          className={`px-3 py-1.5 rounded-lg text-[12px] font-medium transition-colors border ${statusFilter === 'active' ? 'bg-green-600 text-white border-green-600' : 'border-border-primary text-text-secondary hover:bg-bg-secondary'}`}
        >
          Active ({activeCount})
        </button>
        <button
          onClick={() => setStatusFilter('inactive')}
          className={`px-3 py-1.5 rounded-lg text-[12px] font-medium transition-colors border ${statusFilter === 'inactive' ? 'bg-gray-600 text-white border-gray-600' : 'border-border-primary text-text-secondary hover:bg-bg-secondary'}`}
        >
          Inactive ({inactiveCount})
        </button>
      </div>

      <div className="flex flex-col sm:flex-row justify-between gap-4 mb-4">
        <div className="flex gap-2 w-full sm:w-auto">
          <div className="relative w-full sm:w-[320px]">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-text-tertiary" />
            <Input
              placeholder="Search by name, SKU or brand..."
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
          {(search || selectedCategoryId || statusFilter !== 'all') && (
            <button
              className="text-[12px] text-text-secondary hover:text-text-primary self-center whitespace-nowrap"
              onClick={() => { setSearch(""); setSelectedCategoryId(null); setStatusFilter('all'); }}
            >
              Clear filters
            </button>
          )}
        </div>
      </div>

      {/* Category filter badges */}
      {categories.length > 0 && (
        <div className="flex gap-2 mb-4 overflow-x-auto pb-2 flex-wrap">
          <Badge
            variant={selectedCategoryId === null ? "default" : "outline"}
            className={`cursor-pointer whitespace-nowrap text-[12px] px-3 py-1 transition-colors ${selectedCategoryId === null ? 'bg-text-primary text-white hover:bg-text-primary/90' : 'bg-white hover:bg-bg-secondary text-text-secondary'}`}
            onClick={() => setSelectedCategoryId(null)}
          >
            All Categories
          </Badge>
          {categories.map((cat: any, idx: number) => (
            <Badge
              key={cat.id || idx}
              variant={selectedCategoryId === (cat.id || cat) ? "default" : "outline"}
              className={`cursor-pointer whitespace-nowrap text-[12px] px-3 py-1 transition-colors ${selectedCategoryId === (cat.id || cat) ? 'bg-brand text-white hover:bg-brand/90' : 'bg-white hover:bg-bg-secondary text-text-secondary'}`}
              onClick={() => setSelectedCategoryId(selectedCategoryId === (cat.id || cat) ? null : (cat.id || cat))}
            >
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
                    <th className="px-6 py-3 font-medium">Brand</th>
                    <th className="px-6 py-3 font-medium">Price</th>
                    <th className="px-6 py-3 font-medium">Status</th>
                    <th className="px-6 py-3 font-medium text-right">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-border-tertiary">
                  {filtered.length === 0 ? (
                    <tr>
                      <td colSpan={7} className="text-center py-8 text-text-tertiary">
                        {search || selectedCategoryId || statusFilter !== 'all' ? 'No products match your filters.' : 'No products found.'}
                      </td>
                    </tr>
                  ) : (
                    filtered.map((p: any) => (
                      <tr
                        key={p.id}
                        className={`hover:bg-bg-secondary/50 transition-colors cursor-pointer ${selectedProductId === p.id ? 'bg-brand-light/30' : ''}`}
                        onClick={() => setSelectedProductId(selectedProductId === p.id ? null : p.id)}
                      >
                        <td className="px-6 py-3 text-text-secondary font-mono text-[11px]">{p.sku || p.id?.substring(0, 8)}</td>
                        <td className="px-6 py-3 font-medium text-text-primary">
                          <div className="flex items-center gap-2">
                            <Tag className="w-4 h-4 text-brand/50 shrink-0" />
                            {p.name}
                          </div>
                        </td>
                        <td className="px-6 py-3 text-text-secondary">{p.categoryName || 'Uncategorized'}</td>
                        <td className="px-6 py-3 text-text-secondary">{p.brand || '—'}</td>
                        <td className="px-6 py-3 font-medium">${p.price?.toFixed(2)}</td>
                        <td className="px-6 py-3">
                          <span className={`inline-flex items-center gap-1.5 ${p.active ? 'text-green-600' : 'text-text-tertiary'}`}>
                            <div className={`w-1.5 h-1.5 rounded-full ${p.active ? 'bg-green-600' : 'bg-gray-400'}`} />
                            {p.active ? 'Active' : 'Inactive'}
                          </span>
                        </td>
                        <td className="px-6 py-3 text-right" onClick={e => e.stopPropagation()}>
                          <Button
                            variant="ghost"
                            size="sm"
                            className="h-7 text-[11px] px-2"
                            onClick={() => setEditProduct(p)}
                          >
                            <Edit className="w-3.5 h-3.5 mr-1" /> Edit
                          </Button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {/* Product Detail Panel */}
            {selectedProductId && (
              <div className="border-t border-border-tertiary bg-bg-secondary px-6 py-4">
                <div className="flex items-center justify-between mb-3">
                  <div className="text-[12px] font-medium text-text-primary">Product Details</div>
                  <button className="text-text-tertiary hover:text-text-primary" onClick={() => setSelectedProductId(null)}>
                    <X className="w-4 h-4" />
                  </button>
                </div>
                {isLoadingSelected ? (
                  <div className="flex items-center gap-2 text-[12px] text-text-tertiary"><Loader2 className="w-3 h-3 animate-spin" /> Loading product details...</div>
                ) : selectedProduct ? (
                  <div className="grid grid-cols-2 md:grid-cols-5 gap-4 text-[12px]">
                    <div><div className="text-text-tertiary mb-0.5">ID</div><div className="font-mono text-[11px] text-text-secondary">{(selectedProduct as any).id?.substring(0, 16)}...</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Name</div><div className="font-medium text-text-primary">{(selectedProduct as any).name}</div></div>
                    <div><div className="text-text-tertiary mb-0.5">SKU</div><div className="font-medium text-text-primary">{(selectedProduct as any).sku}</div></div>
                    <div><div className="text-text-tertiary mb-0.5">Brand</div><div className="font-medium text-text-primary">{(selectedProduct as any).brand || '—'}</div></div>
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

      <AddProductModal open={addOpen} onClose={() => setAddOpen(false)} categories={categories} />
      {editProduct && (
        <EditProductModal product={editProduct} open={!!editProduct} onClose={() => setEditProduct(null)} categories={categories} />
      )}
    </Shell>
  );
}
