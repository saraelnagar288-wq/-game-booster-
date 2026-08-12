import { motion } from "motion/react";
import { Battery, Thermometer, Zap, ShieldCheck } from "lucide-react";

type Props = { kind: "battery" | "thermal" };

export default function SystemPanel({ kind }: Props) {
  const battery = kind === "battery";
  return (
    <div className="flex-1 overflow-y-auto pb-24 p-4 space-y-5">
      <motion.header initial={{opacity:0,y:10}} animate={{opacity:1,y:0}}>
        <p className="text-xs text-cyan-400 uppercase tracking-[0.2em]">System</p>
        <h1 className="text-2xl font-black">{battery ? "Battery" : "Thermal"}</h1>
      </motion.header>
      <motion.div initial={{opacity:0,scale:.98}} animate={{opacity:1,scale:1}} className="rounded-3xl border border-neutral-800 bg-neutral-900 p-6 text-center">
        {battery ? <Battery size={48} className="mx-auto text-emerald-400"/> : <Thermometer size={48} className="mx-auto text-amber-400"/>}
        <div className="text-4xl font-black mt-4">{battery ? "System API" : "Unavailable"}</div>
        <p className="text-sm text-neutral-500 mt-2">{battery ? "Battery information is available where the browser/device API permits it." : "Exact thermal sensor data is restricted in the web runtime."}</p>
      </motion.div>
      <div className="grid gap-3">
        {(battery ? ["Charging status", "Battery level", "Power saving"] : ["Thermal sensor", "CPU temperature", "GPU temperature"]).map((label) => <div key={label} className="rounded-2xl border border-neutral-800 bg-neutral-900 p-4 flex items-center justify-between"><div className="flex items-center gap-3"><ShieldCheck size={18} className="text-cyan-400"/><span className="text-sm">{label}</span></div><span className="text-xs text-neutral-500">Unavailable</span></div>)}
      </div>
      <div className="rounded-2xl border border-neutral-800 bg-neutral-900 p-4 flex gap-3"><Zap size={18} className="text-yellow-400 shrink-0"/><p className="text-xs leading-relaxed text-neutral-500">GameBoost AI will never invent sensor readings. Native Android telemetry can replace these placeholders when connected to the Android bridge.</p></div>
    </div>
  );
}
