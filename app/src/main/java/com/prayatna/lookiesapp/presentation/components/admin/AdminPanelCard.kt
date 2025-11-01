package com.prayatna.lookiesapp.presentation.components.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.prayatna.lookiesapp.ui.theme.BlackCharcoal
import com.prayatna.lookiesapp.ui.theme.PureWhite

@Composable
fun AdminPanelCard(
    modifier: Modifier = Modifier,
    color: Color = BlackCharcoal,
    contentFor: String,
    textColor: Color = PureWhite,
    onClick: () -> Unit
) {
    Box (
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color = color)
            .clickable { onClick() }
    ) {
        Text(
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 24.dp),
            text = contentFor,
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                color = textColor
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AdminPanelCardPreview() {
    AdminPanelCard(
        contentFor = "Artist Application",
        onClick = {}
    )

}