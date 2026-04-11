package com.prayatna.lookiesapp.presentation.shipment

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.shipmentDetail.ShipmentDetailRoute
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.shipmentListNavigation(
    navController: NavController
) {
    composable(
        route = "${NavigationRoutes.SHIPMENT_LIST}/{merchantId}",
        arguments = listOf(
            navArgument("merchantId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val merchantId = backStackEntry.arguments?.getString("merchantId")

        merchantId?.let { id ->
            ShipmentListRoute(
                navController = navController,
                merchantId = id
            )
        }
    }
}