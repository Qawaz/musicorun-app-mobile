package com.musicorumapp.mobile.state.models

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.*
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.api.models.*
import com.musicorumapp.mobile.repos.ArtistRepository
import com.musicorumapp.mobile.state.SessionState
import com.musicorumapp.mobile.utils.PredominantColorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val artistRepository: ArtistRepository,
    private val sessionState: SessionState
) : ViewModel() {

    var artist by mutableStateOf<Artist?>(null)
    var fetched by mutableStateOf(false)
    var topTracks by mutableStateOf<PagingController<Track>?>(null)
    var topAlbums by mutableStateOf<PagingController<Album>?>(null)
    var predominantColor by mutableStateOf<Color?>(null)
    var imageBitmap by mutableStateOf<Bitmap?>(null)

    fun start(
        context: Context,
        predominantColorState: PredominantColorState,
        snackbarHostState: SnackbarHostState?,
        _artist: Artist
    ) {
        viewModelScope.launch {
            try {
                val user = sessionState.currentUser
                    ?: throw Exception("User not defined in session")

                artistRepository.getArtistInfo(_artist, user.userName)
                artist = _artist


                if (_artist.imageURL == null) {
                    try {
                        MusicorumResource.fetchArtistsResources(listOf(_artist))
                        fetchColors(context, predominantColorState, snackbarHostState)
                    } catch (e: Exception) {

                    }
                } else {
                    fetchColors(context, predominantColorState, snackbarHostState)
                }

                awaitAll(
                    async {
                        try {
                            MusicorumResource.fetchArtistsResources(_artist.similar)
                        } catch (e: Exception) {
                            Log.w(LogTag, "Could not fetch artist resources")
                            e.printStackTrace()
                        }
                    },
                    async {
                        try {
                            topTracks = artistRepository.getArtistTopTracks(_artist.name)
                            MusicorumResource.fetchTracksResources(topTracks!!.getAllItems().take(5))
                        } catch (e: Exception) {
                            Log.w(LogTag, "Could not fetch artist top tracks")
                            e.printStackTrace()
                        }
                    },
                    async {
                        try {
                            topAlbums = artistRepository.getArtistTopAlbums(_artist.name)
                        } catch (e: Exception) {
                            Log.w(LogTag, "Could not fetch artist top albums")
                            e.printStackTrace()
                        }
                    }
                )

                fetched = true

            } catch (e: Exception) {
                e.printStackTrace()
                snackbarHostState?.showSnackbar(e.toString())
            }
        }
    }

    private fun fetchColors(
        context: Context,
        predominantColorState: PredominantColorState,
        snackbarHostState: SnackbarHostState?
    ) {
        if (artist?.imageURL.isNullOrEmpty()) return

        viewModelScope.launch {
            val req = ImageRequest.Builder(context)
                .data(artist?.imageURL)
                .allowHardware(false)
                .build()

            val result = Coil.execute(req)

            if (result is SuccessResult) {
                val bitmap = result.drawable.toBitmap()
                imageBitmap = bitmap
                predominantColorState.resolveColorsFromBitmap(bitmap)
                predominantColor = predominantColorState.color
            } else {
                println(result)
                snackbarHostState?.showSnackbar("Could not download image")
            }
        }
    }
}