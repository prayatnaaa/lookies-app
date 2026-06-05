package com.prayatna.lookiesapp.presentation.chat.privateChat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToPrivateChat(conversationId: String, partyName: String) {
    this.navigate("${NavigationRoutes.PRIVATE_CHAT}/${conversationId}/${partyName}")
}
fun NavGraphBuilder.privateChatNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.PRIVATE_CHAT}/{conversationId}/{otherPartyName}",
        arguments = listOf(
            navArgument("conversationId") { type = NavType.StringType },
            navArgument("otherPartyName") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
        val otherPartyName = backStackEntry.arguments?.getString("otherPartyName") ?: ""
        PrivateChatRoute(
            conversationId = conversationId,
            otherPartyName = otherPartyName,
            navController = navController
        )
    }
}