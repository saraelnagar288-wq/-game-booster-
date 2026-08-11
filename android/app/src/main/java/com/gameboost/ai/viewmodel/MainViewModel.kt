package com.gameboost.ai.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gameboost.ai.hardware.DeviceAnalyzer
import com.gameboost.ai.hardware.DeviceStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _deviceStats = MutableStateFlow<DeviceStats?>(null)
    val deviceStats: StateFlow<DeviceStats?> = _deviceStats.asStateFlow()

    private val _ramUsage = MutableStateFlow(65f)
    val ramUsage: StateFlow<Float> = _ramUsage.asStateFlow()

    init {
        val analyzer = DeviceAnalyzer(application)
        _deviceStats.value = analyzer.analyze()
        
        // Simulated RAM usage fluctuations for the monitor
        viewModelScope.launch {
            while (true) {
                delay(2000)
                _ramUsage.value = (60f + Math.random() * 15f).toFloat()
            }
        }
    }

    fun refreshStats() {
        val analyzer = DeviceAnalyzer(getApplication())
        _deviceStats.value = analyzer.analyze()
    }
}
