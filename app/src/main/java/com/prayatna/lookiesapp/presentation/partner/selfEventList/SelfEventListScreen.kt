package com.prayatna.lookiesapp.presentation.partner.selfEventList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventlist.EventCardList
import com.prayatna.lookiesapp.presentation.components.loading.EventListLoading
import com.prayatna.lookiesapp.presentation.partner.selfEventList.state.SelfEventListUiEvent
import com.prayatna.lookiesapp.presentation.partner.selfEventList.state.SelfEventListUiState
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.NavigationRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelfEventListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SelfEventListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("snackbar_message", null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(snackbarMessage?.value) {
        snackbarMessage?.value?.let { message ->
            snackbarHostState.showSnackbar(message)
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("snackbar_message")
        }
    }

    val statusFilters = listOf(
        "published" to "Published",
        "rejected" to "Rejected",
        "pending_validation" to "Pending",
        "completed" to "Ended",
        "ongoing" to "Ongoing",
        "cancelled" to "Cancelled"
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BackTopBar(
                onBackClick = { navController.popBackStack() },
                title = "My Events"
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                        value = uiState.searchQuery,
                        onValueChange = { viewModel.onEvent(SelfEventListUiEvent.OnSearchQueryChange(it)) },
                        placeholder = { Text("Search by title...") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    viewModel.onEvent(SelfEventListUiEvent.OnSearchQueryChange(""))
                                    viewModel.onEvent(SelfEventListUiEvent.OnSearchTriggered)
                                }) {
                                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.onEvent(SelfEventListUiEvent.OnSearchTriggered)
                                focusManager.clearFocus()
                            }
                        )
                    )

                    IconButton(
                        onClick = { viewModel.onEvent(SelfEventListUiEvent.OnFilterSheetToggle) },
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = "Filters",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.selectedStatus == null,
                            onClick = { viewModel.onEvent(SelfEventListUiEvent.OnStatusSelected(null)) },
                            label = { Text("All") },
                            leadingIcon = if (uiState.selectedStatus == null) {
                                { Icon(imageVector = Icons.Default.Done, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                            } else null
                        )
                    }
                    items(statusFilters) { (slug, label) ->
                        val isSelected = uiState.selectedStatus == slug
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onEvent(SelfEventListUiEvent.OnStatusSelected(slug)) },
                            label = { Text(label) },
                            leadingIcon = if (isSelected) {
                                { Icon(imageVector = Icons.Default.Done, contentDescription = null, modifier = Modifier.size(FilterChipDefaults.IconSize)) }
                            } else null
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        EventListLoading(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }

                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = uiState.errorMessage ?: "Unknown error occurred.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.onEvent(SelfEventListUiEvent.OnRetry) }) {
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
                                if (event.status == "pending_validation" || event.status == "rejected") {
                                    navController.navigate("${NavigationRoutes.EDIT_EVENT}/${event.id}")
                                } else {
                                    navController.navigate("${NavigationRoutes.PARTNER_EVENT_MANAGE}/${event.id}")
                                }
                            }
                        )
                    }

                    else -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No events found.",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Try adjusting your filters.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.isFilterSheetOpen) {
        SelfEventFilterBottomSheet(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            onDismiss = { viewModel.onEvent(SelfEventListUiEvent.OnFilterSheetToggle) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelfEventFilterBottomSheet(
    uiState: SelfEventListUiState,
    onEvent: (SelfEventListUiEvent) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Advanced Filters",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text("Event Format", style = MaterialTheme.typography.labelLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.formats) { format ->
                    FilterChip(
                        selected = uiState.selectedFormatId == format.id,
                        onClick = { onEvent(SelfEventListUiEvent.OnFormatSelected(format.id)) },
                        label = { Text(format.name) }
                    )
                }
            }

            Text("Event Type", style = MaterialTheme.typography.labelLarge)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.types) { type ->
                    FilterChip(
                        selected = uiState.selectedTypeId == type.id,
                        onClick = { onEvent(SelfEventListUiEvent.OnTypeSelected(type.id)) },
                        label = { Text(type.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onEvent(SelfEventListUiEvent.OnResetFilters) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
                
                Button(
                    onClick = { onEvent(SelfEventListUiEvent.OnApplyFilters) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply Filters")
                }
            }
        }
    }
}
