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
import com.prayatna.lookiesapp.presentation.transaction.payment.state.PaymentMethod
import com.prayatna.lookiesapp.utils.formatRupiah
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    orderId: String,
    merchantId: String,
    amount: Double,
    viewModel: PaymentViewModel = hiltViewModel(),
    onPaymentSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.isSuccess, state.paymentSuccessUrl) {
        if (state.isSuccess && state.paymentSuccessUrl != null) {
            val intent = Intent(Intent.ACTION_VIEW, state.paymentSuccessUrl!!.toUri())
            context.startActivity(intent)
            onPaymentSuccess()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pilih Pembayaran") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Info
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = formatRupiah(amount),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text("Metode Pembayaran", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            PaymentOptionItem(
                title = "GoPay",
                icon = Icons.Default.PhoneAndroid,
                isSelected = state.selectedMethod == PaymentMethod.GOPAY,
                onClick = { viewModel.onMethodSelected(PaymentMethod.GOPAY) }
            )

            AnimatedVisibility(visible = state.selectedMethod == PaymentMethod.GOPAY) {
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = viewModel::onPhoneNumberChange,
                    label = { Text("Nomor HP GoPay (08xxx)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )
            }

            PaymentOptionItem(
                title = "Credit / Debit Card",
                icon = Icons.Default.CreditCard,
                isSelected = state.selectedMethod == PaymentMethod.CREDIT_CARD,
                onClick = { viewModel.onMethodSelected(PaymentMethod.CREDIT_CARD) }
            )

            AnimatedVisibility(visible = state.selectedMethod == PaymentMethod.CREDIT_CARD) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = state.cardNumber,
                        onValueChange = viewModel::onCardNumberChange,
                        label = { Text("Nomor Kartu") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = state.cardExpiry,
                            onValueChange = viewModel::onExpiryChange,
                            label = { Text("Expiry (MM/YY)") },
                            modifier = Modifier.weight(1f),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = state.cardCvv,
                            onValueChange = viewModel::onCvvChange,
                            label = { Text("CVV") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }
                }
            }

            if (state.errorMessage != null) {
                Text(
                    text = state.errorMessage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.processPayment(orderId, merchantId, amount)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Bayar Sekarang")
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