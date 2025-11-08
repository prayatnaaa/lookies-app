package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseAuthApi
import com.prayatna.lookiesapp.data.remote.response.auth.LoginResponse
import com.prayatna.lookiesapp.domain.repository.AuthRepository
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.BadRequestRestException
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.NotFoundRestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import io.github.jan.supabase.exceptions.UnknownRestException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.user.UserInfo
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth,
    private val supabaseAuthApi: SupabaseAuthApi,
    private val userPreference: UserPreference
): AuthRepository {

    override suspend fun signIn(email: String, password: String): DataResult<LoginResponse> {
        return try {
             val response = supabaseAuthApi.signIn(email = email, password = password)
            if (response.success) {
                DataResult.Success(response)
            }
            else {
                DataResult.Error(response.message ?: "Unknown error")
            }
        } catch (e: RestException) {
            when (e) {
                is BadRequestRestException -> DataResult.Error(e.error)
                is NotFoundRestException -> DataResult.Error(e.error)
                is UnauthorizedRestException -> DataResult.Error(e.error)
                is UnknownRestException -> DataResult.Error(e.error)
            }
            DataResult.Error(e.message.toString())
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message.toString())
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
        }
    }

    override suspend fun signUp(email: String, password: String): DataResult<UserInfo?> {
        return try {
            val result = supabaseAuthApi.signUp(email = email, password = password)

            Log.d("AUTH", result.toString())
            DataResult.Success(result)
        } catch (e: RestException) {
            when (e) {
                is BadRequestRestException -> DataResult.Error(e.error)
                is NotFoundRestException -> DataResult.Error(e.error)
                is UnauthorizedRestException -> DataResult.Error(e.error)
                is UnknownRestException -> DataResult.Error(e.error)
            }
            DataResult.Error(e.message.toString())
        } catch (e: HttpRequestException) {
            DataResult.Error(e.message.toString())
        } catch (e: Exception) {
            DataResult.Error("Something went wrong! Please check your connection")
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
        } catch (e: SupabaseEncodingException) {
            DataResult.Error("Error decoding session: ${e.localizedMessage}")
        } catch (e: Exception) {
            Log.e("SESSION", "Error checking session: ${e.message}")
            DataResult.Error("Something went wrong while checking your session.")
        }
    }
}