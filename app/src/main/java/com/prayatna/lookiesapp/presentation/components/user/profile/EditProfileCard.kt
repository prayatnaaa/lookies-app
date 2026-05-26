package com.prayatna.lookiesapp.presentation.components.user.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun EditProfileCard(
    modifier: Modifier = Modifier,
    usernameValue: String,
    fullNameValue: String,
    addressValue: String,
    bioValue: String,
    onUsernameChange: (String) -> Unit,
    onFullNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            EditTextField(
                value = usernameValue,
                label = "Username",
                icon = Icons.Filled.Person,
                onValueChange = onUsernameChange,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            EditTextField(
                value = fullNameValue,
                label = "Full Name",
                icon = Icons.Outlined.Badge,
                onValueChange = onFullNameChange,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
            )

            EditTextField(
                value = addressValue,
                label = "Address",
                icon = Icons.Filled.Home,
                onValueChange = onAddressChange,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )

            EditTextField(
                value = bioValue,
                label = "Bio",
                icon = Icons.Outlined.Description,
                onValueChange = onBioChange,
                isSingleLine = false,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences)
            )
        }
    }
}

@Composable
fun EditTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: ImageVector? = null,
    isSingleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        leadingIcon = if (icon != null) {
            { Icon(imageVector = icon, contentDescription = null) }
        } else null,
        singleLine = isSingleLine,
        minLines = if (isSingleLine) 1 else 3,
        maxLines = if (isSingleLine) 1 else 5,
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Preview(showBackground = true)
@Composable
fun EditProfileCardPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center
    ) {
        EditProfileCard(
            usernameValue = "pratanu",
            fullNameValue = "Pratanu Presidya",
            addressValue = "Denpasar, Bali",
            bioValue = "Digital Artist & Software Engineer enthusiast.",
            onUsernameChange = {},
            onFullNameChange = {},
            onAddressChange = {},
            onBioChange = {}
        )
    }
}