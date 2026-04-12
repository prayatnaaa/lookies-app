package com.prayatna.lookiesapp.presentation.checkout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.domain.model.order.OrderItemInput
import com.prayatna.lookiesapp.presentation.checkout.state.CheckoutEvent
import com.prayatna.lookiesapp.presentation.checkout.state.PaymentMethodUiState
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun CheckoutRoute(
    type: String,
    quantity: Int,
    itemId: String,
    navController: NavController,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getDetailItem(type, itemId)
    }

    val addressAdded = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow("address_added", false)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(addressAdded?.value) {
        if (addressAdded?.value == true) {
            viewModel.refreshUserAddresses()
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.set("address_added", false)
        }
    }

    CheckoutScreen(
        type = type,
        uiState = uiState,
        quantity = quantity,
        onEvent = { event ->
            when (event) {

                CheckoutEvent.OnBackClick -> {
                    navController.popBackStack()
                }

                CheckoutEvent.OnRefresh -> {
                    viewModel.getDetailItem(type, itemId)
                }

                is CheckoutEvent.OnPaymentMethodSelected -> {
                    viewModel.onPaymentMethodSelected(event.method)
                }

                is CheckoutEvent.OnShipmentFeeSelected -> {
                    viewModel.onShipmentFeeSelected(event.fee)
                }

                is CheckoutEvent.OnAddressSelected -> {
                    viewModel.onAddressSelected(event.address)
                }

                CheckoutEvent.OnAddAddressClick -> {
                    navController.navigate(NavigationRoutes.CREATE_USER_ADDRESS)
                }

                CheckoutEvent.OnPayClick -> {
                    if (type == "event_registration") {
                        // Order already created during registerEvent, go directly to payment
                        val orderId = uiState.existingOrderId
                        val merchantId = uiState.itemToBuy?.merchantId
                        val totalAmount = uiState.itemToBuy?.price?.toLong() ?: 0L

                        if (uiState.selectedMethod == PaymentMethodUiState.QRIS) {
                            navController.navigate(
                                "${NavigationRoutes.QRIS_PAYMENT}/$orderId/$merchantId/$totalAmount"
                            ) {
                                popUpTo("checkout_route") { inclusive = true }
                            }
                        }
                    } else {
                        val currentItem = uiState.itemToBuy
                        if (currentItem != null) {
                            val orderItem = OrderItemInput(
                                itemType = currentItem.type,
                                itemRefId = currentItem.id,
                                quantity = quantity
                            )
                            viewModel.createCheckout(listOf(orderItem))
                        }
                    }
                }

                CheckoutEvent.OnSuccessConfirmed -> {
                    val orderId = uiState.checkoutSuccessData
                    val merchantId = uiState.itemToBuy?.merchantId
                    val shippingCost = if (type == "painting") {
                        uiState.selectedShipmentFee?.fee?.toDouble() ?: 0.0
                    } else {
                        0.0
                    }
                    val totalAmount =
                        (uiState.itemToBuy?.price?.times(quantity)?.plus(shippingCost))?.toLong()

                    viewModel.onCheckoutResultConsumed()

                    if (uiState.selectedMethod == PaymentMethodUiState.QRIS) {
                        navController.navigate(
                            "${NavigationRoutes.QRIS_PAYMENT}/$orderId/$merchantId/$totalAmount"
                        ) {
                            popUpTo("checkout_route") { inclusive = true }
                        }
                    }
                }

                CheckoutEvent.OnErrorConfirmed -> {
                    viewModel.onCheckoutResultConsumed()
                }
            }
        }
    )
}