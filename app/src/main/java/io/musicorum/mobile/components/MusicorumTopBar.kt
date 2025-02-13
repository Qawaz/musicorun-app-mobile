package io.musicorum.mobile.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import io.musicorum.mobile.LocalAnalytics
import io.musicorum.mobile.LocalNavigation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicorumTopBar(
    text: String,
    scrollBehavior: TopAppBarScrollBehavior,
    fadeable: Boolean,
    likeAction: @Composable RowScope.() -> Unit
) {
    val analytics = LocalAnalytics.current!!
    val nav = LocalNavigation.current
    val fraction = scrollBehavior.state.overlappedFraction
    val colors = topAppBarColors(
        containerColor = Color.Transparent,
        scrolledContainerColor = Color.DarkGray.copy(alpha = fraction)
    )
    val alpha = if (fadeable) {
        fraction
    } else {
        1f
    }

    TopAppBar(
        title = {
            Text(
                text = text, modifier = Modifier.alpha(alpha)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    nav?.popBackStack()
                    analytics.logEvent("topbar_back_pressed", null)
                },
            ) {
                Icon(Icons.Rounded.ArrowBack, contentDescription = "")
            }
        },
        scrollBehavior = scrollBehavior,
        colors = colors,
        actions = likeAction,
    )

}