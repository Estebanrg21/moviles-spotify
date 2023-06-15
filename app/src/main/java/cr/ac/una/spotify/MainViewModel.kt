package cr.ac.una.spotify

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cr.ac.una.spotify.http.RESTClient
import cr.ac.una.spotify.service.SpotifyService
import androidx.lifecycle.viewModelScope
import cr.ac.una.roomdb.BusquedaDAO
import cr.ac.una.spotify.entity.Album
import cr.ac.una.spotify.entity.Busqueda
import cr.ac.una.spotify.entity.Track
import cr.ac.una.spotify.http.AccessInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MainViewModel : ViewModel() {
    private val spotifyAuthService: SpotifyService.Auth by lazy {
        RESTClient.SpotifyAuthClient
            .getInstance().create(SpotifyService.Auth::class.java)
    }

    private val busquedaDao: MutableLiveData<BusquedaDAO> = MutableLiveData()
    private var spotifyService: SpotifyService? = null
    private var _searchTrackResults: MutableLiveData<List<Track>> = MutableLiveData()
    private var _errorMessage: MutableLiveData<String> = MutableLiveData()
    private var _currentTrack: MutableLiveData<Track> = MutableLiveData()
    private var _currentAlbum: MutableLiveData<Album> = MutableLiveData()
    private var _currentTrackPlaying: MutableLiveData<Track> = MutableLiveData()
    val player by lazy {
        MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
        }
    }
    private var _busquedaResults: MutableLiveData<List<Busqueda?>?> = MutableLiveData()
    var searchTrackResults: LiveData<List<Track>> = _searchTrackResults
    var errorMessage: LiveData<String> = _errorMessage
    var currentTrack: LiveData<Track> = _currentTrack
    var currentAlbum: LiveData<Album> = _currentAlbum
    var busquedaResults: LiveData<List<Busqueda?>?> = _busquedaResults

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

    fun getAlbumInfo(album: Album?) = viewModelScope.launch(Dispatchers.IO) {
        if (album != null) {
            val result = spotifyService?.getAlbumInfo(album.id)
            result?.let {
                withContext(Dispatchers.Main) {
                    _currentAlbum.value = it
                }
                _currentTrack.value?.album = it
            }
        }
    }

    fun saveSearch(text: String) = viewModelScope.launch(Dispatchers.IO) {
        busquedaDao.value?.insert(Busqueda(null, text, Date()))
    }

    fun setCurrentTrack(track: Track) {
        _currentTrack.postValue(track)
    }

    fun setBusquedaDAO(dao: BusquedaDAO) = viewModelScope.launch {
        busquedaDao.value = dao
    }

    fun searchCriterion(text: String) = viewModelScope.launch(Dispatchers.IO) {
        val list = busquedaDao.value?.getBusquedas(text)
        withContext(Dispatchers.Main) {
            _busquedaResults.value = list
        }
    }

    fun playTrack(track: Track) {
        track.preview?.let {
            val task = {
                player.stop()
                player.reset()
                player.setDataSource(track.preview)
                player.prepare()
                player.start()
                track.isPlaying = true
                _currentTrackPlaying.value = track
            }
            if (player.isPlaying) {
                _currentTrackPlaying.value?.isPlaying = false
                if (_currentTrackPlaying.value != track) {
                    task()
                } else {
                    player.pause()
                }
            } else {
                if (_currentTrackPlaying.value != track) {
                    task()
                } else if (!track.isPlaying) {
                    player.start()
                }
            }
        }
    }
}