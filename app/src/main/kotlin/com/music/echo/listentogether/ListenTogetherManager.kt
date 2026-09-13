package iad1tya.echo.music.listentogether

import android.content.Context
import kotlinx.coroutines.flow.StateFlow

/**
 * Privacy-fork compatibility manager.
 *
 * Listen Together has no active implementation in this fork. All operations are
 * no-ops and no sockets, servers, persistence or telemetry are touched.
 */
class ListenTogetherManager(
    private val client: ListenTogetherClient,
    @Suppress("UNUSED_PARAMETER") private val context: Context,
) {
    val connectionState: StateFlow<ConnectionState> get() = client.connectionState
    val roomState: StateFlow<RoomState?> get() = client.roomState
    val role: StateFlow<RoomRole> get() = client.role
    val userId: StateFlow<String?> get() = client.userId
    val pendingJoinRequests: StateFlow<List<JoinRequestPayload>> get() = client.pendingJoinRequests
    val bufferingUsers: StateFlow<List<String>> get() = client.bufferingUsers
    val logs: StateFlow<List<LogEntry>> get() = client.logs
    val events get() = client.events
    val blockedUsernames: StateFlow<Set<String>> get() = client.blockedUsernames
    val pendingSuggestions: StateFlow<List<SuggestionReceivedPayload>> get() = client.pendingSuggestions

    val isInRoom: Boolean get() = false
    val isHost: Boolean get() = false
    val hasPersistedSession: Boolean get() = false

    fun initialize(vararg args: Any?) = client.initialize(*args)
    fun connect(vararg args: Any?) = client.connect(*args)
    fun disconnect(vararg args: Any?) = client.disconnect(*args)
    fun createRoom(vararg args: Any?) = client.createRoom(*args)
    fun joinRoom(vararg args: Any?) = client.joinRoom(*args)
    fun leaveRoom(vararg args: Any?) = client.leaveRoom(*args)
    fun approveJoin(vararg args: Any?) = client.approveJoin(*args)
    fun rejectJoin(vararg args: Any?) = client.rejectJoin(*args)
    fun kickUser(vararg args: Any?) = client.kickUser(*args)
    fun blockUser(vararg args: Any?) = client.blockUser(*args)
    fun unblockUser(vararg args: Any?) = client.unblockUser(*args)
    fun clearLogs(vararg args: Any?) = client.clearLogs(*args)
    fun forceReconnect(vararg args: Any?) = client.forceReconnect(*args)
    fun requestSync(vararg args: Any?) = client.requestSync(*args)
    fun transferHost(vararg args: Any?) = client.transferHost(*args)
    fun approveSuggestion(vararg args: Any?) = client.approveSuggestion(*args)
    fun rejectSuggestion(vararg args: Any?) = client.rejectSuggestion(*args)
    fun suggestTrack(vararg args: Any?) = client.suggestTrack(*args)
    fun sendChatMessage(vararg args: Any?) = client.sendChatMessage(*args)
    fun setPlayerConnection(connection: Any?) = client.setPlayerConnection(connection)
    fun getPersistedRoomCode(): String? = null
    fun getSessionAge(): Long? = null
}
