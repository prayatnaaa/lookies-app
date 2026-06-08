package com.prayatna.lookiesapp.presentation.event.eventlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventlist.EventCardList
import com.prayatna.lookiesapp.presentation.components.eventlist.EventFilterBottomSheet
import com.prayatna.lookiesapp.presentation.components.loading.EventListLoading
import com.prayatna.lookiesapp.presentation.event.eventlist.state.EventListEvent
import com.prayatna.lookiesapp.presentation.event.eventlist.state.EventListUiState
import com.prayatna.lookiesapp.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    uiState: EventListUiState,
    onEvent: (EventListEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

//    val statusFilters = listOf(
//        "published" to "Published",
//        "pending_validation" to "Pending",
//        "completed" to "Ended"
//    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { BackTopBar(onBackClick = {
            onEvent(EventListEvent.OnBackClicked)
        }, title = "Events") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // ── Search & Filter Section ──────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Search bar
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = { onEvent(EventListEvent.OnSearchQueryChange(it)) },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Search event...") },
                        singleLine = true,
                        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            if (uiState.searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    onEvent(EventListEvent.OnSearchQueryChange(""))
                                    onEvent(EventListEvent.OnSearchTriggered)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                onEvent(EventListEvent.OnSearchTriggered)
                                focusManager.clearFocus()
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
                    )

                    // Advanced Filter Button
                    IconButton(
                        onClick = { onEvent(EventListEvent.OnFilterSheetToggle) },
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

                // Status filter chips
//                LazyRow(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    contentPadding = PaddingValues(horizontal = 4.dp)
//                ) {
//                    item {
//                        FilterChip(
//                            selected = uiState.selectedStatus == null,
//                            onClick = { onEvent(EventListEvent.OnStatusSelected(null)) },
//                            label = { Text("All") },
//                            leadingIcon = if (uiState.selectedStatus == null) {
//                                {
//                                    Icon(
//                                        imageVector = Icons.Default.Done,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
//                                    )
//                                }
//                            } else null
//                        )
//                    }
//                    items(statusFilters) { (slug, label) ->
//                        val isSelected = uiState.selectedStatus == slug
//                        FilterChip(
//                            selected = isSelected,
//                            onClick = { onEvent(EventListEvent.OnStatusSelected(slug)) },
//                            label = { Text(label) },
//                            leadingIcon = if (isSelected) {
//                                {
//                                    Icon(
//                                        imageVector = Icons.Default.Done,
//                                        contentDescription = null,
//                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
//                                    )
//                                }
//                            } else null
//                        )
//                    }
//                }

                // Sort toggle
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onEvent(EventListEvent.OnSortToggled) }
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FilterList,
                        contentDescription = "Sort",
                        modifier = Modifier.size(18.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (uiState.isTicketPriceAscending) "Price: Low → High" else "Price: High → Low",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // ── Content Section ──────────────────────────────────────────
            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        EventListLoading(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    uiState.errorMessage != null -> {
                        ErrorState(
                            message = uiState.errorMessage,
                            onRetry = { onEvent(EventListEvent.OnRetry) },
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    uiState.events.isNotEmpty() -> {
                        EventCardList(
                            events = uiState.events,
                            modifier = Modifier.fillMaxSize(),
                            onClick = { event ->
                                onEvent(EventListEvent.OnDetailEventClicked(event.id))
                            }
                        )
                    }

                    else -> {
                        EmptyState(
                            query = uiState.searchQuery,
                            hasFilters = uiState.selectedStatus != null || uiState.selectedLocation != null,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }
    }

    if (uiState.isFilterSheetOpen) {
        EventFilterBottomSheet(
            uiState = uiState,
            onEvent = onEvent,
            onDismiss = { onEvent(EventListEvent.OnFilterSheetToggle) }
        )
    }
}

@Composable
private fun EmptyState(
    query: String,
    hasFilters: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = when {
                query.isNotEmpty() -> "No results found for \"$query\""
                hasFilters -> "No events match the selected filter."
                else -> "No events available."
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (query.isNotEmpty() || hasFilters) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Try adjusting your search or filters.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}
