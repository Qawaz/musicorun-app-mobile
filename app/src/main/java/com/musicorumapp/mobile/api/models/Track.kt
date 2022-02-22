package com.musicorumapp.mobile.api.models

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.utils.Utils
import com.squareup.moshi.Json

data class Track(
    override val entity: LastfmEntity = LastfmEntity.TRACK,
    override val name: String,
    val artist: String? = null,
    val url: String? = null,
    val playCount: Int? = null,
    val listeners: Int? = null,
    var images: LastfmImages
) : PageableItem, BaseEntity {
    var resource: TrackResource? = null
        set(value) {
            field = value
            imageUrlState.value = imageURL
        }

    override val imageURL: String?
        get() = images.bestImage ?: resource?.spotify_covers?.getOrNull(0)

    val imageUrlState = mutableStateOf<String?>(null)

    companion object {
        fun fromSample(): Track = Track(
            name = "Sample track",
            images = LastfmImages.fromEmpty(LastfmEntity.TRACK),
            artist = "Sample artist"
        )
    }
}

data class TrackFromArtistTopTracksItem(
    val name: String,
    val playcount: Any,
    val listeners: Any,
    val url: String,
    val artist: TrackFromArtistTopTracksArtistItem,

    ) {
    data class TrackFromArtistTopTracksArtistItem(
        val name: String,
        val url: String
    )

    fun toTrack(): Track = Track(
        name = name,
        url = url,
        playCount = Utils.anyToInt(playcount),
        listeners = Utils.anyToInt(listeners),
        artist = artist.name,
        images = LastfmImages.fromEmpty(LastfmEntity.TRACK)
    )
}


data class LastfmTrackSearchResponse(
    @field:Json(name = "opensearch:totalResults")
    val totalResults: Any,

    @field:Json(name = "opensearch:startIndex")
    val startIndex: Any,

    @field:Json(name = "trackmatches")
    val matches: LastfmTrackSearchMatchesResponse
) {
    data class LastfmTrackSearchMatchesResponse(
        @field:Json(name = "track")
        val tracks: List<LastfmTrackSearchMatchesItemResponse>
    ) {
        data class LastfmTrackSearchMatchesItemResponse(
            val name: String,
            val artist: String?,
            val url: String?,
            val listeners: Any?,
            val image: List<ImageResourceSerializable>
        ) {
            fun toTrack(): Track {
                return Track(
                    name = name,
                    artist = artist,
                    url = url,
                    listeners = Utils.anyToInt(listeners ?: 0),
                    images = LastfmImages.fromEmpty(LastfmEntity.TRACK)
                )
            }
        }
    }
}