package com.prayatna.lookiesapp.data.repository

import android.util.Log
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import javax.inject.Inject

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
}

class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth
): AuthRepository {

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d("REGISTER-TEST", "Email: $email, Pass: $password")
            true
        } catch (e: Exception) {
            Log.e("REGISTER-TEST", "$e.message")
            false
        }
    }

}