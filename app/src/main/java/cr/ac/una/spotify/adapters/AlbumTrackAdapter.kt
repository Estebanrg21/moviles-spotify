package cr.ac.una.spotify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.AlbumTrack
import java.util.concurrent.TimeUnit

class AlbumTrackAdapter(
    private val context: Context,
    private var tracks: List<AlbumTrack>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.album_song, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = tracks[position]
        (holder as ViewHolder).bind(item)
    }

    fun updateData(newData: ArrayList<AlbumTrack>) {
        tracks = newData
        if (newData.isNotEmpty())
            notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackName = itemView.findViewById<TextView>(R.id.track_name)
        val trackDuration = itemView.findViewById<TextView>(R.id.track_duration)

        fun bind(track: AlbumTrack) {
            trackName.setText(track.name)
            val trackDurationValue = track.duration.toLong()
            trackDuration.setText(String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(trackDurationValue),
                TimeUnit.MILLISECONDS.toSeconds(trackDurationValue) -
                        TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(trackDurationValue)
                        )
            ))
        }
    }
}