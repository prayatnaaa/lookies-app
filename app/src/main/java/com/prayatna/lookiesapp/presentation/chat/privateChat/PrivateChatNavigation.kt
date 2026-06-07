package com.prayatna.lookiesapp.presentation.chat.privateChat

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToPrivateChat(
    partyName: String,
    conversationId: String? = null,
    merchantId: String? = null,
    isMerchant: Boolean = false
) {
    val route = "${NavigationRoutes.PRIVATE_CHAT}/$partyName?" +
            "conversationId=${conversationId ?: ""}&" +
            "merchantId=${merchantId ?: ""}&" +
            "isMerchant=$isMerchant"
    this.navigate(route)
}

fun NavGraphBuilder.privateChatNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.PRIVATE_CHAT}/{otherPartyName}?" +
                "conversationId={conversationId}&" +
                "merchantId={merchantId}&" +
                "isMerchant={isMerchant}",
        arguments = listOf(
            navArgument("otherPartyName") { type = NavType.StringType },
            navArgument("conversationId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument("merchantId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument("isMerchant") { 
                type = NavType.BoolType
                defaultValue = false
            }
        )
    ) { backStackEntry ->
        val otherPartyName = backStackEntry.arguments?.getString("otherPartyName") ?: ""
        val conversationId = backStackEntry.arguments?.getString("conversationId")?.takeIf { it.isNotBlank() }
        val merchantId = backStackEntry.arguments?.getString("merchantId")?.takeIf { it.isNotBlank() }
        val isMerchant = backStackEntry.arguments?.getBoolean("isMerchant") ?: false
        
        PrivateChatRoute(
            conversationId = conversationId,
            merchantId = merchantId,
            otherPartyName = otherPartyName,
            isMerchant = isMerchant,
            navController = navController
        )
    }
}
