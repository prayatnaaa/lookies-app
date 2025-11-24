package com.prayatna.lookiesapp.domain.usecase.auth

import com.prayatna.lookiesapp.data.local.datastore.UserPreference
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRoleUseCase @Inject constructor(
    private val userPreference: UserPreference
){
    operator fun invoke(): Flow<String> {
        return userPreference.getRole()
    }
}