package com.prayatna.lookiesapp.presentation.refund

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.refund.createRefund.CreateRefundRoute
import com.prayatna.lookiesapp.presentation.refund.orderRefunds.OrderRefundsScreen
import com.prayatna.lookiesapp.presentation.refund.refundDetail.RefundDetailRoute
import com.prayatna.lookiesapp.presentation.refund.refundList.RefundListScreen
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToRefundDetail(refundId: String) {
    this.navigate("${NavigationRoutes.REFUND_DETAIL}/$refundId")
}

fun NavGraphBuilder.refundNavigation(navController: NavController) {
    composable(NavigationRoutes.REFUND_LIST) {
        RefundListScreen(navController = navController)
    }

    composable(
        route = "${NavigationRoutes.ORDER_REFUNDS}/{orderId}",
        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
    ) { backStackEntry ->
        backStackEntry.arguments?.getString("orderId")?.let { orderId ->
            OrderRefundsScreen(navController = navController, orderId = orderId)
        }
    }

    composable(
        route = "${NavigationRoutes.CREATE_REFUND}/{orderId}",
        arguments = listOf(navArgument("orderId") { type = NavType.StringType })
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
        CreateRefundRoute(navController = navController, orderId = orderId)
    }

    composable(
        route = "${NavigationRoutes.REFUND_DETAIL}/{refundId}",
        arguments = listOf(navArgument("refundId") { type = NavType.StringType })
    ) { backStackEntry ->
        backStackEntry.arguments?.getString("refundId")?.let { refundId ->
            RefundDetailRoute(navController = navController, refundId = refundId)
        }
    }
}
