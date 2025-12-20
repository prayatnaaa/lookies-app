package com.prayatna.lookiesapp.presentation.registerEvent.state

sealed class RegisterEventEvent {
    data class SetEventId(val id: Int) : RegisterEventEvent()
    data object NextStep : RegisterEventEvent()
    data object PrevStep : RegisterEventEvent()
    data class TogglePainting(val id: Int) : RegisterEventEvent()
    data object Submit : RegisterEventEvent()
    data object DismissError : RegisterEventEvent()
}