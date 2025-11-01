package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.response.auth.LoginResponse
import com.prayatna.lookiesapp.utils.DataResult

interface AuthRepository {
    suspend fun signIn(email: String, password: String): DataResult<LoginResponse>
    suspend fun signUp(email: String, password: String): DataResult<String>
    suspend fun saveSession()
    suspend fun isSessionActive(): DataResult<String>
    suspend fun logout(): DataResult<Any>
}