package com.prayatna.lookiesapp.presentation.refund.refundList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.prayatna.lookiesapp.domain.model.transaction.Refund
import com.prayatna.lookiesapp.presentation.components.CustomBottomSheet
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.refund.refundList.state.RefundListEvent
import com.prayatna.lookiesapp.utils.NavigationRoutes
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RefundListScreen(
    navController: NavController,
    viewModel: RefundListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.successMessage != null) {
        CustomBottomSheet(
            title = "Success",
            message = uiState.successMessage!!,
            confirmText = "OK",
            onConfirm = { viewModel.onEvent(RefundListEvent.DismissSuccess) },
            onDismiss = { viewModel.onEvent(RefundListEvent.DismissSuccess) }
        )
    }

    if (uiState.errorMessage != null && !uiState.isLoading) {
        CustomBottomSheet(
            title = "Error",
            message = uiState.errorMessage!!,
            confirmText = "OK",
            onConfirm = { viewModel.onEvent(RefundListEvent.DismissError) },
            onDismiss = { viewModel.onEvent(RefundListEvent.DismissError) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Refund Requests", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> CircularLoading(modifier = Modifier.align(Alignment.Center))

                uiState.refunds.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoneyOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No Refund Requests",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "There are no refund requests at the moment.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(
                            items = uiState.refunds,
                            key = { _, refund -> refund.id }
                        ) { index, refund ->

                            RefundItem(
                                refund = refund,
                                showDivider = index != uiState.refunds.lastIndex,
                                onClicked = {
                                    navController.navigate(
                                        "${NavigationRoutes.PARTNER_REFUND}/${refund.id}"
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RefundItem(
    refund: Refund,
    showDivider: Boolean = true,
    onClicked: () -> Unit = {}
) {

    val statusColor = when (
        refund.status.lowercase()
    ) {
        "pending" ->
            MaterialTheme.colorScheme.tertiary

        "processing" ->
            MaterialTheme.colorScheme.primary

        "completed" ->
            Color(0xFF2E7D32)

        "rejected" ->
            MaterialTheme.colorScheme.error

        else ->
            MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClicked)
            .padding(
                horizontal = 16.dp,
                vertical = 14.dp
            )
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text =
                        "Refund #${refund.id.take(8)}",
                    style =
                        MaterialTheme.typography
                            .titleMedium,
                    fontWeight =
                        FontWeight.SemiBold,
                    maxLines = 1,
                    overflow =
                        TextOverflow.Ellipsis
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text =
                        formatRupiah(
                            refund.amount
                                .toDoubleOrNull()
                                ?: 0.0
                        ),
                    style =
                        MaterialTheme.typography
                            .titleSmall,
                    fontWeight =
                        FontWeight.Bold,
                    color =
                        MaterialTheme.colorScheme
                            .primary
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Box(
                modifier = Modifier
                    .background(
                        color =
                            statusColor.copy(
                                alpha = 0.12f
                            ),
                        shape =
                            RoundedCornerShape(
                                8.dp
                            )
                    )
                    .padding(
                        horizontal = 10.dp,
                        vertical = 5.dp
                    )
            ) {

                Text(
                    text =
                        refund.status
                            .replaceFirstChar {
                                it.uppercase()
                            },
                    style =
                        MaterialTheme.typography
                            .labelSmall,
                    color = statusColor,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        RefundInfoRow(
            label = "Order",
            value = refund.orderId
        )

        RefundInfoRow(
            label = "Bank",
            value =
                "${refund.bankCode.uppercase()} - ${
                    refund.accountNumber
                }"
        )

        RefundInfoRow(
            label = "Holder",
            value =
                refund.accountHolderName
        )

        RefundInfoRow(
            label = "Reason",
            value = refund.reason
        )

        if (showDivider) {

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            HorizontalDivider(
                color =
                    MaterialTheme.colorScheme
                        .outline
                        .copy(alpha = 0.12f)
            )
        }
    }
}

@Composable
private fun RefundInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(0.6f),
            textAlign = TextAlign.End,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
