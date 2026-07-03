package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.domain.model.user.UserNotification
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class GetNotificationsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): DataResult<List<UserNotification>> {
        return userRepository.getNotifications()
    }
}
