package com.prayatna.lookiesapp.presentation.partner.selfEventList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventlist.EventCardList
import com.prayatna.lookiesapp.presentation.components.loading.EventListLoading
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun SelfEventListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SelfEventListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { BackTopBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    EventListLoading(
                        modifier = Modifier.padding(16.dp)
                    )
                }

                uiState.errorMessage != null -> {
                    Column(
                        modifier = modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "Unknown error occurred.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Text("Retry")
                        }
                    }
                }

                uiState.events.isNotEmpty() -> {
                    EventCardList(
                        showStatus = true,
                        events = uiState.events,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { event ->
                            if (event.status != "approved") {
                                navController.navigate("${NavigationRoutes.EDIT_EVENT}/${event.id}")
                            } else {
                                navController.navigate("${NavigationRoutes.PARTNER_EVENT_MANAGE}/${event.id}")
                            }
                        }
                    )
                }

                else -> {
                    Text(
                        text = "No events found.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}