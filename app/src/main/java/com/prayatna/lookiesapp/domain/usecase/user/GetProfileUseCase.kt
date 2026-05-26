package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.data.mapper.asDomainModel
import com.prayatna.lookiesapp.domain.model.user.Profile
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<DataResult<Profile>> {
        return repository.getProfile()
            .map { result ->
                when (result) {
                    is DataResult.Success -> DataResult.Success(result.data.asDomainModel())
                    is DataResult.Error -> DataResult.Error(result.error)
                    is DataResult.Loading -> DataResult.Loading
                    is DataResult.Idle -> DataResult.Idle
                }
            }
    }
}
