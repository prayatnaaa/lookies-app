package com.prayatna.lookiesapp.presentation.user.createUserAddress

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.CustomTextField
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard
import com.prayatna.lookiesapp.presentation.user.createUserAddress.state.CreateUserAddressEvent
import com.prayatna.lookiesapp.presentation.user.createUserAddress.state.CreateUserAddressFormState
import com.prayatna.lookiesapp.presentation.user.createUserAddress.state.CreateUserAddressUiState

@Composable
fun CreateUserAddressScreen(
    uiState: CreateUserAddressUiState,
    formState: CreateUserAddressFormState,
    onEvent: (CreateUserAddressEvent) -> Unit,
    onBackClick: () -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            BackTopBar(title = "Add address", onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            FormSectionCard(
                title = "Recipient Info",
                icon = Icons.Default.Person
            ) {
                CustomTextField(
                    value = formState.name,
                    onValueChange = { onEvent(CreateUserAddressEvent.OnNameChange(it)) },
                    label = "Recipient Name"
                )
                CustomTextField(
                    value = formState.phoneNumber,
                    onValueChange = { onEvent(CreateUserAddressEvent.OnPhoneNumberChange(it)) },
                    label = "Phone Number",
                    keyboardType = KeyboardType.Phone,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }

            // Address Details
            FormSectionCard(
                title = "Address Details",
                icon = Icons.Default.Home
            ) {
                CustomTextField(
                    value = formState.addressLine,
                    onValueChange = { onEvent(CreateUserAddressEvent.OnAddressLineChange(it)) },
                    label = "Full Address",
                    singleLine = false,
                    maxLines = 3
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomTextField(
                        value = formState.city,
                        onValueChange = { onEvent(CreateUserAddressEvent.OnCityChange(it)) },
                        label = "City",
                        modifier = Modifier.weight(1f),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationCity,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    )
                    CustomTextField(
                        value = formState.postalCode,
                        onValueChange = { onEvent(CreateUserAddressEvent.OnPostalCodeChange(it)) },
                        label = "Postal Code",
                        modifier = Modifier.weight(1f),
                        keyboardType = KeyboardType.Number
                    )
                }

                CustomTextField(
                    value = formState.province,
                    onValueChange = { onEvent(CreateUserAddressEvent.OnProvinceChange(it)) },
                    label = "Province",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                )
            }

            // Additional Info
            FormSectionCard(
                title = "Additional Info",
                icon = Icons.Default.Home
            ) {
                CustomTextField(
                    value = formState.notes ?: "",
                    onValueChange = { onEvent(CreateUserAddressEvent.OnNotesChange(it)) },
                    label = "Notes (Optional)",
                    singleLine = false,
                    maxLines = 2
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = formState.isDefault,
                        onCheckedChange = { onEvent(CreateUserAddressEvent.OnIsDefaultChange(it)) }
                    )
                    Text(
                        text = "Set as default address",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onEvent(CreateUserAddressEvent.OnSubmit) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !uiState.isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Save Address", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}