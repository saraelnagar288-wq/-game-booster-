import { useState, useEffect } from "react";
import { useDeviceAnalyzer } from "../lib/useDeviceAnalyzer";
import { Activity, Cpu, Database, Thermometer, Info } from "lucide-react";
import { motion } from "motion/react";
import { cn } from "../lib/utils";

export default function Monitor() {
  const device = useDeviceAnalyzer();
  const [ramUsage, setRamUsage] = useState(0);
  
  useEffect(() => {
    // Simulate RAM fluctuation for demonstration since real RAM usage isn't exposed via Web APIs reliably
    const interval = setInterval(() => {
      setRamUsage(Math.random() * 15 + 60); // 60-75% usage
    }, 2000);
    return () => clearInterval(interval);
  }, []);

  return (
    <div className="flex-1 overflow-y-auto pb-24 p-4 space-y-6">
      <header className="pt-4">
        <h1 className="text-2xl font-bold mb-1">Performance Monitor</h1>
        <p className="text-sm text-neutral-400">Real-time device statistics.</p>
      </header>

      <div className="bg-amber-500/10 border border-amber-500/20 rounded-xl p-4 flex gap-3 text-amber-500/90">
        <Info size={18} className="shrink-0 mt-0.5" />
        <p className="text-xs leading-relaxed">
          <strong>Honesty Notice:</strong> Web browsers restrict access to exact CPU frequencies, real-time GPU loads, and thermal sensors. Data shown is based on available APIs or estimated limits.
        </p>
      </div>

      <div className="space-y-4">
        {/* RAM */}
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} className="bg-neutral-900 border border-neutral-800 rounded-2xl p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 text-neutral-300">
               <Database size={18} className="text-neutral-500" />
               <span className="text-sm font-semibold">RAM Usage (Simulated)</span>
            </div>
            <span className="text-cyan-400 font-bold text-sm">{ramUsage.toFixed(1)}%</span>
          </div>
          <div className="h-2 bg-neutral-950 rounded-full overflow-hidden">
            <div 
              className={cn("h-full transition-all duration-1000", ramUsage > 80 ? "bg-red-500" : "bg-cyan-500")}
              style={{ width: `${ramUsage}%` }}
            />
          </div>
          <div className="mt-3 flex justify-between text-xs text-neutral-500">
            <span>Detected: {device.ram} GB Total</span>
          </div>
        </motion.div>

        {/* CPU */}
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.1 }} className="bg-neutral-900 border border-neutral-800 rounded-2xl p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 text-neutral-300">
               <Cpu size={18} className="text-neutral-500" />
               <span className="text-sm font-semibold">CPU Architecture</span>
            </div>
          </div>
          <div className="grid grid-cols-2 gap-3">
             <div className="bg-neutral-950 rounded-xl p-3 border border-neutral-800/50">
               <div className="text-xs text-neutral-500 mb-1">Cores (Detected)</div>
               <div className="text-sm font-medium">{device.cores} Cores</div>
             </div>
             <div className="bg-neutral-950 rounded-xl p-3 border border-neutral-800/50">
               <div className="text-xs text-neutral-500 mb-1">Frequency</div>
               <div className="text-sm font-medium text-neutral-500">Unavailable</div>
             </div>
          </div>
        </motion.div>

        {/* Thermals */}
        <motion.div initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: 0.2 }} className="bg-neutral-900 border border-neutral-800 rounded-2xl p-5">
          <div className="flex items-center justify-between mb-4">
            <div className="flex items-center gap-2 text-neutral-300">
               <Thermometer size={18} className="text-neutral-500" />
               <span className="text-sm font-semibold">Thermal Status</span>
            </div>
            <span className="text-neutral-500 font-bold text-sm">Unavailable</span>
          </div>
           <p className="text-xs text-neutral-500">Exact thermal APIs are restricted by the system sandbox. Please monitor physical device temperature manually.</p>
        </motion.div>
      </div>
    </div>
  );
}
