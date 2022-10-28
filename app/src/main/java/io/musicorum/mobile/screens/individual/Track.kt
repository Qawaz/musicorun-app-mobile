package io.musicorum.mobile.screens.individual

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import io.musicorum.mobile.coil.PlaceholderType
import io.musicorum.mobile.coil.defaultImageRequestBuilder
import io.musicorum.mobile.components.*
import io.musicorum.mobile.serialization.NavigationTrack
import io.musicorum.mobile.ui.theme.*
import io.musicorum.mobile.utils.Placeholders
import io.musicorum.mobile.utils.createPalette
import io.musicorum.mobile.utils.getBitmap
import io.musicorum.mobile.viewmodels.HomeViewModel
import io.musicorum.mobile.viewmodels.TrackViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


@Composable
fun Track(
    trackData: String?,
    trackViewModel: TrackViewModel = viewModel(),
    homeViewModel: HomeViewModel,
    nav: NavHostController
) {
    if (trackData == null) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(AlmostBlack), verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Something went wrong", textAlign = TextAlign.Center)
        }
    } else {
        val partialTrack = Json.decodeFromString<NavigationTrack>(trackData)
        val track = trackViewModel.track.observeAsState()
        val ctx = LocalContext.current
        var coverPalette: Palette? by remember { mutableStateOf(null) }
        var paletteReady by remember { mutableStateOf(false) }
        val similarTracks = trackViewModel.similar.observeAsState()
        val artistCover = trackViewModel.artistCover.observeAsState()

        LaunchedEffect(key1 = track.value) {
            if (track.value == null) {
                trackViewModel.fetchTrack(
                    partialTrack.trackName,
                    partialTrack.trackArtist,
                    homeViewModel.user.value!!.user.name,
                    null
                )
            } else {
                launch {
                    trackViewModel.fetchSimilar(track.value!!, 5, null)
                }

                launch {
                    val album = track.value!!.album
                    if (!album?.images.isNullOrEmpty()) {
                        if (album?.bestImageUrl?.isEmpty() == true) {
                            album.apply {
                                bestImageUrl = this.fetchExternalImage()
                            }
                        }
                        val bmp = getBitmap(album?.bestImageUrl, ctx)
                        coverPalette = createPalette(bmp)
                        paletteReady = true
                    } else {
                        paletteReady = true
                    }
                }
                launch {
                    trackViewModel.fetchArtistCover(track.value!!.artist)
                }
            }
        }
        if (track.value == null) {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(AlmostBlack),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val t = track.value!!
            val screenScrollState = rememberScrollState()

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AlmostBlack)
                    .verticalScroll(screenScrollState),
                verticalArrangement = Arrangement.Center
            ) {
                GradientHeader(
                    artistCover.value,
                    t.album?.bestImageUrl
                )
                Spacer(Modifier.height(12.dp))
                Text(
                    text = t.name,
                    softWrap = true,
                    style = Heading2,
                    modifier = Modifier.padding(horizontal = 55.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = t.artist.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = Body1,
                    modifier = Modifier.alpha(0.55f)
                )
                Divider(Modifier.padding(vertical = 18.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Statistic(number = t.listeners, label = "Listeners")
                    Statistic(number = t.playCount, label = "Scrobbles")
                    Statistic(number = t.userPlayCount, label = "Your scrobbles")
                }

                Spacer(modifier = Modifier.height(20.dp))
                if (t.topTags != null) {
                    TagList(tags = t.topTags.tags, coverPalette, !paletteReady)
                }
                if (track.value!!.wiki != null) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
                        ItemInformation(palette = coverPalette, info = track.value!!.wiki!!.summary)
                    }
                }
                Divider(Modifier.padding(vertical = 20.dp))
                Row(
                    Modifier
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        Text(text = "Appears on", style = BodySmall)
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.5f)
                        ) {
                            AsyncImage(
                                model = defaultImageRequestBuilder(
                                    url = track.value!!.album?.bestImageUrl,
                                    PlaceholderType.ALBUM
                                ),
                                contentDescription = null,
                                placeholder = Placeholders.TRACK.asPainter(),
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(6.dp))
                            )
                            Text(
                                text = track.value!!.album?.name ?: "Unknown",
                                style = BodyLarge,
                                modifier = Modifier.padding(start = 10.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Column {
                        Text(text = "From", style = BodySmall)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = defaultImageRequestBuilder(
                                    url = track.value!!.artist.bestImageUrl,
                                    PlaceholderType.ARTIST
                                ),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                            )
                            Text(
                                text = track.value!!.artist.name,
                                style = BodyLarge,
                                modifier = Modifier.padding(start = 10.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
                if (similarTracks.value != null) {
                    Divider(Modifier.padding(vertical = 20.dp))
                    Column(
                        Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            "Similar Tracks",
                            style = Heading4,
                            modifier = Modifier.padding(start = 15.dp)
                        )
                        similarTracks.value!!.similarTracks.tracks.forEach {
                            TrackRow(track = it, nav = nav, false)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }


    }
}