package io.musicorum.mobile.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopTracks(
    @SerialName("toptracks")
    val topTracks: TopTracksData
)

@Serializable
data class TopTracksData(
    @SerialName("track")
    val tracks: List<Track>,
    @SerialName("@attr")
    val attributes: RecentTracksAttributes
)