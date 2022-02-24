package com.musicorumapp.mobile.api.models

import androidx.compose.runtime.mutableStateOf
import com.musicorumapp.mobile.utils.Utils
import com.squareup.moshi.Json

class Artist(
    override val entity: LastfmEntity = LastfmEntity.ARTIST,
    override val name: String,
    val url: String,
    var listeners: Int? = null,
    var playCount: Int? = null,
    var userPlayCount: Int? = null,
    val similar: MutableList<Artist> = mutableListOf(),
    val tags: MutableList<String> = mutableListOf(),
    var wiki: Wiki? = null,
) : PageableItem, BaseEntity {
    override val imageURL: String?
        get() = resource?.spotify_images?.getOrNull(0)

    val imageUrlState = mutableStateOf<String?>(null)

    var resource: ArtistResource? = null
        set(value) {
            field = value
            imageUrlState.value = imageURL
        }

    companion object {
        fun fromSample(): Artist {
            return Artist(
                name = "Loona",
                listeners = 361725,
                url = "https://www.last.fm/music/Loona"
            )
        }
    }
}

data class LastfmArtistInfoResponse(
    val name: String,
    val url: String,
    val stats: LastfmArtistInfoResponseStats,
    val similar: LastfmArtistInfoResponseSimilar,
    val tags: LastfmArtistInfoResponseTags?,
    val bio: WikiResponse?
) {
    data class LastfmArtistInfoResponseStats(
        val listeners: Any,
        val playcount: Any,
        val userplaycount: Any,
    )

    data class LastfmArtistInfoResponseSimilar(
        val artist: List<LastfmArtistInfoResponseSimilarItem>
    ) {
        data class LastfmArtistInfoResponseSimilarItem(
            val name: String,
            val url: String
        ) {
            fun toArtist(): Artist = Artist(
                name = name,
                url = url
            )
        }
    }

    data class LastfmArtistInfoResponseTags(
        val tag: List<LastfmArtistInfoResponseTagsItem>?
    ) {
        data class LastfmArtistInfoResponseTagsItem(
            val name: String,
            val url: String
        )
    }

    fun toArtist(): Artist {
        println("TAGS: ${tags?.tag}")
        return Artist(
            name = name,
            url = url,
            listeners = Utils.anyToInt(stats.listeners),
            playCount = Utils.anyToInt(stats.playcount),
            userPlayCount = Utils.anyToInt(stats.userplaycount),
            tags = tags?.tag?.map { it.name }?.toMutableList() ?: mutableListOf(),
            wiki = bio?.toWiki(),
            similar = similar.artist.map { it.toArtist() }.toMutableList()
        )
    }
}

data class LastfmArtistSearchResponse(
    @field:Json(name = "opensearch:totalResults")
    val totalResults: Any,

    @field:Json(name = "opensearch:startIndex")
    val startIndex: Any,

    @field:Json(name = "artistmatches")
    val matches: LastfmArtistSearchMatchesResponse
) {
    data class LastfmArtistSearchMatchesResponse(
        val artist: List<LastfmArtistSearchMatchesItemResponse>
    ) {
        data class LastfmArtistSearchMatchesItemResponse(
            val name: String,
            val listeners: Any,
            val url: String
        ) {
            fun toArtist(): Artist {
                return Artist(
                    name = name,
                    url = url,
                    listeners = Utils.anyToInt(listeners)
                )
            }
        }
    }
}

data class LastfmArtistTopTracksResponse(
    @field:Json(name = "@attr")
    val attributes: ListResponseAttributes,

    @field:Json(name = "track")
    val tracks: List<TrackFromArtistTopTracksItem>
)

data class LastfmArtistTopAlbumsResponse(
    @field:Json(name = "@attr")
    val attributes: ListResponseAttributes,

    @field:Json(name = "album")
    val albums: List<LastfmAlbumFromArtistTopAlbumsResponseItem>
)