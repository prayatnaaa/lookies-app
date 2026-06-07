package com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.merchant.editMerchantProfile.state.EditMerchantProfileEvent
import com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.navigateToSelectPayoutChannel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMerchantProfileScreen(
    navController: NavController,
    businessId: String,
    viewModel: EditMerchantProfileViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observe SavedStateHandle for Payout Channel Selection
    val selectedChannelCode = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("selected_payout_channel_code")
        ?.observeAsState()
    
    val selectedChannelName = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("selected_payout_channel_name")
        ?.observeAsState()

    LaunchedEffect(selectedChannelCode?.value, selectedChannelName?.value) {
        val code = selectedChannelCode?.value
        val name = selectedChannelName?.value
        if (code != null && name != null) {
            viewModel.onEvent(EditMerchantProfileEvent.BankSelected(code, name))
            // Clear the results after consumption
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_payout_channel_code")
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("selected_payout_channel_name")
        }
    }

    LaunchedEffect(businessId) {
        viewModel.onEvent(EditMerchantProfileEvent.Load(businessId))
    }

    LaunchedEffect(state.successMessage, state.errorMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(EditMerchantProfileEvent.DismissMessage)
            navController.popBackStack()
        }
        state.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onEvent(EditMerchantProfileEvent.DismissMessage)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            BackTopBar(
                title = "Edit Merchant Profile",
                onBackClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = { viewModel.onEvent(EditMerchantProfileEvent.Save) },
                    enabled = !state.isSaving && state.profile != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (state.isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Save Changes", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularLoading()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Section: Business Information
                ProfileSection(title = "Business Information") {
                    OutlinedTextField(
                        value = state.legalName,
                        onValueChange = { viewModel.onEvent(EditMerchantProfileEvent.LegalNameChanged(it)) },
                        label = { Text("Legal Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = state.tradingName,
                        onValueChange = { viewModel.onEvent(EditMerchantProfileEvent.TradingNameChanged(it)) },
                        label = { Text("Trading Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = state.description,
                        onValueChange = { viewModel.onEvent(EditMerchantProfileEvent.DescriptionChanged(it)) },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                }

                // Section: Contact Details
                ProfileSection(title = "Contact Details") {
                    OutlinedTextField(
                        value = state.email,
                        onValueChange = { viewModel.onEvent(EditMerchantProfileEvent.EmailChanged(it)) },
                        label = { Text("Business Email") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = state.phoneNumber,
                        onValueChange = { viewModel.onEvent(EditMerchantProfileEvent.PhoneNumberChanged(it)) },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = state.websiteUrl,
                        onValueChange = { viewModel.onEvent(EditMerchantProfileEvent.WebsiteUrlChanged(it)) },
                        label = { Text("Website URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Section: Bank Account (Primary)
                ProfileSection(title = "Bank Account (Primary)") {
                    // Clickable read-only field for Bank Name
                    OutlinedTextField(
                        value = state.bankName,
                        onValueChange = {},
                        label = { Text("Bank Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigateToSelectPayoutChannel() },
                        readOnly = true,
                        enabled = false,
                        trailingIcon = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    
                    OutlinedTextField(
                        value = state.bankCode,
                        onValueChange = {},
                        label = { Text("Bank Code") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                    
                    OutlinedTextField(
                        value = state.accountNumber,
                        onValueChange = { viewModel.onEvent(EditMerchantProfileEvent.AccountNumberChanged(it)) },
                        label = { Text("Account Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    OutlinedTextField(
                        value = state.accountHolderName,
                        onValueChange = { viewModel.onEvent(EditMerchantProfileEvent.AccountHolderNameChanged(it)) },
                        label = { Text("Account Holder Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun ProfileSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        content()
    }
}
