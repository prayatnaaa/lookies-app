package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface AuthRepository {
    val authToken: Flow<String?>
    suspend fun signIn(email: String, password: String): DataResult<String>
    suspend fun signUp(email: String, password: String): DataResult<String>
    suspend fun saveSession()
    suspend fun isSessionActive(): Boolean
    suspend fun getProfile(): Flow<DataResult<ProfileDto>>
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val postgrest: Postgrest,
    private val userPreference: UserPreference
): AuthRepository {

    override val authToken: Flow<String?> = userPreference.authTokenPreference

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
        userPreference.setAuthToken(accessToken as String)
    }

    override suspend fun isSessionActive(): Boolean {
        val token = auth.currentAccessTokenOrNull()
        return !token.isNullOrEmpty()
    }

    override suspend fun getProfile(): Flow<DataResult<ProfileDto>> = flow {
        try {
            emit(DataResult.Loading)
            val userId = auth.currentUserOrNull()?.id
            if (userId == null) {
                emit(DataResult.Error("User not logged in"))
                return@flow
            }

            val result = postgrest
                .from(table = "user_profiles")
                .select {
                    filter {
                        eq("user_id", userId.toString())
                    }
                }
                .decodeSingle<ProfileDto>()

            emit(DataResult.Success(result))

        } catch (e: SupabaseEncodingException) {
            emit(DataResult.Error("Something went wrong: ${e.localizedMessage}"))
        } catch (e: Exception) {
            emit(DataResult.Error("Something went wrong! Please check your connection!"))
        }
    }
}