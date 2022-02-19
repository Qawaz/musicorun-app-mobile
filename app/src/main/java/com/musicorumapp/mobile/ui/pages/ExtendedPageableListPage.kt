package com.musicorumapp.mobile.ui.pages

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.accompanist.insets.statusBarsPadding
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.api.models.*
import com.musicorumapp.mobile.ui.components.AppBar
import com.musicorumapp.mobile.ui.components.SecondaryAppBar
import com.musicorumapp.mobile.ui.components.SecondaryAppBarTextStyle
import com.musicorumapp.mobile.ui.components.TrackListItem
import com.musicorumapp.mobile.ui.theme.PaddingSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtendedPageableListPage(
    @StringRes title: Int,
    from: BaseEntity,
    controller: PagingController<*>
) {
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
            modifier = Modifier.padding(horizontal = PaddingSpacing.HorizontalMainPadding)
        ) {
            item {
                Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding))
            }
            items(controller.getAllItems()) {
                if (it is Track) {
                    TrackListItem(it)
                }
            }
            item {
                Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding))
            }
        }
    }
}