package com.ramoncinp.relojcinbinariocompose.data.repository

import com.ramoncinp.relojcinbinariocompose.data.models.DeviceData
import com.ramoncinp.relojcinbinariocompose.data.models.GetDeviceDataResponse
import com.ramoncinp.relojcinbinariocompose.data.models.SetDeviceRequest
import com.ramoncinp.relojcinbinariocompose.data.network.TcpClient
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeviceCommunicatorImpl @Inject constructor(
    private val tcpClient: TcpClient,
    private val moshi: Moshi
) : DeviceCommunicator {

    override fun setHost(host: String) {
        tcpClient.ipAddress = host
    }

    override suspend fun getData(): DeviceData? = withContext(Dispatchers.Default) {
        runCatching {
            val rawResponse = sendSimpleMessage("{\"key\":\"get_data\"}")
            val response = moshi.adapter(GetDeviceDataResponse::class.java).fromJson(rawResponse)
            response?.let { moshi.adapter(DeviceData::class.java).fromJson(it.message) }
        }.getOrDefault(
            null
        )
    }

    override suspend fun setData(deviceData: DeviceData): String =
        withContext(Dispatchers.Default) {
            runCatching {
                val rawRequest = moshi.adapter(SetDeviceRequest::class.java).toJson(
                    SetDeviceRequest(data = deviceData)
                )
                val rawResponse = sendSimpleMessage(rawRequest)
                val response = moshi.adapter(GetDeviceDataResponse::class.java).fromJson(rawResponse)
                response?.let {
                    if (it.status == "ok") "Data correctly saved" else "Error saving the data"
                } ?: "Data sent"
            }.getOrDefault(
                "Data sent"
            )
        }

    override suspend fun setBrightness(percentage: Int) {
        sendSimpleMessage("{\"key\":\"set_brightness\", \"value\":$percentage}")
    }

    override suspend fun playSound() {
        sendSimpleMessage("{\"key\":\"play_song\"}")
    }

    override suspend fun stopSound() {
        sendSimpleMessage("{\"key\":\"stop_song\"}")
    }

    override suspend fun reboot() {
        sendSimpleMessage("{\"key\":\"reboot\"}")
    }

    private suspend fun sendSimpleMessage(message: String): String = withContext(Dispatchers.IO) {
        tcpClient.sendMessage(message)
    }
}
