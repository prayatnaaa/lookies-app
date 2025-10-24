package com.prayatna.lookiesapp.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.data.model.User
import com.prayatna.lookiesapp.presentation.admin.event.AdminEventScreen
import com.prayatna.lookiesapp.presentation.admin.main.AdminMainScreen
import com.prayatna.lookiesapp.presentation.artist.application.ApplicationScreen
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.editprofile.EditProfileScreen
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.presentation.event.addevent.eventNavGraph
import com.prayatna.lookiesapp.presentation.event.detailevent.DetailEventScreen
import com.prayatna.lookiesapp.presentation.event.eventlist.EventListScreen
import com.prayatna.lookiesapp.presentation.login.LoginScreen
import com.prayatna.lookiesapp.presentation.login.LoginViewModel
import com.prayatna.lookiesapp.presentation.main.MainScreen
import com.prayatna.lookiesapp.presentation.main.MainViewModel
import com.prayatna.lookiesapp.presentation.register.RegisterScreen
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MainNavigation(
    loginViewModel: LoginViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val sessionStatus by loginViewModel.sessionStatus.collectAsStateWithLifecycle()
    val uiState by mainViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        loginViewModel.isSessionActive()
        mainViewModel.getUser()
    }

    AppNavGraph(navController = navController, startDestination = NavigationRoutes.LOGIN, user = uiState.user)

    when {
        uiState.isLoading || sessionStatus == DataResult.Idle || sessionStatus is DataResult.Loading -> {
            CircularLoading()
        }
        uiState.errorMessage != null -> {

            LaunchedEffect(uiState.errorMessage, navController) {
                Log.d("MAIN_NAV", "errorMessage=${uiState.errorMessage}")
                if (uiState.errorMessage == "Failed to get user! user not found.") {
                    Log.d("MAIN_NAV", "Navigating to LOGIN due to missing user")
                    val currentRoute = navController.currentDestination?.route
                    Log.d("MAIN_NAV", "currentRoute before navigate = $currentRoute")
                    if (currentRoute != NavigationRoutes.LOGIN) {
                        navController.navigate(NavigationRoutes.LOGIN) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            }

            if (uiState.errorMessage != "Failed to get user! user not found.") {
                ErrorScreen(message = uiState.errorMessage ?: "Unknown error") {
                    mainViewModel.retry()
                }
            }
        }
        uiState.user != null -> {
            LaunchedEffect(uiState.user, sessionStatus, navController) {
                val startDestination = determineStartDestination(sessionStatus, uiState.user!!)
                val currentRoute = navController.currentDestination?.route
                if (currentRoute != startDestination) {
                    navController.navigate(startDestination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
        }
    }
}

fun determineStartDestination(sessionStatus: DataResult<*>, user: User): String {
    return when (sessionStatus) {
        is DataResult.Idle -> NavigationRoutes.LOGIN
        is DataResult.Loading -> NavigationRoutes.LOGIN
        is DataResult.Error -> NavigationRoutes.LOGIN
        is DataResult.Success -> if (user.role == "admin") NavigationRoutes.ADMIN_MAIN else NavigationRoutes.MAIN
    }
}

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String, user: User?) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(NavigationRoutes.MAIN) {
            user?.let { MainScreen(navHostController = navController, user = it) }
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
            user?.let { if (it.role == "admin") AdminMainScreen(navController = navController) }
        }
        composable(NavigationRoutes.ADMIN_EVENT) {
            AdminEventScreen(navController = navController)
        }
        composable(NavigationRoutes.EVENT_LIST) {
            EventListScreen(navController = navController)
        }
        composable(
            "${NavigationRoutes.DETAIL_EVENT}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.let { eventId ->
                DetailEventScreen(navController = navController, eventId = eventId)
            }
        }
        eventNavGraph(navController = navController)
    }
}
