package com.prayatna.lookiesapp.presentation.exhibitionShipment

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.exhibitionShipmentNavigation(
    navController: NavController
) {
    composable(
        route = NavigationRoutes.EXHIBITION_SHIPMENT + "/{eventPaintingId}",
        arguments = listOf(
            navArgument("eventPaintingId") { type = NavType.StringType },
        )
    ) { backStackEntry ->
        val eventPaintingId = backStackEntry.arguments!!.getString("eventPaintingId")!!
        ExhibitionShipmentRoute(
            navController = navController,
            eventPaintingId = eventPaintingId,
        )
    }
}
