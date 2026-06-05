package com.prayatna.lookiesapp.presentation.chat.conversationList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.chat.merchantConversationList.MerchantConversationListRoute
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToConversationList() {
    this.navigate(NavigationRoutes.CONVERSATION_LIST)
}
fun NavGraphBuilder.conversationListNavigation(navController: NavController) {
    composable(NavigationRoutes.CONVERSATION_LIST) {
        ConversationListRoute(navController = navController)
    }

    composable(
        route = "${NavigationRoutes.MERCHANT_CONVERSATION_LIST}/{merchantId}",
        arguments = listOf(
            navArgument("merchantId") { type = NavType.StringType }
        )
    ) {
        MerchantConversationListRoute(navController = navController)
    }
}
