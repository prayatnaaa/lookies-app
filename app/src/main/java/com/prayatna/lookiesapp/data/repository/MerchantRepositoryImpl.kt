package com.prayatna.lookiesapp.data.repository

import android.content.Context
import android.net.Uri
import com.prayatna.lookiesapp.data.mapper.toDomain
import com.prayatna.lookiesapp.data.mapper.toDto
import com.prayatna.lookiesapp.data.remote.api.supabase.SupabaseMerchantService
import com.prayatna.lookiesapp.domain.mapper.toData
import com.prayatna.lookiesapp.domain.mapper.toDomain
import com.prayatna.lookiesapp.domain.model.merchant.EditMerchantBankAccountInput
import com.prayatna.lookiesapp.domain.model.merchant.EditMerchantInput
import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMemberInput
import com.prayatna.lookiesapp.domain.model.merchant.InviteMerchantMemberOutput
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBankAccount
import com.prayatna.lookiesapp.domain.model.merchant.MerchantBusiness
import com.prayatna.lookiesapp.domain.model.merchant.MerchantMember
import com.prayatna.lookiesapp.domain.model.merchant.MerchantProfile
import com.prayatna.lookiesapp.domain.model.shipment.Shipment
import com.prayatna.lookiesapp.domain.model.user.BusinessAddress
import com.prayatna.lookiesapp.domain.repository.MerchantRepository
import com.prayatna.lookiesapp.utils.DataResult
import com.prayatna.lookiesapp.utils.compressImage
import com.prayatna.lookiesapp.utils.extractSupabaseError
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.exceptions.NotFoundRestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.exceptions.UnauthorizedRestException
import javax.inject.Inject

class MerchantRepositoryImpl @Inject constructor(
    private val supabaseMerchantService: SupabaseMerchantService,
    @param:ApplicationContext private val context: Context
): MerchantRepository {
    override suspend fun inviteMerchantMember(input: InviteMerchantMemberInput): DataResult<InviteMerchantMemberOutput> {
        return try {
            val response = supabaseMerchantService.inviteMerchantMember(input.toData())
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getMerchantAddress(merchantBusinessId: String): DataResult<BusinessAddress> {
        return try {
            val response = supabaseMerchantService.getMerchantAddress(merchantBusinessId)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getPublicMerchantProfile(businessId: String): DataResult<MerchantBusiness> {
        return try {
            val response = supabaseMerchantService.getPublicMerchantProfile(businessId)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getMerchantMembersByMerchantId(merchantBusinessId: String): DataResult<List<MerchantMember>> {
        return try {
            val res = supabaseMerchantService.getMerchantMembersByMerchantId(merchantBusinessId)
            DataResult.Success(data = res.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getMerchantProfile(businessId: String): DataResult<MerchantProfile> {
        return try {
            val response = supabaseMerchantService.getMerchantProfile(id = businessId)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getMerchantMembers(userId: String?): DataResult<List<MerchantMember>> {
        return try {
            val response = supabaseMerchantService.getMerchantMembers(userId)
            DataResult.Success(response.map { it.toDomain() })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getMerchantBankAccounts(merchantAccountId: String): DataResult<List<MerchantBankAccount>> {
        return try {
            val response = supabaseMerchantService.getMerchantBankAccounts(merchantAccountId)
            DataResult.Success(response.map { it.toDomain()  })
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun updateShipmentStatus(shipmentId: String, status: String): DataResult<Shipment> {
        return try {
            val response = supabaseMerchantService.updateShipmentStatus(shipmentId, status)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun createTrackingNumberShipment(shipmentId: String, trackingNumber: String): DataResult<Shipment> {
        return try {
            val response = supabaseMerchantService.createTrackingNumberShipment(shipmentId, trackingNumber)
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            val eMessage = extractSupabaseError(e.error)
            DataResult.Error(eMessage)
        }
    }

    override suspend fun getShipmentsByMerchantId(merchantId: String, status: String?): DataResult<List<Shipment>> {
        return try {
            val result = supabaseMerchantService.getShipmentsByMerchantId(merchantId, status)
            DataResult.Success(result.map { it.toDomain() })
        } catch (e: RestException) {
             when(e) {
                is NotFoundRestException -> {
                    DataResult.Error("Shipment not found")
                }
                is UnauthorizedRestException -> {
                    DataResult.Error("Unauthorized")
                }
                else -> {
                    val eMessage = extractSupabaseError(e.error)
                    DataResult.Error(eMessage)
                }
            }
        }
    }

    override suspend fun uploadShipmentArrivalProof(shipmentId: String, image: Uri): DataResult<String> {
        return try {
            val compressedBytes = image.compressImage(context, 500_000L)
                ?: return DataResult.Error("Failed to compress image")
            val result = supabaseMerchantService.uploadArrivalProof(shipmentId, compressedBytes)
            DataResult.Success(result)
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "Something went wrong")
        }
    }

    override suspend fun updateMerchantBusiness(
        id: String,
        input: EditMerchantInput
    ): DataResult<MerchantBusiness> {
        return try {
            val response = supabaseMerchantService.updateMerchantBusiness(id, input.toDto())
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            DataResult.Error(extractSupabaseError(e.error))
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }

    override suspend fun updateMerchantBankAccount(
        id: String,
        input: EditMerchantBankAccountInput
    ): DataResult<MerchantBankAccount> {
        return try {
            val response = supabaseMerchantService.updateMerchantBankAccount(id, input.toDto())
            DataResult.Success(response.toDomain())
        } catch (e: RestException) {
            DataResult.Error(extractSupabaseError(e.error))
        } catch (e: Exception) {
            DataResult.Error(e.message ?: "An unexpected error occurred")
        }
    }
}
