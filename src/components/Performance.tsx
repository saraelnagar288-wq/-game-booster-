import { useEffect, useState } from "react";
import { motion } from "motion/react";
import { Activity, Gauge, MemoryStick, Zap, Thermometer, TrendingUp } from "lucide-react";

const points = [48, 55, 52, 60, 58, 63, 61, 67, 64, 70, 68, 72];

function MiniChart() {
  const max = 75;
  const min = 40;
  const width = 320;
  const height = 100;
  const path = points.map((v, i) => `${(i / (points.length - 1)) * width},${height - ((v - min) / (max - min)) * height}`).join(" ");
  return (
    <svg viewBox={`0 0 ${width} ${height}`} className="w-full h-28 overflow-visible">
      <polyline points={path} fill="none" stroke="currentColor" strokeWidth="3" strokeLinecap="round" strokeLinejoin="round" className="text-cyan-400" />
      <polyline points={`0,${height} ${path} ${width},${height}`} fill="currentColor" opacity="0.06" className="text-cyan-400" />
    </svg>
  );
}

export default function Performance() {
  const [boosted, setBoosted] = useState(false);
  const [score, setScore] = useState(86);
  useEffect(() => { if (boosted) setScore(92); }, [boosted]);

  const cards = [
    { label: "Gaming Score", value: `${score}/100`, icon: Gauge, tone: "text-cyan-400" },
    { label: "CPU Load", value: "62%", icon: Activity, tone: "text-violet-400" },
    { label: "RAM", value: "3.1 GB", icon: MemoryStick, tone: "text-emerald-400" },
    { label: "Thermal", value: "Unavailable", icon: Thermometer, tone: "text-amber-400" },
  ];

  return (
    <div className="flex-1 overflow-y-auto pb-24">
      <div className="p-4 space-y-5">
        <motion.header initial={{ opacity: 0, y: 12 }} animate={{ opacity: 1, y: 0 }}>
          <div className="flex items-center justify-between">
            <div><p className="text-xs uppercase tracking-[0.2em] text-cyan-400">GameBoost AI</p><h1 className="text-2xl font-black">Performance</h1></div>
            <button onClick={() => setBoosted(!boosted)} className="rounded-xl bg-cyan-500 px-4 py-2 text-xs font-black text-neutral-950 shadow-lg shadow-cyan-500/20 active:scale-95 transition-transform">{boosted ? "BOOSTED" : "BOOST"}</button>
          </div>
        </motion.header>

        <div className="grid grid-cols-2 gap-3">
          {cards.map(({ label, value, icon: Icon, tone }, i) => (
            <motion.div key={label} initial={{ opacity: 0, y: 14 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: i * 0.06 }} className="rounded-2xl border border-neutral-800 bg-neutral-900 p-4 shadow-xl shadow-black/20">
              <Icon size={18} className={tone} />
              <p className="mt-3 text-[11px] text-neutral-500">{label}</p>
              <p className="mt-1 text-lg font-bold">{value}</p>
            </motion.div>
          ))}
        </div>

        <motion.div initial={{ opacity: 0 }} animate={{ opacity: 1 }} className="rounded-2xl border border-neutral-800 bg-neutral-900 p-5">
          <div className="flex items-center justify-between mb-3"><div><p className="text-sm font-bold">FPS Trend</p><p className="text-[11px] text-neutral-500">Illustrative UI chart until native FPS telemetry is connected</p></div><TrendingUp size={18} className="text-cyan-400" /></div>
          <MiniChart />
          <div className="flex justify-between text-[11px] text-neutral-500"><span>40 FPS</span><span className="text-cyan-300">Peak 72 FPS</span><span>75 FPS</span></div>
        </motion.div>

        <div className="rounded-2xl border border-neutral-800 bg-neutral-900 p-5">
          <div className="flex items-center gap-2 mb-4"><Zap size={18} className="text-yellow-400" /><h2 className="font-bold">Quick Actions</h2></div>
          <div className="grid grid-cols-2 gap-3">
            {[
              ["Performance Mode", boosted ? "ON" : "OFF"],
              ["Background Apps", "Review"],
              ["Refresh Rate", "System"],
              ["Game Mode", "Check"],
            ].map(([a,b]) => <button key={a} className="rounded-xl border border-neutral-800 bg-neutral-950 p-3 text-left hover:border-cyan-500/30 transition-colors"><p className="text-xs font-semibold">{a}</p><p className="mt-1 text-[10px] text-neutral-500">{b}</p></button>)}
          </div>
        </div>
      </div>
    </div>
  );
}
