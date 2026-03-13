package com.prayatna.lookiesapp.presentation.components.registerBusiness

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionEvent
import com.prayatna.lookiesapp.presentation.user.partnerSubmission.state.PartnerSubmissionFormState
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun PartnerSubmissionContent(
    modifier: Modifier = Modifier,
    formState: PartnerSubmissionFormState,
    isLoading: Boolean,
    onEvent: (PartnerSubmissionEvent) -> Unit,
    onPickFileClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    val totalFields = 6
    val filledSections by remember(formState) {
        derivedStateOf {
            var count = 0
            if (formState.useLoginEmail || formState.businessEmail.isNotBlank()) count++
            if (formState.legalName.isNotBlank() && formState.tradingName.isNotBlank()) count++
            if (formState.streetLine1.isNotBlank() && formState.city.isNotBlank()) count++
            if (formState.ownerFirstName.isNotBlank() && formState.ownerLastName.isNotBlank()) count++
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
            text = "Complete all sections below to register your business as a Lookies partner.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        FormSectionCard(
            title = "Account Preference",
            icon = Icons.Default.Email
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = formState.useLoginEmail,
                    onCheckedChange = { onEvent(PartnerSubmissionEvent.UseLoginEmailChanged(it)) }
                )
                Text(
                    text = "Use current login email for business",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            AnimatedVisibility(
                visible = !formState.useLoginEmail,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
            ) {
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.businessEmail,
                    onValueChange = { onEvent(PartnerSubmissionEvent.BusinessEmailChanged(it)) },
                    label = { Text("Business Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        FormSectionCard(
            title = "Business Details",
            icon = Icons.Default.Apartment
        ) {
            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.legalName,
                onValueChange = { onEvent(PartnerSubmissionEvent.LegalNameChanged(it)) },
                label = { Text("Legal Name (Ltd / LLC)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.tradingName,
                onValueChange = { onEvent(PartnerSubmissionEvent.TradingNameChanged(it)) },
                label = { Text("Store / Brand Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.description,
                onValueChange = { onEvent(PartnerSubmissionEvent.DescriptionChanged(it)) },
                label = { Text("Short Description") },
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
                onValueChange = { onEvent(PartnerSubmissionEvent.AddressChanged(it)) },
                label = { Text("Street Address") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.city,
                    onValueChange = { onEvent(PartnerSubmissionEvent.CityChanged(it)) },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.province,
                    onValueChange = { onEvent(PartnerSubmissionEvent.ProvinceChanged(it)) },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.postalCode,
                onValueChange = { onEvent(PartnerSubmissionEvent.PostalCodeChanged(it)) },
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
            title = "Owner Information",
            icon = Icons.Default.Person
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.ownerFirstName,
                    onValueChange = { onEvent(PartnerSubmissionEvent.OwnerFirstNameChanged(it)) },
                    label = { Text("First Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.ownerLastName,
                    onValueChange = { onEvent(PartnerSubmissionEvent.OwnerLastNameChanged(it)) },
                    label = { Text("Last Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.ownerEmail,
                onValueChange = { onEvent(PartnerSubmissionEvent.OwnerEmailChanged(it)) },
                label = { Text("Owner Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.ownerPhone,
                onValueChange = { onEvent(PartnerSubmissionEvent.OwnerPhoneChanged(it)) },
                label = { Text("Owner Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
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

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.bankName,
                    onValueChange = { onEvent(PartnerSubmissionEvent.BankNameChanged(it)) },
                    label = { Text("Bank Name") },
                    placeholder = { Text("e.g. BCA, BNI, Mandiri") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                OutlinedTextField(
                    shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                    value = formState.bankCode,
                    onValueChange = { onEvent(PartnerSubmissionEvent.BankCodeChanged(it)) },
                    label = { Text("Bank Code") },
                    placeholder = { Text("e.g. BCA") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }

            OutlinedTextField(
                shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE),
                value = formState.accountNumber,
                onValueChange = { onEvent(PartnerSubmissionEvent.AccountNumberChanged(it)) },
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
                onValueChange = { onEvent(PartnerSubmissionEvent.AccountHolderNameChanged(it)) },
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
                        text = "Upload ID Card / Business License / Registration Number",
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
                        Text("Change File")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onEvent(PartnerSubmissionEvent.Submit) },
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
                    text = "Register Business",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}