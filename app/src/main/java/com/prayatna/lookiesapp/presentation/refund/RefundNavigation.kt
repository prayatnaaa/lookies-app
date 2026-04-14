package com.prayatna.lookiesapp.presentation.refund

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.refund.createRefund.CreateRefundScreen
import com.prayatna.lookiesapp.presentation.refund.orderRefunds.OrderRefundsScreen
import com.prayatna.lookiesapp.presentation.refund.refundList.RefundListScreen
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.refundNavigation(navController: NavController) {

    // Admin: view all refund requests
    composable(route = NavigationRoutes.REFUND_LIST) {
        RefundListScreen(navController = navController)
    }

    // User: create a refund for a specific order
    composable(
        route = "${NavigationRoutes.CREATE_REFUND}/{orderId}",
        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
        CreateRefundScreen(navController = navController, orderId = orderId)
    }

    // User: view their refund requests for a specific order
    composable(
        route = "${NavigationRoutes.ORDER_REFUNDS}/{orderId}",
        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
        OrderRefundsScreen(navController = navController, orderId = orderId)
    }
}
