package com.prayatna.lookiesapp.presentation.merchant.merchantMember

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.merchant.MerchantMemberCard
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MerchantMemberListScreen(
    navController: NavController,
    viewModel: MerchantMemberListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = { navController.popBackStack() },
                title = "Merchant Members"
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { innerPadding ->

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularLoading()
                }
            }

            uiState.errorMessage != null -> {
                ErrorScreen(
                    message = uiState.errorMessage
                        ?: "An unexpected error occurred",
                    onRetry = { viewModel.loadMerchantMembers() }
                )
            }

            uiState.merchantMembers.isEmpty() -> {
                EmptyMerchantMembers(
                    onRetry = { viewModel.loadMerchantMembers() }
                )
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 12.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = "${uiState.merchantMembers.size} found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 8.dp,
                            bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            items = uiState.merchantMembers,
                            key = { it.businessId }
                        ) { member ->

                            Box {
                                MerchantMemberCard(
                                    member = member,
                                    onClick = {
                                        navController.navigate(
                                            "${NavigationRoutes.PARTNER_MAIN_SCREEN}/${member.businessId}"
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
}

@Composable
private fun EmptyMerchantMembers(
    onRetry: () -> Unit
) {
    ErrorScreen(
        message = "No merchant memberships found",
        onRetry = onRetry
    )
}