package iad1tya.echo.music.listentogether

/**
 * Privacy-fork compatibility model layer.
 *
 * Listen Together itself is disabled: these are source-compatibility DTOs only and
 * contain no networking, persistence, sockets or telemetry.
 */
enum class ConnectionState { DISCONNECTED, CONNECTING, CONNECTED, RECONNECTING, ERROR }
enum class RoomRole { HOST, GUEST, NONE }
enum class LogLevel { INFO, WARNING, ERROR, DEBUG }

data class LogEntry(val timestamp: String, val level: LogLevel, val message: String, val details: String? = null)

data class TrackInfo(
    val id: String,
    val title: String,
    val artist: String,
    val album: String? = null,
    val duration: Long = 0L,
    val thumbnail: String? = null,
    val suggestedBy: String? = null,
)

data class UserInfo(
    val userId: String,
    val username: String,
    val isHost: Boolean,
    val isConnected: Boolean = true,
)

data class RoomState(
    val roomCode: String,
    val hostId: String,
    val users: List<UserInfo> = emptyList(),
    val currentTrack: TrackInfo? = null,
    val isPlaying: Boolean = false,
    val position: Long = 0L,
    val lastUpdate: Long = 0L,
    val volume: Float = 1f,
    val queue: List<TrackInfo> = emptyList(),
)

data class JoinRequestPayload(val userId: String, val username: String)
data class RepliedMessage(val username: String, val message: String)
data class ChatMessagePayload(
    val userId: String,
    val username: String,
    val message: String,
    val timestamp: Long,
    val replyTo: RepliedMessage? = null,
)
data class SuggestionReceivedPayload(
    val suggestionId: String,
    val fromUserId: String,
    val fromUsername: String,
    val trackInfo: TrackInfo,
)
data class PlaybackActionPayload(
    val action: String,
    val trackId: String? = null,
    val position: Long? = null,
    val trackInfo: TrackInfo? = null,
    val insertNext: Boolean? = null,
    val queue: List<TrackInfo>? = null,
    val queueTitle: String? = null,
    val volume: Float? = null,
    val serverTime: Long? = null,
)
data class SyncStatePayload(
    val currentTrack: TrackInfo?,
    val isPlaying: Boolean,
    val position: Long,
    val lastUpdate: Long,
    val queue: List<TrackInfo>? = null,
    val volume: Float? = null,
)
data class JoinApprovedPayload(val roomCode: String, val userId: String, val sessionToken: String = "", val state: RoomState)

data class UserJoinedPayload(val userId: String, val username: String)
data class UserLeftPayload(val userId: String, val username: String)
data class UserReconnectedPayload(val userId: String, val username: String)
data class UserDisconnectedPayload(val userId: String, val username: String)
data class SuggestionApprovedPayload(val suggestionId: String, val trackInfo: TrackInfo)
data class SuggestionRejectedPayload(val suggestionId: String, val reason: String? = null)
data class JoinRejectedPayload(val reason: String)

data class JoinRequestCompat(val userId: String, val username: String)

sealed class ListenTogetherEvent {
    data class Connected(val userId: String) : ListenTogetherEvent()
    data object Disconnected : ListenTogetherEvent()
    data class ConnectionError(val error: String) : ListenTogetherEvent()
    data class Reconnecting(val attempt: Int, val maxAttempts: Int) : ListenTogetherEvent()
    data class RoomCreated(val roomCode: String, val userId: String) : ListenTogetherEvent()
    data class JoinRequestReceived(val userId: String, val username: String) : ListenTogetherEvent()
    data class JoinApproved(val roomCode: String, val userId: String, val state: RoomState) : ListenTogetherEvent()
    data class JoinRejected(val reason: String) : ListenTogetherEvent()
    data class UserJoined(val userId: String, val username: String) : ListenTogetherEvent()
    data class UserLeft(val userId: String, val username: String) : ListenTogetherEvent()
    data class HostChanged(val newHostId: String, val newHostName: String) : ListenTogetherEvent()
    data class Kicked(val reason: String) : ListenTogetherEvent()
    data class Reconnected(val roomCode: String, val userId: String, val state: RoomState, val isHost: Boolean) : ListenTogetherEvent()
    data class UserReconnected(val userId: String, val username: String) : ListenTogetherEvent()
    data class UserDisconnected(val userId: String, val username: String) : ListenTogetherEvent()
    data class PlaybackSync(val action: PlaybackActionPayload) : ListenTogetherEvent()
    data class BufferWait(val trackId: String, val waitingFor: List<String>) : ListenTogetherEvent()
    data class BufferComplete(val trackId: String) : ListenTogetherEvent()
    data class SyncStateReceived(val state: SyncStatePayload) : ListenTogetherEvent()
    data class ServerError(val code: String, val message: String) : ListenTogetherEvent()
    data class ChatMessageReceived(val payload: ChatMessagePayload) : ListenTogetherEvent()
    data class LocalSuggestionApproved(val payload: SuggestionReceivedPayload) : ListenTogetherEvent()
}

object ListenTogetherServers {
    data class Server(val name: String, val url: String, val location: String = "", val operator: String = "")
    val defaultServerUrl: String = ""
    val defaultServers: List<Server> = emptyList()
    fun allServers(): List<Server> = emptyList()
}
