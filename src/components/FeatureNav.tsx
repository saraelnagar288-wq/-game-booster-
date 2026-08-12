import { Link, useLocation } from "react-router-dom";
import { Activity, BarChart3, Battery, Bot, Gamepad2, Home, Settings, Thermometer, Zap } from "lucide-react";
import { cn } from "../lib/utils";

const tabs = [
  { to: "/", label: "Dashboard", icon: Home },
  { to: "/performance", label: "Performance", icon: Zap },
  { to: "/games", label: "Games", icon: Gamepad2 },
  { to: "/fps", label: "FPS Monitor", icon: BarChart3 },
  { to: "/ai", label: "AI Assistant", icon: Bot },
  { to: "/battery", label: "Battery", icon: Battery },
  { to: "/thermal", label: "Thermal", icon: Thermometer },
  { to: "/settings", label: "Settings", icon: Settings },
];

export default function FeatureNav() {
  const location = useLocation();
  return (
    <div className="sticky top-0 z-40 border-b border-neutral-800/80 bg-neutral-950/90 backdrop-blur-xl">
      <div className="flex gap-2 overflow-x-auto px-3 py-2 no-scrollbar">
        {tabs.map(({ to, label, icon: Icon }) => {
          const active = to === "/" ? location.pathname === "/" : location.pathname.startsWith(to);
          return (
            <Link
              key={to}
              to={to}
              className={cn(
                "shrink-0 flex items-center gap-1.5 rounded-full px-3 py-2 text-[11px] font-semibold transition-all duration-300",
                active
                  ? "bg-cyan-500/15 text-cyan-300 ring-1 ring-cyan-400/30"
                  : "bg-neutral-900 text-neutral-500 hover:text-neutral-200"
              )}
            >
              <Icon size={14} />
              {label}
            </Link>
          );
        })}
      </div>
    </div>
  );
}
