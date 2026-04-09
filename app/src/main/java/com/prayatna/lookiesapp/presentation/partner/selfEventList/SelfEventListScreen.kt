package com.prayatna.lookiesapp.presentation.partner.selfEventList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.eventlist.EventCardList
import com.prayatna.lookiesapp.presentation.components.loading.EventListLoading
import com.prayatna.lookiesapp.utils.Constants
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun SelfEventListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SelfEventListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    val statusFilters = listOf(
        "published" to "Published",
        "rejected" to "Draft",
        "pending_validation" to "Pending",
        "completed" to "Ended"
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = { BackTopBar(
            onBackClick = {
                navController.popBackStack()
            }, title = "My Events") }
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
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChange(it) },
                    placeholder = { Text("Search by title...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = {
                                viewModel.onSearchQueryChange("")
                                viewModel.onSearchTriggered()
                            }) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.onSearchTriggered()
                            focusManager.clearFocus()
                        }
                    )
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.selectedStatus == null,
                            onClick = { viewModel.onFilterSelected(null) },
                            label = { Text("All") },
                            leadingIcon = if (uiState.selectedStatus == null) {
                                { Icon(imageVector = Icons.Default.Done, contentDescription = null, modifier = Modifier.padding(end = 4.dp)) }
                            } else null
                        )
                    }
                    items(statusFilters) { (slug, label) ->
                        val isSelected = uiState.selectedStatus == slug
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.onFilterSelected(slug) },
                            label = { Text(label) },
                            leadingIcon = if (isSelected) {
                                { Icon(imageVector = Icons.Default.Done, contentDescription = null, modifier = Modifier.padding(end = 4.dp)) }
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
                                if (event.status != "published") {
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
                            if (uiState.searchQuery.isNotEmpty() || uiState.selectedStatus != null) {
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
    }
}