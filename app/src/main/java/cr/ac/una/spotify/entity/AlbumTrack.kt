package cr.ac.una.spotify.entity

import com.google.gson.annotations.SerializedName

data class AlbumTrack(
    val name: String,
    @SerializedName("duration_ms") val duration: Int
) {
}