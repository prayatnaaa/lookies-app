package com.prayatna.lookiesapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    when {
        uiState.isLoading || sessionStatus == DataResult.Idle || sessionStatus is DataResult.Loading -> {
            CircularLoading()
        }
        uiState.errorMessage != null -> {
            ErrorScreen(message = uiState.errorMessage ?: "Unknown error") {
                mainViewModel.retry()
            }
        }
        uiState.user != null -> {
            val user = uiState.user
            user?.let {
                val startDestination = determineStartDestination(sessionStatus, user)
                AppNavGraph(
                    navController = navController,
                    startDestination = startDestination,
                    user = user
                )
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
fun AppNavGraph(navController: NavHostController, startDestination: String, user: User) {
    NavHost(navController = navController, startDestination = startDestination) {

        composable(NavigationRoutes.MAIN) {
            MainScreen(navHostController = navController, user = user)
        }

        composable(NavigationRoutes.LOGIN) {
            LoginScreen(navController = navController, user = user)
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
