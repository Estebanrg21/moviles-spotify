package cr.ac.una.spotify

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.ac.una.spotify.http.RESTClient
import cr.ac.una.spotify.service.SpotifyService
import androidx.lifecycle.viewModelScope
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.http.AccessInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val spotifyAuthService: SpotifyService.Auth by lazy {
        RESTClient.SpotifyAuthClient
            .getInstance().create(SpotifyService.Auth::class.java)
    }

    private var spotifyService: SpotifyService? = null
    private var _searchTrackResults: MutableLiveData<List<Track>> = MutableLiveData()
    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    private var _currentTrack: MutableLiveData<Track> = MutableLiveData()
    var searchTrackResults: LiveData<List<Track>> = _searchTrackResults
    var errorMessage: LiveData<String> = _errorMessage
    var currentTrack: LiveData<Track> = _currentTrack

    fun requestAccessToken() = viewModelScope.launch(Dispatchers.IO) {
        val response = spotifyAuthService.getClientCredentials().execute()
        if (response.isSuccessful) {
            val accessTokenResponse = response.body()
            val accessToken = accessTokenResponse?.accessToken
            if (accessToken != null) {
                RESTClient.SpotifyApiClient.accessInteceptor = AccessInterceptor(accessToken)
                spotifyService = RESTClient.SpotifyApiClient
                    .getInstance()
                    .create(SpotifyService::class.java)
                return@launch
            }
        }
        throw java.lang.IllegalStateException("Spotify Access Token request failed")
    }

    fun searchTrack(trackName: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = spotifyService?.searchTrack(trackName)?.execute()
        if (response?.isSuccessful == true) {
            val trackResponse = response.body()
            if (trackResponse != null && trackResponse.tracks.items.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    _searchTrackResults.value = trackResponse.tracks.items
                }
            } else {
                _errorMessage.value = "No se encontraron canciones."
            }
        } else {
            System.out.println("Mensaje:    " + response?.raw())
            _errorMessage.value = "Error en la respuesta del servidor."
        }
    }

    fun setCurrentTrack(track: Track) {
        _currentTrack.postValue(track)
    }
}