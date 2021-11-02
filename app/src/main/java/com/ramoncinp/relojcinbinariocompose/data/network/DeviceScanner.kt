package com.ramoncinp.relojcinbinariocompose.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

private const val SCAN_MESSAGE = "EVERYTHING IS COPACETIC"
private const val BROADCAST_ADDRESS = "255.255.255.255"
private const val PORT = 2400

class DeviceScanner {

    private val devices = mutableListOf<String>()
    private var datagramSocket: DatagramSocket? = null

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun scanForDevices() = withContext(Dispatchers.IO) {
        datagramSocket = DatagramSocket()
        sendMessage()
        waitForResponse()
        return@withContext devices
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
        Timber.d("Data is now sent!")
    }

    private fun waitForResponse() {
        devices.clear()

        while (true) {

            try {
                val buffer = ByteArray(512)
                val receivedPacket = DatagramPacket(buffer, buffer.size)
                datagramSocket?.receive(receivedPacket)

                val clientAddress = receivedPacket.address.hostAddress
                clientAddress?.let {
                    devices.add(clientAddress)
                }

                Timber.d("This device responded -> $clientAddress")
            } catch (e: Exception) {
                break
            }
        }

        datagramSocket?.close()
    }
}
