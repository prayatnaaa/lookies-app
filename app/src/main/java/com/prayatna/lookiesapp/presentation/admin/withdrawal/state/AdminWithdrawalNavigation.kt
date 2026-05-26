package com.prayatna.lookiesapp.presentation.admin.withdrawal.state

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.admin.withdrawal.detail.AdminWithdrawalDetailRoute
import com.prayatna.lookiesapp.presentation.admin.withdrawal.list.AdminWithdrawalListRoute
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToAdminWithdrawalList() {
    this.navigate(NavigationRoutes.ADMIN_WITHDRAWAL_LIST)
}

fun NavController.navigateToAdminWithdrawalDetail(id: String) {
    this.navigate("${NavigationRoutes.ADMIN_WITHDRAWAL_DETAIL}/$id")
}

fun NavGraphBuilder.adminWithdrawalNavigation(navController: NavController) {
    composable(NavigationRoutes.ADMIN_WITHDRAWAL_LIST) {
        AdminWithdrawalListRoute(
            onNavigateBack = { navController.popBackStack() },
            onNavigateToDetail = { id ->
                navController.navigateToAdminWithdrawalDetail(id)
            }
        )
    }

    composable(
        route = "${NavigationRoutes.ADMIN_WITHDRAWAL_DETAIL}/{withdrawalId}",
        arguments = listOf(
            navArgument("withdrawalId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val withdrawalId = backStackEntry.arguments?.getString("withdrawalId") ?: ""
        AdminWithdrawalDetailRoute(
            withdrawalId = withdrawalId,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
