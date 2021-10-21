package com.ramoncinp.relojcinbinariocompose.ui.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.relojcinbinariocompose.data.network.DeviceScanner
import kotlinx.coroutines.launch

class ConfigDeviceViewModel constructor(
    private val deviceScanner: DeviceScanner = DeviceScanner()
) : ViewModel() {

    private val _connectedDevices = deviceScanner.connectedDevices
    val connectedDevices: LiveData<List<String>>
        get() = _connectedDevices

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        scanForDevices()
    }

    private fun scanForDevices() {
        viewModelScope.launch {
            _isLoading.value = true
            deviceScanner.scanForDevices()
            _isLoading.value = false
        }
    }
}
