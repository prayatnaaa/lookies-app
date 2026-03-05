package com.prayatna.lookiesapp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.prayatna.lookiesapp.domain.model.user.Profile
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
        private val USER_ADDRESS_KEY = stringPreferencesKey("user_address")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val USER_URL_KEY = stringPreferencesKey("user_url")
        private val USER_BIO_KEY = stringPreferencesKey("user_bio")
        private val USER_FULL_NAME_KEY = stringPreferencesKey("user_full_name")
        private val USER_ROLE = stringPreferencesKey("user_role")
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

    suspend fun setRole(value: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ROLE] = value
        }
    }

    fun getRole(): Flow<String> {
        return context.dataStore.data
            .map { preference ->
                preference[USER_ROLE] ?: ""
            }
    }

    suspend fun setProfile(profile: Profile) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = profile.id ?: ""
            preferences[USER_URL_KEY] = profile.profileUrl ?: ""
            preferences[USERNAME_KEY] = profile.username ?: ""
            preferences[USER_ADDRESS_KEY] = profile.address ?: ""
            preferences[USER_BIO_KEY] = profile.bio ?: ""
            preferences[USER_FULL_NAME_KEY] = profile.fullName ?: ""
            preferences[USER_ROLE] = profile.role ?: ""
        }
    }

    fun getProfile(): Flow<Profile> {
        return context.dataStore.data
            .map { preference ->
                Profile(
                    id = preference[USER_ID_KEY] ?: "",
                    profileUrl = preference[USER_URL_KEY] ?: "",
                    username = preference[USERNAME_KEY] ?: "",
                    fullName = preference[USER_FULL_NAME_KEY] ?: "",
                    address = preference[USER_ADDRESS_KEY] ?: "",
                    bio = preference[USER_BIO_KEY] ?: "",
                    role = preference[USER_ROLE] ?: ""
                )
            }
    }

    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}