package com.musicorumapp.mobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.statusBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContext
import com.musicorumapp.mobile.ui.theme.AlmostBlack
import com.musicorumapp.mobile.ui.theme.AppMaterialIcons
import com.musicorumapp.mobile.ui.theme.PoppinsFontFamily
import com.musicorumapp.mobile.ui.theme.SecondaryTextColor

val AppBarTextStyle = TextStyle(
    fontSize = 26.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = PoppinsFontFamily
)

val AppBarSubtitleTextStyle = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Normal,
    fontFamily = PoppinsFontFamily,
    color = SecondaryTextColor
)

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    titleStyle: TextStyle = AppBarTextStyle,
    subTitleStyle: TextStyle = AppBarSubtitleTextStyle,
    showBackButton: Boolean = false,
    backgroundColor: Color = Color.Transparent,
    actions: @Composable RowScope.() -> Unit = {},
    extendToStatusBar: Boolean = false
) {
    val navigationContext = LocalNavigationContext.current

    Column(
        modifier = Modifier.background((backgroundColor))
    ) {
        if (extendToStatusBar) {
            Spacer(modifier = Modifier.statusBarsHeight())
        }
        SmallTopAppBar(
            title = {
                Column {
                    Text(
                        title,
                        style = titleStyle,
                        modifier = if (subTitle != null) Modifier.height(26.dp) else Modifier
                    )
                    if (subTitle != null) {
                        Text(
                            subTitle,
                            style = subTitleStyle
                        )
                    }
                }
            },
            modifier = modifier
                .padding(horizontal = 7.dp),
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = Color.Transparent
            ),
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = {
                        navigationContext.navigationController?.popBackStack()
                    }) {
                        Icon(
                            AppMaterialIcons.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            },
            actions = actions
        )
    }
}

val SecondaryAppBarTextStyle = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = PoppinsFontFamily
)

val SecondaryAppBarSubtitleTextStyle = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Normal,
    fontFamily = PoppinsFontFamily,
    color = SecondaryTextColor
)

@Composable
fun SecondaryAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    titleStyle: TextStyle = SecondaryAppBarTextStyle,
    subTitleStyle: TextStyle = SecondaryAppBarSubtitleTextStyle,
    showBackButton: Boolean = false,
    backgroundColor: Color = AlmostBlack,
    actions: @Composable RowScope.() -> Unit = {},
    extendToStatusBar: Boolean = false
) {
    AppBar(
        title = title,
        subTitle = subTitle,
        titleStyle = titleStyle,
        subTitleStyle = subTitleStyle,
        showBackButton = showBackButton,
        backgroundColor = backgroundColor,
        actions = actions,
        modifier = modifier,
        extendToStatusBar = extendToStatusBar
    )
}