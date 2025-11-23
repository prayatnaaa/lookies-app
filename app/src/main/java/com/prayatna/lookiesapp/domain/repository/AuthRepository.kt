package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.response.auth.LoginResponse
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.gotrue.user.UserInfo

interface AuthRepository {
    suspend fun signIn(email: String, password: String): DataResult<LoginResponse>
    suspend fun signUp(email: String, password: String): DataResult<UserInfo?>
    suspend fun isSessionActive(): DataResult<Boolean>
    suspend fun logout(): DataResult<Any>
    fun getRole(): String
}