package com.ramoncinp.relojcinbinariocompose.ui.config

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramoncinp.relojcinbinariocompose.data.mappers.percentageToPwmValue
import com.ramoncinp.relojcinbinariocompose.data.mappers.pwmValueToPercentage
import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData
import com.ramoncinp.relojcinbinariocompose.data.network.DeviceScanner
import com.ramoncinp.relojcinbinariocompose.data.repository.DeviceCommunicator
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

    private val _brightnessPercentage = MutableLiveData<Float>()
    val brightnessPercentage: LiveData<Float>
        get() = _brightnessPercentage

    private val _message = MutableLiveData("")
    val message: LiveData<String>
        get() = _message


    init {
        scanForDevices()
    }

    private fun initDevice(ipAddress: String) {
        deviceCommunicator.setHost(ipAddress)
        getDeviceData()
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

    private fun getDeviceData() = viewModelScope.launch {
        try {
            val deviceData = deviceCommunicator.getData()
            deviceData?.let { data ->
                _selectedDevice.value = data
                _brightnessPercentage.value = pwmValueToPercentage(deviceData.pwmValue) / 100f
            }
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
    }

    fun setDeviceData() = viewModelScope.launch {
        _isLoading.value = true
        try {
            _selectedDevice.value?.let { data ->
                deviceCommunicator.setData(data)
            }

            _message.value = "Data successfully set"
        } catch (e: Exception) {
            Timber.e(e.toString())
        }
        _isLoading.value = false
    }

    fun editDevice(deviceData: DeviceData) {
        _selectedDevice.value = deviceData
    }

    fun addToTimeZone() {
        editTimeZone(1)
    }

    fun subtractToTimeZone() {
        editTimeZone(-1)
    }

    /**
     * Max time zone is +14
     * Minimum time zone is -12
     */
    private fun editTimeZone(value: Int) {
        val currentTimeZone = _selectedDevice.value?.hourZone
        currentTimeZone?.let { current ->
            var result = current + value
            if (result == 15 && value == 1) result = -12
            if (result == -13 && value == -1) result = 14

            _selectedDevice.value?.copy(hourZone = result)
                ?.let { newDevice -> editDevice(newDevice) }
        }
    }

    fun setBrightnessValue(newValue: Float) {
        Timber.d("The new brightness value is $newValue")
        _brightnessPercentage.value = newValue
    }

    fun sendNewBrightnessValue() = viewModelScope.launch {
        val percentageValue = _brightnessPercentage.value?.times(100)?.toInt()
        percentageValue?.let {
            val pwmValue = percentageToPwmValue(it)
            _selectedDevice.value?.pwmValue = pwmValue
            deviceCommunicator.setBrightness(pwmValue)
        }
    }

    fun onMessageShowed() {
        _message.value = ""
    }
}
