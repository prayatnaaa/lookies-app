package com.prayatna.lookiesapp.presentation.user.partnerapplication.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationFooter
import com.prayatna.lookiesapp.presentation.components.user.partnerapplication.PartnerApplicationSection
import com.prayatna.lookiesapp.presentation.user.partnerapplication.PartnerApplicationViewModel
import com.prayatna.lookiesapp.presentation.user.partnerapplication.event.PartnerApplicationEvent
import com.prayatna.lookiesapp.ui.theme.LightGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerProfileFormScreen(
    navController: NavController,
    viewModel: PartnerApplicationViewModel
) {

    val formState by viewModel.form.collectAsStateWithLifecycle()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    val logoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) viewModel.onEvent(PartnerApplicationEvent.PartnerLogoChanged(uri))
    }

    val ktpPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) viewModel.onEvent(PartnerApplicationEvent.KtpFileChanged(uri))
    }

    val businessPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) viewModel.onEvent(PartnerApplicationEvent.BusinessLicenseFileChanged(uri))
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
        modifier = Modifier
            .fillMaxSize(),
        containerColor = PureWhite,
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
               PartnerApplicationSection(
                   form = formState,
                   onEvent = viewModel::onEvent,
                   onPickLogo = { logoPicker.launch("image/*") },
                   onPickKtp = { ktpPicker.launch("*/*") },
                   onPickBusinessLicense = { businessPicker.launch("*/*") }
               )
           }
        },
        topBar = {
            Column (modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center){
                Text(
                    text = "Partner profile",
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                )
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(color = LightGrey, thickness = 1.dp)
            }
        },
        bottomBar = {
            PartnerApplicationFooter(
                route = NavigationRoutes.PARTNER_APPLICATION,
                onBackButton = {
                    navController.popBackStack()
                },
                isButtonEnable =
                    formState.partnerName.isNotEmpty() &&
                    formState.partnerType.isNotEmpty() &&
                    formState.partnerLogo != null &&
                    formState.locName.isNotEmpty() &&
                    formState.locUrl.isNotEmpty() &&
                    formState.partnerPortfolioLink.isNotEmpty() &&
                    formState.partnerName.isNotBlank(),
                onProfileButton = {},
                onLocationButton = {},
                onSubmissionButton = {
                    viewModel.onEvent(PartnerApplicationEvent.Submit)
                }
            )
        }
    )
}