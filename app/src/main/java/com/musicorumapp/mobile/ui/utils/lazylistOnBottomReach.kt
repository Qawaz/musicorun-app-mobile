package com.musicorumapp.mobile.ui.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.remember
import androidx.compose.runtime.*
import kotlinx.coroutines.flow.collect

@Composable
fun LazyListState.OnBottomReached(
    callback: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf true
            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .collect {
                if (it) callback()
            }
    }
}