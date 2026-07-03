package com.prayatna.lookiesapp.presentation.components.checkout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.shipment.ShipmentFee
import com.prayatna.lookiesapp.domain.model.user.UserAddress
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutUiState
import com.prayatna.lookiesapp.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutContent(
    type: String,
    uiState: CheckoutUiState,
    quantity: Int,
    onBackClick: () -> Unit,
    onPayClick: () -> Unit,
    onRefresh: () -> Unit,
    onShipmentFeeSelected: (ShipmentFee) -> Unit,
    onAddressSelected: (UserAddress) -> Unit,
    onAddAddressClick: () -> Unit,
    children: @Composable () -> Unit,
    snackbarHostState: SnackbarHostState
) {

    val shippingCost = if (type == "painting") {
        uiState.selectedShipmentFee?.fee?.toDouble() ?: 0.0
    } else 0.0

    val subtotal = (uiState.itemToBuy?.price ?: 0.0) * quantity
    val totalPrice = subtotal + shippingCost

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = { Text("Checkout") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            uiState.itemToBuy?.let {
                CheckoutBottomBar(
                    totalPrice = totalPrice,
                    isLoading = uiState.isLoading,
                    onPayClick = onPayClick
                )
            }
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // LOADING
            if (uiState.isLoading && uiState.itemToBuy == null) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
                return@Box
            }

            // EMPTY / FAILED LOAD
            if (uiState.itemToBuy == null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Failed to load item")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onRefresh) {
                        Text("Try again")
                    }
                }
                return@Box
            }

            // CONTENT
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                CheckoutItemCard(
                    title = uiState.itemToBuy.title,
                    imageUrl = uiState.itemToBuy.imageUrl,
                    price = uiState.itemToBuy.price ?: 0.0,
                    subtitle = uiState.itemToBuy.subtitle,
                    quantity = quantity,
                    onQuantityChange = null
                )

                HorizontalDivider()

                // ADDRESS
                AnimatedVisibility(visible = type == "painting") {
                    AddressSection(
                        uiState = uiState,
                        onAddressSelected = onAddressSelected,
                        onAddAddressClick = onAddAddressClick
                    )
                }

                // SHIPPING
                AnimatedVisibility(visible = type == "painting") {
                    ShippingSection(
                        uiState = uiState,
                        onShipmentFeeSelected = onShipmentFeeSelected
                    )
                }

                // SUMMARY
                PaymentSummaryRow("Subtotal", subtotal)

                if (type == "painting" && uiState.selectedShipmentFee != null) {
                    PaymentSummaryRow(
                        "Shipping",
                        uiState.selectedShipmentFee.fee.toDouble()
                    )
                }

                HorizontalDivider()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total")
                    Text(formatRupiah(totalPrice))
                }

                children()
            }
        }
    }
}

@Composable
fun AddressSection(
    uiState: CheckoutUiState,
    onAddressSelected: (UserAddress) -> Unit,
    onAddAddressClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

        Text("Shipping Address", style = MaterialTheme.typography.titleMedium)

        if (uiState.isAddressLoading) {
            CircularProgressIndicator()
        } else if (uiState.userAddresses.isEmpty()) {

            Text("No address found")

            OutlinedButton(
                onClick = onAddAddressClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
            ) {
                Text("+ Add New Address")
            }

        } else {

            Column {
            uiState.userAddresses.forEach { address ->
                val selected = uiState.selectedAddress?.id == address.id
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddressSelected(address) },
                    shape = RoundedCornerShape(12.dp),
                    border = if (selected) {
                        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        BorderStroke(1.dp, Color.Gray)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = selected, onClick = null)

                        Spacer(Modifier.width(8.dp))

                        Column {
                            Text(address.name)
                            Text(address.phoneNumber)
                            Text(address.addressLine)
                        }
                    }
                }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                OutlinedButton(
                    onClick = onAddAddressClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                ) {
                    Text("+ Add New Address")
                }
            }
        }
    }
}

@Composable
fun ShippingSection(
    uiState: CheckoutUiState,
    onShipmentFeeSelected: (ShipmentFee) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        Text("Shipping", style = MaterialTheme.typography.titleMedium)

        if (uiState.isShipmentLoading) {
            CircularProgressIndicator()
        } else {
            uiState.shipmentFees.forEach { fee ->
                val selected = uiState.selectedShipmentFee?.id == fee.id

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShipmentFeeSelected(fee) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected,
                        onClick = { onShipmentFeeSelected(fee) }
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(fee.region, modifier = Modifier.weight(1f))

                    Text(formatRupiah(fee.fee.toDouble()))
                }
            }
        }
    }
}

@Composable
fun PaymentSummaryRow(
    label: String,
    amount: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label)
        Text(formatRupiah(amount))
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
            .navigationBarsPadding()
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