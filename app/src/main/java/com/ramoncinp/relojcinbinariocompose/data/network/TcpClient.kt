package com.ramoncinp.relojcinbinariocompose.data.network

import kotlinx.coroutines.*
import timber.log.Timber
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket

private const val PORT = 2000
private const val TIMEOUT_MS = 1000

class TcpClient {

    var ipAddress: String = ""

    fun sendMessage(message: String): String = runBlocking {
        val client = Socket(ipAddress, PORT).also { it.soTimeout = TIMEOUT_MS }
        val input = BufferedReader(InputStreamReader(client.getInputStream()))

        Timber.d("Sending message -> $message")
        client.getOutputStream().write(message.toByteArray())

        var response = ""
        for (line in input.readLines()) {
            response += line.trim()
        }

        Timber.d("Device responded -> $response")
        client.close()

        response
    }
}
