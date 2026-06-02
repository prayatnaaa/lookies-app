package com.prayatna.lookiesapp.presentation.publicMerchantProfile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.publicMerchantProfile.state.PublicMerchantProfileEffect
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PublicMerchantProfileRoute(
    navController: NavController,
    viewModel: PublicMerchantProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                PublicMerchantProfileEffect.NavigateBack -> navController.popBackStack()
                is PublicMerchantProfileEffect.NavigateToEventDetail -> {
                    navController.navigate("${NavigationRoutes.DETAIL_EVENT}/${effect.eventId}")
                }
            }
        }
    }

    PublicMerchantProfileScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}
