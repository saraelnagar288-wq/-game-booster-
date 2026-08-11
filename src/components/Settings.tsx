import { Info } from "lucide-react";

export default function Settings() {
  return (
    <div className="flex-1 overflow-y-auto pb-24 p-4 space-y-6">
      <header className="pt-4">
        <h1 className="text-2xl font-bold mb-1">Settings</h1>
      </header>

      <div className="bg-neutral-900 border border-neutral-800 rounded-2xl overflow-hidden divide-y divide-neutral-800">
        <div className="p-4 flex justify-between items-center">
          <span className="text-sm">Dark Mode</span>
          <div className="w-10 h-6 bg-cyan-500 rounded-full flex items-center p-1 justify-end">
            <div className="w-4 h-4 bg-black rounded-full" />
          </div>
        </div>
        <div className="p-4 flex justify-between items-center opacity-50">
          <span className="text-sm">Auto-Optimize on Launch</span>
          <div className="w-10 h-6 bg-neutral-800 rounded-full flex items-center p-1 justify-start">
            <div className="w-4 h-4 bg-neutral-500 rounded-full" />
          </div>
        </div>
      </div>

      <div className="bg-neutral-900 border border-neutral-800 rounded-2xl p-5 space-y-3">
        <h3 className="text-sm font-semibold text-neutral-400 uppercase tracking-wider flex items-center gap-2">
          About GameBoost AI <Info size={14} />
        </h3>
        <p className="text-xs text-neutral-400 leading-relaxed">
          GameBoost AI is a concept application demonstrating performance tracking and AI optimization analysis. Because it runs within a Web/PWA sandbox, it strictly adheres to the Honesty System: hardware capabilities are retrieved only via standard browser APIs, and features that require root or deeper Android system access (such as exact FPS monitoring, CPU frequency scaling, or automated in-game graphics modifications) are labeled as restricted or unavailable.
        </p>
      </div>
    </div>
  );
}
