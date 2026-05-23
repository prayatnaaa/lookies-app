package com.prayatna.lookiesapp.presentation.painting.editpainting.state

import android.net.Uri
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute

data class EditPaintingUiState(
    val title: String = "",
    val description: String = "",
    val yearCreated: String = "",
    val price: String = "",
    val dimensionHeight: String = "",
    val dimensionWidth: String = "",
    val subject: String = "",
    val artistId: String = "",
    val selectedArtStyleId: String = "",
    val selectedMediumId: String = "",
    val bannerUri: Uri? = null,
    val existingImageUrl: String? = null,
    
    val artStyles: List<PaintingAttribute> = emptyList(),
    val mediums: List<PaintingAttribute> = emptyList(),
    
    val isLoading: Boolean = false,
    val isLoadingMeta: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
) {
    val isValid: Boolean get() = title.isNotBlank() && 
            yearCreated.isNotBlank() && 
            price.isNotBlank() && 
            dimensionHeight.isNotBlank() && 
            dimensionWidth.isNotBlank() && 
            selectedMediumId.isNotBlank() &&
            artistId.isNotBlank()
}
