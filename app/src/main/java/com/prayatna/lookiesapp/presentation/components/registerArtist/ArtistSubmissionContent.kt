package com.prayatna.lookiesapp.presentation.components.registerArtist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.presentation.components.registerBusiness.FormSectionCard
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionEvent
import com.prayatna.lookiesapp.presentation.user.artistSubmission.state.ArtistSubmissionFormState
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun ArtistSubmissionContent(
    modifier: Modifier = Modifier,
    formState: ArtistSubmissionFormState,
    isLoading: Boolean,
    onEvent: (ArtistSubmissionEvent) -> Unit,
    onPickFileClick: () -> Unit,
    onSelectBankClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    val totalFields = 4
    val filledSections by remember(formState) {
        derivedStateOf {
            var count = 0
            if (formState.fullName.isNotBlank() && formState.phoneNumber.isNotBlank()) count++
            if (formState.streetLine1.isNotBlank() && formState.city.isNotBlank()) count++
            if (formState.bankName.isNotBlank() && formState.accountNumber.isNotBlank() &&
                formState.bankCode.isNotBlank() && formState.accountHolderName.isNotBlank()
            ) count++
            if (formState.kycFileBytes != null) count++
            count
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Progress indicator
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Form Progress",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$filledSections / $totalFields sections",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { filledSections.toFloat() / totalFields },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                strokeCap = StrokeCap.Round,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Complete all sections below to register yourself as a Lookies Artist.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        FormSectionCard(
            title = "Personal Details",
            icon = Icons.Default.Person
        ) {
            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.fullName,
                onValueChange = { onEvent(ArtistSubmissionEvent.FullNameChanged(it)) },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.displayName,
                onValueChange = { onEvent(ArtistSubmissionEvent.DisplayNameChanged(it)) },
                label = { Text("Display / Stage Name (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.phoneNumber,
                onValueChange = { onEvent(ArtistSubmissionEvent.PhoneNumberChanged(it)) },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.bio,
                onValueChange = { onEvent(ArtistSubmissionEvent.BioChanged(it)) },
                label = { Text("Short Bio (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FormSectionCard(
            title = "Address & Contact",
            icon = Icons.Default.LocationOn
        ) {
            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.streetLine1,
                onValueChange = { onEvent(ArtistSubmissionEvent.AddressChanged(it)) },
                label = { Text("Street Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.city,
                    onValueChange = { onEvent(ArtistSubmissionEvent.CityChanged(it)) },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.province,
                    onValueChange = { onEvent(ArtistSubmissionEvent.ProvinceChanged(it)) },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.postalCode,
                onValueChange = { onEvent(ArtistSubmissionEvent.PostalCodeChanged(it)) },
                label = { Text("Postal Code") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FormSectionCard(
            title = "Bank Account",
            icon = Icons.Default.AccountBalance
        ) {
            Text(
                text = "Required for payment disbursement",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            OutlinedTextField(
                value = formState.bankName.ifBlank { "Select Bank" },
                onValueChange = {},
                readOnly = true,
                label = { Text("Bank") },
                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectBankClick() },
                enabled = false,
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.accountNumber,
                onValueChange = { onEvent(ArtistSubmissionEvent.AccountNumberChanged(it)) },
                label = { Text("Account Number") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.accountHolderName,
                onValueChange = { onEvent(ArtistSubmissionEvent.AccountHolderNameChanged(it)) },
                label = { Text("Account Holder Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FormSectionCard(
            title = "Legal Documents (KYC)",
            icon = Icons.Default.Description
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.UploadFile,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Upload ID Card / KTP",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "File: ${formState.kycFileName}",
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(8.dp),
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(onClick = onPickFileClick) {
                        Text("Choose Image")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onEvent(ArtistSubmissionEvent.Submit) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = !isLoading,
            shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.5.dp
                )
            } else {
                Text(
                    text = "Register as Artist",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
