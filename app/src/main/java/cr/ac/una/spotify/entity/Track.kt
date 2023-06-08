package cr.ac.una.spotify.entity

data class Track(
    val name: String,
    var album: Album,
    val uri: String
)