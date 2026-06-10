package com.prayatna.lookiesapp.presentation.components.eventlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.domain.model.event.Event

@Composable
fun EventCardList(
    events: List<Event>,
    modifier: Modifier = Modifier,
    onClick: (Event) -> Unit,
    showStatus: Boolean = false,
    showTicketPrice: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(8.dp)
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(events) { _, event ->
            EventCard(
                showTicketPrice = showTicketPrice,
                showStatus = showStatus,
                event = event,
                onClick = { onClick(event) }
            )
        }
    }
}
