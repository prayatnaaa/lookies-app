package com.prayatna.lookiesapp.presentation.partner.partnerRefund

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.partnerRefundNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.PARTNER_REFUND}/{refundId}",
        arguments = listOf(navArgument("refundId") { type = NavType.StringType })
    ) {
        PartnerRefundRoute(navController = navController)
    }
}