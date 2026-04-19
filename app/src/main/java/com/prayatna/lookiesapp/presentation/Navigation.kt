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
import com.prayatna.lookiesapp.presentation.artistDashboard.ArtistDashboardScreen
import com.prayatna.lookiesapp.presentation.checkout.state.checkoutNavigation
import com.prayatna.lookiesapp.presentation.event.detailevent.DetailEventScreen
import com.prayatna.lookiesapp.presentation.event.eventlist.EventListScreen
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingDetail.EventPaintingDetailScreen
import com.prayatna.lookiesapp.presentation.eventPainting.eventPaintingList.EventPaintingListScreen
import com.prayatna.lookiesapp.presentation.forum.ForumRoute
import com.prayatna.lookiesapp.presentation.forumchannellist.ForumChannelListRoute
import com.prayatna.lookiesapp.presentation.forumlist.ForumListRoute
import com.prayatna.lookiesapp.presentation.loading.MainLoadingScreen
import com.prayatna.lookiesapp.presentation.login.LoginScreen
import com.prayatna.lookiesapp.presentation.login.LoginViewModel
import com.prayatna.lookiesapp.presentation.login.state.AuthState
import com.prayatna.lookiesapp.presentation.main.MainScreen
import com.prayatna.lookiesapp.presentation.main.search.SearchScreen
import com.prayatna.lookiesapp.presentation.merchant.merchantMember.MerchantMemberListScreen
import com.prayatna.lookiesapp.presentation.painting.detailpainting.DetailPaintingScreen
import com.prayatna.lookiesapp.presentation.painting.paintinglist.PersonalPaintingListScreen
import com.prayatna.lookiesapp.presentation.painting.participantPaintingList.ParticipantPaintingListScreen
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.UploadPaintingScreen
import com.prayatna.lookiesapp.presentation.partner.createEvent.CreateEventScreen
import com.prayatna.lookiesapp.presentation.partner.detailpartner.DetailPartnerScreen
import com.prayatna.lookiesapp.presentation.partner.editEvent.EditEventScreen
import com.prayatna.lookiesapp.presentation.partner.main.PartnerMainScreen
import com.prayatna.lookiesapp.presentation.partner.manageEvent.PartnerManageEventScreen
import com.prayatna.lookiesapp.presentation.partner.participantList.ParticipantListScreen
import com.prayatna.lookiesapp.presentation.partner.partnerlist.PartnerListScreen
import com.prayatna.lookiesapp.presentation.partner.selfEventList.SelfEventListScreen
import com.prayatna.lookiesapp.presentation.payment.qrPayment.QrPaymentScreen
import com.prayatna.lookiesapp.presentation.register.RegisterScreen
import com.prayatna.lookiesapp.presentation.registerEvent.RegisterEventScreen
import com.prayatna.lookiesapp.presentation.shipment.shipmentListNavigation
import com.prayatna.lookiesapp.presentation.shipmentDetail.shipmentDetailNavigation
import com.prayatna.lookiesapp.presentation.transaction.detailTransaction.DetailTransactionScreen
import com.prayatna.lookiesapp.presentation.transaction.payment.PaymentScreen
import com.prayatna.lookiesapp.presentation.user.artistSubmission.artistSubmissionNavigation
import com.prayatna.lookiesapp.presentation.user.createUserAddress.createUserAddressNavigation
import com.prayatna.lookiesapp.presentation.user.editprofile.EditProfileScreen
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.PartnerSubmissionScreen
import com.prayatna.lookiesapp.presentation.refund.refundNavigation
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MainNavigation(viewModel: LoginViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.observeSession()
    }

    LaunchedEffect(authState) {

        val destination = when (val state = authState) {

            AuthState.Loading ->
                NavigationRoutes.MAIN_LOADING

            AuthState.Unauthenticated ->
                NavigationRoutes.LOGIN

            is AuthState.Authenticated -> {
                Log.d("SignIn", "role: " + state.role)
                when (state.role) {
                    "admin" -> NavigationRoutes.ADMIN_MAIN
                    "partner" -> NavigationRoutes.MAIN
                    "user", "artist" -> NavigationRoutes.MAIN
                    else -> NavigationRoutes.LOGIN
                }
            }

            is AuthState.Error -> {
                Log.d("SignIn", "Error: ${state.message}")
                NavigationRoutes.LOGIN
            }
        }

        navController.navigate(destination) {
            popUpTo(navController.graph.startDestinationId) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.MAIN_LOADING
    ) {

        shipmentListNavigation(navController = navController)
        composable(
            route = "${NavigationRoutes.QRIS_PAYMENT}/{orderId}/{merchantId}/{amount}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("merchantId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val merchantId = backStackEntry.arguments?.getString("merchantId") ?: ""
            val amount = backStackEntry.arguments?.getLong("amount") ?: 0L

            QrPaymentScreen(
                orderId = orderId,
                merchantId = merchantId,
                amount = amount,
                navController = navController
            )
        }

        composable(
            route = "${NavigationRoutes.PAYMENT}/{orderId}/{merchantId}/{amount}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.StringType },
                navArgument("merchantId") { type = NavType.StringType },
                navArgument("amount") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
            val merchantId = backStackEntry.arguments?.getString("merchantId") ?: ""
            val amount = backStackEntry.arguments?.getFloat("amount")?.toDouble() ?: 0.0

            PaymentScreen(
                orderId = orderId,
                merchantId = merchantId,
                amount = amount,
                onPaymentSuccess = {
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
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

        composable(
            route = NavigationRoutes.MERCHANT_MEMBER_LIST
        ) {
            MerchantMemberListScreen(navController = navController)
        }

        checkoutNavigation(navController = navController)

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
        composable(
            route = "${NavigationRoutes.SELF_EVENT_LIST}/{businessId}",
            arguments = listOf(navArgument("businessId") { type = NavType.StringType })
        ) {
            backStackEntry ->
            backStackEntry.arguments?.getString("businessId")?.let {
                SelfEventListScreen(navController = navController)
            }
        }
        composable(NavigationRoutes.LOGIN) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(NavigationRoutes.REGISTER) {
            RegisterScreen(navController = navController)
        }
        composable(
            route = "${NavigationRoutes.CREATE_EVENT}/{businessId}",
            arguments = listOf(navArgument("businessId") { type = NavType.StringType })
            ) {
            backStackEntry ->
            backStackEntry.arguments?.getString("businessId")?.let {
                CreateEventScreen(navController = navController)
            }
        }
        composable(
            route = "${NavigationRoutes.REGISTER_EVENT}/{eventId}/{maxPaintingPerArtist}/{fee}/{merchantId}",
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType },
                navArgument("maxPaintingPerArtist") { type = NavType.IntType },
                navArgument("fee") { type = NavType.StringType },
                navArgument("merchantId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val eventId = backStackEntry.arguments?.getInt("eventId")
            val maxPaintingPerArtist =
                backStackEntry.arguments?.getInt("maxPaintingPerArtist")
            val fee = backStackEntry.arguments?.getString("fee")?.toDoubleOrNull()
            val merchantId = backStackEntry.arguments?.getString("merchantId")


            if (eventId != null && maxPaintingPerArtist != null) {
                RegisterEventScreen(
                    navController = navController,
                    eventId = eventId,
                    maxPaintingPerArtist = maxPaintingPerArtist,
                    fee = fee!!,
                    merchantId = merchantId!!
                )
            }
        }

        composable(
            route = "${NavigationRoutes.EVENT_PAINTING_LIST}/{eventId}?eventType={eventType}&businessId={businessId}",
            arguments = listOf(
                navArgument("eventId") { type = NavType.StringType },
                navArgument("eventType") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("businessId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("eventId")?.let { participantId ->
                val eventType = backStackEntry.arguments?.getString("eventType")
                val businessId = backStackEntry.arguments?.getString("businessId")

                ParticipantPaintingListScreen(
                    eventId = participantId,
                    navController = navController,
                    eventType = eventType,
                    businessId = businessId
                )
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
            route = "${NavigationRoutes.PARTNER_MAIN_SCREEN}/{businessId}",
            arguments = listOf(navArgument("businessId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("businessId")?.let { businessId ->
                PartnerMainScreen(navHostController = navController, businessId = businessId)
            }
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
            route = "${NavigationRoutes.UPLOAD_PAINTING}/{businessId}",
            arguments = listOf(
                navArgument("businessId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) {
            UploadPaintingScreen(navController)
        }
        composable(
            route = "${NavigationRoutes.PARTNER_APPLICATION}/{merchantType}",
            arguments = listOf(navArgument("merchantType") { type = NavType.StringType })
        ) {
            PartnerSubmissionScreen(navController = navController)
        }
        artistSubmissionNavigation(navController)
        composable(
            route = "${NavigationRoutes.PERSONAL_PAINTING}/{merchantId}",
            arguments = listOf(navArgument("merchantId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("merchantId")?.let { artistId ->
                PersonalPaintingListScreen(artistId = artistId, navController = navController)
            }
        }
        composable(
            route = "${NavigationRoutes.DETAIL_TRANSACTION}/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("orderId")?.let { orderId ->
                DetailTransactionScreen(navController = navController, orderId = orderId)
            }
        }
        createUserAddressNavigation(navController = navController)
        
        composable(NavigationRoutes.FORUM_LIST) {
            ForumListRoute(navController)
        }

        shipmentDetailNavigation(navController)
        
        composable(
            route = "${NavigationRoutes.FORUM_CHANNEL_LIST}/{forumId}",
            arguments = listOf(navArgument("forumId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("forumId")?.let { forumId ->
                ForumChannelListRoute(
                    forumId = forumId,
                    onNavigateToChat = { channelId ->
                        navController.navigate("${NavigationRoutes.FORUM_MESSAGES}/$channelId")
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
        
        composable(
            route = "${NavigationRoutes.FORUM_MESSAGES}/{channelId}",
            arguments = listOf(navArgument("channelId") { type = NavType.StringType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getString("channelId")?.let { channelId ->
                ForumRoute(
                    channelId = channelId,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }

        refundNavigation(navController)
    }
}