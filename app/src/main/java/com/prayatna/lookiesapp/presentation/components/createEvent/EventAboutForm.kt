package com.prayatna.lookiesapp.presentation.components.createEvent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.presentation.components.CustomTextField

@Composable
fun EventAboutForm(
    eventDescription: String,
    onEventDescriptionChange: (String) -> Unit
) {
    Column {
        Text(
            text = "About",
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
        CustomTextField(
            textFieldModifier = Modifier.height(200.dp),
            value = eventDescription,
            onValueChange = onEventDescriptionChange,
            label = "Event description",
            singleLine = false,
            maxLines = 5
        )
    }
}