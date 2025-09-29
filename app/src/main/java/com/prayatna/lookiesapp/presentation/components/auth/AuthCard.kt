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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.prayatna.lookiesapp.ui.theme.light_onPrimary
import com.prayatna.lookiesapp.ui.theme.light_onSecondary
import com.prayatna.lookiesapp.ui.theme.light_primary
import com.prayatna.lookiesapp.ui.theme.light_secondary
import com.prayatna.lookiesapp.ui.theme.light_secondaryContainer

@Composable
fun AuthCard(
    modifier: Modifier = Modifier,
    title: String,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    inRegister: Boolean = false,
    nameValue: String?,
    emailValue: String,
    passwordValue: String,
    onNameChange: ((String) -> Unit)?,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
) {

    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = light_primary),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier.width(340.dp),
        shape = RoundedCornerShape(12.dp)
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
                    color = light_onPrimary
                ),
                modifier = modifier.width(148.dp)
            )

            Spacer(modifier = modifier.height(25.dp))

            if (inRegister && nameValue != null && onNameChange != null) {
              AuthTextField(
                  value = nameValue,
                  onValueChange = onNameChange,
                  icon = {
                      Icon(
                          imageVector = Icons.Filled.Person,
                          contentDescription = "Filled Person",
                      ) },
                  title = "Name",
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)

              )
            }

            Spacer(modifier = modifier.height(14.dp))

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
                            tint = light_secondary,
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
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(109.dp, 34.dp)
                ) {
                    Text(
                        text = if (inRegister) "Sign in" else "Sign up",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = light_secondaryContainer
                        ),
                    )
                }

                Button (
                    onClick = { if (inRegister) onRegister() else onLogin() },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.size(109.dp, 34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = light_secondaryContainer
                    )
                ) {
                    Text(
                        text = if (inRegister) "Sign up" else "Sign in",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = light_primary
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
                style = TextStyle(fontSize = 12.sp, color = light_onPrimary)
            )
        },
        leadingIcon = icon,
        trailingIcon = trailingIcon,
        modifier = modifier.size(303.dp, 48.dp),
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(12.dp),
        textStyle = TextStyle(fontSize = 12.sp, color = light_onSecondary),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = light_secondaryContainer.copy(alpha = 0.5f),
            focusedContainerColor = light_secondary.copy(alpha = 0.3f),
            unfocusedContainerColor = light_primary,
            unfocusedLeadingIconColor = light_secondaryContainer,
            focusedLeadingIconColor = light_secondary,
            focusedBorderColor = light_secondary,
            cursorColor = light_secondary
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
        nameValue = "John Doe",
        emailValue = "newuser@email.com",
        passwordValue = "password123",
        onNameChange = {},
        onEmailChange = {},
        onPasswordChange = {}
    )
}