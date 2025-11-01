package com.prayatna.lookiesapp.presentation.components.addevent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.DarkGrey
import com.prayatna.lookiesapp.ui.theme.Grey
import com.prayatna.lookiesapp.ui.theme.LightGrey
import com.prayatna.lookiesapp.ui.theme.PureWhite
import com.prayatna.lookiesapp.utils.NavigationRoutes

@Composable
fun AddEventFooter(
    modifier: Modifier = Modifier,
    route: String = NavigationRoutes.ADD_EVENT,
    onNextButton: () -> Unit,
    onBackButton: () -> Unit,
    onSubmitButton: () -> Unit,
    isButtonEnable: Boolean = true
) {
    Row (modifier = modifier
        .fillMaxWidth()
        .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (route.startsWith(NavigationRoutes.ADD_EVENT)) Arrangement.End else Arrangement.SpaceBetween) {
        if (route == NavigationRoutes.ADD_DETAIL_EVENT) {
            Button(
                shape = RoundedCornerShape(8.dp),
                onClick = onBackButton,
                colors = ButtonColors(
                    containerColor = DarkGrey,
                    contentColor = PureWhite,
                    disabledContainerColor = Grey,
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
            modifier = modifier
                .padding(8.dp),
            shape = RoundedCornerShape(8.dp),
            onClick = {
                if (route == NavigationRoutes.ADD_EVENT) {
                    onNextButton()
                } else if (route == NavigationRoutes.ADD_DETAIL_EVENT){
                    onSubmitButton()
                }
            },
            colors = ButtonColors(
                containerColor = DarkGrey,
                contentColor = PureWhite,
                disabledContainerColor = Grey,
                disabledContentColor = LightGrey
            ),
            enabled = isButtonEnable
        ) {
            Text(text = if (route == NavigationRoutes.ADD_DETAIL_EVENT) "Submit" else "Next",
                style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                ))
        }
    }
}

@Composable
@Preview
fun AddEventFooterPreview() {
    AddEventFooter(
        onNextButton = {},
        onBackButton = {},
        onSubmitButton = {}
    )
}