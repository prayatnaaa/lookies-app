package com.prayatna.lookiesapp.presentation.transaction.detailTransaction

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.detailTransaction.DetailTransactionContent
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.utils.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTransactionScreen(
    navController: NavController,
    orderId: String,
    viewModel: DetailTransactionViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(orderId) {
        viewModel.getDetailTransaction(orderId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order Details", fontWeight = FontWeight.SemiBold) },
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
                state.isLoading -> {
                    CircularLoading(modifier = Modifier.align(Alignment.Center))
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Failed to load order",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = state.errorMessage ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.getDetailTransaction(orderId) }) {
                            Text("Retry")
                        }
                    }
                }

                state.data != null -> {
                    DetailTransactionContent(
                        data = state.data!!,
                        shipment = state.shipment,
                        isCompleting = state.isCompleting,
                        onCompleteOrder = { viewModel.setOrderToComplete(orderId) },
                        onRequestRefund = {
                            navController.navigate("${NavigationRoutes.CREATE_REFUND}/$orderId/${state.data?.transaction?.totalAmount}")
                        },
                        onViewRefunds = {
                            navController.navigate("${NavigationRoutes.ORDER_REFUNDS}/$orderId")
                        }
                    )
                }
            }
        }
    }
}