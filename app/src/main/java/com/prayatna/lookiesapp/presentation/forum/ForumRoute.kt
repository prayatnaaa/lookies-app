package com.prayatna.lookiesapp.presentation.forum

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ForumRoute(
    channelId: String,
    onBackClick: () -> Unit,
    viewModel: ForumViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(channelId) {
        viewModel.initChannel(channelId)
    }

    ForumScreen(
        state = state,
        onInputChanged = viewModel::onInputTextChanged,
        onSendMessage = viewModel::sendMessage,
        onBackClick = onBackClick
    )
}
