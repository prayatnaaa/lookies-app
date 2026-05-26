package com.prayatna.lookiesapp.domain.model.transaction

data class TransactionItem(
    val id: String,
    val itemType: String,
    val quantity: Int,
    val unitPrice: Double,
    val subtotal: Double,
    val details: ItemDetail? = null,
    val itemRefId: String
)

data class ItemDetail(
    val title: String,
    val imageUrl: String,
    val artistName: String? = null,
    val eventDate: String? = null
)