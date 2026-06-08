package com.prayatna.lookiesapp.presentation.components.registerEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventEvent
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventUiState

@Composable
fun BottomActionBar(
    state: RegisterEventUiState,
    onEvent: (RegisterEventEvent) -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = {
                    if (state.currentStep == 0) onCancel()
                    else onEvent(RegisterEventEvent.PrevStep)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (state.currentStep == 0) "Cancel" else "Back")
            }

            Button(
                onClick = {
                    if (state.currentStep == 2) onEvent(RegisterEventEvent.Submit)
                    else onEvent(RegisterEventEvent.NextStep)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading
            ) {
                Text(
                    when (state.currentStep) {
                        0 -> "Agree"
                        2 -> "Submit"
                        else -> "Next"
                    }
                )
            }
        }
    }
}
