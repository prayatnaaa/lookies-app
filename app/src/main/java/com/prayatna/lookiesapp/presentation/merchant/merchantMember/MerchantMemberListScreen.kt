package com.prayatna.lookiesapp.presentation.merchant.merchantMember

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

    when {
        uiState.isLoading -> {
            CircularLoading()
        }

        uiState.errorMessage != null -> {
            ErrorScreen(
                message = uiState.errorMessage ?: "An unexpected error occurred",
                onRetry = { viewModel.loadMerchantMembers() }
            )
        }

        uiState.merchantMembers.isEmpty() -> {
            ErrorScreen(
                message = "No merchant memberships found",
                onRetry = { viewModel.loadMerchantMembers() }
            )
        }

        else -> {
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
                    .statusBarsPadding()
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(innerPadding)
                    ,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.merchantMembers) { member ->
                        MerchantMemberCard(member = member, onClick = {
                            navController.navigate("${NavigationRoutes.PARTNER_MAIN_SCREEN}/${member.businessId}")
                        })
                    }
                }
            }
        }
    }
}