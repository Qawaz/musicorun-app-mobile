package com.musicorumapp.mobile.ui.pages

import androidx.activity.ComponentActivity
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.smallTopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.insets.statusBarsPadding
import com.musicorumapp.mobile.Constants
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.api.models.Artist
import com.musicorumapp.mobile.api.models.User
import com.musicorumapp.mobile.authentication.AuthenticationPreferences
import com.musicorumapp.mobile.ui.contexts.LocalAuth
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContext
import com.musicorumapp.mobile.state.models.AuthenticationViewModel
import com.musicorumapp.mobile.states.models.HomePageViewModel
import com.musicorumapp.mobile.ui.components.*
import com.musicorumapp.mobile.ui.navigation.ComposableRoutes
import com.musicorumapp.mobile.ui.theme.AppMaterialIcons
import com.musicorumapp.mobile.ui.theme.PaddingSpacing
import com.musicorumapp.mobile.ui.theme.Shapes
import com.musicorumapp.mobile.ui.theme.md_theme_dark_background
import com.musicorumapp.mobile.utils.calculateColorContrast
import com.musicorumapp.mobile.utils.darkerColor
import com.musicorumapp.mobile.utils.gradientBackgroundColorResolver
import com.musicorumapp.mobile.utils.rememberPredominantColor

@Composable
fun HomePage(
    authenticationViewModel: AuthenticationViewModel? = null,
    homePageViewModel: HomePageViewModel = viewModel()
) {

    val prefs = LocalContext.current.getSharedPreferences(
        Constants.AUTH_PREFS_KEY,
        ComponentActivity.MODE_PRIVATE
    )
    val authPrefs = AuthenticationPreferences(prefs)

    val authContent = LocalAuth.current
    val navigationContext = LocalNavigationContext.current


    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        AppBar(
            title = stringResource(R.string.bottom_navigation_item_home),
            modifier = Modifier.statusBarsPadding(),
            actions = {
                IconButton(onClick = {
                    navigationContext.navigationController?.navigate(ComposableRoutes.Search)
                }) {
                    Icon(
                        AppMaterialIcons.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
                IconButton(onClick = {
                    navigationContext.navigationController?.navigate(ComposableRoutes.SettingsMainPage)
                }) {
                    Icon(
                        AppMaterialIcons.Settings,
                        contentDescription = stringResource(id = R.string.search)
                    )
                }
            }
        )


        Spacer(modifier = Modifier.height(6.dp))

        Column(
            modifier = Modifier.padding(horizontal = PaddingSpacing.HorizontalMainPadding)
        ) {
            UserCard(
                user = authenticationViewModel?.user?.value,
                homePageViewModel = homePageViewModel
            )

            Spacer(modifier = Modifier.height(14.dp))

            val artist = Artist.fromSample()

            ArtistListItem(artist = artist, modifier = Modifier.clickable {
                val id = navigationContext.addArtist(artist)
                navigationContext.navigationController?.navigate("artist/$id")
            })
        }
    }
}

private val cardHeight = 140.dp

@Composable
fun UserCard(
    user: User? = null,
    homePageViewModel: HomePageViewModel
) {
    val modifier = Modifier
        .fillMaxWidth()
        .height(cardHeight)
        .clip(Shapes.Medium)

    val predominantColor = homePageViewModel.predominantColor

    val predominantColorState = rememberPredominantColor(
        colorFinder = {
            it.lightVibrantSwatch ?: it.vibrantSwatch ?: it.swatches.maxByOrNull { s ->
                calculateColorContrast(
                    Color(s.rgb), md_theme_dark_background
                )
            }
        },
        defaultOnColor = Color(0xE8FFFFFF)
    ) {
        gradientBackgroundColorResolver(it)
    }

    if (user != null && !homePageViewModel?.colorFetched?.value!!) {
        user.images.bestImage?.let {
            homePageViewModel.fetchColors(
                predominantColorState,
                url = it
            )
        }
    }

    Crossfade(targetState = user == null) {
        if (it) PulsatingSkeleton(modifier)
        else Box(
            modifier = modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            predominantColor.value ?: predominantColorState.color,
                            darkerColor(predominantColor.value ?: predominantColorState.color, 25)
                        ),
//                    start = Offset(0f, 0f),
//                    end = Offset(1f, 1f),
                    )
                )
                .padding(PaddingSpacing.MediumPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                NetworkImage(
//                    url = user?.images?.bestImage,
//                    contentDescription = stringResource(
//                        R.string.home_page_image_content_user
//                    ),
//                    modifier = Modifier
//                        .size(cardHeight - (PaddingSpacing.MediumPadding * 2))
//                        .shadow(
//                            elevation = 6.dp,
//                            shape = CircleShape,
//                        )
//                )
                Image(
                    painter = rememberImagePainter(
                        data = user?.images?.bestImage,
                        builder = {
                            crossfade(true)
                            transformations(CircleCropTransformation())
                        }
                    ),
                    contentDescription = stringResource(R.string.home_page_image_content_user),
                    modifier = Modifier
                        .shadow(
                            elevation = 6.dp,
                            shape = CircleShape,
                        )
                        .size((cardHeight - (PaddingSpacing.MediumPadding * 2)))
                )
                Spacer(modifier = Modifier.width(PaddingSpacing.SmallPadding))
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        user?.displayName.orEmpty(),
                        fontWeight = FontWeight.Bold,
                        color = predominantColorState.onColor,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 20.sp
                    )
                    Text(
                        stringResource(
                            R.string.home_page_scrobbles_text,
                            user?.playCount.toString()
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = predominantColorState.onColor,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}