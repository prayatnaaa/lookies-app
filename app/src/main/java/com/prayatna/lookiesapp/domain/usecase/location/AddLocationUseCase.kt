package com.prayatna.lookiesapp.domain.usecase.location

import com.prayatna.lookiesapp.domain.model.location.AddLocationParams
import com.prayatna.lookiesapp.domain.model.location.Location
import com.prayatna.lookiesapp.domain.repository.LocationRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(data: AddLocationParams): DataResult<Location> {
        return locationRepository.addLocation(name = data.name, url = data.url)
    }
}