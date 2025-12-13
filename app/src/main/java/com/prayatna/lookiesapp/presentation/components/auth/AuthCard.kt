package com.prayatna.lookiesapp.presentation.components.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.DarkSurface

@Composable
fun AuthCard(
    modifier: Modifier = Modifier,
    title: String,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    inRegister: Boolean = false,
    emailValue: String,
    passwordValue: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {

    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier.width(340.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                ),
                modifier = modifier.width(148.dp)
            )

            Spacer(modifier = modifier.height(25.dp))

            AuthTextField(
                value = emailValue,
                onValueChange = onEmailChange,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Filled Email"
                    )
                },
                title = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = modifier.height(14.dp))

            var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
            AuthTextField(
                title = "Password",
                value = passwordValue,
                onValueChange = onPasswordChange,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Filled Locked"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = {isPasswordVisible = !isPasswordVisible}) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = "Filled Visibility"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
            )

            Spacer(modifier = modifier.height(18.dp))

            Row(
                modifier = modifier.width(303.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { if (inRegister) onLogin() else onRegister() },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(109.dp, 34.dp)
                ) {
                    Text(
                        text = if (inRegister) "Sign in" else "Sign up",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                Button (
                    onClick = { if (inRegister) onRegister() else onLogin() },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(109.dp, 34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = if (inRegister) "Sign up" else "Sign in",
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }
    }
}



@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: @Composable (() -> Unit),
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (!it.contains("\n")) onValueChange(it)
        },
        placeholder = {
            Text(
                text = title,
                style = TextStyle(fontSize = 12.sp)
            )
        },
        leadingIcon = icon,
        trailingIcon = trailingIcon,
        modifier = modifier.size(303.dp, 48.dp),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(12.dp),
        textStyle = TextStyle(fontSize = 12.sp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.onSurface,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,

            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,

            focusedLabelColor = MaterialTheme.colorScheme.onSurface,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,

            cursorColor = MaterialTheme.colorScheme.primary,

            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,

            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        keyboardOptions = keyboardOptions
    )
}

@Preview(showBackground = true)
@Composable
fun AuthCardPreview() {
    AuthCard(
        title = "Register",
        onLogin = {},
        onRegister = {},
        inRegister = true,
        emailValue = "newuser@email.com",
        passwordValue = "password123",
        onEmailChange = {},
        onPasswordChange = {}
    )
}