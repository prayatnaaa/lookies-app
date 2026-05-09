package com.prayatna.lookiesapp.presentation.partner.merchantMemberByMerchantId

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.merchantMemberByMerchantIdNavigation(navController: NavController) {
    composable(
        route = "${NavigationRoutes.MERCHANT_MEMBER_BY_MERCHANT_ID_LIST}/{businessId}",
        arguments = listOf(
            navArgument("businessId") { type = NavType.StringType }
        )
    ) { 
        MerchantMemberByMerchantIdRoute(
            navController = navController
        )
    }
}
