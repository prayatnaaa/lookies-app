package com.prayatna.lookiesapp.presentation.chat.conversationList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToConversationList() {
    this.navigate(NavigationRoutes.CONVERSATION_LIST)
}
fun NavGraphBuilder.conversationListNavigation(navController: NavController) {
    composable(NavigationRoutes.CONVERSATION_LIST) {
        ConversationListRoute(navController = navController)
    }
}