package com.prayatna.lookiesapp.presentation.admin.detailEvent

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiEffect
import com.prayatna.lookiesapp.presentation.admin.detailEvent.state.AdminDetailEventUiEvent

@Composable
fun AdminDetailEventRoute(
    eventId: String,
    navController: NavController,
    viewModel: AdminDetailEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(eventId) {
        viewModel.onEvent(AdminDetailEventUiEvent.LoadDetail(eventId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AdminDetailEventUiEffect.ShowToast -> {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbar_message", effect.message)

                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "refresh",
                            true
                        )

                    navController.popBackStack()
                }
                AdminDetailEventUiEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    AdminDetailEventScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = { navController.popBackStack() }
    )
}
