package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return userRepository.isDarkMode()
    }
}
