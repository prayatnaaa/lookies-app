package com.prayatna.lookiesapp.presentation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.prayatna.lookiesapp.presentation.admin.event.AdminEventScreen
import com.prayatna.lookiesapp.presentation.admin.main.AdminMainScreen
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.user.editprofile.EditProfileScreen
import com.prayatna.lookiesapp.presentation.event.addevent.eventNavGraph
import com.prayatna.lookiesapp.presentation.event.detailevent.DetailEventScreen
import com.prayatna.lookiesapp.presentation.event.eventlist.EventListScreen
import com.prayatna.lookiesapp.presentation.loading.MainLoadingScreen
import com.prayatna.lookiesapp.presentation.login.LoginScreen
import com.prayatna.lookiesapp.presentation.login.LoginViewModel
import com.prayatna.lookiesapp.presentation.main.MainScreen
import com.prayatna.lookiesapp.presentation.partner.detailpartner.DetailPartnerScreen
import com.prayatna.lookiesapp.presentation.partner.partnerlist.PartnerListScreen
import com.prayatna.lookiesapp.presentation.payment.addpayment.AddPaymentScreen
import com.prayatna.lookiesapp.presentation.register.RegisterScreen
import com.prayatna.lookiesapp.presentation.user.partnerapplication.partnerApplicationNavGraph
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MainNavigation(viewModel: LoginViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val sessionStatus by viewModel.sessionStatus.collectAsStateWithLifecycle()
    val roleState by viewModel.roleState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.isSessionActive()
    }

    if (sessionStatus == DataResult.Idle || sessionStatus == DataResult.Loading) {
        CircularLoading()
    }

    val startDestination = when (sessionStatus) {
        is DataResult.Error -> NavigationRoutes.LOGIN
        is DataResult.Success -> {
            if ((sessionStatus as DataResult.Success).data) {
                if (roleState == "admin") {
                    NavigationRoutes.ADMIN_MAIN
                } else NavigationRoutes.MAIN
            } else {
                NavigationRoutes.LOGIN
            }
        }
        else -> NavigationRoutes.MAIN_LOADING
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(NavigationRoutes.MAIN_LOADING) {
            MainLoadingScreen()
        }
        composable(NavigationRoutes.MAIN) { backStackEntry ->
            val rootEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavigationRoutes.MAIN)
            }
            val sharedViewModel: SharedViewModel = hiltViewModel(rootEntry)
            MainScreen(navHostController = navController, sharedViewModel = sharedViewModel)
        }
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(navController = navController)
        }
        composable(NavigationRoutes.REGISTER) {
            RegisterScreen(navController = navController)
        }
        composable(
            route = "${NavigationRoutes.EDIT_PROFILE}?isPartnerSignup={isPartnerSignup}",
            arguments = listOf(navArgument("isPartnerSignup") { type = NavType.BoolType })
        ) { navBackStackEntry ->
            val rootEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(NavigationRoutes.MAIN)
            }
            val isPartnerSignup = navBackStackEntry.arguments?.getBoolean("isPartnerSignup") ?: false
            val sharedViewModel: SharedViewModel = hiltViewModel(rootEntry)
            EditProfileScreen(
                navController = navController,
                sharedViewModel = sharedViewModel,
                isPartnerSignup = isPartnerSignup
            )
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
        composable(
            route = NavigationRoutes.PARTNER_LIST
        ) {
            PartnerListScreen(navController = navController)
        }
        composable(
            route = "${NavigationRoutes.DETAIL_PARTNER}/{partnerId}",
            arguments = listOf(navArgument("partnerId") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("partnerId")?.let { partnerId ->
                DetailPartnerScreen(navController = navController, partnerId = partnerId)
            }
        }

        eventNavGraph(navController = navController)

        partnerApplicationNavGraph(navController = navController)
    }
}