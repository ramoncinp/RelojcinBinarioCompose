package com.ramoncinp.relojcinbinariocompose.ui.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData
import com.ramoncinp.relojcinbinariocompose.data.network.DeviceScanner
import com.ramoncinp.relojcinbinariocompose.data.repository.DeviceCommunicator
import com.ramoncinp.relojcinbinariocompose.domain.OK_RESULT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConfigDeviceViewModel @Inject constructor(
    private val deviceScanner: DeviceScanner,
    private val deviceCommunicator: DeviceCommunicator
) : ViewModel() {

    private val _connectedDevices = MutableLiveData<List<String>>()
    val connectedDevices: LiveData<List<String>>
        get() = _connectedDevices

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _selectedDevice = MutableLiveData<DeviceData>()
    val selectedDevice: LiveData<DeviceData>
        get() = _selectedDevice

    init {
        scanForDevices()
    }

    private fun initDevice(ipAddress: String) {
        deviceCommunicator.setHost(ipAddress)
        getDeviceData()
    }

    private fun getDeviceData() = viewModelScope.launch {
        try {
            val deviceData = deviceCommunicator.getData()
            deviceData?.let { data ->
                _selectedDevice.value = data
            }
        } catch (e: Exception) {
            Timber.e(e.toString())
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
