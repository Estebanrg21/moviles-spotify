package cr.ac.una.spotify.service

import cr.ac.una.spotify.entity.AccessTokenResponse
import cr.ac.una.spotify.entity.Album
import cr.ac.una.spotify.entity.TrackResponse
import retrofit2.Call
import retrofit2.http.*

interface SpotifyService {
    interface Auth {
        @FormUrlEncoded
        @POST("api/token")
        fun getClientCredentials(
            @Field("grant_type") grantType: String = "client_credentials"
        ): Call<AccessTokenResponse>
    }

    @GET("v1/search?type=track")
    fun searchTrack(@Query("q") query: String): Call<TrackResponse>

    @GET("v1/albums/{albumId}")
    suspend fun getAlbumInfo(@Path("albumId") albumId: String): Album
}