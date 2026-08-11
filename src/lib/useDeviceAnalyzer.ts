import { useEffect, useState } from "react";
import { DeviceStats } from "../types";

// Helper to extract GPU info safely
function getWebglGpu(): string {
  try {
    const canvas = document.createElement("canvas");
    const gl = canvas.getContext("webgl") || canvas.getContext("experimental-webgl");
    if (gl) {
      const debugInfo = (gl as WebGLRenderingContext).getExtension("WEBGL_debug_renderer_info");
      if (debugInfo) {
        return (gl as WebGLRenderingContext).getParameter(debugInfo.UNMASKED_RENDERER_WEBGL);
      }
    }
  } catch (e) {
    // Ignore
  }
  return "Unknown GPU (Restricted)";
}

export function useDeviceAnalyzer() {
  const [stats, setStats] = useState<DeviceStats>({
    model: "Detecting...",
    manufacturer: "Detecting...",
    os: "Detecting...",
    cores: navigator.hardwareConcurrency || 0,
    ram: (navigator as any).deviceMemory || 0,
    gpu: "Detecting...",
    batteryLevel: null,
    isCharging: null,
  });

  useEffect(() => {
    // Basic OS Detection from User Agent
    const ua = navigator.userAgent;
    let os = "Unknown OS";
    let manufacturer = "Unknown Manufacturer";
    let model = "Unknown Device";

    if (/android/i.test(ua)) {
      os = "Android";
      const match = ua.match(/Android\s([^\s;]+);/i);
      if (match) {
        os = `Android ${match[1]}`;
      }
      
      const buildMatch = ua.match(/Build\/([a-zA-Z0-9_-]+)/i);
      if (buildMatch) {
         model = `Android Device (${buildMatch[1]})`;
      } else {
         model = "Android Device";
      }
    } else if (/iPad|iPhone|iPod/.test(ua)) {
      os = "iOS";
      manufacturer = "Apple";
      model = /iPad/.test(ua) ? "iPad" : "iPhone";
    } else if (/Windows/.test(ua)) {
      os = "Windows";
      model = "PC";
    } else if (/Mac/.test(ua)) {
      os = "macOS";
      manufacturer = "Apple";
      model = "Mac";
    }

    const gpu = getWebglGpu();

    setStats((prev) => ({
      ...prev,
      os,
      manufacturer,
      model,
      gpu,
    }));

    // Battery API (Only works on some browsers like Chrome)
    if ("getBattery" in navigator) {
      (navigator as any).getBattery().then((battery: any) => {
        setStats((prev) => ({
          ...prev,
          batteryLevel: Math.round(battery.level * 100),
          isCharging: battery.charging,
        }));

        battery.addEventListener("levelchange", () => {
          setStats((prev) => ({ ...prev, batteryLevel: Math.round(battery.level * 100) }));
        });
        battery.addEventListener("chargingchange", () => {
          setStats((prev) => ({ ...prev, isCharging: battery.charging }));
        });
      }).catch(() => {
        // Battery API blocked
      });
    }

  }, []);

  return stats;
}
