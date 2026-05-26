package com.prayatna.lookiesapp.domain.usecase.event

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.event.CreateEventParams
import com.prayatna.lookiesapp.domain.model.event.Event
import com.prayatna.lookiesapp.domain.repository.EventRepository
import com.prayatna.lookiesapp.utils.DataResult
import javax.inject.Inject

class CreateEventUseCase @Inject constructor(
    private val repository: EventRepository,
) {
    suspend operator fun invoke(
        params: CreateEventParams,
        imageUri: Uri
    ): DataResult<Event> {
        return repository.createEvent(
            params = params,
            imageByte = imageUri
        )
    }
}
