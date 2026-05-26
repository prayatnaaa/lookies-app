package com.prayatna.lookiesapp.presentation.components.user.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.prayatna.lookiesapp.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EditProfileImageCard(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    onClick: () -> Unit
) {
    val imageUri = imageUrl.toUri()

    Box(
        modifier = modifier
            .size(214.dp)
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Image(
                painter = rememberImagePainter(
                    data = if (imageUrl.isNotEmpty()) imageUri else R.drawable.default_avatar,
                    builder = {
                        crossfade(true)
                        error(R.drawable.default_avatar)
                        placeholder(R.drawable.default_avatar)
                    }
                ),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { onClick() },
                contentScale = ContentScale.Crop
            )

            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .offset(x = (-2).dp, y = (2).dp)
                    .size(32.dp)
                    .clickable { onClick() }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(Color.White)
                        .clip(CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = Color.Black,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditProfileImageCardPreview() {
    EditProfileImageCard {}
}