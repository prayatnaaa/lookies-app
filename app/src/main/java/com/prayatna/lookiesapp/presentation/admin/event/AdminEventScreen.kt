package com.prayatna.lookiesapp.presentation.admin.event

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventlist.EventCardList
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading

@Composable
fun AdminEventScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AdminEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getEvents()
    }

    Scaffold(
        topBar = { BackTopBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                uiState.isLoading -> {
                    CircularLoading()
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
                        events = uiState.events,
                        modifier = Modifier.fillMaxSize(),
                        onClick = { event ->
                            navController.navigate("detail_event/${event.id}")
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
