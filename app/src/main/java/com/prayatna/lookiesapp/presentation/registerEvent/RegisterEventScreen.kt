package com.prayatna.lookiesapp.presentation.registerEvent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.prayatna.lookiesapp.presentation.components.registerEvent.BottomActionBar
import com.prayatna.lookiesapp.presentation.components.registerEvent.ConfirmPaintingsContent
import com.prayatna.lookiesapp.presentation.components.registerEvent.SelectPaintingContent
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventEvent

@Composable
fun RegisterEventScreen(
    viewModel: RegisterEventViewModel = hiltViewModel(),
    navController: NavController,
    eventId: Int
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    LaunchedEffect(eventId) {
        viewModel.onEvent(RegisterEventEvent.SetEventId(eventId))
    }

    Scaffold(
        topBar = {
            Column(Modifier.padding(16.dp).statusBarsPadding()) {
                Text(
                    text = if (state.currentStep == 1) "Langkah 1: Pilih Karya" else "Langkah 2: Review & Submit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kuota Event: ${state.selectedIds.size} / ${state.maxLimit}",
                    color = if (state.selectedIds.size > state.maxLimit) Color.Red else Color.Gray
                )
                LinearProgressIndicator(
                    progress = { if (state.currentStep == 1) 0.5f else 1f },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                )
            }
        },
        bottomBar = {
            BottomActionBar(state = state, onEvent = onEvent, onCancel = {
                navController.popBackStack()
            })
        }
    ) { padding ->
        Box(modifier = Modifier
            .padding(padding)
            .fillMaxSize()) {
            if (state.currentStep == 1) {
                SelectPaintingContent(state, onEvent)
            } else {
                ConfirmPaintingsContent(state, onEvent)
            }
        }
    }
}