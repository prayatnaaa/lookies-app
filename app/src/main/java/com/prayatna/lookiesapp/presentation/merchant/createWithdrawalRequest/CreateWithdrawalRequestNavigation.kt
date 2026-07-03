package com.prayatna.lookiesapp.presentation.merchant.createWithdrawalRequest

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.merchant.withdrawalConfirmation.WithdrawalConfirmationScreen
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToCreateWithdrawalRequest(businessId: String) {
    this.navigate("${NavigationRoutes.CREATE_WITHDRAWAL_REQUEST}/$businessId")
}

fun NavController.navigateToWithdrawalConfirmation(withdrawalId: String) {
    this.navigate("${NavigationRoutes.WITHDRAWAL_CONFIRMATION}/$withdrawalId")
}

fun NavGraphBuilder.createWithdrawalRequestNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.CREATE_WITHDRAWAL_REQUEST}/{businessId}",
        arguments = listOf(navArgument("businessId") { type = NavType.StringType })
    ) {
        CreateWithdrawalRequestRoute(navController = navController)
    }

    composable(
        route = "${NavigationRoutes.WITHDRAWAL_CONFIRMATION}/{withdrawalId}",
        arguments = listOf(navArgument("withdrawalId") { type = NavType.StringType })
    ) {
        WithdrawalConfirmationScreen(
            navController = navController,
        )
    }
}
