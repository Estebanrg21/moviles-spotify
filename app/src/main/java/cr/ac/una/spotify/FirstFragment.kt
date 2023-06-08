package cr.ac.una.spotify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cr.ac.una.spotify.adapters.TrackAdapter
import cr.ac.una.spotify.databinding.FragmentFirstBinding
import cr.ac.una.spotify.entity.Track

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var tracks :List<Track>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var adapter = TrackAdapter(
            requireContext(), mutableListOf<Track>(),
            (requireActivity() as MainActivity)::openMenu
        )
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        tracks = mutableListOf<Track>()

        mainViewModel = (activity as MainActivity).mainViewModel

        mainViewModel.searchTrackResults.observe(viewLifecycleOwner) {tracks ->
            adapter.updateData(tracks as ArrayList<Track>)
            this.tracks = tracks
        }
        binding.buscarBtn.setOnClickListener {
            mainViewModel.searchTrack(binding.songName.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}