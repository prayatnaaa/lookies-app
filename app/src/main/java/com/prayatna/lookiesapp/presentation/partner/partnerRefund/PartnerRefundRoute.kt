package com.prayatna.lookiesapp.presentation.partner.partnerRefund

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.partner.partnerRefund.state.PartnerRefundEffect

@Composable
fun PartnerRefundRoute(
    navController: NavController,
    viewModel: PartnerRefundViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {

                is PartnerRefundEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                PartnerRefundEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    PartnerRefundScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}