package com.prayatna.lookiesapp.presentation.publicMerchantProfile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToPublicMerchantProfile(businessId: String) {
    this.navigate("${NavigationRoutes.PUBLIC_MERCHANT_PROFILE}/$businessId")
}

fun NavGraphBuilder.publicMerchantProfileNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.PUBLIC_MERCHANT_PROFILE}/{businessId}",
        arguments = listOf(
            navArgument("businessId") { type = NavType.StringType }
        )
    ) {
        PublicMerchantProfileRoute(
            navController = navController,
        )
    }
}
