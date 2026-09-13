package iad1tya.echo.music.listentogether

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** Privacy fork compatibility stub. Listen Together networking is disabled. */
class ListenTogetherClient(@Suppress("UNUSED_PARAMETER") context: Context) {
    val connectionState: StateFlow<Any?> = MutableStateFlow(null)
    val roomState: StateFlow<Any?> = MutableStateFlow(null)
    val role: StateFlow<Any?> = MutableStateFlow(null)
    val userId: StateFlow<String?> = MutableStateFlow(null)
    val pendingJoinRequests: StateFlow<List<Any>> = MutableStateFlow(emptyList())
    val bufferingUsers: StateFlow<List<Any>> = MutableStateFlow(emptyList())
    val logs: StateFlow<List<Any>> = MutableStateFlow(emptyList())
    val events: StateFlow<List<Any>> = MutableStateFlow(emptyList())
    val blockedUsernames: StateFlow<List<String>> = MutableStateFlow(emptyList())
    val pendingSuggestions: StateFlow<List<Any>> = MutableStateFlow(emptyList())

    val isInRoom: Boolean get() = false
    val isHost: Boolean get() = false
    val hasPersistedSession: Boolean get() = false
}
