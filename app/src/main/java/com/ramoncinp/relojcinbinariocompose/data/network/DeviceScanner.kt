package com.ramoncinp.relojcinbinariocompose.data.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

private const val SCAN_MESSAGE = "EVERYTHING IS COPACETIC"
private const val BROADCAST_ADDRESS = "255.255.255.255"
private const val PORT = 2400

class DeviceScanner {

    private var datagramSocket: DatagramSocket? = null
    val connectedDevices = MutableLiveData<List<String>>()

    suspend fun scanForDevices() {
        withContext(Dispatchers.IO) {
            datagramSocket = DatagramSocket()
            sendMessage()
            waitForResponse()
        }
    }

    private fun sendMessage() {
        datagramSocket?.broadcast = true
        datagramSocket?.soTimeout = 1000

        val dataToSend = SCAN_MESSAGE.toByteArray()
        val packetToSend = DatagramPacket(
            dataToSend,
            dataToSend.size,
            InetAddress.getByName(BROADCAST_ADDRESS),
            PORT
        )

        datagramSocket?.send(packetToSend)
        Log.d("UDPScanner", "Data is now sent!")
    }

    private fun waitForResponse() {
        val devices = mutableListOf<String>()

        while (true) {
            @Suppress("BlockingMethodInNonBlockingContext")
            try {
                val buffer = ByteArray(512)
                val receivedPacket = DatagramPacket(buffer, buffer.size)
                datagramSocket?.receive(receivedPacket)

                val clientAddress = receivedPacket.address.hostAddress
                clientAddress?.let {
                    devices.add(clientAddress)
                }

                Log.d("UDPScanner", "This device responded -> $clientAddress")
            }
            catch (e: Exception) {
                break
            }
        }

        connectedDevices.postValue(devices)
        datagramSocket?.close()
    }
}
