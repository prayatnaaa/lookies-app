package com.prayatna.lookiesapp.presentation.components.createEvent

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.prayatna.lookiesapp.utils.Constants

@Composable
fun EventBannerCard(modifier: Modifier = Modifier,
                    onClick: () -> Unit,
                    imageUri: Uri? = null,
                    imageUrl: String? = null) {
    val shape = RoundedCornerShape(Constants.ROUNDED_CORNER_SHAPE)

    Card(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape)
            .background(color = Color.Transparent)
            .clickable {
                onClick()
            },
        shape = shape,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Selected banner image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape),
                    contentScale = ContentScale.Fit
                )
            } else if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl
                        .replace("http://172.21.179.110", "http://10.0.2.2"),
                    contentDescription = "Selected banner image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape),
                    contentScale = ContentScale.Crop
                )
            }
            else {
                Icon(
                    imageVector = Icons.Default.Photo,
                    contentDescription = "Select image",
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Select image",
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventBannerCardPreview() {
    EventBannerCard(
        onClick = {},
        imageUri = null
    )
}