package cr.ac.una.spotify.entity

import com.google.gson.annotations.SerializedName

data class AlbumTrackItems(
    @SerializedName("preview_url") val preview: String?
)