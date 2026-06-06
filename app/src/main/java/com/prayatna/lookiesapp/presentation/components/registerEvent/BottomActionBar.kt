package com.prayatna.lookiesapp.presentation.components.registerEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventEvent
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventUiState

@Composable
fun BottomActionBar(state: RegisterEventUiState, onEvent: (RegisterEventEvent) -> Unit, onCancel: ()-> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (state.currentStep == 2) {
            OutlinedButton(
                onClick = { onEvent(RegisterEventEvent.PrevStep) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Previous")
            }
        }

        if (state.currentStep == 1) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancel")
            }
        }

        Button (
            onClick = {
                if (state.currentStep == 1) onEvent(RegisterEventEvent.NextStep)
                else if (state.currentStep == 2) onEvent(RegisterEventEvent.Submit)
            },
            modifier = Modifier.weight(1f),
            enabled = state.selectedIds.isNotEmpty() || !state.isLoading
        ) {
            Text(if (state.currentStep == 1) "Continue" else "Send registration")
        }
    }
}