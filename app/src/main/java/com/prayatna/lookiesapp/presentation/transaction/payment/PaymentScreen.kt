package com.prayatna.lookiesapp.presentation.transaction.payment

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentEvent
import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentMethod
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    orderId: String,
    merchantId: String,
    amount: Double,
    onPaymentSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    LaunchedEffect(state.isSuccess, state.paymentSuccessUrl) {
        if (state.isSuccess && state.paymentSuccessUrl != null) {
            val intent = Intent(Intent.ACTION_VIEW, state.paymentSuccessUrl!!.toUri())
            context.startActivity(intent)
            onPaymentSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Payment Method") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.CreditCard, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .imePadding() // <-- keyboard will push content
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            /**
             * Summary Card
             */
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Amount", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = formatRupiah(amount),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = "Payment Method",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            /**
             * GoPay
             */
            PaymentOptionItem(
                title = "GoPay",
                icon = Icons.Default.PhoneAndroid,
                isSelected = state.selectedMethod == PaymentMethod.GOPAY,
                onClick = {
                    viewModel.onEvent(
                        PaymentEvent.SelectMethod(PaymentMethod.GOPAY)
                    )
                }
            )

            AnimatedVisibility(state.selectedMethod == PaymentMethod.GOPAY) {
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = {
                        viewModel.onEvent(PaymentEvent.PhoneChanged(it))
                    },
                    label = { Text("GoPay Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }

            /**
             * Credit Card
             */
            PaymentOptionItem(
                title = "Credit / Debit Card",
                icon = Icons.Default.CreditCard,
                isSelected = state.selectedMethod == PaymentMethod.CREDIT_CARD,
                onClick = {
                    viewModel.onEvent(
                        PaymentEvent.SelectMethod(PaymentMethod.CREDIT_CARD)
                    )
                }
            )

            AnimatedVisibility(state.selectedMethod == PaymentMethod.CREDIT_CARD) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    OutlinedTextField(
                        value = state.cardNumber,
                        onValueChange = {
                            viewModel.onEvent(PaymentEvent.CardNumberChanged(it))
                        },
                        label = { Text("Card Number") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                        OutlinedTextField(
                            value = state.cardExpiry,
                            onValueChange = {
                                viewModel.onEvent(PaymentEvent.CardExpiryChanged(it))
                            },
                            label = { Text("Expiry (MM/YY)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = state.cardCvv,
                            onValueChange = {
                                viewModel.onEvent(PaymentEvent.CardCvvChanged(it))
                            },
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }
            }

            /**
             * Error Message
             */
            state.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            /**
             * Submit Button
             */
            Button(
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                onClick = {
                    viewModel.onEvent(
                        PaymentEvent.SubmitPayment(
                            orderId = orderId,
                            merchantId = merchantId,
                            amount = amount
                        )
                    )
                }
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Pay Now")
                }
            }
        }
    }
}

@Composable
fun PaymentOptionItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.weight(1f))
            RadioButton(selected = isSelected, onClick = onClick)
        }
    }
}