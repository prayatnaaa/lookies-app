package com.prayatna.lookiesapp.presentation.insertEventPaintings

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.insertEventPaintingsNavigation(
    navController: NavController
) {
    composable(
        route = "${NavigationRoutes.INSERT_EVENT_PAINTINGS_ROUTE}/{eventId}/{merchantId}",
        arguments = listOf(
            navArgument("eventId") { type = NavType.IntType },
            navArgument("merchantId") { type = NavType.StringType }
        )
    ) {
        InsertEventPaintingsRoute(
            navController = navController
        )
    }
}