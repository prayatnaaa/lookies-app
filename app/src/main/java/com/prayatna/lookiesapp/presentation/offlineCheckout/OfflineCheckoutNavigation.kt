package com.prayatna.lookiesapp.presentation.offlineCheckout

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToOfflineCheckout(itemId: String, quantity: Int) {
    this.navigate("${NavigationRoutes.OFFLINE_CHECKOUT}/$itemId/$quantity")
}

fun NavGraphBuilder.offlineCheckoutNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.OFFLINE_CHECKOUT}/{itemId}/{quantity}",
        arguments = listOf(
            navArgument("itemId") { type = NavType.StringType },
            navArgument("quantity") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
        val quantity = backStackEntry.arguments?.getInt("quantity") ?: 1
        OfflineCheckoutRoute(
            navController = navController
        )
    }
}
