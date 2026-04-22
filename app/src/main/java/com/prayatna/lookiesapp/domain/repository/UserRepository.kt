package com.prayatna.lookiesapp.domain.repository

import android.net.Uri
import com.prayatna.lookiesapp.data.remote.dto.ProfileDto
import com.prayatna.lookiesapp.data.remote.dto.response.user.RoleApplicationResponse
import com.prayatna.lookiesapp.domain.model.user.ArtistApplicationInput
import com.prayatna.lookiesapp.domain.model.user.CreateUserAddressInput
import com.prayatna.lookiesapp.domain.model.user.RoleApplicationInput
import com.prayatna.lookiesapp.domain.model.user.UserAddress
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun updateFcmToken(token: String):
            DataResult<Unit>
    fun getProfile():
            Flow<DataResult<ProfileDto>>
    suspend fun editProfile(
        fullName: String,
        bio: String,
        address: String,
        username: String
    ):
            DataResult<String>
    suspend fun editProfileImage(image: ByteArray):
            DataResult<String>
    suspend fun registerBusiness(
        request: RoleApplicationInput,
        kycFile: Uri,
        fileName: String
    ): DataResult<RoleApplicationResponse>
    suspend fun getUserAddresses():
            DataResult<List<UserAddress>>
    suspend fun createUserAddress(address: CreateUserAddressInput):
            DataResult<UserAddress>
    suspend fun becomeArtist(
        request: ArtistApplicationInput,
        kycFile: Uri,
        fileName: String
    ): DataResult<RoleApplicationResponse>
    suspend fun getFcmToken():
            String?
}