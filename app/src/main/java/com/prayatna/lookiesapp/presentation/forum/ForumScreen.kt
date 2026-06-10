package com.prayatna.lookiesapp.presentation.forum

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.domain.model.message.ForumChannelMessagesView
import com.prayatna.lookiesapp.presentation.components.CustomAsyncImage
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.forum.state.ForumEvent
import com.prayatna.lookiesapp.presentation.forum.state.ForumUiState
import com.prayatna.lookiesapp.utils.formatChatTime
import kotlinx.coroutines.launch

@Composable
fun ForumScreen(
    isMemberReadOnly: Boolean,
    state: ForumUiState,
    onEvent: (ForumEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    
    // Find all pinned messages, newest first
    val pinnedMessages = remember(state.messages) {
        state.messages.filter { it.isPinned }.sortedByDescending { it.createdAt }
    }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = "Channel Chat ${isMemberReadOnly}",
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
        ) {
            // ── Multi-Pinned Message Header (Carousel) ──────────────────────
            AnimatedVisibility(
                visible = pinnedMessages.isNotEmpty(),
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                MultiPinnedMessageHeader(
                    messages = pinnedMessages,
                    onClick = { msg ->
                        val index = state.messages.indexOfFirst { it.id == msg.id }
                        if (index != -1) {
                            coroutineScope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (state.isLoading && state.messages.isEmpty()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else if (state.messages.isEmpty()) {
                    Text(
                        text = "No messages yet. Start the conversation!",
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.messages) { message ->
                            DiscordStyleMessageItem(
                                message = message,
                                isMine = message.senderId == state.currentUserId,
                                userRole = state.userRole,
                                onEvent = onEvent
                            )
                        }
                    }
                }
            }

            ChatInputBar(
                text = state.currentInputString,
                onTextChanged = { onEvent(ForumEvent.InputChanged(it)) },
                onSendClick = { onEvent(ForumEvent.SendMessage) },
                isSending = state.isSending,
                isEditing = state.editingMessage != null,
                onCancelEdit = { onEvent(ForumEvent.CancelEditing) },
                isMemberReadOnly = isMemberReadOnly
            )
        }
    }
}

@Composable
private fun MultiPinnedMessageHeader(
    messages: List<ForumChannelMessagesView>,
    onClick: (ForumChannelMessagesView) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { messages.size })

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 1.dp
    ) {
        Column {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val message = messages[page]
                Row(
                    modifier = Modifier
                        .clickable { onClick(message) }
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (messages.size > 1) "Pinned Message (${page + 1}/${messages.size})" else "Pinned Message",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = message.content,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            // Minimal pagination indicator
            if (messages.size > 1) {
                LinearProgressIndicator(
                    progress = { (pagerState.currentPage + 1).toFloat() / messages.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    strokeCap = StrokeCap.Butt
                )
            }
        }
    }
}

@Composable
fun DiscordStyleMessageItem(
    message: ForumChannelMessagesView,
    isMine: Boolean,
    userRole: String,
    onEvent: (ForumEvent) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        if (!isMine) {
            CustomAsyncImage(
                model = message.profilePictureUrl?.replace("http://172.21.179.110", "http://10.0.2.2") ?: "",
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(12.dp))
        }

        Column(
            horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
            modifier = Modifier
                .fillMaxWidth(0.85f)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .background(
                            if (isMine) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { showMenu = true }
                        .padding(12.dp)
                ) {
                    if (!isMine || message.isPinned) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (!isMine) {
                                Text(
                                    text = message.fullName.ifEmpty { "Unknown User" },
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                            
                            if (message.isPinned) {
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = "Pinned",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text("Pinned", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }

                    Text(
                        text = message.content,
                        color = if (isMine) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (message.editedAt != null) {
                            Text(
                                text = "edited",
                                fontSize = 9.sp,
                                color = if (isMine) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f) else Color.Gray
                            )
                            Spacer(Modifier.width(6.dp))
                        }
                        Text(
                            text = formatChatTime(message.createdAt),
                            fontSize = 10.sp,
                            color = if (isMine) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f) else Color.Gray
                        )
                    }
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    if (isMine) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                onEvent(ForumEvent.StartEditing(message))
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Edit, null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                            onClick = {
                                onEvent(ForumEvent.DeleteMessage(message.id))
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) }
                        )
                    }
                    
                    if (userRole == "admin" || userRole == "organizer") {
                        DropdownMenuItem(
                            text = { Text(if (message.isPinned) "Unpin" else "Pin") },
                            onClick = {
                                onEvent(ForumEvent.TogglePinMessage(message.id, message.isPinned))
                                showMenu = false
                            },
                            leadingIcon = { Icon(Icons.Default.PushPin, null) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChatInputBar(
    isMemberReadOnly: Boolean,
    text: String,
    onTextChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean,
    isEditing: Boolean,
    onCancelEdit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        if (isEditing) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.width(8.dp))
                    Text("Editing message", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onCancelEdit, modifier = Modifier.size(20.dp)) {
                    Icon(Icons.Default.Close, null, modifier = Modifier.size(16.dp))
                }
            }
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                enabled = !isMemberReadOnly && text.isNotBlank() && !isSending,
                value = text,
                onValueChange = onTextChanged,
                placeholder = { Text(if (!isMemberReadOnly) "Message..." else "This is read only") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(
                onClick = onSendClick,
                enabled = text.isNotBlank() && !isSending || !isMemberReadOnly,
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (text.isNotBlank() && !isSending) MaterialTheme.colorScheme.primary 
                        else Color.Gray.copy(alpha = 0.3f),
                        CircleShape
                    )
            ) {
                if (isSending) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        imageVector = if (isEditing) Icons.Default.Check else Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = if (text.isBlank()) Color.White else MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}
