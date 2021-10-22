package com.ramoncinp.relojcinbinariocompose.data.repository

import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData

interface DeviceCommunicator {

    fun setHost(host: String)

    suspend fun getData(): DeviceData?

    fun setData(deviceData: DeviceData)

    fun setBrightness(percentage: Int)

    fun syncHour(currentHour: Int)

    fun playSound()

    fun stopSound()

    fun reboot()
}
