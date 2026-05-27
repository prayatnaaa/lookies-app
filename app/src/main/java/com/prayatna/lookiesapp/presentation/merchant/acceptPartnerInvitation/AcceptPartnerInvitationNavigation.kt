package com.prayatna.lookiesapp.presentation.merchant.acceptPartnerInvitation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.acceptPartnerInvitationNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.ACCEPT_PARTNER_INVITATION}/{merchantAccountId}",
        arguments = listOf(
            navArgument("merchantAccountId") { type = NavType.StringType }
        )
    ) {
        AcceptPartnerInvitationRoute(navController = navController)
    }
}

fun NavController.navigateToAcceptPartnerInvitation(merchantAccountId: String) {
    navigate("${NavigationRoutes.ACCEPT_PARTNER_INVITATION}/$merchantAccountId")
}
