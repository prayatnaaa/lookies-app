package com.prayatna.lookiesapp.presentation.notification.state

import com.prayatna.lookiesapp.domain.model.user.UserNotification

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<UserNotification> = emptyList(),
    val errorMessage: String? = null
)
