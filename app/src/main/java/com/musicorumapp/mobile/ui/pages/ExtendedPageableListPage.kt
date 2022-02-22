package com.musicorumapp.mobile.ui.pages

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.statusBarsPadding
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.api.models.*
import com.musicorumapp.mobile.ui.components.*
import com.musicorumapp.mobile.ui.theme.PaddingSpacing
import com.musicorumapp.mobile.ui.utils.OnBottomReached
import kotlinx.coroutines.launch
import java.lang.Exception

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtendedPageableListPage(
    @StringRes title: Int,
    from: BaseEntity,
    controller: PagingController<*>
) {
    val items = controller.itemsAsState
    var loading by remember { mutableStateOf(false) }
    val lazyColumnState = rememberLazyListState()
    val pageFetcherScope = rememberCoroutineScope()

    lazyColumnState.OnBottomReached {
        loading = true
        pageFetcherScope.launch {
            try {
                val newItems = controller.fetchNextPage()
                Log.v(LogTag, "Fetching new page: ${controller.pages.size}")
                loading = false
                if (newItems != null) {
                    if (controller.entity === LastfmEntity.TRACK) {
                        @Suppress("UNCHECKED_CAST") // Suppressing as the entity was checked
                        MusicorumResource.fetchTracksResources(newItems as List<Track>)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect("") {
        pageFetcherScope.launch {
            try {
                if (controller.entity === LastfmEntity.TRACK) {
                    @Suppress("UNCHECKED_CAST") // Suppressing as the entity was checked
                    MusicorumResource.fetchTracksResources(controller.getAllItems() as List<Track>)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            SecondaryAppBar(
                title = stringResource(title),
                subTitle = from.name,
                extendToStatusBar = true,
                showBackButton = true
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = PaddingSpacing.HorizontalMainPadding)
                .fillMaxWidth(),
            state = lazyColumnState
        ) {
            item {
                Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding))
            }
            items(items) {
                when (it) {
                    is Track -> {
                        TrackListItem(track = it)
                    }
                    is Album -> {
                        AlbumListItem(album = it)
                    }
                    is Artist -> {
                        ArtistListItem(artist = it)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (loading) {
                        CircularProgressIndicator()
                    }
                }
                Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding * 2))
            }
        }
    }
}