package com.gameboost.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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
import com.gameboost.ai.ui.theme.Neutral500
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
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NavItem("⌂", "Home", "dashboard", route, navController)
                                NavItem("⚡", "Boost", "performance", route, navController)
                                NavItem("🎮", "Games", "games", route, navController)
                                NavItem("📊", "FPS", "fps", route, navController)
                                NavItem("🧠", "AI", "ai", route, navController)
                                NavItem("🔋", "Battery", "battery", route, navController)
                                NavItem("🌡", "Thermal", "thermal", route, navController)
                                NavItem("⚙", "Settings", "settings", route, navController)
                            }
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("dashboard") {
                            DashboardScreen(viewModel) { destination ->
                                navController.navigate(destination) { launchSingleTop = true }
                            }
                        }
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
    navController: NavHostController
) {
    val selected = current == destination
    Surface(
        modifier = Modifier
            .padding(horizontal = 1.dp, vertical = 4.dp)
            .clickable {
                navController.navigate(destination) {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
        color = if (selected) Cyan400.copy(alpha = 0.14f) else Neutral900,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, color = if (selected) Cyan400 else Neutral500)
            Text(label, color = if (selected) Cyan400 else Neutral500, fontSize = 7.sp)
        }
    }
}
