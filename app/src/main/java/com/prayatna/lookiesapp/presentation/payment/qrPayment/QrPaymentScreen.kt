package com.prayatna.lookiesapp.presentation.payment.qrPayment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.QrCodeGenerator
import com.prayatna.lookiesapp.utils.formatRupiah

@Composable
fun QrPaymentScreen(
    viewModel: QrPaymentViewModel = hiltViewModel(),
    merchantId: String,
    orderId: String,
    amount: Int,
    description: String? = null,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderId, merchantId, amount) {
        viewModel.createQrisPaymentRequest(
            merchantId = merchantId,
            orderId = orderId,
            amount = amount,
            description = description
        )
    }

    LaunchedEffect(uiState.errorMessage) {
        showErrorDialog = uiState.errorMessage != null
    }

    val qrString = uiState.qrPaymentData
        ?.data?.paymentMethod?.qrCode
        ?.channelProperties?.qrString

    val bitmap = remember(qrString) {
        qrString?.let { QrCodeGenerator.generate(it) }
    }

    Scaffold { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Scan to pay",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Use e-wallet or mobile banking application",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(260.dp)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QR Code"
                            )
                        } else {
                            Text("QR is not available")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Total amount",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = formatRupiah(amount = amount.toDouble()),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("How to:", fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("1. Open e-wallet / m-banking app")
                        Text("2. Scan the QR code")
                        Text("3. Confirm payment")
                    }
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularLoading()
                }
            }
        }
    }

    if (showErrorDialog) {
        CustomBottomSheet(
            title = "Error",
            message = uiState.errorMessage ?: "",
            confirmText = "OK",
            onConfirm = {
                showErrorDialog = false
            },
            onDismiss = {
                showErrorDialog = false
            }
        )
    }
}