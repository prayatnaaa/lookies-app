package com.prayatna.lookiesapp.data.repository

import android.util.Log
import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import com.prayatna.lookiesapp.utils.DataResult
import io.github.jan.supabase.exceptions.SupabaseEncodingException
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject

interface AuthRepository {
    suspend fun signIn(email: String, password: String): DataResult<String>
    suspend fun signUp(email: String, password: String): DataResult<String>
    suspend fun saveSession()
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
            DataResult.Error("${e.message}")
        } catch (e: Exception) {
            Log.e("LOGIN", "${e.message}")
            DataResult.Error("Something went wrong!")
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
            Log.e("REGISTER-TEST", "$e.message")
            DataResult.Error("${e.message}")
        } catch (e: Exception) {
            DataResult.Error("Something went wrong!")
        }
    }

    override suspend fun saveSession() {
        val accessToken = auth.currentAccessTokenOrNull()
        userPreference.setAuthToken(accessToken as String)
    }


}