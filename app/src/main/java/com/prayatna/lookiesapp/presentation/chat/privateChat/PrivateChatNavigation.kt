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
    isMerchant: Boolean = false,
    metadataType: String? = null,
    metadataId: String? = null,
    metadataImageUrl: String? = null,
    metadataTitle: String? = null
) {
    val route = "${NavigationRoutes.PRIVATE_CHAT}/$partyName?" +
            "conversationId=${conversationId ?: ""}&" +
            "merchantId=${merchantId ?: ""}&" +
            "isMerchant=$isMerchant&" +
            "metadataType=${metadataType ?: ""}&" +
            "metadataId=${metadataId ?: ""}&" +
            "metadataImageUrl=${metadataImageUrl ?: ""}&" +
            "metadataTitle=${metadataTitle ?: ""}"
    this.navigate(route)
}

fun NavGraphBuilder.privateChatNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.PRIVATE_CHAT}/{otherPartyName}?" +
                "conversationId={conversationId}&" +
                "merchantId={merchantId}&" +
                "isMerchant={isMerchant}&" +
                "metadataType={metadataType}&" +
                "metadataId={metadataId}&" +
                "metadataImageUrl={metadataImageUrl}&" +
                "metadataTitle={metadataTitle}",
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
            },
            navArgument("metadataType") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument("metadataId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument("metadataImageUrl") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            },
            navArgument("metadataTitle") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }
        )
    ) { backStackEntry ->
        val otherPartyName = backStackEntry.arguments?.getString("otherPartyName") ?: ""
        val conversationId = backStackEntry.arguments?.getString("conversationId")?.takeIf { it.isNotBlank() }
        val merchantId = backStackEntry.arguments?.getString("merchantId")?.takeIf { it.isNotBlank() }
        val isMerchant = backStackEntry.arguments?.getBoolean("isMerchant") ?: false
        
        val metadataType = backStackEntry.arguments?.getString("metadataType")?.takeIf { it.isNotBlank() }
        val metadataId = backStackEntry.arguments?.getString("metadataId")?.takeIf { it.isNotBlank() }
        val metadataImageUrl = backStackEntry.arguments?.getString("metadataImageUrl")?.takeIf { it.isNotBlank() }
        val metadataTitle = backStackEntry.arguments?.getString("metadataTitle")?.takeIf { it.isNotBlank() }
        
        PrivateChatRoute(
            conversationId = conversationId,
            merchantId = merchantId,
            otherPartyName = otherPartyName,
            isMerchant = isMerchant,
            metadataType = metadataType,
            metadataId = metadataId,
            metadataImageUrl = metadataImageUrl,
            metadataTitle = metadataTitle,
            navController = navController
        )
    }
}
