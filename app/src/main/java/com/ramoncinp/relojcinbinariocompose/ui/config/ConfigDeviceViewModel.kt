package com.ramoncinp.relojcinbinariocompose.ui.config

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.relojcinbinariocompose.data.network.DeviceScanner
import com.ramoncinp.relojcinbinariocompose.data.network.TcpClient
import kotlinx.coroutines.launch

class ConfigDeviceViewModel constructor(
    private val deviceScanner: DeviceScanner = DeviceScanner()
) : ViewModel() {

    private lateinit var tcpClient: TcpClient

    private val _connectedDevices = MutableLiveData<List<String>>()
    val connectedDevices: LiveData<List<String>>
        get() = _connectedDevices

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _selectedDevice = MutableLiveData<String>()
    val selectedDevice: LiveData<String>
        get() = _selectedDevice

    init {
        scanForDevices()
    }

    private fun initDevice(ipAddress: String) {
        tcpClient = TcpClient(ipAddress)
        getDeviceData()
    }

    private fun getDeviceData() = viewModelScope.launch {
        try {
            val deviceData = tcpClient.sendMessage("{\"key\":\"get_data\"}")
            _selectedDevice.value = deviceData
        } catch (e: Exception) {
            Log.e("ConfigDeviceViewModel", e.toString())
        }
    }

    private fun scanForDevices() {
        viewModelScope.launch {
            _isLoading.value = true
            val devices = deviceScanner.scanForDevices()
            evaluateScannedDevices(devices)
            _connectedDevices.value = devices
            _isLoading.value = false
        }
    }

    private fun evaluateScannedDevices(devices: List<String>) {
        if (devices.isNotEmpty()) {
            val chosenDevice = devices[0]
            initDevice(chosenDevice)
        }
    }
}
