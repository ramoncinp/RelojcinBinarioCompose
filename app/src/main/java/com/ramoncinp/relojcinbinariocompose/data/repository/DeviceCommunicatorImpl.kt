package com.ramoncinp.relojcinbinariocompose.data.repository

import com.ramoncinp.relojcinbinariocompose.data.mappers.percentageToPwmValue
import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData
import com.ramoncinp.relojcinbinariocompose.data.models.GetDeviceDataResponse
import com.ramoncinp.relojcinbinariocompose.data.network.TcpClient
import com.squareup.moshi.Moshi
import timber.log.Timber
import javax.inject.Inject

@Suppress("BlockingMethodInNonBlockingContext")
class DeviceCommunicatorImpl @Inject constructor(
    private val tcpClient: TcpClient,
    private val moshi: Moshi
) : DeviceCommunicator {

    override fun setHost(host: String) {
        tcpClient.ipAddress = host
    }

    override suspend fun getData(): DeviceData? {
        val rawResponse = tcpClient.sendMessage("{\"key\":\"get_data\"}")
        val response = moshi.adapter(GetDeviceDataResponse::class.java).fromJson(rawResponse)

        return response?.let {
            moshi.adapter(DeviceData::class.java).fromJson(it.message)
        }
    }

    override fun setData(deviceData: DeviceData) {
        TODO("Not yet implemented")
    }

    override suspend fun setBrightness(percentage: Int) {
        Timber.d("Received percentage is $percentage")
        val pwmValue = percentageToPwmValue(percentage)
        tcpClient.sendMessage("{\"key\":\"set_brightness\", \"value\":$pwmValue}")
    }

    override fun syncHour(currentHour: Int) {
        TODO("Not yet implemented")
    }

    override fun playSound() {
        TODO("Not yet implemented")
    }

    override fun stopSound() {
        TODO("Not yet implemented")
    }

    override fun reboot() {
        TODO("Not yet implemented")
    }
}
