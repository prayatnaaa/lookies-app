package com.prayatna.lookiesapp.domain.usecase.partner

import com.prayatna.lookiesapp.domain.repository.PartnerRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDetailPartnerUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
){
    operator fun invoke(id: Int) = flow {
        emit(DataResult.Loading)
        emit(partnerRepository.getDetailPartner(id))
    }
}