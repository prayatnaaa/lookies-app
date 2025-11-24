package com.prayatna.lookiesapp.presentation.partner.detailpartner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.partner.PartnerProfileSection
import com.prayatna.lookiesapp.presentation.error.ErrorScreen

@Composable
fun DetailPartnerScreen(
    partnerId: Int,
    viewModel: DetailPartnerViewModel = hiltViewModel(),
    onPortfolioClick: (String) -> Unit = {},
    navController: NavController
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(partnerId) {
        viewModel.loadPartnerDetail(partnerId)
    }

    when {
        state.isLoading -> {
            CircularLoading()
        }

        state.error != null -> {
            ErrorScreen (
                message = state.error!!,
                onRetry = { viewModel.loadPartnerDetail(partnerId) }
            )
        }

        state.data != null -> {
            val data = state.data
            if (data != null) {
                Scaffold(
                    topBar = {
                        BackTopBar(
                            navController = navController
                        )
                    },
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            PartnerProfileSection(
                                data = data,
                                onPortofolioClick = { onPortfolioClick(state.data!!.portfolioLink!!) },
                                showStatus = true
                            )
                        }
                    }
                )
            }
        }
    }
}