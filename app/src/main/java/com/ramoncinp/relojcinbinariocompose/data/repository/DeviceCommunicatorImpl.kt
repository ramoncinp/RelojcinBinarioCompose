package com.ramoncinp.relojcinbinariocompose.data.repository

import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData
import com.ramoncinp.relojcinbinariocompose.data.models.GetDeviceDataResponse
import com.ramoncinp.relojcinbinariocompose.data.models.SetDeviceRequest
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

    override suspend fun setData(deviceData: DeviceData) {
        val rawRequest =
            moshi.adapter(SetDeviceRequest::class.java).toJson(SetDeviceRequest(data = deviceData))

        val response = tcpClient.sendMessage(rawRequest)
        Timber.d(response)
    }

    override suspend fun setBrightness(pwmValue: Int) {
        Timber.d("Received percentage is $pwmValue")
        tcpClient.sendMessage("{\"key\":\"set_brightness\", \"value\":$pwmValue}")
    }

    override fun syncHour(currentHour: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun playSound() {
        tcpClient.sendMessage("{\"key\":\"play_song\"}")
    }

    override suspend fun stopSound() {
        tcpClient.sendMessage("{\"key\":\"stop_song\"}")
    }

    override suspend fun reboot() {
        tcpClient.sendMessage("{\"key\":\"reboot\"}")
    }
}
