package br.senai.sp.jandira.s_book

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class tentando {
    private var socket: Socket? = null

    init {
        try {
            socket = IO.socket("http://26.166.70.79:3001")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }

    fun onMessageReceived(listener: (String) -> Unit) {
        socket?.on("message") { args ->
            val message = args[0] as String
            listener.invoke(message)
        }

    }

    fun sendMessage(message: String) {
        socket?.emit("message", message)
    }
}