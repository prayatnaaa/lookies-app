package com.prayatna.lookiesapp.data.remote.api.supabase

import com.prayatna.lookiesapp.data.remote.dto.MessageDto
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SupabaseChatService @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest
) {
     @OptIn(SupabaseExperimental::class)
     fun getMessages(): Flow<List<MessageDto>> {
         val senderId = auth.currentUserOrNull()?.id ?: throw IllegalStateException("User not logged in")
        val response = postgrest.from("messages_view")
            .selectAsFlow(
                MessageDto::id,
                filter = FilterOperation("sender_id", FilterOperator.EQ, senderId)
            )
        return response
    }
}