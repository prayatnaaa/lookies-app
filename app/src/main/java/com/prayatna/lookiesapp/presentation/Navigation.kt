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
import com.prayatna.lookiesapp.presentation.login.LoginScreen
import com.prayatna.lookiesapp.presentation.login.LoginViewModel
import com.prayatna.lookiesapp.presentation.main.MainScreen
import com.prayatna.lookiesapp.presentation.main.MainViewModel
import com.prayatna.lookiesapp.presentation.register.RegisterScreen
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MainNavigation(
    viewModel: LoginViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val sessionStatus by viewModel.sessionStatus.collectAsStateWithLifecycle()
    val role by mainViewModel.role.collectAsStateWithLifecycle()

    LaunchedEffect (Unit) {
        viewModel.isSessionActive()
        mainViewModel.getRole()
    }

    if (sessionStatus == DataResult.Idle || sessionStatus == DataResult.Loading || role == "") {
        CircularLoading()
        return
    }

    val startDestination = when (sessionStatus) {
        is DataResult.Error -> NavigationRoutes.LOGIN
        is DataResult.Success -> when (role) {
            "admin" -> NavigationRoutes.ADMIN_MAIN
            else -> NavigationRoutes.MAIN
        }
        else -> NavigationRoutes.LOGIN
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(NavigationRoutes.MAIN) {
            MainScreen(navHostController = navController, role = role)
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
        composable(
            "${NavigationRoutes.DETAIL_EVENT}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            DetailEventScreen(navController = navController, eventId = eventId.toString())
        }
        eventNavGraph(navController = navController)
    }
}