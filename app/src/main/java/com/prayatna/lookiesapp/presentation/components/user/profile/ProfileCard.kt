package com.prayatna.lookiesapp.presentation.components.user.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.prayatna.lookiesapp.R

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileCard(
    modifier: Modifier = Modifier,
    imagePainter: Painter = rememberImagePainter(data = R.drawable.default_avatar),
    username: String,
    onEditProfileClick: () -> Unit
) {
    OutlinedCard(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = modifier
                        .matchParentSize(),
                    alignment = Alignment.Center,
                    painter = imagePainter,
                    contentDescription = null
                )
            }

            Spacer(modifier = modifier.height(25.dp))

            Text(
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp,
                ),
                text = username
            )

            Spacer(modifier = modifier.height(25.dp))

            OutlinedButton (
                onClick = onEditProfileClick,
                shape = RoundedCornerShape(4.dp),
                modifier = modifier
                    .width(140.dp),
                border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.outline)

            ) {
                Text(text = "Edit profile",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    )
            }
        }
    }
}

@Preview
@Composable
fun ProfileCardPreview() {
    ProfileCard(
        username = "Today",
        onEditProfileClick = {}
    )
}