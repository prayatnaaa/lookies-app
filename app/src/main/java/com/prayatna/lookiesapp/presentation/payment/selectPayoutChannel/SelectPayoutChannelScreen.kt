package com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.payment.PayoutChannel
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.state.SelectPayoutChannelUiEvent
import com.prayatna.lookiesapp.presentation.payment.selectPayoutChannel.state.SelectPayoutChannelUiState
import com.prayatna.lookiesapp.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPayoutChannelScreen(
    uiState: SelectPayoutChannelUiState,
    onEvent: (SelectPayoutChannelUiEvent) -> Unit,
    onChannelSelected: (PayoutChannel) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = { onEvent(SelectPayoutChannelUiEvent.OnBackClicked) },
                title = "Select Bank"
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { onEvent(SelectPayoutChannelUiEvent.OnSearchQueryChange(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Search bank name...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> CircularLoading(modifier = Modifier.align(Alignment.Center))
                    uiState.errorMessage != null -> {
                        Text(
                            text = uiState.errorMessage,
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    uiState.filteredChannels.isEmpty() -> {
                        Text(
                            text = "No banks found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(uiState.filteredChannels) { channel ->
                                PayoutChannelItem(
                                    channel = channel,
                                    onClick = { onChannelSelected(channel) }
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PayoutChannelItem(
    channel: PayoutChannel,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = channel.channelName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = channel.channelCode,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
