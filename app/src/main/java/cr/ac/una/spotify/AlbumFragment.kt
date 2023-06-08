package cr.ac.una.spotify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import cr.ac.una.spotify.adapters.AlbumTrackAdapter
import cr.ac.una.spotify.databinding.FragmentAlbumBinding
import cr.ac.una.spotify.databinding.FragmentFirstBinding
import cr.ac.una.spotify.entity.AlbumTrack
import cr.ac.una.spotify.entity.Track

class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var adapter = AlbumTrackAdapter(
            requireContext(),
            mutableListOf<AlbumTrack>()
        )
        val glide by lazy { Glide.with(requireContext()) }
        mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.currentAlbum.observe(requireActivity()) {
            it.albumTracks?.let { albumTracks ->
                adapter.updateData(albumTracks.items as ArrayList<AlbumTrack>)
            }
            glide
                .load(it.images[0].url)
                .into(binding.albumImage)
            binding.albumName.text = it.name
            binding.release.text = it.releaseDate
            binding.genres.text = it.genres.let {
                if (it != null && it.isNotEmpty()) {
                    it.joinToString(" - ")
                } else {
                    ""
                }
            }
        }
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        mainViewModel.getAlbumInfo(mainViewModel.currentTrack.value?.album)
    }

}