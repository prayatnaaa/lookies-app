package com.prayatna.lookiesapp.presentation.user.partnerSubmission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel
import com.prayatna.lookiesapp.presentation.components.registerBusiness.PartnerSubmissionContent
import com.prayatna.lookiesapp.presentation.components.registerBusiness.SuccessDialog
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionEvent
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartnerSubmissionScreen(
    viewModel: PartnerSubmissionViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    val formState by viewModel.formState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    var payoutChannels by remember { mutableStateOf<List<PayoutChannel>>(emptyList()) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onEvent(PartnerSubmissionEvent.KycFileSelected(it))
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is PartnerSubmissionUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as PartnerSubmissionUiState.Error).message)
                viewModel.onEvent(PartnerSubmissionEvent.DismissError)
            }
            is PartnerSubmissionUiState.MetaLoaded -> {
                payoutChannels = (uiState as PartnerSubmissionUiState.MetaLoaded).payoutChannels
            }
            else -> Unit
        }
    }

    if (uiState is PartnerSubmissionUiState.Success) {
        SuccessDialog(
            message = "Your partner application has been submitted. We'll review it and get back to you.",
            onConfirm = {
                navController.popBackStack()
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Partner Application",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
        content = { innerPadding ->
            PartnerSubmissionContent(
                paddingValues = innerPadding,
                formState = formState,
                isLoading = uiState is PartnerSubmissionUiState.Loading,
                onEvent = viewModel::onEvent,
                onPickFileClick = { launcher.launch("*/*") },
                payoutChannels = payoutChannels
            )
        }
    )
}
