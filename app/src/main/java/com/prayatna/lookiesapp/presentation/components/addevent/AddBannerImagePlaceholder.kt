package com.prayatna.lookiesapp.presentation.components.addevent

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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

@Composable
fun AddImageBannerPlaceholder(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    imageUri: Uri?
) {
    val shape = RoundedCornerShape(16.dp)

    ElevatedCard(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(160.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface, shape)
            .background(color = Color.Transparent)
            .clickable {
                onClick()
            },
        shape = shape,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
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
                    contentDescription = "Selected image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape),
                    contentScale = ContentScale.Crop
                )
            }
            else {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Add image",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Add image",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddImageBannerPlaceholderPreview() {
    AddImageBannerPlaceholder(
        onClick = {},
        imageUri = null
    )
}
