package com.musicorumapp.mobile.repos

import com.musicorumapp.mobile.api.LastfmArtistEndpoint
import com.musicorumapp.mobile.api.LastfmTrackEndpoint
import com.musicorumapp.mobile.api.models.*
import com.musicorumapp.mobile.utils.Utils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArtistRepository @Inject constructor(
    private val artistEndpoint: LastfmArtistEndpoint,
    private val trackEndpoint: LastfmTrackEndpoint
) {
    suspend fun getArtistInfo(artist: Artist, userName: String) {
        val source = artistEndpoint.artistGetInfo(artist.name, userName).toArtist()

        artist.listeners = source.listeners
        artist.playCount = source.playCount
        artist.userPlayCount = source.userPlayCount
        artist.wiki = source.wiki

        artist.tags.clear()
        artist.tags.addAll(source.tags)

        println("Simular: ${source.similar}")
        artist.similar.clear()
        artist.similar.addAll(source.similar)
    }

    suspend fun getArtistTopTracks(artist: String, perPage: Int = 20): PagingController<Track> {
        var totalResponses = 0
        var totalPages = 0
        val controller = PagingController<Track>(
            entity = LastfmEntity.TRACK,
            perPage = perPage,
            requester = { pg ->
                val response = artistEndpoint.getTopTracks(artist, limit = perPage, page = pg)

                totalResponses = Utils.anyToInt(response.attributes.total)
                totalPages = Utils.anyToInt(response.attributes.totalPages)
                response.tracks.map { it.toTrack() }
            })
        controller.fetchPage(1)
        controller.totalResults = totalResponses
        controller.totalPages = totalPages

        return controller
    }

    suspend fun getArtistTopAlbums(artist: String, perPage: Int = 20): PagingController<Album> {
        var totalResponses = 0
        var totalPages = 0
        val controller = PagingController(
            entity = LastfmEntity.ALBUM,
            perPage = perPage,
            requester = { pg ->
                val response = artistEndpoint.getTopAlbums(artist, limit = perPage, page = pg)

                totalResponses = Utils.anyToInt(response.attributes.total)
                totalPages = Utils.anyToInt(response.attributes.totalPages)
                response.albums.map { it.toAlbum() }
            })
        controller.fetchPage(1)
        controller.totalResults = totalResponses
        controller.totalPages = totalPages

        return controller
    }
}