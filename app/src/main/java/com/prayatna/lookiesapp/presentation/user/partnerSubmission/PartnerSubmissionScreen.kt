package com.prayatna.lookiesapp.presentation.user.partnerSubmission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel
import com.prayatna.lookiesapp.presentation.components.registerBusiness.PartnerSubmissionContent
import com.prayatna.lookiesapp.presentation.components.registerBusiness.SuccessDialog
import com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.navigateToSelectPayoutChannel
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

    var currentPickingType by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            currentPickingType?.let { type ->
                viewModel.onEvent(PartnerSubmissionEvent.KycFileSelected(type, it))
            }
        }
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is PartnerSubmissionUiState.Error -> {
                snackbarHostState.showSnackbar((uiState as PartnerSubmissionUiState.Error).message)
                viewModel.onEvent(PartnerSubmissionEvent.DismissError)
            }
            else -> Unit
        }
    }

    val selectedCode = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_payout_channel_code")
    val selectedName = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selected_payout_channel_name")

    LaunchedEffect(selectedCode, selectedName) {
        if (selectedCode != null && selectedName != null) {
            viewModel.onEvent(PartnerSubmissionEvent.BankCodeChanged(selectedCode))
            viewModel.onEvent(PartnerSubmissionEvent.BankNameChanged(selectedName))
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_payout_channel_code")
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_payout_channel_name")
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
            TopAppBar(
                title = {
                    Text(
                        text = "Partner Application",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            PartnerSubmissionContent(
                paddingValues = innerPadding,
                formState = formState,
                isLoading = uiState is PartnerSubmissionUiState.Loading,
                onEvent = viewModel::onEvent,
                onPickFileClick = { type ->
                    currentPickingType = type
                    launcher.launch("*/*")
                },
                onSelectBankClick = {
                    navController.navigateToSelectPayoutChannel()
                }
            )
        }
    )
}
