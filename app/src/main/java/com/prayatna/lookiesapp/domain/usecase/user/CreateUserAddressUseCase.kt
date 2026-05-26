package com.prayatna.lookiesapp.domain.usecase.user

import com.prayatna.lookiesapp.domain.model.user.CreateUserAddressInput
import com.prayatna.lookiesapp.domain.model.user.UserAddress
import com.prayatna.lookiesapp.domain.repository.UserRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateUserAddressUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(address: CreateUserAddressInput): DataResult<UserAddress> {
        return userRepository.createUserAddress(address)
    }
}