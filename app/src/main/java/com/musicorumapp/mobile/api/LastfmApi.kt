package com.musicorumapp.mobile.api

import android.util.Log
import com.musicorumapp.mobile.LogTag
import com.musicorumapp.mobile.api.interceptors.LastfmKeyInterceptor
import com.musicorumapp.mobile.api.interceptors.LastfmMethodInterceptor
import com.musicorumapp.mobile.api.interceptors.SignedRequestInterceptor
import com.musicorumapp.mobile.api.models.*
import com.musicorumapp.mobile.utils.Utils
import com.serjltt.moshi.adapters.FallbackOnNull
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


private const val LASTFM_API_URL = "https://ws.audioscrobbler.com/"

class LastfmApi {
    companion object {
        fun getInstance(): Retrofit {

            val client = OkHttpClient.Builder()
                .addInterceptor(LastfmKeyInterceptor())
                .addInterceptor(LastfmMethodInterceptor())
                .addInterceptor(SignedRequestInterceptor())
                .addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()


            return Retrofit.Builder()
                .client(client)
                .baseUrl(LASTFM_API_URL)
                .addConverterFactory(MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(Wrapped.ADAPTER_FACTORY)
                        .add(FallbackOnNull.ADAPTER_FACTORY)
                        .build()
                ))
                .build()
        }

        fun getAuthEndpoint(): LastfmAuthEndpoint {
            return getInstance().create(LastfmAuthEndpoint::class.java)
        }

        fun getUserEndpoint(): LastfmUserEndpoint {
            return getInstance().create(LastfmUserEndpoint::class.java)
        }

        fun getArtistEndpoint(): LastfmArtistEndpoint {
            return getInstance().create(LastfmArtistEndpoint::class.java)
        }

        fun getAlbumsEndpoint(): LastfmAlbumEndpoint {
            return getInstance().create(LastfmAlbumEndpoint::class.java)
        }

        fun getTracksEndpoint(): LastfmTrackEndpoint {
            return getInstance().create(LastfmTrackEndpoint::class.java)
        }

        suspend fun searchArtists(query: String, perPage: Int = 30): PagingController<Artist> {
            var totalResults = 0

            val controller = PagingController(
                entity = LastfmEntity.ARTIST,
                perPage = perPage
            ) { pg ->
                val items = getArtistEndpoint().searchArtists(query, perPage, pg)

                Log.v(LogTag, items.totalResults.toString())
                totalResults = Utils.anyToInt(items.totalResults)

                items.matches.artist.map { it.toArtist() }
            }
            controller.fetchPage(1)
            controller.totalPages = totalResults / perPage
            controller.totalResults.value = totalResults

            Log.v(LogTag, controller.toString())

            return controller
        }

        suspend fun searchAlbums(query: String, perPage: Int = 30): PagingController<Album> {
            var totalResults = 0

            val controller = PagingController(
                entity = LastfmEntity.ALBUM,
                perPage = perPage
            ) { pg ->
                val items = getAlbumsEndpoint().searchAlbums(query, perPage, pg)

                totalResults = Utils.anyToInt(items.totalResults)

                items.matches.albums.map { it.toAlbum() }
            }
            controller.fetchPage(1)
            controller.totalPages = totalResults / perPage
            controller.totalResults.value = totalResults

            return controller
        }

        suspend fun searchTracks(query: String, perPage: Int = 30): PagingController<Track> {
            var totalResults = 0

            val controller = PagingController(
                entity = LastfmEntity.TRACK,
                perPage = perPage
            ) { pg ->
                val items = getTracksEndpoint().searchTracks(query, perPage, pg)

                totalResults = Utils.anyToInt(items.totalResults)

                items.matches.tracks.map { it.toTrack() }
            }
            controller.fetchPage(1)
            controller.totalPages = totalResults / perPage
            controller.totalResults.value = totalResults

            return controller
        }
    }
}