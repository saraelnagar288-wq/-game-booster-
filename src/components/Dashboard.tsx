import { useDeviceAnalyzer } from "../lib/useDeviceAnalyzer";
import { calculateGamingScore, getTier } from "../lib/optimizationEngine";
import { Zap, Battery, Thermometer, Cpu, Activity, Info, Bot } from "lucide-react";
import { Link } from "react-router-dom";
import { motion } from "motion/react";

export default function Dashboard() {
  const device = useDeviceAnalyzer();
  const score = calculateGamingScore(device);
  const tier = getTier(score);

  return (
    <div className="flex-1 overflow-y-auto pb-20 p-4 space-y-6">
      <header className="pt-4">
        <h1 className="text-2xl font-bold tracking-tight bg-gradient-to-r from-cyan-400 to-blue-500 bg-clip-text text-transparent">
          GAMEBOOST AI
        </h1>
        <p className="text-sm text-neutral-400">Optimize. Analyze. Play Better.</p>
      </header>

      <motion.div 
        initial={{ opacity: 0, y: 10 }}
        animate={{ opacity: 1, y: 0 }}
        className="bg-neutral-900 border border-neutral-800 rounded-2xl p-6 relative overflow-hidden"
      >
        <div className="absolute -right-10 -top-10 w-40 h-40 bg-cyan-500/10 blur-3xl rounded-full" />
        
        <div className="flex justify-between items-start mb-6 relative z-10">
          <div>
            <h2 className="text-sm font-semibold text-neutral-400 uppercase tracking-wider">Gaming Score</h2>
            <div className="text-4xl font-black mt-1 text-white">{score}<span className="text-xl text-neutral-500 font-medium">/100</span></div>
            <div className="text-cyan-400 font-medium text-sm mt-1">Tier: {tier}</div>
          </div>
          <div className="w-12 h-12 rounded-full bg-neutral-800 flex items-center justify-center text-cyan-400 border border-neutral-700">
            <Zap size={24} />
          </div>
        </div>

        <div className="space-y-3 relative z-10">
          <h3 className="text-sm font-semibold text-neutral-400 uppercase tracking-wider mb-2">Device (Detected)</h3>
          <div className="grid grid-cols-2 gap-3">
             <div className="bg-neutral-950 rounded-xl p-3 border border-neutral-800/50">
               <div className="text-xs text-neutral-500 mb-1">Model</div>
               <div className="text-sm font-medium truncate">{device.model}</div>
             </div>
             <div className="bg-neutral-950 rounded-xl p-3 border border-neutral-800/50">
               <div className="text-xs text-neutral-500 mb-1">OS</div>
               <div className="text-sm font-medium truncate">{device.os}</div>
             </div>
             <div className="bg-neutral-950 rounded-xl p-3 border border-neutral-800/50 col-span-2">
               <div className="text-xs text-neutral-500 mb-1">GPU</div>
               <div className="text-sm font-medium truncate">{device.gpu}</div>
             </div>
          </div>
        </div>
      </motion.div>

      <div className="grid grid-cols-2 gap-4">
         <Link to="/monitor" className="bg-neutral-900 border border-neutral-800 rounded-xl p-4 flex flex-col items-center justify-center gap-2 hover:bg-neutral-800 transition-colors">
            <Activity className="text-emerald-400" size={24} />
            <span className="text-sm font-medium">Performance Monitor</span>
         </Link>
         <Link to="/ai" className="bg-neutral-900 border border-neutral-800 rounded-xl p-4 flex flex-col items-center justify-center gap-2 hover:bg-neutral-800 transition-colors">
            <Bot className="text-purple-400" size={24} />
            <span className="text-sm font-medium">Ask AI Assistant</span>
         </Link>
      </div>

      <div className="bg-neutral-900 border border-neutral-800 rounded-2xl p-5 space-y-4">
        <h3 className="text-sm font-semibold text-neutral-400 uppercase tracking-wider flex items-center gap-2">
          Current Status <Info size={14} />
        </h3>
        <div className="flex flex-col gap-3">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3 text-neutral-300">
               <Battery size={18} className="text-neutral-500" />
               <span className="text-sm">Battery</span>
            </div>
            <div className="text-sm font-medium text-white flex items-center gap-2">
              {device.batteryLevel !== null ? `${device.batteryLevel}%` : "Unavailable"}
              {device.isCharging && <Zap size={14} className="text-yellow-400" />}
            </div>
          </div>
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3 text-neutral-300">
               <Thermometer size={18} className="text-neutral-500" />
               <span className="text-sm">Thermal Status</span>
            </div>
            <div className="text-sm font-medium text-emerald-400">
              Unavailable (Web API)
            </div>
          </div>
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-3 text-neutral-300">
               <Cpu size={18} className="text-neutral-500" />
               <span className="text-sm">CPU Cores</span>
            </div>
            <div className="text-sm font-medium text-white">
              {device.cores} Cores (Detected)
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
