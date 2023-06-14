package cr.ac.una.spotify.entity

import com.google.gson.annotations.SerializedName

data class Track(
    val name: String,
    var album: Album,
    val uri: String,
    @SerializedName("preview_url") val preview: String,
    var isPlaying: Boolean = false
)