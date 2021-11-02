package com.ramoncinp.relojcinbinariocompose.data.repository

import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData

interface DeviceCommunicator {

    fun setHost(host: String)

    suspend fun getData(): DeviceData?

    suspend fun setData(deviceData: DeviceData)

    suspend fun setBrightness(pwmValue: Int)

    fun syncHour(currentHour: Int)

    suspend fun playSound()

    suspend fun stopSound()

    suspend fun reboot()
}
