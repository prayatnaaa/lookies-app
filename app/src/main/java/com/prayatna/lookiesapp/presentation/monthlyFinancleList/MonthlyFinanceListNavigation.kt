package com.prayatna.lookiesapp.presentation.monthlyFinancleList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.monthlyFinanceListScreen(navController: NavController) {
    composable(
        route = "${NavigationRoutes.MONTHLY_FINANCE_LIST_SCREEN}/{businessId}",
        arguments = listOf(navArgument("businessId") { type = NavType.StringType })
    ) {
        MonthlyFinanceListRoute(navController = navController)
    }
}
