package com.prayatna.lookiesapp.presentation.forum.forumchannellist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun ForumChannelListRoute(
    forumId: String,
    onNavigateToChat: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ForumChannelListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(forumId) {
        viewModel.initForum(forumId)
    }

    ForumChannelListScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is ForumChannelListEvent.OnChannelClick -> onNavigateToChat(event.channelId)
                else -> viewModel.onEvent(event)
            }
        },
        onBackClick = onBackClick
    )
}
