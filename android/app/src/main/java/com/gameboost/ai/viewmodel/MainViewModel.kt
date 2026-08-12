package com.gameboost.ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gameboost.ai.hardware.DeviceAnalyzer
import com.gameboost.ai.hardware.DeviceStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val analyzer = DeviceAnalyzer(application)
    private val _deviceStats = MutableStateFlow<DeviceStats?>(null)
    val deviceStats: StateFlow<DeviceStats?> = _deviceStats.asStateFlow()

    private val _ramUsage = MutableStateFlow(0f)
    val ramUsage: StateFlow<Float> = _ramUsage.asStateFlow()

    init {
        refreshStats()
        viewModelScope.launch {
            while (true) {
                delay(2000)
                refreshStats()
            }
        }
    }

    fun refreshStats() {
        val stats = analyzer.analyze()
        _deviceStats.value = stats
        _ramUsage.value = stats.ramUsagePercent
    }
}
