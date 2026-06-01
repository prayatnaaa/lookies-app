package com.prayatna.lookiesapp.presentation.partner.eventTransactions

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToEventTransactionList(eventId: String) {
    this.navigate("${NavigationRoutes.EVENT_TRANSACTION_LIST}/$eventId")
}

fun NavGraphBuilder.eventTransactionListNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.EVENT_TRANSACTION_LIST}/{eventId}",
        arguments = listOf(
            navArgument("eventId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
        EventTransactionListRoute(
            navController = navController,
            eventId = eventId
        )
    }
}
