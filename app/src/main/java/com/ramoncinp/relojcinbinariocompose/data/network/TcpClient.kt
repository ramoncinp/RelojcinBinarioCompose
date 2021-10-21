package com.ramoncinp.relojcinbinariocompose.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

private const val PORT = 2000

class TcpClient constructor(private val ipAddress: String) {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun sendMessage(message: String): String = withContext(Dispatchers.IO) {
        val client = Socket(ipAddress, PORT)
        val input = BufferedReader(InputStreamReader(client.getInputStream()))

        client.getOutputStream().write(message.toByteArray())

        var response = ""
        for (line in input.readLines()) {
            response += line.trim()
        }

        Log.d("TcpClient", "Device responded $response")
        client.close()

        return@withContext response
    }
}
