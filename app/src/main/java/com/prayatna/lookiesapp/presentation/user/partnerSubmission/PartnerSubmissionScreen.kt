package com.prayatna.lookiesapp.presentation.user.partnerSubmission

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionEvent
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionFormState
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionUiState
import com.prayatna.lookiesapp.utils.Constants

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
        SuccessDialog(
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

@Composable
fun SuccessDialog(
    message: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { /* Prevent dismiss on outside click */ },
        icon = {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "Registration Successful",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = message,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun PartnerSubmissionContent(
    modifier: Modifier = Modifier,
    formState: PartnerSubmissionFormState,
    isLoading: Boolean,
    onEvent: (PartnerSubmissionEvent) -> Unit,
    onPickFileClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        FormSection(title = "Account Preference") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = formState.useLoginEmail,
                    onCheckedChange = { onEvent(PartnerSubmissionEvent.UseLoginEmailChanged(it)) }
                )
                Text(
                    text = "Use current login email for business",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            AnimatedVisibility(
                visible = !formState.useLoginEmail,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
            ) {
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.businessEmail,
                    onValueChange = { onEvent(PartnerSubmissionEvent.BusinessEmailChanged(it)) },
                    label = { Text("Business Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
            }
        }

        FormSection(title = "Business Details") {
            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.legalName,
                onValueChange = { onEvent(PartnerSubmissionEvent.LegalNameChanged(it)) },
                label = { Text("Legal Name (Ltd / LLC)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.tradingName,
                onValueChange = { onEvent(PartnerSubmissionEvent.TradingNameChanged(it)) },
                label = { Text("Store / Brand Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.description,
                onValueChange = { onEvent(PartnerSubmissionEvent.DescriptionChanged(it)) },
                label = { Text("Short Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
        }

        FormSection(title = "Address & Contact") {
            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.streetLine1,
                onValueChange = { onEvent(PartnerSubmissionEvent.AddressChanged(it)) },
                label = { Text("Street Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.city,
                    onValueChange = { onEvent(PartnerSubmissionEvent.CityChanged(it)) },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.province,
                    onValueChange = { onEvent(PartnerSubmissionEvent.ProvinceChanged(it)) },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.postalCode,
                onValueChange = { onEvent(PartnerSubmissionEvent.PostalCodeChanged(it)) },
                label = { Text("Postal Code") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )
        }

        FormSection(title = "Owner Information") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.ownerFirstName,
                    onValueChange = { onEvent(PartnerSubmissionEvent.OwnerFirstNameChanged(it)) },
                    label = { Text("First Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.ownerLastName,
                    onValueChange = { onEvent(PartnerSubmissionEvent.OwnerLastNameChanged(it)) },
                    label = { Text("Last Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.ownerEmail,
                onValueChange = { onEvent(PartnerSubmissionEvent.OwnerEmailChanged(it)) },
                label = { Text("Owner Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.ownerPhone,
                onValueChange = { onEvent(PartnerSubmissionEvent.OwnerPhoneChanged(it)) },
                label = { Text("Owner Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                ),
                singleLine = true
            )
        }

        FormSection(title = "Legal Documents (KYC)") {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Upload ID Card / Business License / Registration Number",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "File: ${formState.kycFileName}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp),
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = onPickFileClick) {
                        Text("Change File")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onEvent(PartnerSubmissionEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading,
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Register Business",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FormSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}