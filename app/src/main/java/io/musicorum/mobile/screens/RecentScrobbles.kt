package io.musicorum.mobile.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import io.musicorum.mobile.LocalAnalytics
import io.musicorum.mobile.LocalUser
import io.musicorum.mobile.R
import io.musicorum.mobile.components.CenteredLoadingSpinner
import io.musicorum.mobile.components.MusicorumTopBar
import io.musicorum.mobile.components.TrackItem
import io.musicorum.mobile.viewmodels.RecentSrcobblesViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RecentScrobbles(
    recentSrcobblesViewModel: RecentSrcobblesViewModel = viewModel(),
) {
    val analytics = LocalAnalytics.current!!
    LaunchedEffect(Unit) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "recent_scrobbles")
        }
    }
    val user = LocalUser.current!!
    val recentTracks = recentSrcobblesViewModel.fetchRecentTracks(user.user.name)
        .collectAsLazyPagingItems()
    val state = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            MusicorumTopBar(
                text = stringResource(R.string.recent_scrobbles),
                scrollBehavior = scrollBehavior,
                fadeable = false
            ) {}
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            if (recentTracks.loadState.refresh == LoadState.Loading) {
                CenteredLoadingSpinner()
                return@Column
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(0.dp), state = state) {
                items(recentTracks) { track ->
                    TrackItem(track = track)
                }


                item {
                    when (recentTracks.loadState.append) {
                        LoadState.Loading -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(35.dp))
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}