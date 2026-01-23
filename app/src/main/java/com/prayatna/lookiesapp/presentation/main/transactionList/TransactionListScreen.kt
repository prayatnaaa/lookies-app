package com.prayatna.lookiesapp.presentation.main.transactionList

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
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
import com.prayatna.lookiesapp.presentation.components.transactionList.TransactionItemCard
import com.prayatna.lookiesapp.presentation.main.transactionList.state.TransactionListUiState

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
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(currentState.data) { transaction ->
                            TransactionItemCard(
                                transaction = transaction,
                                onClick = {
//                                    navController.navigate("${NavigationRoutes.TRANSACTION_DETAIL}/${transaction.id}")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}