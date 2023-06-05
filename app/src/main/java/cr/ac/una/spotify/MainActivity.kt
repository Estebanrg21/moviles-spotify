package cr.ac.una.spotify;

import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import cr.ac.una.spotify.databinding.ActivityMainBinding

import cr.ac.una.spotify.entity.AccessTokenResponse
import cr.ac.una.spotify.entity.TrackResponse
import cr.ac.una.spotify.http.RESTClient
import cr.ac.una.spotify.service.SpotifyService

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var binding: ActivityMainBinding
    public val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.requestAccessToken()
        mainViewModel.errorMessage.observe(this) {message ->
            if (message.isNotEmpty()) {
                displayErrorMessage(message)
            }
        }
        mainViewModel.requestAccessToken()
    }

    private fun displayTrackInfo(trackName: String, artistName: String) {
        val message = "Canción encontrada: $trackName - $artistName"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun displayErrorMessage(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}