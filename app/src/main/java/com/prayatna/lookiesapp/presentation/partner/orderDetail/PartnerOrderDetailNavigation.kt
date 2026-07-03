package com.prayatna.lookiesapp.presentation.partner.orderDetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToPartnerOrderDetail(orderId: String) {
    this.navigate("${NavigationRoutes.PARTNER_ORDER_DETAIL}/$orderId")
}

fun NavGraphBuilder.partnerOrderDetailNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.PARTNER_ORDER_DETAIL}/{orderId}",
        arguments = listOf(
            navArgument("orderId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
        PartnerOrderDetailRoute(
            navController = navController,
            orderId = orderId
        )
    }
}
