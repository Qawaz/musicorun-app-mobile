package com.musicorumapp.mobile.ui.pages

import android.icu.text.NumberFormat
import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.statusBarsPadding
import com.musicorumapp.mobile.Constants
import com.musicorumapp.mobile.R
import com.musicorumapp.mobile.ui.contexts.LocalNavigationContext
import com.musicorumapp.mobile.states.models.DiscoverPageViewModel
import com.musicorumapp.mobile.states.models.SearchResults
import com.musicorumapp.mobile.ui.components.*
import com.musicorumapp.mobile.ui.theme.PaddingSpacing
import com.musicorumapp.mobile.ui.theme.PoppinsFontFamily
import com.musicorumapp.mobile.ui.theme.Shapes

@Composable
fun DiscoverPage(
    discoverPageViewModel: DiscoverPageViewModel = viewModel()
) {
    val searchValue = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val localFocusManager = LocalFocusManager.current

    val searchState by discoverPageViewModel.searchState.observeAsState()
    val results by discoverPageViewModel.results.observeAsState()
    val resourcesFetched by discoverPageViewModel.resourcesFetched.observeAsState()

    LazyColumn {
        item {
            AppBar(
                title = stringResource(R.string.search),
                showBackButton = true,
                modifier = Modifier
                    .statusBarsPadding()
            )
        }

        item {
            Column(
                modifier = Modifier
                    .padding(horizontal = PaddingSpacing.HorizontalMainPadding)
                    .padding(bottom = PaddingSpacing.HorizontalMainPadding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(Shapes.Small)
                        .background(Color(0x23FFFFFF))
                ) {
                    BasicTextField(
                        value = searchValue.value,
                        onValueChange = { searchValue.value = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(horizontal = 15.dp, vertical = 10.dp)
                            .focusRequester(focusRequester),
//            colors = TextFieldDefaults.textFieldColors(
//                backgroundColor = Color(0xFF484848),
//                unfocusedIndicatorColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//            ),
                        textStyle = TextStyle(
                            color = Color.White,
                            fontFamily = PoppinsFontFamily
                        ),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search,
                        ),
                        singleLine = true,
                        keyboardActions = KeyboardActions {
                            Log.i(Constants.LOG_TAG, searchValue.value)
                            discoverPageViewModel.search(searchValue.value)
                            localFocusManager.clearFocus()
                        },
                        readOnly = searchState == DiscoverPageViewModel.SearchState.LOADING
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Crossfade(targetState = searchState) {
                    when (it) {
                        DiscoverPageViewModel.SearchState.NONE -> {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    "Search something...",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        DiscoverPageViewModel.SearchState.LOADING -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Box(modifier = Modifier.align(Alignment.Center)) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                        DiscoverPageViewModel.SearchState.RESULTS -> {
                            results?.let {
                                ResultsView(
                                    results!!,
                                    resourcesFetched = resourcesFetched
                                )
                            }
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultsView(
    results: SearchResults,
    resourcesFetched: Boolean?
) {
    if (results.hasResults) {
        val navigationContext = LocalNavigationContext.current

        Log.i(Constants.LOG_TAG, "----- RESULTS VIEW RECOMPOSE")

        Column {
            Section(
                title = stringResource(id = R.string.artists),
                subTitle = stringResource(
                    id = R.string.discover_page_results_text,
                    NumberFormat.getInstance().format(results.artists?.totalResults ?: 0)
                ),
                onClick = {},
            ) {
                Column {
                    results.artists?.getAllItems()?.take(3)?.forEach {
                        ArtistListItem(artist = it, modifier = Modifier.clickable {
                            val id = navigationContext.addArtist(it)
                            navigationContext.navigationController?.navigate("artist/$id")
                        })
                    }
                }
            }
            Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding))
            Section(
                title = stringResource(id = R.string.albums),
                subTitle = stringResource(
                    id = R.string.discover_page_results_text,
                    NumberFormat.getInstance().format(results.albums?.totalResults ?: 0)
                ),
                onClick = {},
            ) {
                Column {
                    results.albums?.getAllItems()?.take(3)?.forEach {
                        AlbumListItem(album = it, modifier = Modifier.clickable { })
                    }
                }
            }
            Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding))
            Section(
                title = stringResource(id = R.string.tracks),
                subTitle = stringResource(
                    id = R.string.discover_page_results_text,
                    NumberFormat.getInstance().format(results.tracks?.totalResults ?: 0)
                ),
                onClick = {},
            ) {
                Column {
                    results.tracks?.getAllItems()?.take(3)?.forEach {
                        TrackListItem(track = it, modifier = Modifier.clickable { })
                    }
                }
            }
            Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding))
            Section(
                title = stringResource(id = R.string.user),
                subTitle = if (results.user == null) stringResource(id = R.string.discover_page_no_results) else null
            ) {
                if (results.user != null) {
                    UserListItem(user = results.user!!)
                }
            }
            Spacer(modifier = Modifier.height(PaddingSpacing.MediumPadding))
        }
    }
}