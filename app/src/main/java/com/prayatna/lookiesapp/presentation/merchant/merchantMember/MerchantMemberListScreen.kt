package com.prayatna.lookiesapp.presentation.merchant.merchantMember

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.merchant.MerchantMemberItem
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun MerchantMemberListScreen(
    navController: NavController,
    viewModel: MerchantMemberListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow("refresh", false)
            ?.collect { shouldRefresh ->
                if (shouldRefresh) {

                    viewModel.loadMerchantMembers()

                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh", false)
                }
            }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate("${NavigationRoutes.PARTNER_APPLICATION}/partner")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.GroupAdd,
                        contentDescription = "Join us"
                    )
                },
                text = {
                    Text("Join us!")
                }
            )
        },
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
                EmptyMerchantMembers()
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
                            top = 8.dp,
                            bottom = 24.dp
                        )
                    ) {
                        itemsIndexed(
                            items = uiState.merchantMembers,
                            key = { _, item -> item.businessId }
                        ) { index, member ->

                            MerchantMemberItem(
                                member = member,
                                showDivider = index != uiState.merchantMembers.lastIndex,
                                onClick = {
                                    navController.navigate(
                                        if (member.status == "invited") {
                                            "${NavigationRoutes.ACCEPT_PARTNER_INVITATION}/${member.merchantAccountId}"
                                        } else {
                                            "${NavigationRoutes.PARTNER_MAIN_SCREEN}/${member.businessId}"
                                        }
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
private fun EmptyMerchantMembers() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Groups,
            contentDescription = null,
            modifier = Modifier.size(72.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No memberships yet",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You haven't joined any merchant memberships yet. Join one to get started.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
    }
}