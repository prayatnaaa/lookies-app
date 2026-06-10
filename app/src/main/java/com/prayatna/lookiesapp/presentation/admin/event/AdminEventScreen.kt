package com.prayatna.lookiesapp.presentation.admin.event

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventlist.EventCardList
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.partner.FilterItem
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun AdminEventScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: AdminEventViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val snackbarMessage =
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<String?>(
                "snackbar_message",
                null
            )
            ?.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getEvents()
    }

    LaunchedEffect(snackbarMessage?.value) {
        snackbarMessage?.value?.let { message ->
            snackbarHostState.showSnackbar(
                message,
                withDismissAction = true
            )

            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("snackbar_message")
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        },
        topBar = { BackTopBar(title = "Review Events", onBackClick = {
            navController.popBackStack()
        }) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // ── Search Bar ──────────────────────────────────────────
            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("Search events by title...") },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (uiState.title.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.onTitleChanged("")
                            viewModel.getEvents()
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        viewModel.getEvents()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )

            // ── Advanced Filters Section ────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Refine Search",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // 1. Status Filter
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    item {
                        FilterItem(
                            title = "All Status",
                            selected = uiState.status == null,
                            onClick = { viewModel.onStatusSelected(null) }
                        )
                    }
                    items(EventStatus.entries) { status ->
                        FilterItem(
                            title = status.name.lowercase().replace("_", " ").replaceFirstChar { it.uppercase() },
                            selected = uiState.status == status,
                            onClick = { viewModel.onStatusSelected(status) }
                        )
                    }
                }

                // 2. Type Filter
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    item {
                        FilterItem(
                            title = "All Types",
                            selected = uiState.selectedType == null,
                            onClick = { viewModel.onTypeSelected(null) }
                        )
                    }
                    items(uiState.eventTypes) { type ->
                        FilterItem(
                            title = type.name,
                            selected = uiState.selectedType?.id == type.id,
                            onClick = { viewModel.onTypeSelected(type) }
                        )
                    }
                }

                // 3. Format Filter
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    item {
                        FilterItem(
                            title = "All Formats",
                            selected = uiState.selectedFormat == null,
                            onClick = { viewModel.onFormatSelected(null) }
                        )
                    }
                    items(uiState.eventFormats) { format ->
                        FilterItem(
                            title = format.name,
                            selected = uiState.selectedFormat?.id == format.id,
                            onClick = { viewModel.onFormatSelected(format) }
                        )
                    }
                }
            }

            // ── Event List Content ──────────────────────────────────
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                when {
                    uiState.isLoading && uiState.events.isEmpty() -> {
                        CircularLoading()
                    }

                    uiState.errorMessage != null && uiState.events.isEmpty() -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage ?: "Unknown error occurred.",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.retry() },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Retry Connection")
                            }
                        }
                    }

                    uiState.events.isNotEmpty() -> {
                        EventCardList(
                            showStatus = true,
                            showTicketPrice = true,
                            events = uiState.events,
                            contentPadding = PaddingValues(16.dp),
                            onClick = { event ->
                                navController.navigate("${NavigationRoutes.ADMIN_DETAIL_EVENT}/${event.id}")
                            }
                        )
                    } else -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No events found",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Try adjusting your filters or search query.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.onTitleChanged("")
                                    viewModel.onStatusSelected(null)
                                    viewModel.onTypeSelected(null)
                                    viewModel.onFormatSelected(null)
                                },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Reset All Filters")
                            }
                        }
                    }
                }
            }
        }
    }
}
