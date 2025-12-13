package com.prayatna.lookiesapp.presentation.components.painting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

@Composable
fun PaintingCard(
    paintingUrl: String,
    name: String,
    price: Number? = null,
    onClick: () -> Unit
) {

    ElevatedCard(
        shape = RoundedCornerShape(4.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Box {
            AsyncImage(
                model = paintingUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.7f),
                                Color.Black.copy(alpha = 0.8f),
                                Color.Black.copy(alpha = 0.9f)
                            )
                        )
                    )
                    .padding(8.dp)
            ) {
                Column {
                    Text(
                        text = name,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    price?.let {
                        Text(
                            text = "Rp. $it",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PaintingCardPreview() {
    PaintingCard(
        paintingUrl = "https://picsum.photos/200/300",
        name = "Painting Name",
        price = 100000,
        onClick = {}
    )
}
