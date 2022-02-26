package com.musicorumapp.mobile.ui.contexts

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.api.models.*
import com.musicorumapp.mobile.utils.Utils
import kotlin.random.Random

class LocalNavigationContextContent(
    val navigationController: NavHostController? = null,
    val artistsStore: MutableMap<String, Artist> = mutableMapOf(),
    val albumsStore: MutableMap<String, Album> = mutableMapOf(),
    val tracksStore: MutableMap<String, Track> = mutableMapOf(),
    val usersStore: MutableMap<String, User> = mutableMapOf(),
    val pagingControllerStore: MutableMap<String, ExtendedListStoreItem> = mutableMapOf(),
) {
    private fun generateId(): String {
        return (Random.nextInt(0, Int.MAX_VALUE) + System.nanoTime()).toString()
    }

    fun addArtist(artist: Artist): String {
        val id = generateId()
        artistsStore[id] = artist
        return id
    }

    fun addAlbum(album: Album): String {
        val id = generateId()
        albumsStore[id] = album
        return id
    }

    fun addTrack(track: Track): String {
        val id = generateId()
        tracksStore[id] = track
        return id
    }

    fun addPagingController(
        @StringRes title: Int,
        controller: PagingController<*>,
        from: String?
    ): String {
        val id = generateId()
        pagingControllerStore[id] = ExtendedListStoreItem(title, controller, from)
        return id
    }

    data class ExtendedListStoreItem(
        @StringRes val title: Int,
        val controller: PagingController<*>,
        val from: String?
    )
}

val LocalNavigationContext = compositionLocalOf {
    LocalNavigationContextContent()
}