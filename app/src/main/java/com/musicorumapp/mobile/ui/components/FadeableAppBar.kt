package com.musicorumapp.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun FadeableAppBar(
    alpha: Float = 1f,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    FadeableAppBarContent(
        alpha = alpha,
        navigationIcon = navigationIcon,
        actions = actions
    ) {
        Box(
            modifier = Modifier.graphicsLayer(
                alpha = alpha
            )
        ) {
            content()
        }
    }
}

@Composable
fun FadeableAppBarContent(
    alpha: Float,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit,
    content: @Composable () -> Unit,
) {
    val color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha)
    Column(
        modifier = Modifier.background(color)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsHeight()
        )
        SmallTopAppBar(
            title = content,
            navigationIcon = navigationIcon,
            actions = actions,
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            ),
        )
    }
}