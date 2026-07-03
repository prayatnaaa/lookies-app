package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.domain.repository.UserRepository
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(isDarkMode: Boolean) {
        userRepository.setDarkMode(isDarkMode)
    }
}
