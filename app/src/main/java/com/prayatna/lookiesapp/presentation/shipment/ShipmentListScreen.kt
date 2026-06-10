package com.prayatna.lookiesapp.presentation.shipment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.shipment.ShipmentCard
import com.prayatna.lookiesapp.presentation.shipment.state.ShipmentListUiEvent
import com.prayatna.lookiesapp.presentation.shipment.state.ShipmentListUiState
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun ShipmentListScreen(
    uiState: ShipmentListUiState,
    onEvent: (ShipmentListUiEvent) -> Unit,
    onBackPress: () -> Unit,
    onShipmentCardClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val statuses = listOf("all", "pending", "processing", "shipped", "delivered", "cancelled", "refunded")

    Scaffold(
        topBar = {
            BackTopBar(
                title = "Shipments",
                onBackClick = onBackPress,
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar for Tracking Number
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { onEvent(ShipmentListUiEvent.SearchQueryChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search by tracking number...") },
                leadingIcon = { 
                    Icon(
                        imageVector = Icons.Default.Search, 
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    ) 
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onEvent(ShipmentListUiEvent.SearchQueryChanged("")) }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                        }
                    }
                },
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(statuses) { status ->
                    FilterChip(
                        selected = (uiState.selectedStatus ?: "all") == status,
                        onClick = {
                            onEvent(ShipmentListUiEvent.FilterByStatus(if (status == "all") null else status))
                        },
                        label = {
                            Text(text = status.replaceFirstChar { it.uppercase() })
                        }
                    )
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                when {
                    uiState.isLoading && uiState.data.isEmpty() -> {
                        CircularLoading()
                    }

                    uiState.errorMessage != null -> {
                        Text(
                            text = uiState.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )
                    }

                    uiState.data.isEmpty() -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = "No shipments found",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (uiState.searchQuery.isEmpty()) 
                                    "When you have active shipments with this status, they will appear here."
                                    else "No shipments matching \"${uiState.searchQuery}\" were found.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 8.dp)
                        ) {
                            itemsIndexed(
                                items = uiState.data,
                                key = { _, shipment -> shipment.id }
                            ) { index, shipment ->

                                ShipmentCard(
                                    shipment = shipment,
                                    showDivider = index != uiState.data.lastIndex,
                                    onClick = {
                                        onShipmentCardClick(shipment.orderId)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
