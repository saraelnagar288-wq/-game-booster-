import { Link, useLocation } from "react-router-dom";
import { Home, Gamepad2, Cpu, Bot, Settings } from "lucide-react";
import { cn } from "../lib/utils";

export default function BottomNav() {
  const location = useLocation();

  const links = [
    { to: "/", icon: Home, label: "Dashboard" },
    { to: "/games", icon: Gamepad2, label: "Games" },
    { to: "/monitor", icon: Cpu, label: "Monitor" },
    { to: "/ai", icon: Bot, label: "AI Assist" },
    { to: "/settings", icon: Settings, label: "Settings" }
  ];

  return (
    <div className="fixed bottom-0 left-0 right-0 bg-neutral-900 border-t border-neutral-800 z-50 flex justify-center pb-safe">
      <div className="w-full max-w-md flex justify-around items-center h-16">
        {links.map((link) => {
          const Icon = link.icon;
          const isActive = location.pathname === link.to || (link.to !== "/" && location.pathname.startsWith(link.to));
          
          return (
            <Link
              key={link.to}
              to={link.to}
              className={cn(
                "flex flex-col items-center justify-center w-16 h-full gap-1 transition-colors",
                isActive ? "text-cyan-400" : "text-neutral-500 hover:text-neutral-300"
              )}
            >
              <Icon size={20} className={cn("transition-transform", isActive ? "scale-110" : "")} />
              <span className="text-[10px] font-medium">{link.label}</span>
            </Link>
          );
        })}
      </div>
    </div>
  );
}
