package com.prayatna.lookiesapp.presentation

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
import com.prayatna.lookiesapp.presentation.artistDashboard.ArtistDashboardScreen
import com.prayatna.lookiesapp.presentation.checkout.CheckoutScreen
import com.prayatna.lookiesapp.presentation.painting.paintinglist.PersonalPaintingListScreen
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.UploadPaintingScreen
import com.prayatna.lookiesapp.presentation.event.detailevent.DetailEventScreen
import com.prayatna.lookiesapp.presentation.event.eventlist.EventListScreen
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.EventPaintingDetailScreen
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.EventPaintingListScreen
import com.prayatna.lookiesapp.presentation.user.editprofile.EditProfileScreen
import com.prayatna.lookiesapp.presentation.loading.MainLoadingScreen
import com.prayatna.lookiesapp.presentation.login.LoginScreen
import com.prayatna.lookiesapp.presentation.login.LoginViewModel
import com.prayatna.lookiesapp.presentation.login.state.AuthState
import com.prayatna.lookiesapp.presentation.main.MainScreen
import com.prayatna.lookiesapp.presentation.main.search.SearchScreen
import com.prayatna.lookiesapp.presentation.painting.detailpainting.DetailPaintingScreen
import com.prayatna.lookiesapp.presentation.painting.participantPaintingList.ParticipantPaintingListScreen
import com.prayatna.lookiesapp.presentation.partner.createEvent.CreateEventScreen
import com.prayatna.lookiesapp.presentation.partner.detailpartner.DetailPartnerScreen
import com.prayatna.lookiesapp.presentation.partner.editEvent.EditEventScreen
import com.prayatna.lookiesapp.presentation.partner.main.PartnerMainScreen
import com.prayatna.lookiesapp.presentation.partner.manageEvent.PartnerManageEventScreen
import com.prayatna.lookiesapp.presentation.partner.participantList.ParticipantListScreen
import com.prayatna.lookiesapp.presentation.partner.partnerlist.PartnerListScreen
import com.prayatna.lookiesapp.presentation.partner.selfEventList.SelfEventListScreen
import com.prayatna.lookiesapp.presentation.register.RegisterScreen
import com.prayatna.lookiesapp.presentation.registerEvent.RegisterEventScreen
import com.prayatna.lookiesapp.presentation.user.partnerapplication.partnerApplicationNavGraph
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MainNavigation(viewModel: LoginViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.isSessionActive()
    }

    LaunchedEffect(authState) {

        val destination = when (val state = authState) {

            AuthState.Loading ->
                NavigationRoutes.MAIN_LOADING

            AuthState.Unauthenticated ->
                NavigationRoutes.LOGIN

            is AuthState.Authenticated -> {
                when (state.role) {
                    "admin" -> NavigationRoutes.ADMIN_MAIN
                    "partner" -> NavigationRoutes.PARTNER_MAIN_SCREEN
                    "user", "artist" -> NavigationRoutes.MAIN
                    else -> NavigationRoutes.LOGIN
                }
            }

            is AuthState.Error ->
                NavigationRoutes.LOGIN
        }

        val currentRoute =
            navController.currentBackStackEntry?.destination?.route
        if (currentRoute == destination) return@LaunchedEffect

        navController.navigate(destination) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.MAIN_LOADING
    ) {
        composable(NavigationRoutes.MAIN_LOADING) {
            MainLoadingScreen()
        }
        composable(
            route = "${NavigationRoutes.DETAIL_EVENT_PAINTING}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("id")?.let { id ->
                EventPaintingDetailScreen(navController = navController, id = id)
            }

        }
        composable(
            route = "${NavigationRoutes.PARTNER_EVENT_MANAGE}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.let { eventId ->
                PartnerManageEventScreen(
                    navController = navController,
                    eventId = eventId
                )
            }
        }
        composable("${NavigationRoutes.CHECKOUT}/{type}/{id}",
                arguments = listOf(
                navArgument("type") { type = NavType.StringType },
            navArgument("id") { type = NavType.StringType }
        )) { backStackEntry ->
            val type = backStackEntry.arguments?.getString("type") ?: ""
            val id = backStackEntry.arguments?.getString("id") ?: ""
            CheckoutScreen(
                type = type,
                itemId = id,
                navController = navController
            )

        }
        composable(NavigationRoutes.SEARCH) {
            SearchScreen(navController = navController)
        }
        composable(NavigationRoutes.MAIN) { backStackEntry ->
            val rootEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavigationRoutes.MAIN)
            }
            val sharedViewModel: SharedViewModel = hiltViewModel(rootEntry)
            MainScreen(navHostController = navController, sharedViewModel = sharedViewModel)
        }
        composable(NavigationRoutes.EVENT_LIST) {
            EventListScreen(navController = navController)
        }
        composable(NavigationRoutes.SELF_EVENT_LIST) {
            SelfEventListScreen(navController = navController)
        }
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(NavigationRoutes.REGISTER) {
            RegisterScreen(navController = navController)
        }
        composable(NavigationRoutes.CREATE_EVENT) {
            CreateEventScreen(navController = navController)
        }
        composable(
            route = "${NavigationRoutes.REGISTER_EVENT}/{eventId}/{maxPaintingPerArtist}",
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType },
                navArgument("maxPaintingPerArtist") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val eventId = backStackEntry.arguments?.getInt("eventId")
            val maxPaintingPerArtist =
                backStackEntry.arguments?.getInt("maxPaintingPerArtist")

            if (eventId != null && maxPaintingPerArtist != null) {
                RegisterEventScreen(
                    navController = navController,
                    eventId = eventId,
                    maxPaintingPerArtist = maxPaintingPerArtist
                )
            }
        }

        composable(
            route = "${NavigationRoutes.EVENT_PAINTING_LIST}/{participantId}",
            arguments = listOf(navArgument("participantId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("participantId")?.let { participantId ->
                ParticipantPaintingListScreen(participantId = participantId, navController = navController)
            }
        }
        composable(
            route = "${NavigationRoutes.EDIT_PROFILE}?isPartnerSignup={isPartnerSignup}",
            arguments = listOf(navArgument("isPartnerSignup") { type = NavType.BoolType })
        ) { navBackStackEntry ->
            val rootEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(NavigationRoutes.MAIN)
            }
            val isPartnerSignup =
                navBackStackEntry.arguments?.getBoolean("isPartnerSignup") ?: false
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
        composable(
            route = NavigationRoutes.PARTNER_LIST
        ) {
            PartnerListScreen(navController = navController)
        }
        composable(
            route = NavigationRoutes.PARTNER_MAIN_SCREEN
        ) {
            PartnerMainScreen(navHostController = navController)
        }
        composable(
            route = "${NavigationRoutes.PARTICIPANT_LIST}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.let { eventId ->
                ParticipantListScreen(eventId = eventId, navController = navController)
            }
        }
        composable(route = NavigationRoutes.EVENT_PAINTING_LIST) {
            EventPaintingListScreen(navController = navController)
        }
        composable(
            route = NavigationRoutes.ARTIST_DASHBOARD
        ) {
            ArtistDashboardScreen(navController = navController)
        }
        composable(
            route = "${NavigationRoutes.DETAIL_PARTNER}/{partnerId}",
            arguments = listOf(navArgument("partnerId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("partnerId")?.let { partnerId ->
                DetailPartnerScreen(navController = navController, partnerId = partnerId)
            }
        }
        composable(
            route = "${NavigationRoutes.DETAIL_EVENT}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) {backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.let { eventId ->
                DetailEventScreen(navController = navController, eventId = eventId)
            }
        }
        composable(
            route = "${NavigationRoutes.EDIT_EVENT}/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) {backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.let { eventId ->
                EditEventScreen(navController = navController, eventId = eventId)
            }

        }
        composable(
            route = "${NavigationRoutes.DETAIL_PAINTING}/{paintingId}",
            arguments = listOf(navArgument("paintingId") {
                type = NavType.IntType
            })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("paintingId")?.let { id ->
                DetailPaintingScreen(paintingId = id, navController = navController)
            }
        }
        composable(
            route = NavigationRoutes.UPLOAD_PAINTING
        ) {
            UploadPaintingScreen(navController = navController)
        }
        composable(
            route = "${NavigationRoutes.PERSONAL_PAINTING}/{artistId}",
            arguments = listOf(navArgument("artistId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("artistId")?.let { artistId ->
                PersonalPaintingListScreen(artistId = artistId, navController = navController)
            }
            partnerApplicationNavGraph(navController = navController)
        }
    }
}