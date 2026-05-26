package com.prayatna.lookiesapp.presentation.admin.transactionList

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavController.navigateToAdminTransactionList() {
    this.navigate(NavigationRoutes.ADMIN_TRANSACTION_LIST_ROUTE)
}

fun NavGraphBuilder.adminTransactionListNavigation(navController: NavController) {
    composable(NavigationRoutes.ADMIN_TRANSACTION_LIST_ROUTE) {
        AdminTransactionListRoute(navController = navController)
    }
}
