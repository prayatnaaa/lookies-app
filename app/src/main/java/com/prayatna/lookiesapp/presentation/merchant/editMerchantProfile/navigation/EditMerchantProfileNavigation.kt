package com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile.EditMerchantProfileScreen
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToEditMerchantProfile(businessId: String) {
    this.navigate("${NavigationRoutes.EDIT_MERCHANT_PROFILE}/$businessId")
}

fun NavGraphBuilder.editMerchantProfileNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.EDIT_MERCHANT_PROFILE}/{businessId}",
        arguments = listOf(navArgument("businessId") { type = NavType.StringType })
    ) { backStackEntry ->
        val businessId = backStackEntry.arguments?.getString("businessId") ?: ""
        EditMerchantProfileScreen(
            navController = navController,
            businessId = businessId
        )
    }
}
