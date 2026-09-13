package iad1tya.echo.music.listentogether

import android.content.Context
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Privacy-fork compatibility client.
 *
 * The original WebSocket implementation is intentionally removed. This class keeps
 * the public API used by the existing UI compiling while every operation fails closed
 * and performs no network, persistence, notification or telemetry I/O.
 */
class ListenTogetherClient(@Suppress("UNUSED_PARAMETER") context: Context) {
    companion object {
        const val ACTION_APPROVE_JOIN = "iad1tya.echo.music.LISTEN_TOGETHER_APPROVE_JOIN"
        const val ACTION_REJECT_JOIN = "iad1tya.echo.music.LISTEN_TOGETHER_REJECT_JOIN"
        const val ACTION_APPROVE_SUGGESTION = "iad1tya.echo.music.LISTEN_TOGETHER_APPROVE_SUGGESTION"
        const val ACTION_REJECT_SUGGESTION = "iad1tya.echo.music.LISTEN_TOGETHER_REJECT_SUGGESTION"
        const val EXTRA_USER_ID = "extra_user_id"
        const val EXTRA_SUGGESTION_ID = "extra_suggestion_id"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
        private var instance: ListenTogetherClient? = null
        fun getInstance(): ListenTogetherClient? = instance
        fun setInstance(client: ListenTogetherClient) { instance = client }
    }

    init { setInstance(this) }

    val connectionState: StateFlow<ConnectionState> = MutableStateFlow(ConnectionState.DISCONNECTED)
    val roomState: StateFlow<RoomState?> = MutableStateFlow(null)
    val role: StateFlow<RoomRole> = MutableStateFlow(RoomRole.NONE)
    val userId: StateFlow<String?> = MutableStateFlow(null)
    val pendingJoinRequests: StateFlow<List<JoinRequestPayload>> = MutableStateFlow(emptyList())
    val bufferingUsers: StateFlow<List<String>> = MutableStateFlow(emptyList())
    val logs: StateFlow<List<LogEntry>> = MutableStateFlow(emptyList())
    val events: SharedFlow<ListenTogetherEvent> = MutableSharedFlow()
    val blockedUsernames: StateFlow<Set<String>> = MutableStateFlow(emptySet())
    val pendingSuggestions: StateFlow<List<SuggestionReceivedPayload>> = MutableStateFlow(emptyList())

    val isInRoom: Boolean get() = false
    val isHost: Boolean get() = false
    val hasPersistedSession: Boolean get() = false

    fun initialize(vararg args: Any?) = Unit
    fun connect(vararg args: Any?) = Unit
    fun disconnect(vararg args: Any?) = Unit
    fun createRoom(vararg args: Any?) = Unit
    fun joinRoom(vararg args: Any?) = Unit
    fun leaveRoom(vararg args: Any?) = Unit
    fun approveJoin(vararg args: Any?) = Unit
    fun rejectJoin(vararg args: Any?) = Unit
    fun kickUser(vararg args: Any?) = Unit
    fun blockUser(vararg args: Any?) = Unit
    fun unblockUser(vararg args: Any?) = Unit
    fun clearLogs(vararg args: Any?) = Unit
    fun forceReconnect(vararg args: Any?) = Unit
    fun requestSync(vararg args: Any?) = Unit
    fun transferHost(vararg args: Any?) = Unit
    fun approveSuggestion(vararg args: Any?) = Unit
    fun rejectSuggestion(vararg args: Any?) = Unit
    fun suggestTrack(vararg args: Any?) = Unit
    fun sendChatMessage(vararg args: Any?) = Unit
    fun setPlayerConnection(@Suppress("UNUSED_PARAMETER") connection: Any?) = Unit
    fun getPersistedRoomCode(): String? = null
    fun getSessionAge(): Long? = null
}
