package br.senai.sp.jandira.s_book

import io.socket.client.IO
import io.socket.client.Socket

class ChatClient(private val username: String) {
    private val socket: Socket = IO.socket("http://26.166.70.79:3001")

    fun connect() {
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            println("Connected to server")
            socket.emit("set_username", username)
        }

        socket.on("message") { args ->
            val message = args[0] as String
            println("Received message: $message")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("Disconnected from server")
        }
    }

    fun sendMessage(message: String) {
        socket.emit("message", message)
    }

    fun disconnect() {
        socket.disconnect()
    }
}