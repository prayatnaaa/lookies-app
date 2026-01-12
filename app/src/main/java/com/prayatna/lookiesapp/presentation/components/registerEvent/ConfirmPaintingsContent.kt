package com.prayatna.lookiesapp.presentation.components.registerEvent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.painting.PaintingCard
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventEvent
import com.prayatna.lookiesapp.presentation.registerEvent.state.RegisterEventUiState

@Composable
fun ConfirmPaintingsContent(
    state: RegisterEventUiState,
    onEvent: (RegisterEventEvent) -> Unit
) {
    val selectedPaintings = state.allPaintings.filter { it.id in state.selectedIds }

    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        item {
            Text(
                "Pastikan data benar. Anda bisa mengubah harga khusus untuk event ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(selectedPaintings) { painting ->
//            Card(
//                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
//                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
//                elevation = CardDefaults.cardElevation(2.dp)
//            ) {

                PaintingCard(
                    paintingUrl = painting.paintingUrl,
                    name = painting.title,
                    price = painting.price,
                    onClick = {},
                    artistName = ""
                )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                OutlinedButton(
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.error),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    onClick = {}) {
                    Text("Remove")
                }
                Spacer(Modifier.width(8.dp))

                OutlinedButton(onClick = {}) {
                    Text("Edit")
                }
            }

//                Column(Modifier.padding(16.dp)) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        CustomAsyncImage(
//                            model = painting.paintingUrl,
//                            modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp)),
//                            contentDescription = null
//                        )
//                        Spacer(Modifier.width(12.dp))
//                        Text(painting.title, fontWeight = FontWeight.SemiBold)
//                        Spacer(Modifier.weight(1f))
//                        IconButton(onClick = { onEvent(RegisterEventEvent.TogglePainting(painting.id)) }) {
//                            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Red)
//                        }
//                    }
//
//                    Spacer(Modifier.height(12.dp))
//
//                     Input Harga (Override)
//                    OutlinedTextField(
//                        value = currentPrice,
//                        onValueChange = { onEvent(JoinEventEvent.UpdatePrice(painting.id, it)) },
//                        label = { Text("Harga Jual di Event (Rp)") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        modifier = Modifier.fillMaxWidth(),
//                        singleLine = true
//                    )
//                }
//            }
        }
    }
}