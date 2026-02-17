package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.dto.response.auth.LoginResponse
import com.prayatna.lookiesapp.domain.model.auth.RegisterInput
import com.prayatna.lookiesapp.domain.model.auth.RegisterOutput
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun listenUserSession(): DataResult<Flow<SessionStatus>>
    suspend fun signIn(email: String, password: String): DataResult<LoginResponse>
    suspend fun signUp(data: RegisterInput): DataResult<RegisterOutput>
    suspend fun isSessionActive(): DataResult<Boolean>
    suspend fun logout(): DataResult<Any>
    fun getRole(): String
}