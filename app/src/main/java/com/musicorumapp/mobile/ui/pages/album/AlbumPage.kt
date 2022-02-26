package com.musicorumapp.mobile.ui.pages.album

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.api.models.LastfmEntity
import com.musicorumapp.mobile.ui.components.*
import com.musicorumapp.mobile.ui.contexts.LocalCompactDecimalFormatContext
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContext
import com.musicorumapp.mobile.ui.contexts.LocalSnackbarContext
import com.musicorumapp.mobile.ui.navigation.ComposableRoutes
import com.musicorumapp.mobile.ui.theme.AppMaterialIcons
import com.musicorumapp.mobile.ui.theme.PaddingSpacing
import com.musicorumapp.mobile.ui.theme.Shapes
import com.musicorumapp.mobile.ui.theme.md_theme_dark_background
import com.musicorumapp.mobile.utils.Utils
import com.musicorumapp.mobile.utils.calculateColorContrast
import com.musicorumapp.mobile.utils.rememberPredominantColor
import kotlinx.coroutines.launch

@Composable
fun AlbumPage(
    storeId: String?,
    viewModel: AlbumPageViewModel = hiltViewModel()
) {
    val albumFromStore = LocalNavigationContext.current.albumsStore[storeId]
    val context = LocalContext.current
    val snackbarContext = LocalSnackbarContext.current

    val predominantColorState = rememberPredominantColor(
        colorFinder = {
            it.lightVibrantSwatch ?: it.vibrantSwatch ?: it.swatches.maxByOrNull { s ->
                calculateColorContrast(
                    Color(s.rgb), md_theme_dark_background
                )
            }
        }
    )

    LaunchedEffect("") {
        if (albumFromStore != null) {
            viewModel.fetch(
                albumFromStore,
                context = context,
                predominantColorState = predominantColorState,
                snackbarHostState = snackbarContext.snackbarHostState
            )
        }
    }

    if (albumFromStore != null) {
        AlbumPageContent(viewModel)
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Not found!")
        }
    }
}


@Composable
fun AlbumPageContent(
    viewModel: AlbumPageViewModel
) {
    val scrollState = rememberScrollState()

    val painter = rememberImagePainter(
        viewModel.imageBitmap,
        builder = {
            crossfade(300)
            error(LastfmEntity.ALBUM.asDrawableSource())
        }
    )

    val backgroundPainter = rememberImagePainter(
        data = viewModel.artistResource?.spotify_images?.getOrNull(0),
        builder = {
            crossfade(300)
        },
    )

    AlbumPageContentList(
        viewModel = viewModel,
        mainImagePainter = painter,
        backgroundPainter = backgroundPainter,
        scrollState = scrollState
    )

    AlbumAppBar(
        scrollState = scrollState,
        mainImagePainter = painter,
        title = viewModel.album?.name.orEmpty()
    )
}

@Composable
fun AlbumPageContentList(
    viewModel: AlbumPageViewModel,
    mainImagePainter: Painter,
    backgroundPainter: Painter,
    scrollState: ScrollState
) {
    val album = viewModel.album
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        GradientContentHeader(
            title = album?.name,
            mainImagePainter = mainImagePainter,
            backgroundPainter = backgroundPainter,
            shape = Shapes.Large
        )
        Spacer(modifier = Modifier.height(PaddingSpacing.HorizontalMainPadding))
        Divider()
        Spacer(modifier = Modifier.height(PaddingSpacing.HorizontalMainPadding))
        AlbumPageTracksSection(viewModel)
    }
}

@Composable
private fun AlbumAppBar (
    scrollState: ScrollState,
    mainImagePainter: Painter,
    title: String
) {
    val navContext = LocalNavigationContext.current

    FadeableAppBar(
        alpha = Utils.interpolateValues(scrollState.value.toFloat(), 1100f, 1500f, 0f, 1f)
            .coerceIn(0f, 1f),
        navigationIcon = {
            IconButton(onClick = {
                navContext.navigationController?.popBackStack()
            }) {
                Icon(imageVector = AppMaterialIcons.ArrowBack, contentDescription = "Back")
            }
        }
    ) {
        val size = 42.dp

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 6.dp)
                    .clip(Shapes.Small)
                    .size(size)
            ) {
                Image(
                    painter = painterResource(id = LastfmEntity.ARTIST.asDrawableSource()),
                    contentDescription = null,
                    modifier = Modifier.size(size)
                )
                Image(
                    painter = mainImagePainter,
                    contentDescription = null,
                    modifier = Modifier.size(size)
                )
            }

            Text(
                title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.offset(y = 2.dp),
                style = AppBarTextStyle.copy(
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Composable
private fun TrackLeftLabel(
    number: Short? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.1f))
            .size(listItemLeftImageDefaultSize)
    ) {
        Text(
            (number?.toString()).orEmpty(),
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun AlbumPageTracksSection(
    viewModel: AlbumPageViewModel
) {
    val resources = LocalContext.current.resources
    val compactDecimalInstance = LocalCompactDecimalFormatContext.current.instance

    val tracks = viewModel.album?.tracks

    Section(
        title = stringResource(R.string.tracks),
        subTitle = if (tracks != null) resources.getQuantityString(
            R.plurals.tracks_quantity,
            tracks.size,
            compactDecimalInstance.format(tracks.size)
        ) else "",
        modifier = Modifier.padding(horizontal = PaddingSpacing.HorizontalMainPadding),
    ) {
        Column {
            if (tracks != null) {
                tracks.forEach {
                    ListItem(
                        title = it.value.name,
                        leftImage = {
                            TrackLeftLabel(it.key)
                        }
                    )
                }
            } else {
                for (x in 1..5) {
                    ListItem(
                        title = null,
                        leftImage = {
                            TrackLeftLabel()
                        }
                    )
                }
            }
        }
    }
}