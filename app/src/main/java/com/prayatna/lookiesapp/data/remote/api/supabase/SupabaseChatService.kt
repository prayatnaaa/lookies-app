package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.ChatRoomDto
import com.prayatna.lookiesapp.data.remote.dto.MessageDto
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.PostgresAction
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SupabaseChatService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
) {


    private fun generateConversationId(myId: String, otherId: String): String {
        val ids = listOf(myId, otherId).sorted()
        return "${ids[0]}_${ids[1]}"
    }

     @OptIn(SupabaseExperimental::class)
     fun getMessages(targetId: String): Flow<List<MessageDto>> {
         val senderId = auth.currentUserOrNull()?.id ?: throw IllegalStateException("User not logged in")
         val conversationId = generateConversationId(senderId, targetId)
        val response = postgrest.from("messages_view")
            .selectAsFlow(
                MessageDto::id,
                filter = FilterOperation("conversation_id", FilterOperator.EQ, conversationId),
            )
        return response
    }

    suspend fun getChatInbox(): List<ChatRoomDto> {
        return postgrest.from("chat_inbox_view")
            .select {
//                filter {
//                    eq("user_id", myUserId)
//                }
                order("sent_at", Order.DESCENDING)
            }
            .decodeList<ChatRoomDto>()
    }
}