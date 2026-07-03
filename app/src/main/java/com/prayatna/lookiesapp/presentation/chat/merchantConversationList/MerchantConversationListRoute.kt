package com.prayatna.lookiesapp.presentation.chat.merchantConversationList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.chat.conversationList.ConversationListScreen
import com.prayatna.lookiesapp.presentation.chat.conversationList.state.ConversationListEffect
import com.prayatna.lookiesapp.presentation.chat.privateChat.navigateToPrivateChat

@Composable
fun MerchantConversationListRoute(
    navController: NavController,
    viewModel: MerchantConversationListViewModel = hiltViewModel()
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
                        conversationId = effect.conversationId,
                        isMerchant = true
                    )
                }
            }
        }
    }

    ConversationListScreen(
        state = state,
        onEvent = viewModel::onEvent,
        isMerchant = true,
    )
}
