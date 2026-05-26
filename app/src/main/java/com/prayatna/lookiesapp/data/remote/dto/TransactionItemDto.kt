package com.prayatna.lookiesapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TransactionItemDto(
    val id: String,

    @SerialName("item_type")
    val itemType: String,

    val quantity: Int,

    @SerialName("unit_price")
    val unitPrice: Double,

    val subtotal: Double,

    val details: ItemDetailDto? = null,
    @SerialName("item_ref_id")
    val itemRefId: String
)

@Serializable
data class ItemDetailDto(
    val title: String,

    @SerialName("image")
    val imageUrl: String,

    @SerialName("artist_name")
    val artistName: String? = null,

    @SerialName("event_date")
    val eventDate: String? = null
)