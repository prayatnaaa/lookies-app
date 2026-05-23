package com.prayatna.lookiesapp.presentation.painting.editpainting

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.painting.editpainting.state.EditPaintingUiEvent
import com.prayatna.lookiesapp.presentation.painting.editpainting.state.EditPaintingUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPaintingScreen(
    uiState: EditPaintingUiState,
    onEvent: (EditPaintingUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { onEvent(EditPaintingUiEvent.ImageChanged(it)) }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = onBackClick,
                title = "Edit Painting"
            )
        },
        bottomBar = {
            Button(
                onClick = { onEvent(EditPaintingUiEvent.Submit) },
                enabled = uiState.isValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("Update Painting", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { innerPadding ->
        if (uiState.isLoading && uiState.title.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularLoading()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Image Section
                Text("Artwork Image", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    onClick = { imagePickerLauncher.launch("image/*") }
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        if (uiState.bannerUri != null) {
                            AsyncImage(
                                model = uiState.bannerUri,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (uiState.existingImageUrl != null) {
                            AsyncImage(
                                model = uiState.existingImageUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null, modifier = Modifier.size(48.dp))
                                Text("Change Image", style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }

                // Title
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { onEvent(EditPaintingUiEvent.TitleChanged(it)) },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Description
                OutlinedTextField(
                    value = uiState.description,
                    onValueChange = { onEvent(EditPaintingUiEvent.DescriptionChanged(it)) },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Year
                    OutlinedTextField(
                        value = uiState.yearCreated,
                        onValueChange = { onEvent(EditPaintingUiEvent.YearChanged(it)) },
                        label = { Text("Year") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // Price
                    OutlinedTextField(
                        value = uiState.price,
                        onValueChange = { onEvent(EditPaintingUiEvent.PriceChanged(it)) },
                        label = { Text("Price (IDR)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Height
                    OutlinedTextField(
                        value = uiState.dimensionHeight,
                        onValueChange = { onEvent(EditPaintingUiEvent.HeightChanged(it)) },
                        label = { Text("Height (cm)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // Width
                    OutlinedTextField(
                        value = uiState.dimensionWidth,
                        onValueChange = { onEvent(EditPaintingUiEvent.WidthChanged(it)) },
                        label = { Text("Width (cm)") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // Art Style
                DropdownSelector(
                    label = "Art Style",
                    selectedId = uiState.selectedArtStyleId,
                    items = uiState.artStyles.map { it.id to it.name },
                    onSelect = { onEvent(EditPaintingUiEvent.ArtStyleChanged(it)) }
                )

                // Medium
                DropdownSelector(
                    label = "Medium",
                    selectedId = uiState.selectedMediumId,
                    items = uiState.mediums.map { it.id to it.name },
                    onSelect = { onEvent(EditPaintingUiEvent.MediumChanged(it)) }
                )

                // Subject
                OutlinedTextField(
                    value = uiState.subject,
                    onValueChange = { onEvent(EditPaintingUiEvent.SubjectChanged(it)) },
                    label = { Text("Subject") },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("e.g. Portrait, Landscape, Still Life") }
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownSelector(
    label: String,
    selectedId: String,
    items: List<Pair<String, String>>,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedName = items.find { it.first == selectedId }?.second ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = selectedName,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { (id, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onSelect(id)
                        expanded = false
                    }
                )
            }
        }
    }
}
