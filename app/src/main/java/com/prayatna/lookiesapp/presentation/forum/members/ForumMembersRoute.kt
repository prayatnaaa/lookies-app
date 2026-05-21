package com.prayatna.lookiesapp.presentation.forum.members

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.prayatna.lookiesapp.presentation.forum.members.state.ForumMembersUiEvent

@Composable
fun ForumMembersRoute(
    forumId: String,
    onBackClick: () -> Unit,
    viewModel: ForumMembersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(forumId) {
        viewModel.onEvent(ForumMembersUiEvent.LoadMembers(forumId))
    }

    ForumMembersScreen(
        uiState = uiState,
        onBackClick = onBackClick
    )
}
