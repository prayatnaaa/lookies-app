package com.prayatna.lookiesapp.presentation.chat.privateChat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.chat.privateChat.state.PrivateChatEffect
import com.prayatna.lookiesapp.presentation.chat.privateChat.state.PrivateChatEvent

@Composable
fun PrivateChatRoute(
    conversationId: String?,
    merchantId: String?,
    otherPartyName: String,
    isMerchant: Boolean = false,
    metadataType: String? = null,
    metadataId: String? = null,
    metadataImageUrl: String? = null,
    metadataTitle: String? = null,
    navController: NavController,
    viewModel: PrivateChatViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(conversationId, merchantId, otherPartyName) {
        viewModel.onEvent(PrivateChatEvent.InitChat(
            conversationId = conversationId,
            merchantId = merchantId,
            otherPartyName = otherPartyName,
            metadataType = metadataType,
            metadataId = metadataId,
            metadataImageUrl = metadataImageUrl,
            metadataTitle = metadataTitle
        ))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PrivateChatEffect.NavigateBack -> navController.popBackStack()
            }
        }
    }

    PrivateChatScreen(
        state = state,
        onEvent = viewModel::onEvent,
        isMerchant = isMerchant
    )
}
