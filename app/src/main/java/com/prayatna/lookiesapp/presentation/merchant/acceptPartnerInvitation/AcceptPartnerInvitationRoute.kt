package com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation.state.AcceptPartnerInvitationEffect

@Composable
fun AcceptPartnerInvitationRoute(
    navController: NavController,
    viewModel: AcceptPartnerInvitationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var messageToShow by remember { mutableStateOf("") }
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        CustomBottomSheet(
            title = "Notification",
            message = messageToShow,
            onConfirm = { showSheet = false },
            onDismiss = { showSheet = false }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AcceptPartnerInvitationEffect.NavigateBack -> {
                    navController.popBackStack()
                }

                is AcceptPartnerInvitationEffect.ShowMessage -> {
                    messageToShow = effect.message
                    showSheet = true
                }

                is AcceptPartnerInvitationEffect.InvitationAccepted -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh", true)
                    navController.popBackStack()
                }

                is AcceptPartnerInvitationEffect.InvitationRejected -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh", true)
                    navController.popBackStack()
                }
            }
        }
    }

    AcceptPartnerInvitationScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
