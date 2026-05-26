package com.prayatna.lookiesapp.presentation.admin.transactionDetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToAdminTransactionDetail(orderId: String) {
    this.navigate("${NavigationRoutes.ADMIN_TRANSACTION_DETAIL}/$orderId")
}

fun NavGraphBuilder.adminTransactionDetailNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.ADMIN_TRANSACTION_DETAIL}/{orderId}",
        arguments = listOf(
            navArgument("orderId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
        AdminTransactionDetailRoute(
            navController = navController,
            orderId = orderId
        )
    }
}
