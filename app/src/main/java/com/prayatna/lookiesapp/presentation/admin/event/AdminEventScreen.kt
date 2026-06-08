package com.prayatna.lookiesapp.presentation.admin.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.components.partner.FilterItem
import com.prayatna.lookiesapp.utils.Constants
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
        topBar = { BackTopBar(title = "Review events", onBackClick = {
            navController.popBackStack()
        }) },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            OutlinedTextField(
                value = uiState.title,
                onValueChange = viewModel::onTitleChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
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
                    if (uiState.title.isNotEmpty()) {
                        IconButton(onClick = {
                            viewModel.onTitleChanged("")
                            viewModel.getEvents()
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
                        focusManager.clearFocus()
                        viewModel.getEvents()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                navController.navigate("${NavigationRoutes.ADMIN_DETAIL_EVENT}/${event.id}")
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
