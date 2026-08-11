import { Game } from "../types";

export const GAMES_DB: Game[] = [
  {
    id: "pubg",
    name: "PUBG Mobile",
    package: "com.tencent.ig",
    iconUrl: "https://play-lh.googleusercontent.com/JRd05pyBH41qjgsJuWduRJpDeZG0Htp0yOMzmiVGqNx58Pxd_8E89JX4Z5KyCEb-9A=w240-h480-rw",
    engine: "Unreal Engine 4",
    cpuIntensity: "High",
    gpuIntensity: "High"
  },
  {
    id: "freefire",
    name: "Free Fire",
    package: "com.dts.freefireth",
    iconUrl: "https://play-lh.googleusercontent.com/902-1Wv3UuW42nEsq70uW-2rR7y7g4D_v46Hn2D0rCg90A4oEwQd4sE7_92c_A-R490=w240-h480-rw",
    engine: "Unity",
    cpuIntensity: "Medium",
    gpuIntensity: "Medium"
  },
  {
    id: "codm",
    name: "Call of Duty Mobile",
    package: "com.activision.callofduty.shooter",
    iconUrl: "https://play-lh.googleusercontent.com/9C0D30x7uGz-rPj0B1Jz1QdE4yE3b1C5QvG9uP2_y5n0f5i1vE5_9003g58X2H2L7Q=w240-h480-rw",
    engine: "Unity",
    cpuIntensity: "High",
    gpuIntensity: "High"
  },
  {
    id: "genshin",
    name: "Genshin Impact",
    package: "com.miHoYo.GenshinImpact",
    iconUrl: "https://play-lh.googleusercontent.com/gK11b30D_GjV4jY6yA8Z9T7h_A2F_J_3X5Wq1uT6U_A_0r-L-L_U8e37q0A4Q4G_oA=w240-h480-rw",
    engine: "Unity",
    cpuIntensity: "Extreme",
    gpuIntensity: "Extreme"
  },
  {
    id: "roblox",
    name: "Roblox",
    package: "com.roblox.client",
    iconUrl: "https://play-lh.googleusercontent.com/WNWZaxi9RdJKe2GQM3vqXIAkk69mnIl4Cc8EyZcir2SKlVOxeUv9tZGfNTmNaLC717Ht=w240-h480-rw",
    engine: "Roblox Studio",
    cpuIntensity: "Medium",
    gpuIntensity: "Low"
  }
];
