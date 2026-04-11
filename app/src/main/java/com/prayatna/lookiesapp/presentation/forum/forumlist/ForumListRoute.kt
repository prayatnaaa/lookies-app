package com.prayatna.lookiesapp.presentation.forum.forumlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun ForumListRoute(
    navController: NavController,
    viewModel: ForumListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    ForumListScreen(
        state = state,
        onEvent = { event ->
            when (event) {
                is ForumListEvent.OnForumClick -> {
                    navController.navigate("${NavigationRoutes.FORUM_CHANNEL_LIST}/${event.forumId}")
                }
                else -> viewModel.onEvent(event)
            }
        },
        onBackClick = {
            navController.popBackStack()
        }
    )
}
