package com.prayatna.lookiesapp.presentation.user.partnerapplication

import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.prayatna.lookiesapp.presentation.user.partnerapplication.screen.AddLocationScreen
import com.prayatna.lookiesapp.presentation.user.partnerapplication.screen.PartnerProfileFormScreen
import com.prayatna.lookiesapp.utils.NavigationRoutes

const val PARTNER_FLOW = "partner_flow"
fun NavGraphBuilder.partnerApplicationNavGraph(navController: NavController) {
    navigation(
        startDestination = NavigationRoutes.ADD_LOCATION,
        route = PARTNER_FLOW
    ) {
        composable(NavigationRoutes.ADD_LOCATION) { backStackEntry ->
            val mainEntry = remember(backStackEntry) {
                navController.getBackStackEntry(PARTNER_FLOW)
            }
            val viewModel: PartnerApplicationViewModel = hiltViewModel(mainEntry)
            AddLocationScreen(viewModel = viewModel, navController = navController)
        }
        composable(NavigationRoutes.PARTNER_APPLICATION) {
            val mainEntry = remember(it) {
                navController.getBackStackEntry(PARTNER_FLOW)
            }
            val viewModel: PartnerApplicationViewModel = hiltViewModel(mainEntry)
            PartnerProfileFormScreen(viewModel = viewModel, navController = navController)
        }
    }
}