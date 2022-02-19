package com.musicorumapp.mobile.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.ui.theme.AppMaterialIcons


data class PageIconResolveable(
    val material: ImageVector? = null,
    val drawable: Int? = null,
)


sealed class BottomPage(
    val name: String,
    @StringRes val titleResource: Int,
    val icon: PageIconResolveable
) {
    object Home : BottomPage(
        "home",
        R.string.bottom_navigation_item_home,
        PageIconResolveable(material = AppMaterialIcons.Home)
    )

    object Scrobbling : BottomPage(
        "scrobbling",
        R.string.bottom_navigation_item_scrobbling,
        PageIconResolveable(drawable = R.drawable.ic_round_queue_music_24)
    )

    object Charts : BottomPage(
        "charts",
        R.string.bottom_navigation_item_charts,
        PageIconResolveable(drawable = R.drawable.ic_round_show_chart_24)
    )

    object Profile : BottomPage(
        "profile",
        R.string.bottom_navigation_item_profile,
        PageIconResolveable(material = AppMaterialIcons.Person)
    )
}

val mainPages = listOf(
    BottomPage.Home,
    BottomPage.Scrobbling,
    BottomPage.Charts,
    BottomPage.Profile
)

val pagesWithBottomBar = listOf(
    BottomPage.Home.name,
    BottomPage.Scrobbling.name,
    BottomPage.Charts.name,
    BottomPage.Profile.name,
//    "artist/{storeId}"
)