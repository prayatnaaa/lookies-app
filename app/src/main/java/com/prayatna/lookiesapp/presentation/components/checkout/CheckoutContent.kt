package com.prayatna.lookiesapp.presentation.components.checkout

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutContent(
    uiState: CheckoutUiState,
    onBackClick: () -> Unit,
    quantity: Int,
    onPayClick: () -> Unit,
    onRefresh: () -> Unit,
    snackbarHost: @Composable () -> Unit = {}
) {

    Scaffold(
        snackbarHost = snackbarHost,
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (uiState.itemToBuy != null) {
                CheckoutBottomBar(
                    totalPrice = uiState.itemToBuy.price!!.times(quantity),
                    isLoading = uiState.isLoading,
                    onPayClick = { onPayClick() }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (uiState.isLoading && uiState.itemToBuy == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else if (uiState.errorMessage != null && uiState.itemToBuy == null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Failed to load item")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = onRefresh) { Text("Try again") }
                }
            }
            else if (uiState.itemToBuy != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Purchased Item",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    CheckoutItemCard(
                        title = uiState.itemToBuy.title,
                        imageUrl = uiState.itemToBuy.imageUrl,
                        price = uiState.itemToBuy.price ?: 0.0,
                        subtitle = uiState.itemToBuy.subtitle,
                        quantity = quantity,
                        onQuantityChange = null
                    )

                    HorizontalDivider()

                    Text(
                        text = "Payments Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    PaymentSummaryRow(label = "Unit price", amount = uiState.itemToBuy.price ?: 0.0)
//                    PaymentSummaryRow(label = "Service fee", amount = 0.0)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formatRupiah((uiState.itemToBuy.price?.times(quantity)) ?: 0.0),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Composable
fun PaymentSummaryRow(label: String, amount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text(text = formatRupiah(amount), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun CheckoutBottomBar(
    totalPrice: Double,
    isLoading: Boolean,
    onPayClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Button(
            onClick = onPayClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !isLoading,
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Total: ${formatRupiah(totalPrice)}")
                    Text("Pay", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}