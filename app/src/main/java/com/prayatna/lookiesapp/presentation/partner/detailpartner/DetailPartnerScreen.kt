package com.prayatna.lookiesapp.presentation.partner.detailpartner

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.admin.AdminPartnerButtons
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.partner.PartnerProfileSection
import com.prayatna.lookiesapp.presentation.error.ErrorScreen

@Composable
fun DetailPartnerScreen(
    partnerId: String,
    viewModel: DetailPartnerViewModel = hiltViewModel(),
    onPortfolioClick: (String) -> Unit = {},
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val adminState by viewModel.adminState.collectAsStateWithLifecycle()
    val role by viewModel.roleState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(partnerId) {
        viewModel.loadPartnerDetail(partnerId)
    }

    LaunchedEffect(adminState.success) {
        adminState.success?.let {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("shouldRefresh", true)

            navController.navigateUp()
        }
    }

    LaunchedEffect(adminState.error) {
        adminState.error?.let {
            snackbarHostState.showSnackbar(it, withDismissAction = true)
        }
    }

    when {
        state.isLoading -> CircularLoading()

        state.error != null -> ErrorScreen(
            message = state.error!!,
            onRetry = { viewModel.loadPartnerDetail(partnerId) }
        )

        else -> {
            state.data?.let { data ->

                val showAdminButtons =
                    role == "admin" && data.status == "pending"

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = { BackTopBar(navController = navController) },
                    bottomBar = {
                        if (showAdminButtons) {
                            AdminPartnerButtons(
                                onApprovedButtonClick = { viewModel.approvePartner(data.id) },
                                onRejectButtonClick = { viewModel.rejectPartner(data.id) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        PartnerProfileSection(
                            data = data,
                            onPortofolioClick = {
                                data.websiteUrl?.let(onPortfolioClick)
                            },
                            isAdmin = showAdminButtons
                        )
                    }
                }
            }
        }
    }
}
