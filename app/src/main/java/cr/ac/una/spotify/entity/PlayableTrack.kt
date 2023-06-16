package cr.ac.una.spotify.entity

open abstract class PlayableTrack(
    var isPlaying: Boolean = false,
) {
    abstract fun getPreviewUrl(): String?
}
