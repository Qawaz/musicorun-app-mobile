package com.musicorumapp.mobile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.getValue
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.placeholder
import com.musicorumapp.mobile.Constants
import com.musicorumapp.mobile.api.models.*
import com.musicorumapp.mobile.ui.theme.MusicorumTheme
import com.musicorumapp.mobile.ui.theme.SkeletonPrimaryColor
import com.musicorumapp.mobile.ui.theme.SkeletonSecondaryColor
import com.musicorumapp.mobile.ui.theme.md_theme_dark_onSecondary

private val roundedImageClip = RoundedCornerShape(4.dp)
val listItemLeftImageDefaultSize = 50.dp

@Composable
fun ListItem(
    title: String?,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    subtitleIcon: @Composable () -> Unit = {},
    leftImage: @Composable () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .composed { modifier }
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        leftImage()

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                title ?: "Placeholder",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .placeholder(
                        visible = title == null,
                        highlight = PlaceholderHighlight.fade(
                            highlightColor = SkeletonSecondaryColor
                        ),
                        color = SkeletonPrimaryColor,
                        shape = CircleShape
                    )
                    .height(20.dp)
            )
            if (title == null || (subTitle != null)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    subtitleIcon()

                    Text(
                        if (title == null) "Secondary placeholder" else subTitle.orEmpty(),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.placeholder(
                            visible = title == null,
                            highlight = PlaceholderHighlight.fade(
                                highlightColor = SkeletonSecondaryColor
                            ),
                            color = SkeletonPrimaryColor,
                            shape = CircleShape
                        )
                            .padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ArtistListItem(
    artist: Artist,
    modifier: Modifier = Modifier
) {

    val imageUrl by artist.imageUrlState
    val painter = rememberImagePainter(
        imageUrl.orEmpty(),
        builder = {
            crossfade(true)
            placeholder(LastfmEntity.ARTIST.asDrawableSource())
        }
    )

//    artist.onResourcesChange { painter.request = it.imageURL }

    ListItem(
        modifier = modifier,
        title = artist.name,
        leftImage = {
            Box(
                modifier = Modifier
                    .size(listItemLeftImageDefaultSize)
                    .clip(CircleShape)
            ) {
                Image(
                    painter = painterResource(LastfmEntity.ARTIST.asDrawableSource()),
                    contentDescription = artist.name
                )
                Image(
                    painter = painter,
                    contentDescription = artist.name
                )
            }
        }
    )
}

@Composable
fun AlbumListItem(
    album: Album? = null,
    modifier: Modifier = Modifier
) {
    val painter = rememberImagePainter(
        album?.imageURL,
        builder = {
            crossfade(true)
            placeholder(LastfmEntity.ALBUM.asDrawableSource())
        }
    )


//    album.onResourcesChange { painter.request = it.imageURL }

    ListItem(
        modifier = modifier,
        title = album?.name,
        subTitle = album?.artist,
        leftImage = {
            Box(
                modifier = Modifier
                    .size(listItemLeftImageDefaultSize)
                    .clip(roundedImageClip)
            ) {
                Image(
                    painter = painterResource(LastfmEntity.ALBUM.asDrawableSource()),
                    contentDescription = album?.name
                )
                Image(
                    painter = painter,
                    contentDescription = album?.name
                )
            }
        }
    )
}

@Composable
fun TrackListItem(
    modifier: Modifier = Modifier,
    track: Track? = null,
) {
    if (track == null) {
        ListItem(
            modifier = modifier,
            title = null,
            leftImage = {
                Box(
                    modifier = Modifier
                        .size(listItemLeftImageDefaultSize)
                        .clip(roundedImageClip)
                ) {
                    Image(
                        painter = painterResource(id = LastfmEntity.TRACK.asDrawableSource()),
                        contentDescription = "Empty"
                    )
                }
            }
        )
    } else {
        val imageUrl by track.imageUrlState
        val painter = rememberImagePainter(
            imageUrl.orEmpty(),
            builder = {
                crossfade(true)
                placeholder(LastfmEntity.TRACK.asDrawableSource())
            }
        )
        ListItem(
            modifier = modifier,
            title = track.name,
            subTitle = track.artist,
            leftImage = {
                Box(
                    modifier = Modifier
                        .size(listItemLeftImageDefaultSize)
                        .clip(roundedImageClip)
                ) {
                    Image(
                        painter = painterResource(LastfmEntity.TRACK.asDrawableSource()),
                        contentDescription = track.name
                    )
                    Image(painter = painter, contentDescription = track.name)
                }
            }
        )
    }
}

@Composable
fun UserListItem(
    user: User,
    modifier: Modifier = Modifier
) {

    ListItem(
        modifier = modifier,
        title = user.displayName,
        subTitle = if (user.name != null) "@${user.userName}" else null,
        leftImage = {
            Box(
                modifier = Modifier
                    .size(listItemLeftImageDefaultSize)
                    .clip(CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                )

                LastfmImageComponent(images = user.images, contentDescription = user.displayName)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ListItemPreview() {
    MusicorumTheme {
        Scaffold(
            modifier = Modifier.height(400.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                ArtistListItem(artist = Artist.fromSample(), modifier = Modifier.clickable { })
                Divider()
                TrackListItem(track = Track.fromSample(), modifier = Modifier.clickable { })
                Divider()
                TrackListItem(track = null, modifier = Modifier.clickable { })
            }
        }
    }
}