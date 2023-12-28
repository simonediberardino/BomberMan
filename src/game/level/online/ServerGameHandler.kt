package game.level.online

import game.http.callbacks.TCPServerCallback
import game.http.dao.EntityDao
import game.http.dispatch.HttpMessageReceiverHandler
import game.http.events.forward.AssignIdHttpEventForwarder
import game.http.messages.AssignIdHttpMessage
import game.http.messages.SpawnedEntityHttpMessage
import game.http.models.HttpMessageTypes
import game.http.repo.HttpRepository
import game.http.serializing.HttpParserSerializer
import game.http.sockets.TCPServer

// add level as parameter, online handler
class ServerGameHandler(private val port: Int) : TCPServerCallback {
    private lateinit var server: TCPServer
    var running: Boolean = false
        private set

    fun create() {
        server = TCPServer(port).also { it.open() }
        server.register(this)
    }

    override fun onCloseServer() {
        running = false
    }

    override fun onStartServer() {
        running = true
    }

    override fun onClientConnected(indexedClient: TCPServer.IndexedClient) {
        val data: MutableMap<String, String> = HashMap()
        data["id"] = indexedClient.id.toString()

        // After connection, the server delivers the assigned ID to the client;
        AssignIdHttpEventForwarder().invoke(data)
    }

    override fun onStart() {
        create()
    }

    override fun onClose() {}

    override fun sendData(data: String) {
        server.sendData(data)
    }

    override fun sendData(data: String, receiverId: Int) {
        if (receiverId == -1) {
            sendData(data)
            return
        }

        server.sendData(receiverId, data)
    }

    override fun onDataReceived(data: String) {
        val formattedData: Map<String, String> = HttpParserSerializer.instance.parse(data)
        HttpMessageReceiverHandler.instance.handle(formattedData)
    }
}