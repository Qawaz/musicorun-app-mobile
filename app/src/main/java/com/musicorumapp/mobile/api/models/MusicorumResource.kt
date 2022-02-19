package com.musicorumapp.mobile.api.models

import com.musicorumapp.mobile.api.AlbumsResourcesRequest
import com.musicorumapp.mobile.api.ArtistsResourcesRequest
import com.musicorumapp.mobile.api.MusicorumApi
import com.musicorumapp.mobile.api.TracksResourcesRequest

class MusicorumResource {
    companion object {
        suspend fun fetchArtistsResources(artists: List<Artist>) {
            val names = artists.map { it.name }

            val resources = MusicorumApi.getResourcesEndpoint().fetchArtistsResources(
                ArtistsResourcesRequest(names)
            )

            artists.forEachIndexed { i, artist ->
                artist.resource = resources[i]
            }
        }

        suspend fun fetchAlbumsResources(albums: List<Album>) {
            val resources = MusicorumApi.getResourcesEndpoint().fetchAlbumsResources(
                AlbumsResourcesRequest(albums.map { AlbumsResourcesRequest.AlbumItem(it.name, it.artist.orEmpty()) })
            )

            albums.forEachIndexed { i, album ->
                album.resource = resources[i]
            }
        }

        suspend fun fetchTracksResources(tracks: List<Track>, deezer: Boolean = false, preview: Boolean = false, analysis: Boolean = false) {
            val resources = MusicorumApi.getResourcesEndpoint().fetchTracksResources(
                deezer = deezer,
                preview = preview,
                analysis = analysis,
                body = TracksResourcesRequest(
                    tracks = tracks.map { TracksResourcesRequest.TrackItem(it.name, it.artist.orEmpty()) }
                )
            )

            tracks.forEachIndexed { i, album ->
                album.resource = resources[i]
            }
        }
    }
}

data class ArtistResource(
    val hash: String,
    val name: String?,
    val spotify_id: String?,
    val spotify_images: List<String>
)

data class AlbumResource(
    val hash: String,
    val name: String?,
    val spotify_id: String?,
    val spotify_covers: List<String>
)

data class TrackResource(
    val hash: String,
    val name: String?,
    val artists: List<String>,
    val album: String?,
    val preview: String?,
    val spotify_id: String?,
    val deezer_id: String?,
    val spotify_covers: List<String>,
    val duration: Long?,
    val features: TrackResourceFeatures?
) {
    data class TrackResourceFeatures(
        val energy: Double?,
        val danceability: Double,
        val speechiness: Double,
        val instrumentalness: Double,
        val valence: Double,
        val tempo: Double,
    )
}