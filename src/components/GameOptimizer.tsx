import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { GAMES_DB } from "../lib/gamesDb";
import { useDeviceAnalyzer } from "../lib/useDeviceAnalyzer";
import { calculateGamingScore, estimateFps, generateSettings } from "../lib/optimizationEngine";
import { OptimizationMode } from "../types";
import { ArrowLeft, Play, Settings2, Activity, Info, ShieldAlert } from "lucide-react";
import { motion } from "motion/react";
import { cn } from "../lib/utils";

export default function GameOptimizer() {
  const { id } = useParams();
  const navigate = useNavigate();
  const game = GAMES_DB.find(g => g.id === id);
  const device = useDeviceAnalyzer();
  const score = calculateGamingScore(device);
  
  const [mode, setMode] = useState<OptimizationMode>("BALANCED");
  
  if (!game) return <div className="p-4 text-center mt-20">Game not found</div>;

  const fpsEstimate = estimateFps(score, game, mode);
  const settings = generateSettings(score, game, mode);

  return (
    <div className="flex-1 flex flex-col h-full bg-neutral-950 overflow-y-auto pb-24">
      {/* Header */}
      <div className="relative h-48 bg-neutral-900 border-b border-neutral-800 flex flex-col justify-end p-4">
        <div className="absolute inset-0 opacity-20 bg-gradient-to-t from-neutral-950 to-cyan-900/20" />
        <button 
          onClick={() => navigate(-1)}
          className="absolute top-4 left-4 w-10 h-10 bg-black/50 backdrop-blur-md rounded-full flex items-center justify-center text-white z-10"
        >
          <ArrowLeft size={20} />
        </button>
        
        <div className="relative z-10 flex gap-4 items-end">
          <img src={game.iconUrl} className="w-20 h-20 rounded-xl shadow-lg border border-neutral-800 bg-neutral-800" alt="" />
          <div className="pb-1">
            <h1 className="text-xl font-bold text-white leading-tight">{game.name}</h1>
            <p className="text-sm text-neutral-400">{game.engine}</p>
          </div>
        </div>
      </div>

      <div className="p-4 space-y-6">
        {/* Mode Selector */}
        <div>
          <h3 className="text-sm font-semibold text-neutral-400 uppercase tracking-wider mb-3">Optimization Mode</h3>
          <div className="grid grid-cols-3 gap-2">
            {(["MAX FPS", "BALANCED", "MAX QUALITY"] as OptimizationMode[]).map((m) => (
              <button
                key={m}
                onClick={() => setMode(m)}
                className={cn(
                  "p-3 rounded-xl border text-xs font-bold transition-all flex flex-col items-center gap-1",
                  mode === m 
                    ? "bg-cyan-500/10 border-cyan-500/50 text-cyan-400"
                    : "bg-neutral-900 border-neutral-800 text-neutral-500 hover:text-neutral-300"
                )}
              >
                {m}
              </button>
            ))}
          </div>
        </div>

        {/* FPS Estimator */}
        <motion.div 
          key={mode + "fps"}
          initial={{ opacity: 0, y: 5 }}
          animate={{ opacity: 1, y: 0 }}
          className="bg-neutral-900 rounded-2xl border border-neutral-800 p-5 space-y-4"
        >
          <div className="flex justify-between items-start">
             <div>
               <h3 className="text-sm font-semibold text-neutral-400 uppercase tracking-wider flex items-center gap-2">
                 FPS Estimator <Info size={14} />
               </h3>
               <div className="text-3xl font-black text-white mt-1">{fpsEstimate.estimatedFpsRange}</div>
               <div className="text-xs text-neutral-500 mt-1">Average: {fpsEstimate.average} FPS | 1% Low: {fpsEstimate.low1Percent} FPS</div>
             </div>
             <div className="bg-neutral-950 p-2 rounded-lg border border-neutral-800 flex flex-col items-center">
               <span className="text-[10px] text-neutral-500 uppercase font-bold">Confidence</span>
               <span className={cn(
                 "text-xs font-bold",
                 fpsEstimate.confidence === 'High' ? "text-emerald-400" :
                 fpsEstimate.confidence === 'Medium' ? "text-yellow-400" : "text-red-400"
               )}>{fpsEstimate.confidence}</span>
             </div>
          </div>
          
          <div className="grid grid-cols-2 gap-3 pt-3 border-t border-neutral-800">
            <div>
              <div className="text-xs text-neutral-500 mb-1">Stability</div>
              <div className="text-sm font-medium text-neutral-200">{fpsEstimate.stability}</div>
            </div>
            <div>
              <div className="text-xs text-neutral-500 mb-1">Thermal Risk</div>
              <div className="text-sm font-medium text-neutral-200">{fpsEstimate.thermalRisk}</div>
            </div>
          </div>
        </motion.div>

        {/* Recommended Settings */}
        <motion.div
           key={mode + "settings"}
           initial={{ opacity: 0, y: 5 }}
           animate={{ opacity: 1, y: 0 }}
           className="bg-neutral-900 rounded-2xl border border-neutral-800 p-5 space-y-4"
        >
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-semibold text-neutral-400 uppercase tracking-wider flex items-center gap-2">
              Recommended In-Game Settings
            </h3>
            <Settings2 size={16} className="text-neutral-500" />
          </div>
          
          <div className="bg-amber-500/10 border border-amber-500/20 rounded-lg p-3 flex gap-3 text-amber-500/90 items-start">
            <ShieldAlert size={16} className="shrink-0 mt-0.5" />
            <p className="text-xs leading-relaxed">
              Android restricts 3rd-party apps from modifying in-game graphics. Apply these settings manually in the game's menu for best results.
            </p>
          </div>

          <div className="grid grid-cols-2 gap-x-4 gap-y-3">
             {Object.entries(settings).map(([key, val]) => (
               <div key={key} className="flex flex-col">
                 <span className="text-xs text-neutral-500 capitalize">{key.replace(/([A-Z])/g, ' $1').trim()}</span>
                 <span className="text-sm font-medium text-neutral-200">{val}</span>
               </div>
             ))}
          </div>
        </motion.div>

        <button className="w-full bg-cyan-500 hover:bg-cyan-400 text-black font-bold py-4 rounded-xl flex items-center justify-center gap-2 transition-colors">
          <Play size={18} className="fill-black" />
          Launch Game
        </button>
      </div>
    </div>
  );
}
