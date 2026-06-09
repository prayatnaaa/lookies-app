package com.prayatna.lookiesapp.domain.usecase.chat

import com.prayatna.lookiesapp.domain.model.message.ForumMember
import com.prayatna.lookiesapp.domain.model.message.PresenceData
import com.prayatna.lookiesapp.domain.repository.ChatRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.realtime.decodeJoinsAs
import io.github.jan.supabase.realtime.decodeLeavesAs
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class GetForumMembersUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend operator fun invoke(forumId: String): Flow<List<ForumMember>> {
        val membersResult = repository.getForumMembers(forumId)
        val initialMembers = (membersResult as? DataResult.Success)?.data ?: emptyList()

        val onlineUsers =
            MutableStateFlow<Map<String, Long>>(emptyMap())

        return repository.listenToForumPresence(forumId)
            .onEach { action ->
                val currentUsers = onlineUsers.value.toMutableMap()

                val joined = action.decodeJoinsAs<PresenceData>()
                val left = action.decodeLeavesAs<PresenceData>()

                joined.forEach {
                    currentUsers[it.userId] = it.onlineAt
                }

                left.forEach {
                    currentUsers.remove(it.userId)
                }

                onlineUsers.value = currentUsers
            }
            .map {
                initialMembers.map { member ->
                    val onlineAt = onlineUsers.value[member.userId]

                    member.copy(
                        isOnline = onlineAt != null,
                        onlineAt = onlineAt
                    )
                }
            }
            .onStart {
                emit(initialMembers)
            }
    }
}
