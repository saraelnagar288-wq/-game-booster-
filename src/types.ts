export type DeviceStats = {
  model: string;
  manufacturer: string;
  os: string;
  cores: number;
  ram: number;
  gpu: string;
  batteryLevel: number | null;
  isCharging: boolean | null;
};

export type Game = {
  id: string;
  name: string;
  package: string;
  iconUrl: string;
  engine: string;
  cpuIntensity: 'Low' | 'Medium' | 'High' | 'Extreme';
  gpuIntensity: 'Low' | 'Medium' | 'High' | 'Extreme';
};

export type OptimizationMode = 'MAX FPS' | 'BALANCED' | 'MAX QUALITY';

export type GameSettings = {
  graphics: string;
  fps: string;
  shadows: string;
  antiAliasing: string;
  effects: string;
  postProcessing: string;
  resolution: string;
};

export type FPSReport = {
  estimatedFpsRange: string;
  average: number;
  low1Percent: number;
  stability: string;
  confidence: 'Low' | 'Medium' | 'High';
  thermalRisk: 'Low' | 'Moderate' | 'High' | 'Critical';
};
