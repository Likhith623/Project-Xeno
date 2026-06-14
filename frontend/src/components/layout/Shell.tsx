import { Sidebar } from "./Sidebar";
import { Topbar } from "./Topbar";

export function Shell({
  children,
  title,
  topbarActions
}: {
  children: React.ReactNode;
  title: string;
  topbarActions?: React.ReactNode;
}) {
  return (
    <div className="flex h-screen w-full bg-[#F1F3F5] overflow-hidden font-sans">
      <Sidebar />
      <div className="flex-1 flex flex-col min-w-0 overflow-hidden">
        <Topbar title={title}>{topbarActions}</Topbar>
        <main className="flex-1 overflow-y-auto p-6">
          {children}
        </main>
      </div>
    </div>
  );
}
