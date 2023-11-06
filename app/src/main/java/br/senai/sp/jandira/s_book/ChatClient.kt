package br.senai.sp.jandira.s_book

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.local.LocalStore
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ChatClient(private val username: String, private val localStore: Storage, private val context: Context) {
    private val socket: Socket = IO.socket("http://10.107.144.28:3001")

    // Adicione uma instância do LiveData para armazenar a lista de contatos
    private val contactListLiveData = ContactListLiveData()

    fun connect() {
        socket.connect()

        socket.emit("listContacts", 1)

//        socket.on("receive_contacts") { args ->
//            Log.e("RECEBER CONTATOS", "connect: ${args[0]}", )
//            val contactList = args[0].toString()
//
//            // Atualize o LiveData com a nova lista de contatos na thread principal
//            contactListLiveData.setContactList(contactList)
//        }

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

    fun receiveContacts(onContactsChange: (String) -> Unit) {
        socket.on("receive_contacts"){
            Log.e("RECEBER CONTATOS", "connect: ${it[0]}", )

            onContactsChange(it[0].toString())
        }
    }

    suspend fun receiveContacts2(): JSONObject {
        return withContext(Dispatchers.IO) {
            val contatosDeferred = CompletableDeferred<JSONObject>()

            socket.on("receive_contacts") { args ->
                val contatos = args[0] as JSONObject
                Log.e("Recebiiiiii", "$contatos", )
                contatosDeferred.complete(contatos)
            }

            val contatos = contatosDeferred.await()

            if (contatos.length() > 1) {
                Log.e("Rec-Cont-2", "$contatos")
            }

            contatos
        }
    }


    fun sendMessage(message: String) {
        socket.emit("message", message)
    }

    fun disconnect() {
        socket.disconnect()
    }

    // Adicione um método para obter o LiveData da lista de contatos
    fun getContactListLiveData(): LiveData<String> {
        return contactListLiveData.getContactList()
    }
}
