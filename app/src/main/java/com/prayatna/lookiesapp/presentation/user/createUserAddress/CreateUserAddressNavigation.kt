package com.prayatna.lookiesapp.presentation.user.createUserAddress

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.prayatna.lookiesapp.utils.NavigationRoutes

fun NavGraphBuilder.createUserAddressNavigation(
    navController: NavController
) {
    composable(
        route = NavigationRoutes.CREATE_USER_ADDRESS
    ) {
        CreateUserAddressRoute(navController = navController)
    }
}