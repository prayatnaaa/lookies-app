package com.prayatna.lookiesapp.presentation.exhibitionShipment

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToExhibitionShipment(eventPaintingId: String, isPartner: Boolean = false) {
    this.navigate(NavigationRoutes.EXHIBITION_SHIPMENT + "/$eventPaintingId" + "?isPartner=$isPartner")
}

fun NavGraphBuilder.exhibitionShipmentNavigation(
    navController: NavController
) {
    composable(
        route = NavigationRoutes.EXHIBITION_SHIPMENT + "/{eventPaintingId}" + "?isPartner={isPartner}",
        arguments = listOf(
            navArgument("eventPaintingId") { type = NavType.StringType },
            navArgument("isPartner") {
                type = NavType.BoolType
                defaultValue = false
            },
        )
    ) { backStackEntry ->
        val eventPaintingId = backStackEntry.arguments!!.getString("eventPaintingId")!!
        ExhibitionShipmentRoute(
            navController = navController,
            eventPaintingId = eventPaintingId,
        )
    }
}
