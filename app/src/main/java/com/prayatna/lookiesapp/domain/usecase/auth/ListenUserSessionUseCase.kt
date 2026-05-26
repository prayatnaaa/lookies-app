package com.prayatna.lookiesapp.domain.usecase.auth

import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListenUserSessionUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): DataResult<Flow<SessionStatus>> {
        return authRepository.listenUserSession()
    }
}