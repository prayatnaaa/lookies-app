package com.prayatna.lookiesapp.presentation.forum

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.prayatna.lookiesapp.presentation.forum.state.ForumEvent

@Composable
fun ForumRoute(
    channelId: String,
    onBackClick: () -> Unit,
    viewModel: ForumViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(channelId) {
        viewModel.onEvent(ForumEvent.InitChannel(channelId))
    }

    ForumScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}
