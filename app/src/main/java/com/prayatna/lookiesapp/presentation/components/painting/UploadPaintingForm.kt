package com.prayatna.lookiesapp.presentation.components.painting

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.event.UploadPaintingEvent
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.state.UploadPaintingUiState
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading

@Composable
fun UploadPaintingForm(
    params: AddPaintingParams,
    selectedImage: Uri?,
    uiState: UploadPaintingUiState,
    onEvent: (UploadPaintingEvent) -> Unit,
    artStyles: List<PaintingAttribute>,
    mediums: List<PaintingAttribute>,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(UploadPaintingEvent.ValidateAndSubmit) },
                containerColor = MaterialTheme.colorScheme.surface,
            ) { Text("Upload") }
        }
    ) { padding ->

        // 🔥 LOADING INDICATOR -------------------------------------------------
        if (uiState is UploadPaintingUiState.Loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularLoading()
            }
        }

        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clickable { onEvent(UploadPaintingEvent.OnImageSelected(null)) }, // handled at Screen
                contentAlignment = Alignment.Center
            ) {
                if (selectedImage != null)
                    Image(
                        painter = rememberAsyncImagePainter(selectedImage),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                else
                    Text("Tap to upload image")
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = params.title,
                onValueChange = { onEvent(UploadPaintingEvent.OnTitleChange(it)) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = params.description,
                onValueChange = { onEvent(UploadPaintingEvent.OnDescriptionChange(it)) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Row {
                OutlinedTextField(
                    value = if (params.dimensionHeight == 0.0) "" else params.dimensionHeight.toString(),
                    onValueChange = { onEvent(UploadPaintingEvent.OnHeightChange(it)) },
                    label = { Text("Height") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = if (params.dimensionWidth == 0.0) "" else params.dimensionWidth.toString(),
                    onValueChange = { onEvent(UploadPaintingEvent.OnWidthChange(it)) },
                    label = { Text("Width") },
                    modifier = Modifier.weight(1f)
                )
            }

            PaintingDropdown(
                label = "Medium",
                items = mediums,
                selectedItem = mediums.find { it.id == params.medium },
                onItemSelected = {
                    onEvent(UploadPaintingEvent.OnMediumChange(it))
                }
            )

            OutlinedTextField(
                value = params.yearCreated.takeIf { it > 0 }?.toString() ?: "",
                onValueChange = { onEvent(UploadPaintingEvent.OnYearChange(it)) },
                label = { Text("Year Created") },
                modifier = Modifier.fillMaxWidth()
            )

            PaintingDropdown(
                label = "Art Style (Optional)",
                items = artStyles,
                selectedItem = artStyles.find { it.id == params.artStyle },
                onItemSelected = {
                    onEvent(UploadPaintingEvent.OnArtStyleChange(it))
                }
            )

            OutlinedTextField(
                value = params.subject ?: "",
                onValueChange = { onEvent(UploadPaintingEvent.OnSubjectChange(it)) },
                label = { Text("Subject (Optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 🔥 Success Popup ------------------------------------------------------
        if (uiState is UploadPaintingUiState.Success) {
            AlertDialog(
                title = { Text("Success") },
                text = { Text(uiState.message) },
                onDismissRequest = { onEvent(UploadPaintingEvent.DismissDialog) },
                confirmButton = {
                    Button(onClick = { onEvent(UploadPaintingEvent.DismissDialog) }) {
                        Text("Close")
                    }
                }
            )
        }

        // 🔥 Error Popup --------------------------------------------------------
        if (uiState is UploadPaintingUiState.Error) {
            AlertDialog(
                title = { Text("Error") },
                text = { Text(uiState.message) },
                onDismissRequest = { onEvent(UploadPaintingEvent.DismissDialog) },
                confirmButton = {
                    Button(onClick = { onEvent(UploadPaintingEvent.DismissDialog) }) {
                        Text("Ok")
                    }
                }
            )
        }
    }
}
