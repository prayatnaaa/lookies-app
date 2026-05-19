package com.prayatna.lookiesapp.domain.model.painting

data class PaintingFilter(
    val artistId: String? = null,
    val medium: String? = null,
    val artStyle: String? = null,
    val subject: String? = null,
    val minYear: Int? = null,
    val maxYear: Int? = null,
    val status: String? = null,
    val sortBy: SortType? = null
)

enum class SortType {
    YEAR_ASC,
    YEAR_DESC,
    NAME,
    CREATED_AT
}