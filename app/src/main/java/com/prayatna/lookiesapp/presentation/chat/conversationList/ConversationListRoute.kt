package com.prayatna.lookiesapp.presentation.chat.conversationList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.chat.conversationList.state.ConversationListEffect
import com.prayatna.lookiesapp.presentation.chat.privateChat.navigateToPrivateChat

@Composable
fun ConversationListRoute(
    navController: NavController,
    viewModel: ConversationListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                ConversationListEffect.NavigateBack -> navController.popBackStack()
                is ConversationListEffect.NavigateToChat -> {
                    navController.navigateToPrivateChat(
                        partyName = effect.otherPartyName,
                        conversationId = effect.conversationId
                    )
                }
            }
        }
    }

    ConversationListScreen(
        state = state,
        onEvent = viewModel::onEvent,
        isMerchant = false
    )
}
