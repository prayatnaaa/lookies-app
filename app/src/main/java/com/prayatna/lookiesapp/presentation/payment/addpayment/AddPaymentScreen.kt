package com.prayatna.lookiesapp.presentation.payment.addpayment

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.domain.model.event.DetailEventInfo
import com.prayatna.lookiesapp.presentation.components.detailevent.DetailEventInfoSection
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.presentation.event.detailevent.DetailEventViewModel
import com.prayatna.lookiesapp.presentation.payment.addpayment.state.AddPaymentFormState

@Composable
fun AddPaymentScreen(
    navController: NavController,
    eventId: String,
    detailEventViewModel: DetailEventViewModel = hiltViewModel(),
    addPaymentViewModel: AddPaymentViewModel = hiltViewModel(),
) {
    val eventState by detailEventViewModel.state.collectAsStateWithLifecycle()
    val formState by addPaymentViewModel.addPaymentFormState.collectAsState()
    val uiState by addPaymentViewModel.addPaymentState.collectAsState()

    LaunchedEffect(eventId) {
        if (eventState.info == null && !eventState.isLoading) {
            detailEventViewModel.getEvent(eventId)
        }
    }

    when {
        eventState.isLoading -> CircularLoading()

        eventState.errorMessage != null -> {
            ErrorScreen(
                message = eventState.errorMessage!!,
                onRetry = { detailEventViewModel.retry(eventId) }
            )
        }

        eventState.info != null -> {
            AddPaymentContent(
                formState = formState,
                isLoading = uiState.isLoading,
                error = uiState.error,
                onAmountChanged = addPaymentViewModel::onAmountChanged,
                onPaymentTypeChanged = addPaymentViewModel::onPaymentTypeChanged,
                onSubmit = addPaymentViewModel::submitPayment,
                eventInfo = eventState.info!!
            )
        }
    }
}


@Composable
private fun AddPaymentContent(
    formState: AddPaymentFormState,
    isLoading: Boolean,
    error: String?,
    onAmountChanged: (String) -> Unit,
    onPaymentTypeChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    eventInfo: DetailEventInfo
) {
    var expanded by remember { mutableStateOf(false) }
    val paymentTypes = listOf("Cash", "Credit Card", "Transfer")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(text = "Add Payment", style = MaterialTheme.typography.titleLarge)

        DetailEventInfoSection(
            event = eventInfo.event,
            detailEvent = eventInfo.detail
        )

        OutlinedTextField(
            value = formState.amount,
            onValueChange = onAmountChanged,
            label = { Text("Amount") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Dropdown for Payment Type
        Box {
            OutlinedTextField(
                value = formState.paymentType,
                onValueChange = {},
                label = { Text("Payment Type") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                            contentDescription = null
                        )
                    }
                }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                paymentTypes.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            onPaymentTypeChanged(type)
                            expanded = false
                        }
                    )
                }
            }
        }

        if (error != null) {
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(22.dp))
            } else {
                Text("Submit")
            }
        }
    }
}
