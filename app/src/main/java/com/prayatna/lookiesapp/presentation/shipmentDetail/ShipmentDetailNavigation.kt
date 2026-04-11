package com.prayatna.lookiesapp.presentation.shipmentDetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.shipmentDetailNavigation(
    navController: NavController
) {
    composable(
        route = "${NavigationRoutes.SHIPMENT_DETAIL}/{orderId}",
        arguments = listOf(
            navArgument("orderId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val orderId = backStackEntry.arguments?.getString("orderId")

        orderId?.let { id ->
            ShipmentDetailRoute(
                navController = navController,
                orderId = id
            )
        }
    }
}