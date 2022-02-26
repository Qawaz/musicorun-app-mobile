package com.musicorumapp.mobile.ui.pages.album

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.Coil
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.musicorumapp.mobile.api.ArtistsResourcesRequest
import com.musicorumapp.mobile.api.MusicorumApi
import com.musicorumapp.mobile.api.models.Album
import com.musicorumapp.mobile.api.models.ArtistResource
import com.musicorumapp.mobile.api.models.MusicorumResource
import com.musicorumapp.mobile.repos.AlbumRepository
import com.musicorumapp.mobile.state.SessionState
import com.musicorumapp.mobile.utils.PredominantColorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class AlbumPageViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val sessionState: SessionState
) : ViewModel() {
    var album by mutableStateOf<Album?>(null)
    var artistResource by mutableStateOf<ArtistResource?>(null)
    var imageBitmap by mutableStateOf<Bitmap?>(null)
    var predominantColor by mutableStateOf<Color?>(null)

    fun fetch(
        originalAlbum: Album,
        context: Context,
        predominantColorState: PredominantColorState,
        snackbarHostState: SnackbarHostState?,
    ) {
        viewModelScope.launch {
            try {
                val user = sessionState.currentUser
                    ?: throw Exception("User not defined in session")

                albumRepository.getAlbumInfo(originalAlbum, user.userName)
                album = originalAlbum

                if (album?.imageURL != null) {
                    fetchColors(context, predominantColorState, snackbarHostState)
                }

                if (originalAlbum.artist != null) {
                    val resources =  MusicorumApi.getResourcesEndpoint().fetchArtistsResources(
                        ArtistsResourcesRequest(listOf(originalAlbum.artist!!))
                    )
                    artistResource = resources[0]
                }
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
        if (album == null || album?.imageURL.isNullOrEmpty()) return

        viewModelScope.launch {
            val req = ImageRequest.Builder(context)
                .data(album!!.imageURL)
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