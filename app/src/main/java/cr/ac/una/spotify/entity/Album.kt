package cr.ac.una.spotify.entity

import com.google.gson.annotations.SerializedName

data class Album(
    val id: String,
    val name: String,
    val images: List<AlbumImage>,
    @SerializedName("release_date") val releaseDate: String = "",
    val genres: List<String> = emptyList(),
    @SerializedName("tracks") val albumTracks: AlbumTracks? = null
) {
}