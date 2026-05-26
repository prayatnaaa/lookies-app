package com.prayatna.lookiesapp.presentation.admin.detailEvent

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToAdminDetailEvent(eventId: String) {
    this.navigate("${NavigationRoutes.ADMIN_DETAIL_EVENT}/$eventId")
}

fun NavGraphBuilder.adminDetailEventNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.ADMIN_DETAIL_EVENT}/{eventId}",
        arguments = listOf(
            navArgument("eventId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
        AdminDetailEventRoute(
            eventId = eventId,
            navController = navController
        )
    }
}
