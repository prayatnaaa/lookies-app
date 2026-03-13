package com.prayatna.lookiesapp.presentation.user.partnerSubmission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
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

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.onEvent(PartnerSubmissionEvent.KycFileSelected(it))
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is PartnerSubmissionUiState.Error) {
            val errorMessage = (uiState as PartnerSubmissionUiState.Error).message
            snackbarHostState.showSnackbar(message = errorMessage)
            viewModel.onEvent(PartnerSubmissionEvent.DismissError)
        }
    }

    if (uiState is PartnerSubmissionUiState.Success) {
        val response = (uiState as PartnerSubmissionUiState.Success).businessId
        SuccessDialog (
            message = response.message,
            onConfirm = {
                viewModel.onEvent(PartnerSubmissionEvent.DismissError)
                navController.navigateUp()
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Partner Registration", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        PartnerSubmissionContent(
            modifier = Modifier.padding(paddingValues),
            formState = formState,
            isLoading = uiState is PartnerSubmissionUiState.Loading,
            onEvent = viewModel::onEvent,
            onPickFileClick = { launcher.launch("image/*") }
        )
    }
}