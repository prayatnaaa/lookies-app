package com.prayatna.lookiesapp.presentation.components.user.partnerapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.LightGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite

@Composable
fun PartnerApplicationTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = TextStyle(
                color = BlackCharcoal,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        )
        Spacer(modifier = modifier.height(2.dp))
        OutlinedTextField(
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        color = LightGrey,
                        fontSize = 16.sp
                    )
                )
            },
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BlackCharcoal,
                focusedContainerColor = PureWhite,
                focusedLabelColor = BlackCharcoal,
                cursorColor = BlackCharcoal,
                focusedTrailingIconColor = BlackCharcoal,
                focusedTextColor = BlackCharcoal,
                unfocusedTextColor = DarkGrey
            )
        )
    }
}