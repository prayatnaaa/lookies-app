package com.prayatna.lookiesapp.presentation.createPaintingReview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.createPaintingReviewNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.CREATE_PAINTING_REVIEW}/{eventPaintingId}/{orderId}",
        arguments = listOf(
            navArgument("eventPaintingId") { type = NavType.StringType },
            navArgument("orderId") { type = NavType.StringType }
        )
    ) {
        CreatePaintingReviewRoute(navController = navController)
    }
}