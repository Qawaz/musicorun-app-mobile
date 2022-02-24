package com.musicorumapp.mobile.ui.contexts

import androidx.annotation.StringRes
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
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
    fun addArtist(artist: Artist): String {
        val id = Utils.md5Hash(artist.name)
        artistsStore[id] = artist
        return id
    }

    fun addPagingController(@StringRes title: Int, controller: PagingController<*>, from: String?): String {
        val id = Random.nextInt(0, Int.MAX_VALUE).toString()
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