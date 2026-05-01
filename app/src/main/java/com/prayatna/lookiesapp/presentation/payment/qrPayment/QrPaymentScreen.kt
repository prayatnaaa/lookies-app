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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.QrCodeGenerator
import com.prayatna.lookiesapp.utils.formatRupiah
import kotlinx.coroutines.delay

@Composable
fun QrPaymentScreen(
    viewModel: QrPaymentViewModel = hiltViewModel(),
    merchantId: String,
    orderId: String,
    amount: Long,
    description: String? = null,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    LaunchedEffect(orderId, merchantId, amount) {
        viewModel.createQrisPaymentRequest(
            merchantId = merchantId,
            orderId = orderId,
            amount = amount.toInt(),
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
        qrString?.let { QrCodeGenerator.generate(it, size = 1024) }
    }

    LaunchedEffect(uiState.qrPaymentData) {
        if (uiState.qrPaymentData != null) {
            viewModel.getPaymentAttempt(orderId)
        }
    }

    LaunchedEffect(uiState.isPaid) {
        if (uiState.isPaid) {
            showSuccessDialog = true

            delay(1000)
            navController.navigate("${NavigationRoutes.DETAIL_TRANSACTION}/$orderId") {
                popUpTo("${NavigationRoutes.QRIS_PAYMENT}/{orderId}/{merchantId}/{amount}") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
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

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Use e-wallet or mobile banking application",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "QR Code"
                            )
                        } else {
                            CircularLoading()
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

    if (showSuccessDialog) {
        CustomBottomSheet(
            title = "Congrats!",
            message = "Your payment has been success!",
            confirmText = "OK",
            onConfirm = {
                showSuccessDialog = false
            },
            onDismiss = {
                showSuccessDialog = false
            }
        )
    }
}