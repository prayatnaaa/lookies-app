package com.prayatna.lookiesapp.presentation.checkout.state

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.checkout.CheckoutRoute
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.checkoutNavigation(
    navController: NavController
) {
    composable("${NavigationRoutes.CHECKOUT}/{type}/{id}/{quantity}",
        arguments = listOf(
            navArgument("type") { type = NavType.StringType },
            navArgument("id") { type = NavType.StringType },
            navArgument("quantity") { type = NavType.IntType }
        )) { backStackEntry ->
        val type = backStackEntry.arguments?.getString("type") ?: ""
        val id = backStackEntry.arguments?.getString("id") ?: ""
        val quantity = backStackEntry.arguments?.getInt("quantity") ?: 1
        CheckoutRoute(
            quantity = quantity,
            type = type,
            itemId = id,
            navController = navController
        )
    }
}