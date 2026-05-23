package com.prayatna.lookiesapp.presentation.painting.editpainting

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToEditPainting(id: Int) {
    this.navigate("${NavigationRoutes.EDIT_PAINTING}/$id")
}

fun NavGraphBuilder.editPaintingNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.EDIT_PAINTING}/{paintingId}",
        arguments = listOf(
            navArgument("paintingId") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val paintingId = backStackEntry.arguments?.getInt("paintingId") ?: -1
        EditPaintingRoute(
            navController = navController,
            paintingId = paintingId
        )
    }
}
