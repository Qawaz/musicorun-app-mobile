package com.musicorumapp.mobile.api.models

import com.musicorumapp.mobile.utils.Utils
import com.squareup.moshi.Json

data class Album(
    override val entity: LastfmEntity = LastfmEntity.ALBUM,
    override var name: String,
    var artist: String? = null,
    var url: String,
    var images: LastfmImages,
    var listeners: Int? = null,
    var playCount: Int? = null,
    var userPlayCount: Int? = null,
    var tracks: MutableMap<Short, Track> = mutableMapOf(),
    var tags: MutableList<String> = mutableListOf(),
    var wiki: Wiki? = null,

    ) : PageableItem, BaseEntity {
    private val onResourcesChangeCallbacks: MutableList<(Album) -> Unit> = mutableListOf()

    var resource: AlbumResource? = null
        set(value) {
            field = value
            onResourcesChangeCallbacks.forEach { it(this) }
        }

    override val imageURL: String?
        get() = images.bestImage ?: resource?.spotify_covers?.getOrNull(0)

    fun onResourcesChange(cb: (Album) -> Unit) {
        onResourcesChangeCallbacks.add(cb)
    }

    companion object {
        fun fromSample(): Album {
            return Album(
                name = "Planet Her",
                artist = "Doja cat",
                url = "https://last.fm/music/Doja+Cat/Planet+Her",
                images = LastfmImages.fromEmpty(LastfmEntity.ALBUM),
            )
        }
    }
}

data class LastfmAlbumInfoResponse(
    val name: String,
    val artist: String,
    val url: String,
    val image: List<ImageResourceSerializable>,
    val listeners: Any,
    val playcount: Any,
    val userplaycount: Any,
    val tracks: LastfmAlbumInfoResponseTracks?,
    val tags: Any?,
    val wiki: WikiResponse?
) {
    data class LastfmAlbumInfoResponseTracks(
        val track: List<LastfmAlbumInfoResponseTracksItem>?
    ) {
        data class LastfmAlbumInfoResponseTracksItem(
            val name: String,
            val url: String,
            val artist: LastfmAlbumInfoResponseTracksItemArtist,

            @field:Json(name = "@attr")
            val attr: LastfmAlbumInfoResponseTracksItemAttr,
        ) {
            data class LastfmAlbumInfoResponseTracksItemAttr(
                val rank: Any
            )

            data class LastfmAlbumInfoResponseTracksItemArtist(
                val name: String
            )

            fun toTrack(): Track = Track(
                name = name,
                artist = artist.name,
                images = LastfmImages.fromEmpty(LastfmEntity.ALBUM),
                url = url
            )
        }
    }

    data class LastfmAlbumInfoResponseTags(
        val tag: List<LastfmAlbumInfoResponseTagsItem>
    ) {
        data class LastfmAlbumInfoResponseTagsItem(
            val name: String,
        )
    }

    fun toAlbum(): Album {
        val images = LastfmImages.fromSerializable(image, LastfmEntity.ALBUM)

        val tags = if (tags is LastfmAlbumInfoResponseTags)
            tags.tag.map { it.name }
        else null
        val tracksItems = mutableMapOf<Short, Track>()

        tracks?.track?.forEach {
            val t = it.toTrack()
            t.images = images
            tracksItems[Utils.anyToInt(it.attr.rank).toShort()] = t
        }

        return Album(
            name = name,
            artist = artist,
            url = url,
            images = images,
            listeners = Utils.anyToInt(listeners),
            playCount = Utils.anyToInt(playcount),
            userPlayCount = Utils.anyToInt(userplaycount),
            tags = (tags ?: emptyList()).toMutableList(),
            wiki = wiki?.toWiki(),
            tracks = tracksItems
        )
    }
}

data class LastfmAlbumFromArtistTopAlbumsResponseItem(
    val name: String,
    val playcount: Any,
    val url: String,
    val artist: LastfmAlbumFromArtistTopAlbumsResponseArtistItem,
    val image: List<ImageResourceSerializable>,

    ) {
    data class LastfmAlbumFromArtistTopAlbumsResponseArtistItem(
        val name: String,
        val url: String
    )

    fun toAlbum(): Album = Album(
        name = name,
        playCount = Utils.anyToInt(playcount),
        url = url,
        artist = artist.name,
        images = LastfmImages.fromSerializable(image, LastfmEntity.ALBUM)
    )
}

data class LastfmAlbumSearchResponse(
    @field:Json(name = "opensearch:totalResults")
    val totalResults: Any,

    @field:Json(name = "opensearch:startIndex")
    val startIndex: Any,

    @field:Json(name = "albummatches")
    val matches: LastfmAlbumSearchMatchesResponse
) {
    data class LastfmAlbumSearchMatchesResponse(
        @field:Json(name = "album")
        val albums: List<LastfmAlbumSearchMatchesItemResponse>
    ) {
        data class LastfmAlbumSearchMatchesItemResponse(
            val name: String,
            val artist: String?,
            val url: String,
            val image: List<ImageResourceSerializable>
        ) {
            fun toAlbum(): Album {
                return Album(
                    name = name,
                    artist = artist,
                    url = url,
                    images = LastfmImages.fromSerializable(image, LastfmEntity.ALBUM)
                )
            }
        }
    }
}