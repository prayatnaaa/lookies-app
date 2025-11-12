package com.prayatna.lookiesapp.domain.repository

import com.prayatna.lookiesapp.domain.model.location.Location
import com.prayatna.lookiesapp.utils.DataResult

interface LocationRepository {
    suspend fun getLocationsById(): DataResult<List<Location>>
    suspend fun addLocation(name: String, url: String): DataResult<Location>
}