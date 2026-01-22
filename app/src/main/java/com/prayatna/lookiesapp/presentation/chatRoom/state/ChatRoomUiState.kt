package com.prayatna.lookiesapp.presentation.chatRoom.state

import com.prayatna.lookiesapp.data.remote.dto.ChatRoomDto

data class ChatRoomUiState(
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
    val chatRooms: List<ChatRoomDto> = emptyList()
)
