package com.ramoncinp.relojcinbinariocompose.data.repository

import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData

interface DeviceCommunicator {

    fun setHost(host: String)

    suspend fun getData(): DeviceData?

    suspend fun setData(deviceData: DeviceData): String

    suspend fun setBrightness(percentage: Int)

    suspend fun playSound()

    suspend fun stopSound()

    suspend fun reboot()
}
