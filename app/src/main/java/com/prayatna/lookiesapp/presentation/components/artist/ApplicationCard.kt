package com.prayatna.lookiesapp.presentation.components.artist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.LightGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite

@Composable
fun ArtistApplicationCard(
    modifier: Modifier = Modifier,
    businessTypeValue: String,
    portoUrlValue: String? = null,
    motiveLetterValue: String? = null,
    onPortoChange: (String) -> Unit,
    onMotiveLetterChange: (String) -> Unit,
    onBusinessTypeChange: (String) -> Unit
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = BlackCharcoal),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Join us!",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontSize = 24.sp,
                    color = PureWhite
                ),
            )

            Spacer(modifier = modifier.height(32    .dp))

            ApplicationDropDown( selectedText = businessTypeValue, onOptionSelected = onBusinessTypeChange)

            Spacer(modifier = modifier.height(16.dp))

            ApplicationTextField(
                value = motiveLetterValue ?: "",
                onValueChange = onMotiveLetterChange,
                labelFor = "Motive letter Url (Optional)"
            )

            Spacer(modifier = modifier.height(16.dp))

            ApplicationTextField(
                value = portoUrlValue ?: "",
                onValueChange = onPortoChange,
                labelFor = "PortoFolio url (Optional)"
            )

            Spacer(modifier = modifier.height(16.dp))
        }

    }
}

@Composable
fun ApplicationTextField(
    modifier: Modifier = Modifier,
    labelFor: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = labelFor,
            style =
                TextStyle(
                    color = PureWhite,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    fontSize = 12.sp
                )
            )

        Spacer( modifier = modifier.height(4.dp))
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
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

        Spacer( modifier = modifier.height(4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationDropDown(
    modifier: Modifier = Modifier,
    selectedText: String = "",
    options: List<String> = listOf(
        "Artist or Single Artist Studio",
        "Art Advisor",
        "Art Dealer",
        "Art Fair",
        "Art Gallery",
        "Auction House",
        "Design Gallery",
        "Institution / Museum"
    ),
    onOptionSelected: (String) -> Unit
) {
    var isDropDownExpanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(selectedText) }

    Column {
        Text(text = "Business type",
            style =
                TextStyle(
                    color = PureWhite,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 1.sp,
                    fontSize = 12.sp
                )
        )

        Spacer( modifier = modifier.height(4.dp))
        ExposedDropdownMenuBox(
            expanded = isDropDownExpanded,
            onExpandedChange = { isDropDownExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isDropDownExpanded) },
                modifier = modifier
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable
                    )
                    .fillMaxWidth(),
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

            ExposedDropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = { isDropDownExpanded = false },
                modifier = modifier.background(
                    color = BlackCharcoal
                ),
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        colors = MenuItemColors(
                            textColor = PureWhite,
                            disabledTextColor = PureWhite,
                            disabledLeadingIconColor = LightGrey,
                            disabledTrailingIconColor = LightGrey,
                            leadingIconColor = PureWhite,
                            trailingIconColor = PureWhite,
                        ),
                        text = { Text(option) },
                        onClick = {
                            selectedOption = option
                            isDropDownExpanded = false
                            onOptionSelected(option)
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ApplicationDropDownPreview() {
    ArtistApplicationCard(
        businessTypeValue = "",
        onPortoChange = {},
        onMotiveLetterChange = {},
        onBusinessTypeChange = {}
    )
}
