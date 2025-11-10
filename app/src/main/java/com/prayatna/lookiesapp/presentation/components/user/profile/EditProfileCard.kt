package com.prayatna.lookiesapp.presentation.components.user.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite

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
    ElevatedCard (
        modifier = modifier.padding(horizontal = 12.dp),
        shape = RoundedCornerShape( 8.dp),
        colors = CardDefaults.cardColors(BlackCharcoal)
    ) {
        Column (
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Spacer( modifier = modifier.height(12.dp))

            EditTextField(
                value = usernameValue,
                labelFor = "Username",
                onValueChange = onUsernameChange
            )

            EditTextField(
                value = fullNameValue,
                labelFor = "Full name",
                onValueChange = onFullNameChange
            )

            EditTextField(
                value = addressValue,
                labelFor = "Address",
                onValueChange = onAddressChange
            )

            EditTextField(
                value = bioValue,
                labelFor = "Bio",
                onValueChange = onBioChange
            )

        }
    }
}

@Composable
fun EditTextField(
    modifier: Modifier = Modifier,
    labelFor: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column (
        modifier = modifier.padding(bottom = 32.dp)
    ) {
        Text(
            text = labelFor,
            modifier = modifier.padding(bottom = 8.dp),
            style = TextStyle(
                color = PureWhite,
                fontSize = 16.sp
            )
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            maxLines = if (labelFor != "Bio") 1 else 5,
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
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileCardPreview() {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ){
        EditProfileCard(
            usernameValue = "",
            fullNameValue = "",
            addressValue = "",
            bioValue = "",
            onUsernameChange = {},
            onFullNameChange = {},
            onAddressChange = {},
            onBioChange = {}
        )
    }
}