package com.prayatna.lookiesapp.presentation.chat.privateChat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.message.Message
import com.prayatna.lookiesapp.domain.model.message.MessageMetadata
import com.prayatna.lookiesapp.presentation.chat.privateChat.state.PrivateChatEvent
import com.prayatna.lookiesapp.presentation.chat.privateChat.state.PrivateChatUiState
import com.prayatna.lookiesapp.presentation.components.CustomAsyncImage
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.forum.ChatInputBar
import com.prayatna.lookiesapp.utils.formatChatTime

@Composable
fun PrivateChatScreen(
    state: PrivateChatUiState,
    onEvent: (PrivateChatEvent) -> Unit,
    isMerchant: Boolean
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = state.otherPartyName.ifEmpty { "Chat" },
                onBackClick = { onEvent(PrivateChatEvent.OnBackClicked) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (state.isLoading && state.messages.isEmpty()) {
                    CircularLoading(modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.messages) { message ->
                            val isMine = if (isMerchant) {
                                message.senderType == "merchant"
                            } else {
                                message.senderType == "user"
                            }
                            PrivateMessageItem(
                                message = message,
                                isMine = isMine && message.senderUserId == state.currentUserId
                            )
                        }
                    }
                }
            }

            // Pending Metadata Preview
            state.pendingMetadata?.let { metadata ->
                MetadataPreviewCard(
                    metadata = metadata,
                    onClearClicked = { onEvent(PrivateChatEvent.OnClearMetadata) }
                )
            }

            ChatInputBar(
                text = state.currentInputString,
                onTextChanged = { onEvent(PrivateChatEvent.InputChanged(it)) },
                onSendClick = { onEvent(PrivateChatEvent.SendMessage) },
                isSending = state.isSending,
                isEditing = false,
                onCancelEdit = {},
                isMemberReadOnly = false
            )
        }
    }
}

@Composable
private fun MetadataPreviewCard(
    metadata: MessageMetadata,
    onClearClicked: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomAsyncImage(
                model = metadata.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Referencing ${metadata.type.replaceFirstChar { it.uppercase() }}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = metadata.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            IconButton(onClick = onClearClicked) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear metadata",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun PrivateMessageItem(
    message: Message,
    isMine: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Column(
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Surface(
                color = if (isMine) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isMine) 16.dp else 4.dp,
                    bottomEnd = if (isMine) 4.dp else 16.dp
                )
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Message Metadata Attachment
                    message.metadata?.let { metadata ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            color = Color.Black.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CustomAsyncImage(
                                    model = metadata.imageUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(6.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = metadata.title,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = metadata.type.replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Text(
                        text = message.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isMine) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Text(
                        text = formatChatTime(message.sentAt.toString()),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isMine) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f) else Color.Gray,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}
