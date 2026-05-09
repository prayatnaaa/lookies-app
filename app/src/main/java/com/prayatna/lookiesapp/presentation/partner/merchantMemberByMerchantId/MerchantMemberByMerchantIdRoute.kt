package com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId.state.MerchantMemberByMerchantIdEffect

@Composable
fun MerchantMemberByMerchantIdRoute(
    navController: NavController,
    viewModel: MerchantMemberByMerchantIdViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                MerchantMemberByMerchantIdEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    MerchantMemberByMerchantIdScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
