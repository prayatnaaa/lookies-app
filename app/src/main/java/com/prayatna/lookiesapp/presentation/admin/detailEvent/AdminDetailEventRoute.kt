package com.prayatna.lookiesapp.presentation.admin.detailEvent

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(eventId) {
        viewModel.onEvent(AdminDetailEventUiEvent.LoadDetail(eventId))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AdminDetailEventUiEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
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
