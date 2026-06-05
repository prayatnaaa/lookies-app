package com.prayatna.lookiesapp.presentation.chat.privateChat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToPrivateChat(conversationId: String, partyName: String, isMerchant: Boolean = false) {
    this.navigate("${NavigationRoutes.PRIVATE_CHAT}/${conversationId}/${partyName}?isMerchant=${isMerchant}")
}

fun NavGraphBuilder.privateChatNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.PRIVATE_CHAT}/{conversationId}/{otherPartyName}?isMerchant={isMerchant}",
        arguments = listOf(
            navArgument("conversationId") { type = NavType.StringType },
            navArgument("otherPartyName") { type = NavType.StringType },
            navArgument("isMerchant") { 
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) { backStackEntry ->
        val conversationId = backStackEntry.arguments?.getString("conversationId") ?: ""
        val otherPartyName = backStackEntry.arguments?.getString("otherPartyName") ?: ""
        val isMerchant = backStackEntry.arguments?.getBoolean("isMerchant") ?: false
        
        PrivateChatRoute(
            conversationId = conversationId,
            otherPartyName = otherPartyName,
            isMerchant = isMerchant,
            navController = navController
        )
    }
}
