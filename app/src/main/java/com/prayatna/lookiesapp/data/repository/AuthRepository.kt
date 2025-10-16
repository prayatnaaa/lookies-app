package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.flow.first
import javax.inject.Inject

interface AuthRepository {
    suspend fun signIn(email: String, password: String): DataResult<String>
    suspend fun signUp(email: String, password: String): DataResult<String>
    suspend fun saveSession()
    suspend fun isSessionActive(): DataResult<String>
    suspend fun logout(): DataResult<Any>
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val userPreference: UserPreference
): AuthRepository {

    override suspend fun signIn(email: String, password: String): DataResult<String> {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            saveSession()
            DataResult.Success("You are logged in")
        } catch (e: SupabaseEncodingException) {
            DataResult.Error(e.localizedMessage as String)
        } catch (e: Exception) {
            Log.e("LOGIN", "${e.message}")
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override suspend fun signUp(email: String, password: String): DataResult<String> {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            DataResult.Success("You are registered! Please check your e-mail to confirm.")
        } catch (e: SupabaseEncodingException) {
            e.localizedMessage?.let { Log.e("REGISTER-TEST", it) }
            DataResult.Error(e.localizedMessage as String)
        } catch (e: Exception) {
            Log.e("REGISTER-TEST", "$e.message")
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override suspend fun saveSession() {
        val accessToken = auth.currentAccessTokenOrNull()
        val userId = auth.currentUserOrNull()?.id

        if (accessToken.isNullOrEmpty() || userId.isNullOrEmpty()) {
            return
        }

        userPreference.setAuthToken(accessToken)
        userPreference.setUserId(userId)
    }


    override suspend fun isSessionActive(): DataResult<String> {
        return try {
            val token = userPreference.authTokenPreference.first()
            val currentSession  = auth.currentSessionOrNull()

            if (currentSession != null) {
                return DataResult.Success("You are logged in")
            }

            if (token.isNullOrEmpty()) {
                return DataResult.Error("You are not logged in!")
            }

            auth.refreshCurrentSession()

            val newToken = auth.currentSessionOrNull()?.accessToken
            if (newToken.isNullOrEmpty()) {
                userPreference.logout()
                return DataResult.Error("Session expired. Please log in again.")
            }

            userPreference.setAuthToken(newToken)
            DataResult.Success("User logged in")

        } catch (e: SupabaseEncodingException) {
            DataResult.Error("Error decoding session: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("SESSION", "Error checking session: ${e.message}")
            DataResult.Error("Something went wrong while checking your session.")
        }
    }

    override suspend fun logout(): DataResult<Any> {
        return try {
            auth.clearSession()
            userPreference.logout()
            DataResult.Success(Any())
        } catch (e: SupabaseEncodingException) {
            DataResult.Error("Error decoding session: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("SESSION", "Error checking session: ${e.message}")
            DataResult.Error("Something went wrong while checking your session.")
        }
    }
}