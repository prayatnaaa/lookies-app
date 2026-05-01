package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                    // show snackbar/toast
                }
            }
        }
    }

    PartnerHomeScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}