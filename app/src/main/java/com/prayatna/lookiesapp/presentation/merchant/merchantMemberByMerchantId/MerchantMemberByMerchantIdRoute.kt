package com.prayatna.lookiesapp.presentation.merchant.merchantMemberByMerchantId

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.merchant.merchantMemberByMerchantId.state.MerchantMemberByMerchantIdEffect
import com.prayatna.lookiesapp.utils.NavigationRoutes

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

                is MerchantMemberByMerchantIdEffect.NavigateInviteMember -> {
                    navController.navigate("${NavigationRoutes.INVITE_MERCHANT_MEMBER}/${effect.businessId}")
                }
            }
        }
    }

    MerchantMemberByMerchantIdScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
