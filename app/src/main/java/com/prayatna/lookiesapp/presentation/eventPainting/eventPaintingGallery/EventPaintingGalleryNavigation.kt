package com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingGallery

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToEventPaintingGallery(eventId: String) {
    this.navigate("${NavigationRoutes.EVENT_PAINTING_GALLERY}/$eventId")
}

fun NavGraphBuilder.eventPaintingGalleryNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.EVENT_PAINTING_GALLERY}/{eventId}",
        arguments = listOf(
            navArgument("eventId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
        EventPaintingGalleryRoute(
            navController = navController,
            eventId = eventId
        )
    }
}
