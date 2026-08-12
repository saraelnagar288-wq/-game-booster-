import { motion } from "motion/react";
import { BarChart3, Gauge, Timer, Activity } from "lucide-react";

const fps = [58, 60, 59, 57, 61, 60, 62, 60, 58, 61, 63, 60];

export default function FPSMonitor() {
  return (
    <div className="flex-1 overflow-y-auto pb-24 p-4 space-y-5">
      <motion.header initial={{ opacity: 0, y: 10 }} animate={{ opacity: 1, y: 0 }}><p className="text-xs text-cyan-400 uppercase tracking-[0.2em]">Telemetry</p><h1 className="text-2xl font-black">FPS Monitor</h1><p className="text-sm text-neutral-500 mt-1">Live-style monitoring UI; exact FPS requires native game telemetry.</p></motion.header>
      <div className="grid grid-cols-2 gap-3">
        {[["Current FPS","60",Gauge],["Average","59.9",BarChart3],["1% Low","57",Activity],["Frame Time","16.7 ms",Timer]].map(([label,value,Icon]) => <div key={String(label)} className="rounded-2xl border border-neutral-800 bg-neutral-900 p-4"><Icon size={18} className="text-cyan-400"/><p className="mt-3 text-xs text-neutral-500">{label}</p><p className="text-xl font-black mt-1">{value}</p></div>)}
      </div>
      <div className="rounded-2xl border border-neutral-800 bg-neutral-900 p-5">
        <h2 className="font-bold mb-4">Frame Rate</h2>
        <div className="flex items-end gap-1 h-36">{fps.map((v,i)=><motion.div key={i} initial={{height:0}} animate={{height:`${((v-50)/15)*100}%`}} transition={{delay:i*.03}} className="flex-1 rounded-t bg-cyan-400/70 min-w-0" />)}</div>
        <div className="flex justify-between mt-3 text-[10px] text-neutral-600"><span>50</span><span>60 FPS</span><span>65</span></div>
      </div>
    </div>
  );
}
