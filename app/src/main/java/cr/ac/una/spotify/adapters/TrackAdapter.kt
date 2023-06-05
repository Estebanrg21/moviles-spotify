package cr.ac.una.spotify.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import cr.ac.una.spotify.R
import cr.ac.una.spotify.entity.Track

class TrackAdapter(private val context: Context,private var tracks: List<Track>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = tracks[position]
        (holder as ViewHolder).bind(item, Glide.with(context))
    }

    fun updateData(newData: ArrayList<Track>) {
        tracks = newData
        if (newData.isNotEmpty())
            notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val albumImage = itemView.findViewById<ImageButton>(R.id.album_image)
        val trackName = itemView.findViewById<TextView>(R.id.track_name)
        val albumName = itemView.findViewById<TextView>(R.id.album_name)

        fun bind(track: Track, glide: RequestManager) {
            trackName.setText(track.name)
            albumName.setText(track.album.name)
            glide.load(track.album.images[0].url).into(albumImage)
        }
    }
}