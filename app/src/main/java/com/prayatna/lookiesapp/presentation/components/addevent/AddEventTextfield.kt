package com.prayatna.lookiesapp.presentation.components.addevent

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.Grey
import com.prayatna.lookiesapp.ui.theme.PureWhite

@Composable
fun AddEventTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Column (
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 4.dp),
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
                color = if (!isFocused) Grey else PureWhite
            )
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                },
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PureWhite,
                focusedContainerColor = DarkGrey,
                focusedLabelColor = PureWhite,
                cursorColor = PureWhite,
                focusedTrailingIconColor = PureWhite,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite
            )
        )
    }
}