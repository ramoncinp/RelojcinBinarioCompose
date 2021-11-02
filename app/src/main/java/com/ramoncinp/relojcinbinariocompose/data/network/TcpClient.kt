package com.ramoncinp.relojcinbinariocompose.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

private const val PORT = 2000

class TcpClient {

    var ipAddress: String = ""

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun sendMessage(message: String): String = withContext(Dispatchers.IO) {
        val client = Socket(ipAddress, PORT)
        val input = BufferedReader(InputStreamReader(client.getInputStream()))

        Timber.d("Sending message -> $message")
        client.getOutputStream().write(message.toByteArray())

        var response = ""
        for (line in input.readLines()) {
            response += line.trim()
        }

        Timber.d("Device responded -> $response")
        client.close()

        return@withContext response
    }
}
