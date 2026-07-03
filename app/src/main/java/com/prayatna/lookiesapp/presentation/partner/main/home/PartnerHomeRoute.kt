package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.chat.merchantConversationList.navigateToMerchantConversationList
import com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile.navigation.navigateToEditMerchantProfile
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeEffect
import com.prayatna.lookiesapp.presentation.partner.main.home.state.PartnerHomeEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerHomeRoute(
    navController: NavController,
    businessId: String,
    viewModel: PartnerHomeViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarMessage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("snackbar_message", null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarMessage?.value) {
        snackbarMessage?.value?.let { message ->
            snackbarHostState.showSnackbar(message, withDismissAction = true)
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("snackbar_message")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            PartnerHomeEvent.Load(businessId)
        )
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {

                PartnerHomeEffect.NavigateBack ->
                    navController.popBackStack()

                is PartnerHomeEffect.NavigateCreateEvent ->
                    navController.navigate(
                        "${NavigationRoutes.CREATE_EVENT}/${businessId}"
                    )

                is PartnerHomeEffect.NavigateMyEvents ->
                    navController.navigate(
                        "${NavigationRoutes.SELF_EVENT_LIST}/${businessId}"
                    )

                PartnerHomeEffect.NavigateRefund ->
                    navController.navigate(
                        NavigationRoutes.REFUND_LIST
                    )

                is PartnerHomeEffect.NavigatePainting ->
                    navController.navigate(
                        "${NavigationRoutes.PERSONAL_PAINTING}/${businessId}"
                    )

                is PartnerHomeEffect.NavigateShipment ->
                    navController.navigate(
                        "${NavigationRoutes.SHIPMENT_LIST}/${businessId}"
                    )

                is PartnerHomeEffect.ShowMessage -> {
                    snackbarHostState.showSnackbar(effect.message, withDismissAction = true)
                }

                PartnerHomeEffect.NavigateMonthlyFinanceList -> {
                    navController.navigate(
                        "${NavigationRoutes.MONTHLY_FINANCE_LIST_SCREEN}/${businessId}"
                    )
                }

                is PartnerHomeEffect.NavigateMemberList -> {
                    navController.navigate("${NavigationRoutes.MERCHANT_MEMBER_BY_MERCHANT_ID_LIST}/${effect.merchantBusinessId}")
                }

                is PartnerHomeEffect.NavigateToChat -> {
                    navController.navigateToMerchantConversationList(merchantId = effect.merchantId)
                }

                is PartnerHomeEffect.NavigateEditProfile -> {
                    navController.navigateToEditMerchantProfile(effect.businessId)
                }
            }
        }
    }

    PartnerHomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}