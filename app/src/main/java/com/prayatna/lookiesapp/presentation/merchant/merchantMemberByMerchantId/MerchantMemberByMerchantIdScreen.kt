package com.prayatna.lookiesapp.presentation.merchant.merchantMemberByMerchantId

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.merchant.MerchantMemberCard
import com.prayatna.lookiesapp.presentation.error.ErrorScreen
import com.prayatna.lookiesapp.presentation.merchant.merchantMemberByMerchantId.state.MerchantMemberByMerchantIdEvent
import com.prayatna.lookiesapp.presentation.merchant.merchantMemberByMerchantId.state.MerchantMemberByMerchantIdUiState

@Composable
fun MerchantMemberByMerchantIdScreen(
    uiState: MerchantMemberByMerchantIdUiState,
    onEvent: (MerchantMemberByMerchantIdEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = {
                    onEvent(MerchantMemberByMerchantIdEvent.BackClicked)
                },
                title = "Merchant Members",

                actions = {
                    IconButton(
                        onClick = {
                            onEvent(MerchantMemberByMerchantIdEvent.InviteMemberClicked)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Member"
                        )
                    }
                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularLoading()
                }

                uiState.errorMessage != null -> {
                    ErrorScreen(
                        message = uiState.errorMessage ,
                        onRetry = { onEvent(MerchantMemberByMerchantIdEvent.Retry) }
                    )
                }

                uiState.merchantMembers.isEmpty() -> {
                    ErrorScreen(
                        message = "No members found for this merchant",
                        onRetry = { onEvent(MerchantMemberByMerchantIdEvent.Retry) }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.merchantMembers) { member ->
                            MerchantMemberCard(
                                member = member,
                                isShowMemberName = true,
                                onClick = {
                                    // Handle member click if needed
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
