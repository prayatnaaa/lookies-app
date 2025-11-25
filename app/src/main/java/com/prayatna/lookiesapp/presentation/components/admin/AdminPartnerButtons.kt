package com.prayatna.lookiesapp.presentation.components.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prayatna.lookiesapp.ui.theme.DarkGreen
import com.prayatna.lookiesapp.ui.theme.Maroon

@Composable
fun AdminPartnerButtons(
    onApprovedButtonClick: () -> Unit,
    onRejectButtonClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ElevatedButton(
            onClick = onRejectButtonClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Maroon,
                contentColor = Color.White
            )
        ) {
            Text(text = "Rejected")
        }
        Spacer(
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        ElevatedButton(
            onClick = onApprovedButtonClick,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkGreen,
                contentColor = Color.White
            )
        ) {
            Text(text = "Approve")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminPartnerButtonsPreview() {
    AdminPartnerButtons(
        onApprovedButtonClick = {},
        onRejectButtonClick = {}
    )
}