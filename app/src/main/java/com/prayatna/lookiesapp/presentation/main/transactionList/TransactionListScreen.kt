package com.prayatna.lookiesapp.presentation.main.transactionList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.transactionList.EmptyTransactionState
import com.prayatna.lookiesapp.presentation.components.transactionList.TransactionItemItem
import com.prayatna.lookiesapp.presentation.main.transactionList.state.TransactionListUiState
import com.prayatna.lookiesapp.utils.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionListScreen(
    navController: NavController,
    viewModel: TransactionListViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Orders", fontWeight = FontWeight.SemiBold) },
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val currentState = state) {
                is TransactionListUiState.Loading -> {
                    CircularLoading(modifier = Modifier.align(Alignment.Center))
                }
                is TransactionListUiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Error: ${currentState.message}", color = MaterialTheme.colorScheme.error)
                        Button(onClick = { viewModel.getTransactions() }) {
                            Text("Retry")
                        }
                    }
                }
                is TransactionListUiState.Empty -> {
                    EmptyTransactionState()
                }
                is TransactionListUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        itemsIndexed(
                            items = currentState.data,
                            key = { _, transaction -> transaction.id }
                        ) { index, transaction ->

                            TransactionItemItem(
                                transaction = transaction,
                                showDivider = index != currentState.data.lastIndex,
                                onClick = {
                                    val status = transaction.status.lowercase()
                                    if (status == "awaiting_payment" || status == "pending") {
                                        val channel = transaction.paymentInfo?.channel?.lowercase()
                                        if (channel == "virtual_account") {
                                            navController.navigate(
                                                "${NavigationRoutes.EXISTING_VA_PAYMENT}/${transaction.id}"
                                            )
                                        } else if (channel == "qris") {
                                            navController.navigate(
                                                "${NavigationRoutes.EXISTING_QRIS_PAYMENT}/${transaction.id}"
                                            )
                                        } else {
                                            navController.navigate(
                                                "${NavigationRoutes.DETAIL_TRANSACTION}/${transaction.id}"
                                            )
                                        }
                                    } else {
                                        navController.navigate(
                                            "${NavigationRoutes.DETAIL_TRANSACTION}/${transaction.id}"
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}