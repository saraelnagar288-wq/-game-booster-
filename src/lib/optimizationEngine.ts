import { DeviceStats, Game, OptimizationMode, GameSettings, FPSReport } from "../types";

export function calculateGamingScore(device: DeviceStats): number {
  let score = 30; // base score

  // CPU Cores mapping (approx)
  if (device.cores >= 8) score += 20;
  else if (device.cores >= 6) score += 15;
  else if (device.cores >= 4) score += 10;
  else score += 5;

  // RAM Mapping
  if (device.ram >= 8) score += 20;
  else if (device.ram >= 6) score += 15;
  else if (device.ram >= 4) score += 10;
  else score += 5;

  // GPU string matching heuristic
  const gpu = device.gpu.toLowerCase();
  if (gpu.includes("adreno 7") || gpu.includes("mali-g715") || gpu.includes("apple a17") || gpu.includes("rtx")) {
    score += 30;
  } else if (gpu.includes("adreno 6") || gpu.includes("mali-g710") || gpu.includes("apple a16")) {
    score += 20;
  } else if (gpu.includes("mali-g5") || gpu.includes("adreno 5") || gpu.includes("powervr")) {
    score += 10;
  } else {
    score += 5;
  }

  return Math.min(100, score);
}

export function getTier(score: number): string {
  if (score <= 20) return "Very Low";
  if (score <= 40) return "Low";
  if (score <= 60) return "Entry";
  if (score <= 75) return "Mid-range";
  if (score <= 90) return "High-end";
  return "Extreme";
}

export function generateSettings(score: number, game: Game, mode: OptimizationMode): GameSettings {
  // A simplified rule engine for graphics settings
  let graphics = "Low";
  let fps = "30 FPS";
  let shadows = "Off";
  let antiAliasing = "Off";
  let effects = "Low";
  let postProcessing = "Low";
  let resolution = "720p";

  if (score >= 75) {
    graphics = "Ultra";
    fps = mode === "MAX FPS" ? "90/120 FPS (If Supported)" : "60 FPS";
    shadows = mode === "MAX FPS" ? "Medium" : "High";
    antiAliasing = "On (TAA)";
    effects = "High";
    postProcessing = "High";
    resolution = "1080p";
  } else if (score >= 50) {
    graphics = "Medium";
    fps = mode === "MAX FPS" ? "60 FPS" : "45 FPS";
    shadows = mode === "MAX FPS" ? "Off" : "Medium";
    antiAliasing = mode === "MAX QUALITY" ? "On (FXAA)" : "Off";
    effects = "Medium";
    postProcessing = "Medium";
    resolution = "HD+";
  } else {
    graphics = mode === "MAX QUALITY" ? "Medium" : "Smooth/Lowest";
    fps = mode === "MAX FPS" ? "45/60 FPS" : "30 FPS";
    shadows = "Off";
    antiAliasing = "Off";
    effects = "Low";
    postProcessing = "Low";
    resolution = "Standard";
  }

  return {
    graphics,
    fps,
    shadows,
    antiAliasing,
    effects,
    postProcessing,
    resolution
  };
}

export function estimateFps(score: number, game: Game, mode: OptimizationMode): FPSReport {
  let baseFps = 30;
  
  if (score > 80) baseFps = 60;
  else if (score > 55) baseFps = 45;
  
  if (game.cpuIntensity === 'Extreme') baseFps -= 10;
  if (game.gpuIntensity === 'Extreme') baseFps -= 10;

  if (mode === 'MAX FPS') baseFps += 15;
  if (mode === 'MAX QUALITY') baseFps -= 10;

  baseFps = Math.max(15, Math.min(120, baseFps));

  return {
    estimatedFpsRange: `${Math.max(15, baseFps - 10)}-${Math.min(120, baseFps + 5)} FPS`,
    average: baseFps,
    low1Percent: Math.max(10, baseFps - 15),
    stability: baseFps >= 50 ? "Good" : baseFps >= 30 ? "Acceptable" : "Poor",
    confidence: score === 30 ? "Low" : "Medium", // If we couldn't detect much
    thermalRisk: baseFps >= 60 ? "Moderate" : "Low"
  };
}
