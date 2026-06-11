package com.prayatna.lookiesapp.presentation.notification

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToNotifications() {
    this.navigate(NavigationRoutes.NOTIFICATIONS)
}

fun NavGraphBuilder.notificationNavigation(navController: NavController) {
    composable(NavigationRoutes.NOTIFICATIONS) {
        NotificationScreen(navController = navController)
    }
}
