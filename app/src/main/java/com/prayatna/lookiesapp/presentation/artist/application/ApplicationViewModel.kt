package com.prayatna.lookiesapp.presentation.artist.application

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prayatna.lookiesapp.domain.model.artist.ArtistApplication
import com.prayatna.lookiesapp.domain.repository.ArtistRepository
import com.prayatna.lookiesapp.utils.DataResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplicationViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
): ViewModel(){
    private val _applicationStatus = MutableStateFlow<DataResult<String>>(DataResult.Idle)
    val applicationStatus = _applicationStatus.asStateFlow()

    var portoUrlValue by mutableStateOf("")
        private set

    var motiveLetterValue by mutableStateOf("")

    var businessTypeValue by mutableStateOf("")

    fun onPortoChange(portoUrlValue: String) {
        this.portoUrlValue = portoUrlValue
    }

    fun onMotiveLetterChange(motiveLetterValue: String) {
        this.motiveLetterValue = motiveLetterValue
    }

    fun onBusinessTypeChange(businessTypeValue: String) {
        this.businessTypeValue = businessTypeValue
    }

    fun artistApplication() {
        _applicationStatus.value = DataResult.Loading
        viewModelScope.launch {
            val artistApplication = ArtistApplication(
                userId = "",
                portoUrl = portoUrlValue,
                motiveLetter = motiveLetterValue,
                businessType = businessTypeValue,
            )
            val result = artistRepository.artistApplication(
                artistApplication = artistApplication
            )
            _applicationStatus.value = result
        }
    }

}