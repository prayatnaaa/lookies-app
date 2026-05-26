package com.prayatna.lookiesapp.presentation.user.artistSubmission

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.artistSubmissionNavigation(navController: NavController) {
    composable(
        route = NavigationRoutes.ARTIST_APPLICATION
    ) {
        ArtistSubmissionRoute(navController = navController)
    }
}