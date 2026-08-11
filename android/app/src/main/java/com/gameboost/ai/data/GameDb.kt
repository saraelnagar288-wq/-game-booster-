package com.gameboost.ai.data

import com.gameboost.ai.data.models.Game

object GameDb {
    val games = listOf(
        Game("pubg", "PUBG Mobile", "com.tencent.ig", "Unreal Engine 4", "High", "High"),
        Game("freefire", "Free Fire", "com.dts.freefireth", "Unity", "Medium", "Medium"),
        Game("codm", "Call of Duty Mobile", "com.activision.callofduty.shooter", "Unity", "High", "High"),
        Game("genshin", "Genshin Impact", "com.miHoYo.GenshinImpact", "Unity", "Extreme", "Extreme"),
        Game("roblox", "Roblox", "com.roblox.client", "Roblox Studio", "Medium", "Low")
    )
}
