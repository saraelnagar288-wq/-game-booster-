package com.gameboost.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.gameboost.ai.ui.screens.DashboardScreen
import com.gameboost.ai.ui.screens.PerformanceScreen
import com.gameboost.ai.ui.theme.GameBoostAITheme
import com.gameboost.ai.ui.theme.Neutral900
import com.gameboost.ai.ui.theme.Neutral950
import com.gameboost.ai.ui.theme.Cyan400
import com.gameboost.ai.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GameBoostAITheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Neutral950) {
                    val navController = rememberNavController()
                    Scaffold(
                        bottomBar = {
                            NavigationBar(containerColor = Neutral900, contentColor = Neutral950) {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentRoute = navBackStackEntry?.destination?.route

                                NavigationBarItem(
                                    icon = { Text("⌂", color = if (currentRoute == "dashboard") Cyan400 else Color.Gray) },
                                    label = { Text("Dashboard", color = if (currentRoute == "dashboard") Cyan400 else Color.Gray) },
                                    selected = currentRoute == "dashboard",
                                    onClick = { navController.navigate("dashboard") },
                                    colors = NavigationBarItemDefaults.colors(indicatorColor = Neutral950)
                                )
                                NavigationBarItem(
                                    icon = { Text("⚡", color = if (currentRoute == "performance") Cyan400 else Color.Gray) },
                                    label = { Text("Performance", color = if (currentRoute == "performance") Cyan400 else Color.Gray) },
                                    selected = currentRoute == "performance",
                                    onClick = { navController.navigate("performance") },
                                    colors = NavigationBarItemDefaults.colors(indicatorColor = Neutral950)
                                )
                                NavigationBarItem(
                                    icon = { Text("◉", color = if (currentRoute == "monitor") Cyan400 else Color.Gray) },
                                    label = { Text("Monitor", color = if (currentRoute == "monitor") Cyan400 else Color.Gray) },
                                    selected = currentRoute == "monitor",
                                    onClick = { navController.navigate("monitor") },
                                    colors = NavigationBarItemDefaults.colors(indicatorColor = Neutral950)
                                )
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "dashboard",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable("dashboard") { DashboardScreen(viewModel) }
                            composable("performance") { PerformanceScreen() }
                            composable("monitor") {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text("Monitor (WIP)", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
