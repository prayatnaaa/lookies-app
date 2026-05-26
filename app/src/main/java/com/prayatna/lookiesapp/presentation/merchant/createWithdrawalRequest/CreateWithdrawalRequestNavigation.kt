package com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.createWithdrawalRequestNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.CREATE_WITHDRAWAL_REQUEST}/{businessId}",
        arguments = listOf(
            navArgument("businessId") { type = NavType.StringType }
        )
    ) {
        CreateWithdrawalRequestRoute(navController = navController)
    }
}
