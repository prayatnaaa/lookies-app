package com.prayatna.lookiesapp.presentation.components.painting

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import com.prayatna.lookiesapp.domain.model.painting.AddPaintingParams
import com.prayatna.lookiesapp.domain.model.painting.PaintingAttribute
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.event.UploadPaintingEvent
import com.prayatna.lookiesapp.presentation.painting.uploadpainting.state.UploadPaintingUiState
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun UploadPaintingForm(
    params: AddPaintingParams,
    selectedImage: Uri?,
    uiState: UploadPaintingUiState,
    onEvent: (UploadPaintingEvent) -> Unit,
    artStyles: List<PaintingAttribute>,
    mediums: List<PaintingAttribute>,
    topBar: @Composable () -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = topBar,
        contentWindowInsets = WindowInsets.ime,
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = { onEvent(UploadPaintingEvent.ValidateAndSubmit) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Upload Painting",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) { padding ->

        if (uiState is UploadPaintingUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.5f))
                    .clickable(enabled = false) {},
                contentAlignment = Alignment.Center
            ) {
                CircularLoading()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE))
                    .clickable { onEvent(UploadPaintingEvent.OnImageSelected(null)) },
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImage != null) {
                        Image(
                            painter = rememberAsyncImagePainter(selectedImage),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Tap to add image",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            OutlinedTextField(
                value = params.title,
                onValueChange = { onEvent(UploadPaintingEvent.OnTitleChange(it)) },
                label = { Text("Artwork Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
            )

            OutlinedTextField(
                value = if (params.price == 0.0) "" else params.price.toString() ,
                onValueChange = { onEvent(UploadPaintingEvent.OnSubjectChange(it)) },
                label = { Text("Price") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
            )

            OutlinedTextField(
                value = params.description,
                onValueChange = { onEvent(UploadPaintingEvent.OnDescriptionChange(it)) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = if (params.dimensionHeight == 0.0) "" else params.dimensionHeight.toString(),
                    onValueChange = { onEvent(UploadPaintingEvent.OnHeightChange(it)) },
                    label = { Text("Height (cm)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
                )
                OutlinedTextField(
                    value = if (params.dimensionWidth == 0.0) "" else params.dimensionWidth.toString(),
                    onValueChange = { onEvent(UploadPaintingEvent.OnWidthChange(it)) },
                    label = { Text("Width (cm)") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
                )
            }

            PaintingDropdown(
                label = "Medium",
                items = mediums,
                selectedItem = mediums.find { it.id == params.medium },
                onItemSelected = { onEvent(UploadPaintingEvent.OnMediumChange(it)) }
            )

            OutlinedTextField(
                value = params.yearCreated.takeIf { it > 0 }?.toString() ?: "",
                onValueChange = { onEvent(UploadPaintingEvent.OnYearChange(it)) },
                label = { Text("Year Created") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
            )

            PaintingDropdown(
                label = "Art Style (Optional)",
                items = artStyles,
                selectedItem = artStyles.find { it.id == params.artStyle },
                onItemSelected = { onEvent(UploadPaintingEvent.OnArtStyleChange(it)) }
            )

            OutlinedTextField(
                value = params.subject ?: "",
                onValueChange = { onEvent(UploadPaintingEvent.OnSubjectChange(it)) },
                label = { Text("Subject (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)
            )

            Spacer(modifier = Modifier.height(80.dp))

        }

        if (uiState is UploadPaintingUiState.Success) {
            AlertDialog(
                onDismissRequest = { onEvent(UploadPaintingEvent.DismissDialog) },
                title = { Text("Upload Success") },
                text = { Text(uiState.message) },
                confirmButton = {
                    TextButton(onClick = { onEvent(UploadPaintingEvent.DismissDialog) }) {
                        Text("Close")
                    }
                }
            )
        }

        if (uiState is UploadPaintingUiState.Error) {
            AlertDialog(
                onDismissRequest = { onEvent(UploadPaintingEvent.DismissDialog) },
                title = { Text("Upload Failed") },
                text = { Text(uiState.message) },
                confirmButton = {
                    TextButton(onClick = { onEvent(UploadPaintingEvent.DismissDialog) }) {
                        Text("Try Again")
                    }
                },
                containerColor = MaterialTheme.colorScheme.errorContainer,
                titleContentColor = MaterialTheme.colorScheme.onErrorContainer,
                textContentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}