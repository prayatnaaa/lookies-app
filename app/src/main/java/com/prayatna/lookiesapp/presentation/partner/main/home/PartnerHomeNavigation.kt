package com.prayatna.lookiesapp.presentation.partner.main.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes


fun NavGraphBuilder.partnerHomeScreenNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.PARTNER_MAIN_SCREEN}/{businessId}",
        arguments = listOf(
            navArgument("businessId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val businessId = backStackEntry.arguments?.getString("businessId") ?: ""
        PartnerHomeRoute(navController = navController, businessId = businessId)
    }
}