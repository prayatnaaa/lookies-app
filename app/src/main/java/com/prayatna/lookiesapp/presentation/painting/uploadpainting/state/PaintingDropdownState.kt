package com.prayatna.lookiesapp.presentation.painting.uploadpainting.state

import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute

data class PaintingDropdownState(
    val artStyles: List<PaintingAttribute> = emptyList(),
    val mediums: List<PaintingAttribute> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

val NONE_ITEM = PaintingAttribute(
    id = "",
    code = "",
    name = "None"
)
