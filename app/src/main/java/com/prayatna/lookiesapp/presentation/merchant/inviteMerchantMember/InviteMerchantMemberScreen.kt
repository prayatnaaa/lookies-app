package com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.presentation.components.backtopbar.BackTopBar
import com.prayatna.lookiesapp.presentation.components.loading.CircularLoading
import com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember.state.InviteMerchantMemberEvent
import com.prayatna.lookiesapp.presentation.merchant.inviteMerchantMember.state.InviteMerchantMemberUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteMerchantMemberScreen(
    uiState: InviteMerchantMemberUiState,
    onEvent: (InviteMerchantMemberEvent) -> Unit
) {
    var roleExpanded by remember { mutableStateOf(false) }
    var emailExpanded by remember { mutableStateOf(false) }

    val roles = listOf("member", "admin")

    Scaffold(
        topBar = {
            BackTopBar(
                onBackClick = {
                    onEvent(InviteMerchantMemberEvent.BackClicked)
                },
                title = "Invite Member"
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(
                    text = "Invite a new member to your business. Only registered users can be invited.",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Email Dropdown
                ExposedDropdownMenuBox(
                    expanded = emailExpanded &&
                            uiState.filteredEmails.isNotEmpty(),
                    onExpandedChange = {
                        emailExpanded = !emailExpanded
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = uiState.selectedEmail,
                        onValueChange = { value ->
                            onEvent(
                                InviteMerchantMemberEvent.EmailChanged(value)
                            )

                            emailExpanded = value.isNotBlank()
                        },
                        label = {
                            Text("User Email")
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        singleLine = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = emailExpanded
                            )
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = emailExpanded &&
                                uiState.filteredEmails.isNotEmpty(),
                        onDismissRequest = {
                            emailExpanded = false
                        }
                    ) {

                        uiState.filteredEmails.forEach { userEmail ->
                            DropdownMenuItem(
                                text = {
                                    Text(userEmail.email)
                                },
                                onClick = {
                                    onEvent(
                                        InviteMerchantMemberEvent.EmailChanged(
                                            userEmail.email
                                        )
                                    )

                                    // Tutup dropdown setelah dipilih
                                    emailExpanded = false
                                }
                            )
                        }
                    }
                }

                // Role Dropdown
                ExposedDropdownMenuBox(
                    expanded = roleExpanded,
                    onExpandedChange = {
                        roleExpanded = !roleExpanded
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = uiState.selectedRole.replaceFirstChar {
                            it.uppercase()
                        },
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Role")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = roleExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = roleExpanded,
                        onDismissRequest = {
                            roleExpanded = false
                        }
                    ) {

                        roles.forEach { role ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        role.replaceFirstChar {
                                            it.uppercase()
                                        }
                                    )
                                },
                                onClick = {
                                    onEvent(
                                        InviteMerchantMemberEvent.RoleChanged(role)
                                    )
                                    roleExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Button(
                    onClick = {
                        onEvent(
                            InviteMerchantMemberEvent.InviteClicked
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isInviting &&
                            uiState.selectedEmail.isNotEmpty()
                ) {

                    if (uiState.isInviting) {
                        CircularLoading()
                    } else {
                        Text("Send Invitation")
                    }
                }
            }
        }
    }
}