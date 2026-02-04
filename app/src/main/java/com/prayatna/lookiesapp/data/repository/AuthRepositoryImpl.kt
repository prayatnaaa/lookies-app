package com.prayatna.lookiesapp.data.repository

import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseAuthService
import com.prayatna.lookiesapp.data.remote.dto.response.auth.LoginResponse
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.auth.RegisterInput
import com.prayatna.lookiesapp.domain.model.auth.RegisterOutput
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val supabaseAuthService: SupabaseAuthService,
    private val userPreference: UserPreference
): AuthRepository {

    override suspend fun signIn(email: String, password: String): DataResult<LoginResponse> {
        return try {
            val response = supabaseAuthService.signIn(email = email, password = password)
            if (response.success) {
                userPreference.setRole(response.role)
                DataResult.Success(response)
            }
            else {
                DataResult.Error(response.message ?: "Unknown error")
            }
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override suspend fun signUp(data: RegisterInput): DataResult<RegisterOutput> {
        return try {
            val result = supabaseAuthService.signUp(request = data.toDto())
            if (result.status != "success") {
                DataResult.Error(result.message)
            } else {
                DataResult.Success(result.toDomain())
            }
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }

    override suspend fun isSessionActive(): DataResult<Boolean> {
        return try {
            val currentSession = auth.currentSessionOrNull()

            if (currentSession != null) {
                return DataResult.Success(true)
            }

            return try {
                auth.refreshCurrentSession()
                DataResult.Success(true)
            } catch (e: Exception) {
                userPreference.logout()
                DataResult.Success(false)
            }

        } catch (e: Exception) {
            DataResult.Error("Failed to check session: ${e.localizedMessage}")
        }
    }



    override suspend fun logout(): DataResult<Any> {
        return try {
            auth.clearSession()
            userPreference.logout()
            DataResult.Success(Any())
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override fun getRole(): String {
        return try {
            val response = supabaseAuthService.getRole()
            response
        } catch (e: Exception) {
            e.message.toString()
        } catch (e: RestException) {
            e.message.toString()
        }
    }
}