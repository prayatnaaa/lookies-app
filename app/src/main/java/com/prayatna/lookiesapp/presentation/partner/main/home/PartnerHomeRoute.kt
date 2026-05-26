package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
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

    LaunchedEffect(navController.currentBackStackEntry) {

        val savedStateHandle =
            navController.currentBackStackEntry
                ?.savedStateHandle

        savedStateHandle
            ?.getStateFlow<String?>(
                "snackbar_message",
                null
            )
            ?.collect { message ->

                if (message != null) {

                    snackbarHostState.showSnackbar(
                        message = message,
                        withDismissAction = true
                    )

                    savedStateHandle["snackbar_message"] = null
                }
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
            }
        }
    }

    PartnerHomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        snackbarHostState = snackbarHostState
    )
}