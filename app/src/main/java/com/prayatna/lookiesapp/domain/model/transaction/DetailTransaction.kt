package com.prayatna.lookiesapp.domain.model.transaction

import com.prayatna.lookiesapp.domain.model.ticket.Ticket

data class DetailTransaction(
    val transaction: Transaction,
    val tickets: List<Ticket>
)