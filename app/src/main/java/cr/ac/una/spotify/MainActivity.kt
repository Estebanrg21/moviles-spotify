package cr.ac.una.spotify;

import android.os.Bundle
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import cr.ac.una.spotify.databinding.ActivityMainBinding
import cr.ac.una.spotify.entity.Track


class MainActivity : AppCompatActivity() {

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    public val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        mainViewModel.requestAccessToken()
        mainViewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                displayErrorMessage(message)
            }
        }
        mainViewModel.requestAccessToken()
    }

    fun showPopup(v: View) {
        val popup = PopupMenu(this, v).apply {
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.album -> {
                        navController.navigate(R.id.action_FirstFragment_to_AlbumFragment)
                        true
                    }
                    R.id.artista -> {
                        true
                    }
                    else -> true

                }
            }
        }
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.song_menu, popup.menu)
        popup.show()
    }

    public fun openMenu(track: Track, view: View) {
        mainViewModel.setCurrentTrack(track)
        showPopup(view)
    }


    private fun displayTrackInfo(trackName: String, artistName: String) {
        val message = "Canción encontrada: $trackName - $artistName"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}