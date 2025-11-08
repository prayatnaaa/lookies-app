package com.prayatna.lookiesapp.presentation.components.detailevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.PureWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailEventBottomModal(
    modifier: Modifier = Modifier,
    onBuyButtonClick: () -> Unit,
    onDismiss: () -> Unit,
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange = 0..100
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Row (modifier = modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            AddTicketNumberField(
                value = value,
                onValueChange = onValueChange,
                range = range
            )

            ElevatedButton(
                onClick = onBuyButtonClick,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = BlackCharcoal,
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    modifier = modifier
                        .padding(4.dp),
                    text = "Buy now"
                )
            }
        }
    }
}


@Composable
fun AddTicketNumberField(
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    range: IntRange
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { if (value > range.first) onValueChange(value - 1) }
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = null
            )
        }

        TextField(
            value = value.toString(),
            onValueChange = { input ->
                val newValue = input.toIntOrNull()
                if (newValue != null && newValue in range) {
                    onValueChange(newValue)
                }
            },
            modifier = modifier.width(80.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        IconButton(
            onClick = { if (value < range.last) onValueChange(value + 1) }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }
}
