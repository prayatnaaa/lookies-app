package com.prayatna.lookiesapp.presentation.components.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.prayatna.lookiesapp.ui.theme.*

@Composable
fun AuthCard(
    modifier: Modifier = Modifier,
    title: String,

    // actions
    onLogin: () -> Unit,
    onRegister: () -> Unit,

    // state flags
    inRegister: Boolean = false,
    isRegister: Boolean = false,

    // values
    fullNameValue: String = "",
    emailValue: String,
    passwordValue: String,
    verifyPasswordValue: String = "",

    // callbacks
    onFullNameChange: (String) -> Unit = {},
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onVerifyPasswordChange: (String) -> Unit = {}
) {

    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var isConfirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    ElevatedCard(
        modifier = modifier.width(340.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AmoledBlack),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ✅ FULL NAME
            if (isRegister) {
                AuthTextField(
                    title = "Full Name",
                    value = fullNameValue,
                    onValueChange = onFullNameChange,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Full Name"
                        )
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // EMAIL
            AuthTextField(
                title = "Email",
                value = emailValue,
                onValueChange = onEmailChange,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // PASSWORD
            AuthTextField(
                title = "Password",
                value = passwordValue,
                onValueChange = onPasswordChange,
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Password"
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector =
                                if (isPasswordVisible) Icons.Filled.VisibilityOff
                                else Icons.Filled.Visibility,
                            contentDescription = "Toggle Password"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation =
                    if (isPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation()
            )

            // CONFIRM PASSWORD
            if (isRegister) {
                Spacer(modifier = Modifier.height(14.dp))

                AuthTextField(
                    title = "Confirm Password",
                    value = verifyPasswordValue,
                    onValueChange = onVerifyPasswordChange,
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Confirm Password"
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isConfirmPasswordVisible = !isConfirmPasswordVisible
                            }
                        ) {
                            Icon(
                                imageVector =
                                    if (isConfirmPasswordVisible)
                                        Icons.Filled.VisibilityOff
                                    else Icons.Filled.Visibility,
                                contentDescription = "Toggle Confirm Password"
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation =
                        if (isConfirmPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.width(303.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                TextButton(
                    onClick = { if (inRegister) onLogin() else onRegister() },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(109.dp, 34.dp)
                ) {
                    Text(
                        text = if (inRegister) "Sign in" else "Sign up",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    enabled =
                        if (isRegister)
                            fullNameValue.isNotBlank() &&
                                    emailValue.isNotBlank() &&
                                    passwordValue.isNotBlank()
                        else
                            emailValue.isNotBlank() && passwordValue.isNotBlank(),

                    onClick = { if (inRegister) onRegister() else onLogin() },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(109.dp, 34.dp)
                ) {
                    Text(
                        text = if (inRegister) "Sign up" else "Sign in",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
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
    icon: @Composable () -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = { if (!it.contains("\n")) onValueChange(it) },
        placeholder = {
            Text(text = title, fontSize = 12.sp)
        },
        leadingIcon = icon,
        trailingIcon = trailingIcon,
        modifier = modifier.size(303.dp, 48.dp),
        shape = RoundedCornerShape(12.dp),
        textStyle = TextStyle(fontSize = 12.sp),
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PureWhite,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            focusedContainerColor = BlackText,
            unfocusedContainerColor = BlackText,
            focusedTextColor = PureWhite,
            unfocusedTextColor = GreyTextLight
        )
    )
}

@Preview(showBackground = true)
@Composable
fun AuthCardRegisterPreview() {
    AuthCard(
        title = "Register",
        isRegister = true,
        inRegister = true,
        fullNameValue = "John Doe",
        emailValue = "john@email.com",
        passwordValue = "password123",
        onFullNameChange = {},
        onEmailChange = {},
        onPasswordChange = {},
        onRegister = {},
        onLogin = {}
    )
}
