package com.musicorumapp.mobile.repos

import com.musicorumapp.mobile.api.LastfmAlbumEndpoint
import com.musicorumapp.mobile.api.models.Album
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepository @Inject constructor(
    private val albumEndpoint: LastfmAlbumEndpoint
) {
    suspend fun getAlbumInfo (album: Album, userName: String) {
        val source = albumEndpoint.getAlbumInfo(album.name, album.artist.orEmpty(), userName).toAlbum()

        album.name = source.name
        album.url = source.url
        album.artist = source.artist
        album.listeners = source.listeners
        album.playCount = source.playCount
        album.userPlayCount = source.userPlayCount
        album.wiki = source.wiki
        album.tracks = source.tracks
        album.images = source.images
    }
}