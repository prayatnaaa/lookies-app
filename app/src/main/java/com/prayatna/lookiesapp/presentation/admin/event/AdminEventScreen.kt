package com.prayatna.lookiesapp.presentation.admin.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.prayatna.lookiesapp.presentation.components.partner.FilterItem

@Composable
fun AdminEventScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AdminEventViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.events) {
        viewModel.getEvents()
    }

    Scaffold(
        topBar = { BackTopBar(onBackClick = {
            navController.popBackStack()
        }) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            ) {

                item {
                    FilterItem(
                        title = "all",
                        selected = uiState.status == null,
                        onClick = { viewModel.onStatusSelected(null) }
                    )
                }

                items(EventStatus.entries) { status ->
                    FilterItem(
                        title = status.name.lowercase(),
                        selected = uiState.status == status,
                        onClick = { viewModel.onStatusSelected(status) }
                    )
                }
            }
            Box(
                modifier = modifier
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
                            showStatus = true,
                            showTicketPrice = true,
                            events = uiState.events,
                            onClick = { event ->
                                navController.navigate("detail_event/${event.id}")
                            }
                        )
                    } else -> {
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
}
