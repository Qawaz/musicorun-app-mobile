package io.musicorum.mobile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.musicorum.mobile.coil.PlaceholderType
import io.musicorum.mobile.coil.defaultImageRequestBuilder
import io.musicorum.mobile.screens.individual.PartialAlbum
import io.musicorum.mobile.serialization.TopAlbum
import io.musicorum.mobile.ui.theme.BodySmall
import io.musicorum.mobile.ui.theme.Poppins
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Composable
fun TopAlbumsRow(albums: List<TopAlbum>, nav: NavHostController) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier.padding(start = 20.dp)
    ) {
        items(albums) { album ->
            AlbumCard(album = album, nav)
        }
    }
}


@Composable
fun AlbumCard(album: TopAlbum, nav: NavHostController) {
    val partialAlbum = Json.encodeToString(PartialAlbum(album.name, album.artist?.artistName ?: "unknown"))
    Column(modifier = Modifier.clickable { nav.navigate("album/$partialAlbum") }) {
        AsyncImage(
            model = defaultImageRequestBuilder(album.bestImageUrl, PlaceholderType.ALBUM),
            contentDescription = null,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Text(
            text = album.name,
            fontFamily = Poppins,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(120.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            album.artist?.artistName ?: "Unknown",
            style = BodySmall,
            modifier = Modifier.alpha(0.55f)
        )
        Text(
            (album.playCount ?: "0") + " plays",
            style = BodySmall,
            modifier = Modifier.alpha(0.55f)
        )
    }
}