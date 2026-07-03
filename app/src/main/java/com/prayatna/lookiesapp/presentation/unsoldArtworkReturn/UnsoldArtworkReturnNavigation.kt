package com.prayatna.lookiesapp.presentation.unsoldArtworkReturn

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToUnsoldArtworkReturn(eventPaintingId: String) {
    this.navigate("${NavigationRoutes.UNSOLD_ARTWORK_RETURN}/$eventPaintingId")
}

fun NavGraphBuilder.unsoldArtworkReturnNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.UNSOLD_ARTWORK_RETURN}/{eventPaintingId}",
        arguments = listOf(
            navArgument("eventPaintingId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val eventPaintingId = backStackEntry.arguments?.getString("eventPaintingId") ?: ""
        UnsoldArtworkReturnRoute(
            navController = navController,
            eventPaintingId = eventPaintingId
        )
    }
}
