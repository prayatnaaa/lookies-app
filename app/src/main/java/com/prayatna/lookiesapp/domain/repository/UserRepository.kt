package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.domain.model.user.PartnerSubmissionParams
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(): Flow<DataResult<ProfileDto>>
    suspend fun editProfile(
        fullName: String,
        bio: String,
        address: String,
        username: String
    ): DataResult<String>
    suspend fun editProfileImage(image: ByteArray): DataResult<String>
}