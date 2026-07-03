package com.prayatna.lookiesapp.data.repository

import android.util.Log
import coil.network.HttpException
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseAuthService
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.mapper.toDto
import com.prayatna.lookiesapp.domain.model.auth.LoginOutput
import com.prayatna.lookiesapp.domain.model.auth.RegisterInput
import com.prayatna.lookiesapp.domain.model.auth.RegisterOutput
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.extractSupabaseError
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val supabaseAuthService: SupabaseAuthService,
    private val userPreference: UserPreference
): AuthRepository {
    override fun listenUserSession(): DataResult<Flow<SessionStatus>> {
        return try {
            val response = supabaseAuthService.listenUserSession()
            DataResult.Success(response)
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        }
    }

    override suspend fun signIn(email: String, password: String): DataResult<LoginOutput> {
        return try {
            val response = supabaseAuthService.signIn(email = email, password = password)
            Log.d("SignIn", "Response: $response")
            if (response.success) {
                userPreference.setRole(response.role)
                DataResult.Success(response.toDomain())
            }
            else {
                Log.d("LoginTest", "Response: $response")
                DataResult.Error(response.message ?: "Unknown error")
            }
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (_: Exception) {
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
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            DataResult.Error(errorMsg)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
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
            } catch (_: Exception) {
                userPreference.logout()
                DataResult.Success(false)
            }

        } catch (e: Exception) {
            DataResult.Error("Failed to check session: ${e.localizedMessage}")
        }
    }



    override suspend fun logout(): DataResult<Any> {
        return try {
            val userId = auth.currentSessionOrNull()?.user?.id ?: DataResult.Error("Something went wrong!")
            postgrest["user_profiles"]
                .update({
                    set("fcm_token", "")
                }) {
                    filter {
                        eq("user_id", userId)
                    }
                }
            auth.signOut()
            userPreference.logout()
            DataResult.Success(Any())
        } catch (e: RestException) {
            val msg = extractSupabaseError(e.error)
            DataResult.Error(msg)
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message ?: "Network error")
        } catch (_: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override fun getRole(): String {
        return try {
            val response = supabaseAuthService.getRole()
            response
        } catch (e: RestException) {
            e.message.toString()
        } catch (e: HttpException) {
            val errorMsg = e.response.message
            errorMsg
        } catch (e: Exception) {
            e.message ?: "Something went wrong"
        }
    }
}