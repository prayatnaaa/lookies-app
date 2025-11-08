package com.prayatna.lookiesapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.admin.event.AdminEventScreen
import com.prayatna.lookiesapp.presentation.admin.main.AdminMainScreen
import com.prayatna.lookiesapp.presentation.artist.application.ApplicationScreen
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.editprofile.EditProfileScreen
import com.prayatna.lookiesapp.presentation.event.addevent.eventNavGraph
import com.prayatna.lookiesapp.presentation.event.detailevent.DetailEventScreen
import com.prayatna.lookiesapp.presentation.event.eventlist.EventListScreen
import com.prayatna.lookiesapp.presentation.login.LoginScreen
import com.prayatna.lookiesapp.presentation.login.LoginViewModel
import com.prayatna.lookiesapp.presentation.main.MainScreen
import com.prayatna.lookiesapp.presentation.payment.addpayment.AddPaymentScreen
import com.prayatna.lookiesapp.presentation.register.RegisterScreen
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MainNavigation(viewModel: LoginViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val sessionStatus by viewModel.sessionStatus.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.isSessionActive()
    }

    if (sessionStatus == DataResult.Idle || sessionStatus == DataResult.Loading) {
        CircularLoading()
    }

    val startDestination = when (sessionStatus) {
        is DataResult.Error -> NavigationRoutes.LOGIN
        is DataResult.Success -> NavigationRoutes.MAIN
        else -> NavigationRoutes.LOGIN
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationRoutes.MAIN) {
            MainScreen(navHostController = navController)
        }
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(NavigationRoutes.REGISTER) {
            RegisterScreen(navController = navController)
        }
        composable(NavigationRoutes.ARTIST_APPLICATION) {
            ApplicationScreen(navController = navController)
        }
        composable(NavigationRoutes.EDIT_PROFILE) {
            EditProfileScreen(navController = navController)
        }
        composable(NavigationRoutes.ADMIN_MAIN) {
            AdminMainScreen(navController = navController)
        }
        composable(NavigationRoutes.ADMIN_EVENT) {
            AdminEventScreen(navController = navController)
        }
        composable(NavigationRoutes.EVENT_LIST) {
            EventListScreen(navController = navController)
        }
        composable(
            route = "${NavigationRoutes.DETAIL_EVENT}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.let { eventId ->
                DetailEventScreen(navController = navController, eventId = eventId)
            }
        }
        composable(
            route = "${NavigationRoutes.ADD_PAYMENT}/{eventId}?quantity={quantity}",
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType },
                navArgument("quantity") { type = NavType.IntType; defaultValue = 0 }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")!!
            AddPaymentScreen(navController = navController, eventId = eventId)
        }

        eventNavGraph(navController = navController)
    }
}