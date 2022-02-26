package com.musicorumapp.mobile.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.placeholder
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.ui.theme.PaddingSpacing
import com.musicorumapp.mobile.ui.theme.SkeletonPrimaryColor
import com.musicorumapp.mobile.ui.theme.SkeletonSecondaryColor


private val backgroundHeight = 520.dp

@Composable
fun GradientContentHeader(
    title: String?,
    mainImagePainter: Painter,
    backgroundPainter: Painter = rememberImagePainter(null),
    shape: Shape = CircleShape
    ) {

    val imageSize = LocalConfiguration.current.screenWidthDp.dp - (PaddingSpacing.HorizontalMainPadding * 4)
    Log.v(LogTag, "Gradient component render")
    Box {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(backgroundHeight)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.06f),
                            Color.White.copy(alpha = 0.045f),
                            Color.White.copy(alpha = 0f),
                        ),
                    )
                )
        )
        Image(
            painter = backgroundPainter,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(backgroundHeight)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(backgroundHeight)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                            MaterialTheme.colorScheme.background,
                        ),
                    )
                )
        )

        Box(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 220.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PaddingSpacing.HorizontalMainPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = mainImagePainter,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(imageSize, imageSize)
                        .clip(shape)
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(title.orEmpty(),
                    style = MaterialTheme.typography.headlineMedium,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(25.dp))
            }
        }
    }
}