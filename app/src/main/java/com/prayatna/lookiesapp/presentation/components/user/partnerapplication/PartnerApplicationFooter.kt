package com.prayatna.lookiesapp.presentation.components.user.partnerapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.LightGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun PartnerApplicationFooter(
    route: String,
    onBackButton: () -> Unit,
    isButtonEnable: Boolean = true,
    onProfileButton: () -> Unit,
    onLocationButton: () -> Unit,
    onSubmissionButton: () -> Unit
) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (route.startsWith(NavigationRoutes.EDIT_PROFILE)) Arrangement.End else Arrangement.SpaceBetween) {
        if (route == NavigationRoutes.PARTNER_APPLICATION || route == NavigationRoutes.ADD_LOCATION) {
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = onBackButton,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = BlackCharcoal,
                    contentColor = PureWhite,
                    disabledContainerColor = DarkGrey,
                    disabledContentColor = LightGrey
                )
            ) {
                Text(
                    text = "Back",
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }
        }

        Button(
            modifier = Modifier
                .padding(8.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                when (route) {
                    "${NavigationRoutes.EDIT_PROFILE}?isPartnerSignup=true" -> {
                        onProfileButton()
                    }
                    NavigationRoutes.ADD_LOCATION -> {
                        onLocationButton()
                    }
                    NavigationRoutes.PARTNER_APPLICATION -> {
                        onSubmissionButton()
                    }
                }
            },
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = BlackCharcoal,
                contentColor = PureWhite,
                disabledContainerColor = DarkGrey,
                disabledContentColor = LightGrey
            ),
            enabled = isButtonEnable
        ) {
            Text(text = if (route == NavigationRoutes.PARTNER_APPLICATION) "Submit" else "Next",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
        }
    }
}