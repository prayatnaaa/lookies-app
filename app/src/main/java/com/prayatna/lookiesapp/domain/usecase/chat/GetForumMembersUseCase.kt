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

        // State to keep track of current online user IDs
        val onlineUserIds = MutableStateFlow<Set<String>>(emptySet())

        return repository.listenToForumPresence(forumId)
            .onEach { action ->
                val currentSet = onlineUserIds.value.toMutableSet()
                
                // Decode joins and leaves from the action
                val joined = action.decodeJoinsAs<PresenceData>()
                val left = action.decodeLeavesAs<PresenceData>()
                
                currentSet.addAll(joined.map { it.userId })
                currentSet.removeAll(left.map { it.userId }.toSet())

                onlineUserIds.value = currentSet
            }
            .map { _ ->
                initialMembers.map { member ->
                    member.copy(isOnline = onlineUserIds.value.contains(member.userId))
                }
            }
            .onStart {
                // Immediately emit the initial list so UI doesn't stick on loading
                emit(initialMembers)
            }
    }
}
