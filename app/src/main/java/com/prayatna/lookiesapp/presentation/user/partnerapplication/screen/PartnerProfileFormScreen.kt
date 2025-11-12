package com.prayatna.lookiesapp.presentation.user.partnerapplication.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationCard
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationFooter
import com.prayatna.lookiesapp.presentation.user.partnerapplication.PartnerApplicationViewModel
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerProfileFormScreen(
    navController: NavController,
    viewModel: PartnerApplicationViewModel
) {

    val formState by viewModel.addPartnerSubmissionFormState.collectAsStateWithLifecycle()
    val uiState by viewModel.addPartnerSubmissionState.collectAsStateWithLifecycle()
    val imagePickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onImageLogoChange(it)
        }
    }
    val snackBarHostState: SnackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        if (uiState.success != null) {
            navController.navigate(NavigationRoutes.MAIN) {
                popUpTo(NavigationRoutes.MAIN) {
                    inclusive = true
                }
            }
        }

        if (uiState.error != null) {
            val errMessage = uiState.error
            errMessage?.let {
                snackBarHostState.showSnackbar(message = it,
                    withDismissAction = true)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { innerPadding ->
            if (uiState.isLoading) {
                CircularLoading()
            }
           Column(
               modifier = Modifier
                   .padding(innerPadding)
                   .fillMaxSize()
           ) {
               PartnerApplicationCard(
                   value = formState,
                   onPartnerChange = viewModel::onPartnerNameChanged,
                   onTypeChange = viewModel::onPartnerTypeChange,
                   onPortfolioChange = viewModel::onPortofolioChange,
                   onLogoClick = {
                       imagePickLauncher.launch("image/*")
                   }
               )
           }
        },
        bottomBar = {
            PartnerApplicationFooter(
                route = NavigationRoutes.PARTNER_APPLICATION,
                onBackButton = {
                    navController.popBackStack()
                },
                onProfileButton = {},
                onLocationButton = {},
                onSubmissionButton = {
                    viewModel.submitPartnerSubmission()
                }
            )
        }
    )
}