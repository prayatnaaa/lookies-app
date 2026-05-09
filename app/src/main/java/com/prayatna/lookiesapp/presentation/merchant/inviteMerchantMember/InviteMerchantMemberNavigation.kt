package com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.inviteMerchantMemberNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.INVITE_MERCHANT_MEMBER}/{businessId}",
        arguments = listOf(
            navArgument("businessId") { type = NavType.StringType }
        )
    ) {
        InviteMerchantMemberRoute(navController = navController)
    }
}
