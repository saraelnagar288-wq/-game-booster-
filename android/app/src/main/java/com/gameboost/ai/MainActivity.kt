package com.gameboost.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gameboost.ai.ui.screens.AiAssistantScreen
import com.gameboost.ai.ui.screens.BatteryScreen
import com.gameboost.ai.ui.screens.DashboardScreen
import com.gameboost.ai.ui.screens.FpsMonitorScreen
import com.gameboost.ai.ui.screens.GamesScreen
import com.gameboost.ai.ui.screens.PerformanceScreen
import com.gameboost.ai.ui.screens.SettingsScreen
import com.gameboost.ai.ui.screens.ThermalScreen
import com.gameboost.ai.ui.theme.Cyan400
import com.gameboost.ai.ui.theme.GameBoostAITheme
import com.gameboost.ai.ui.theme.Neutral900
import com.gameboost.ai.ui.theme.Neutral950
import com.gameboost.ai.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameBoostAITheme {
                val navController = rememberNavController()
                val entry by navController.currentBackStackEntryAsState()
                val route = entry?.destination?.route ?: "dashboard"

                Scaffold(
                    containerColor = Neutral950,
                    bottomBar = {
                        NavigationBar(containerColor = Neutral900) {
                            NavItem("⌂", "Dashboard", "dashboard", route, navController)
                            NavItem("⚡", "Performance", "performance", route, navController)
                            NavItem("🎮", "Games", "games", route, navController)
                            NavItem("📊", "FPS", "fps", route, navController)
                            NavItem("🧠", "AI", "ai", route, navController)
                            NavItem("🔋", "Battery", "battery", route, navController)
                            NavItem("🌡️", "Thermal", "thermal", route, navController)
                            NavItem("⚙️", "Settings", "settings", route, navController)
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("dashboard") { DashboardScreen(viewModel) }
                        composable("performance") { PerformanceScreen() }
                        composable("games") { GamesScreen() }
                        composable("fps") { FpsMonitorScreen() }
                        composable("ai") { AiAssistantScreen() }
                        composable("battery") { BatteryScreen(this@MainActivity) }
                        composable("thermal") { ThermalScreen(this@MainActivity) }
                        composable("settings") { SettingsScreen(this@MainActivity) }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavItem(
    icon: String,
    label: String,
    destination: String,
    current: String,
    navController: androidx.navigation.NavHostController
) {
    NavigationBarItem(
        selected = current == destination,
        onClick = {
            navController.navigate(destination) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        icon = { Text(icon) },
        label = { Text(label, maxLines = 1) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Cyan400,
            selectedTextColor = Cyan400,
            indicatorColor = Neutral950,
            unselectedIconColor = androidx.compose.ui.graphics.Color.Gray,
            unselectedTextColor = androidx.compose.ui.graphics.Color.Gray
        )
    )
}
