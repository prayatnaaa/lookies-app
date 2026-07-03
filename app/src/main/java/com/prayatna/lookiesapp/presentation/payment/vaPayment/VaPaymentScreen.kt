package com.prayatna.lookiesapp.presentation.payment.vaPayment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.partner.orderDetail.navigateToPartnerOrderDetail
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.NavigationRoutes.VA_PAYMENT
import com.prayatna.lookiesapp.utils.formatRupiah
import kotlinx.coroutines.delay
import java.time.Instant

@Composable
fun VaPaymentScreen(
    viewModel: VaPaymentViewModel = hiltViewModel(),
    merchantId: String,
    orderId: String,
    amount: Long,
    bankCode: String,
    customerName: String,
    isOfflinePurchase: Boolean = false,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showCopied by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(orderId, merchantId, amount, bankCode, customerName) {
        viewModel.createVaPaymentRequest(
            merchantId = merchantId,
            orderId = orderId,
            amount = amount.toInt(),
            channelCode = bankCode,
            customerName = customerName
        )
    }

    LaunchedEffect(uiState.errorMessage) {
        showErrorDialog = uiState.errorMessage != null
    }

    LaunchedEffect(uiState.vaPaymentData) {
        if (uiState.vaPaymentData != null) {
            viewModel.getPaymentAttempt(orderId)
        }
    }

    LaunchedEffect(uiState.isPaid) {
        if (uiState.isPaid) {
//            showSuccessDialog = true
//            delay(1000)
            if (isOfflinePurchase) {
                navController.navigate("${NavigationRoutes.PARTNER_ORDER_DETAIL}/$orderId") {
                    popUpTo("${NavigationRoutes.VA_PAYMENT}/{orderId}/{merchantId}/{amount}/{bankCode}/{customerName}?isOfflinePurchase={isOfflinePurchase}") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            } else {
                navController.navigate("${NavigationRoutes.DETAIL_TRANSACTION}/$orderId") {
                    popUpTo("${NavigationRoutes.VA_PAYMENT}/{orderId}/{merchantId}/{amount}/{bankCode}/{customerName}?isOfflinePurchase={isOfflinePurchase}") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        }
    }

    LaunchedEffect(showCopied) {
        if (showCopied) {
            delay(2000)
            showCopied = false
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
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Bank Icon
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Virtual Account",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = bankCode,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Transfer to this virtual account number",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // VA Number Card
                Card(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Virtual Account Number",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val vaNumber = uiState.vaPaymentData?.virtualAccountNumber

                        if (vaNumber != null) {
                            Text(
                                text = vaNumber,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(vaNumber))
                                    showCopied = true
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (showCopied) "Copied!" else "Copy Number")
                            }
                        } else {
                            CircularLoading()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Amount Card
                Card(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Total Amount",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatRupiah(amount.toDouble()),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Countdown timer
                uiState.vaPaymentData?.expiresAt?.let { expiresAt ->
                    VaCountdownTimer(expiresAt = expiresAt)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // How to pay card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "How to pay:",
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("1. Open your $bankCode mobile banking / ATM")
                        Text("2. Select \"Virtual Account\" / \"Transfer\"")
                        Text("3. Enter the VA number above")
                        Text("4. Confirm the amount and complete payment")
                        Text("5. Keep your receipt for reference")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
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
            onConfirm = { showErrorDialog = false },
            onDismiss = { showErrorDialog = false }
        )
    }

    if (showSuccessDialog) {
        CustomBottomSheet(
            title = "Payment Confirmed!",
            message = "Your payment has been received. Thank you!",
            confirmText = "OK",
            onConfirm = { showSuccessDialog = false },
            onDismiss = { showSuccessDialog = false }
        )
    }
}

/**
 * Lightweight countdown timer composable.
 * Parses [expiresAt] (ISO-8601) once and ticks every second using a simple
 * delay(1000) loop — no Flow, no extra state, no external library.
 */
@Composable
fun VaCountdownTimer(expiresAt: String) {
    // Parse expiry epoch seconds once — remember so it never reparses on recompose
    val expiryEpochSeconds = remember(expiresAt) {
        runCatching { Instant.parse(expiresAt).epochSecond }.getOrDefault(0L)
    }

    var remainingSeconds by remember { mutableLongStateOf(expiryEpochSeconds - Instant.now().epochSecond) }

    // Single lightweight coroutine: delay 1 s → recompute → repeat
    LaunchedEffect(expiryEpochSeconds) {
        while (remainingSeconds > 0) {
            delay(1_000L)
            remainingSeconds = expiryEpochSeconds - Instant.now().epochSecond
        }
    }

    val isExpired = remainingSeconds <= 0
    val timerColor = when {
        isExpired                  -> MaterialTheme.colorScheme.error
        remainingSeconds < 5 * 60 -> MaterialTheme.colorScheme.error          // < 5 min → red
        remainingSeconds < 30 * 60 -> MaterialTheme.colorScheme.tertiary       // < 30 min → amber
        else                       -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val displayText = if (isExpired) {
        "Expired"
    } else {
        val h = remainingSeconds / 3600
        val m = (remainingSeconds % 3600) / 60
        val s = remainingSeconds % 60
        if (h > 0) "%02d:%02d:%02d".format(h, m, s)
        else       "%02d:%02d".format(m, s)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Schedule,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = timerColor
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = if (isExpired) "Payment expired" else "Expires in  $displayText",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = if (remainingSeconds < 5 * 60) FontWeight.SemiBold else FontWeight.Normal,
            color = timerColor
        )
    }
}
