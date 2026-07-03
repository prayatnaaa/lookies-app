package com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToSelectPayoutChannel() {
    this.navigate(NavigationRoutes.SELECT_PAYOUT_CHANNEL)
}

fun NavGraphBuilder.selectPayoutChannelNavigation(navController: NavController) {
    composable(NavigationRoutes.SELECT_PAYOUT_CHANNEL) {
        SelectPayoutChannelRoute(navController = navController)
    }
}
