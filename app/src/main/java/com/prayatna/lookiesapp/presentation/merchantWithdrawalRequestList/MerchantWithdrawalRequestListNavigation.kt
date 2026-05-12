package com.prayatna.lookiesapp.presentation.merchantWithdrawalRequestList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.merchantWithdrawalRequestListNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.MERCHANT_WITHDRAWAL_REQUEST_LIST}/{businessId}",
        arguments = listOf(
            navArgument("businessId") { type = NavType.StringType }
        )
    ) {
        MerchantWithdrawalRequestListRoute(navController = navController)
    }
}
