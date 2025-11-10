package com.prayatna.lookiesapp.presentation.components.user.partnerapplication

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.ui.theme.DarkGrey

@Composable
fun PartnerLogoSection(imageLogo: Uri?) {
    if (imageLogo != null) {
        AsyncImage(
            model = imageLogo,
            contentDescription = "Partner Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(bottom = 12.dp)
        )
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Add image",
                tint = DarkGrey,
                modifier = Modifier.size(40.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Add image",
                color = DarkGrey,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
