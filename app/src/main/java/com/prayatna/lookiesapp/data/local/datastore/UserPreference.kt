package com.prayatna.lookiesapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "user_preference")

@Singleton
class UserPreference @Inject constructor(@ApplicationContext private val context: Context) {

    companion object {
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    }

    suspend fun setDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preference ->
            preference[DARK_MODE_KEY] = isDarkMode
        }
    }

     val darkModePreference: Flow<Boolean> = context.dataStore.data
         .map { preference ->
            preference[DARK_MODE_KEY] ?: false
        }

    suspend fun setAuthToken(token: String) {
        context.dataStore.edit { preference ->
            preference[AUTH_TOKEN_KEY] = token
        }
    }

    val authTokenPreference: Flow<String?> = context.dataStore.data
        .map { preference ->
            preference[AUTH_TOKEN_KEY]
        }

    suspend fun setUserEmail(email: String) {
        context.dataStore.edit { preference ->
            preference[USER_EMAIL_KEY] = email
        }
    }

    val userEmailPreference: Flow<String?> = context.dataStore.data
        .map { preference ->
            preference[USER_EMAIL_KEY]
        }

    suspend fun logout() {
        context.dataStore.edit { preference ->
            preference[AUTH_TOKEN_KEY] = ""
            preference[USER_EMAIL_KEY] = ""
        }
    }
}