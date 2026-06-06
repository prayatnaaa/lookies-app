package com.prayatna.lookiesapp.presentation.payment.existingQrPayment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.payment.existingQrPayment.state.ExistingQrPaymentUiState
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.QrCodeGenerator
import com.prayatna.lookiesapp.utils.formatRupiah
import kotlinx.coroutines.delay

@Composable
fun ExistingQrPaymentScreen(
    state: ExistingQrPaymentUiState,
    onBackClick: () -> Unit,
    navController: NavController
) {
    val attempt = state.paymentAttempt
    var showSuccessDialog by remember { mutableStateOf(false) }

    val bitmap = remember(attempt?.qrString) {
        attempt?.qrString?.let { QrCodeGenerator.generate(it, size = 1024) }
    }

    LaunchedEffect(state.isPaid) {
        if (state.isPaid && attempt != null) {
            showSuccessDialog = true
            delay(1000)
            navController.navigate("${NavigationRoutes.DETAIL_TRANSACTION}/${attempt.orderId}") {
                popUpTo("${NavigationRoutes.EXISTING_QRIS_PAYMENT}/${attempt.orderId}") {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = {
            // Can add a top bar if needed
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading && attempt == null) {
                CircularLoading(modifier = Modifier.align(Alignment.Center))
            } else if (attempt != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
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
                        text = formatRupiah(amount = attempt.amount),
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
            } else if (state.errorMessage != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center).padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = onBackClick) {
                        Text("Go Back")
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        CustomBottomSheet(
            title = "Congrats!",
            message = "Your payment has been success!",
            confirmText = "OK",
            onConfirm = { showSuccessDialog = false },
            onDismiss = { showSuccessDialog = false }
        )
    }
}
