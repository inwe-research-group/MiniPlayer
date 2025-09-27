package com.dsm.miniplayer.home

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dsm.firebaseauth.presentation.model.Player
import com.dsm.firebaseauth.presentation.model.Song
import com.dsm.miniplayer.repository.MusicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MusicViewModel(
    private val repo: MusicRepository = MusicRepository()
): ViewModel() {
        private val _songs = MutableStateFlow<List<Song>>(emptyList())
        val songs: StateFlow<List<Song>> = _songs

        private val _player = MutableStateFlow<Player?>(null)
        val player: StateFlow<Player?> = _player

        private var mediaPlayer: MediaPlayer? = null

        init {
            loadSongs()
        }

        private fun loadSongs() {
            viewModelScope.launch {
                val newSongs = repo.getSongs()
                if (_songs.value != newSongs) { // solo si realmente cambia
                    _songs.value = newSongs
                }
                if (_player.value == null) {
                    _player.value = repo.getPlayer() ?: Player()
                }
            }
        }


    fun playSongAt(index: Int) {
        if (index !in _songs.value.indices) return
        if (_player.value?.currentSongIndex == index && _player.value?.isPlaying == true) {
            return // ya está en reproducción, no hacer nada
        }

        val song = _songs.value[index]
        stopSong()

        mediaPlayer = MediaPlayer().apply {
            setDataSource(song.url)
            prepareAsync()
            setOnPreparedListener {
                start()
                val newPlayer = Player(
                    currentSongIndex = index,
                    isPlaying = true,
                    position = 0
                )
                repo.updatePlayer(newPlayer)
                _player.value = newPlayer
            }
        }
    }


    fun pauseSong() {
        mediaPlayer?.pause()
        _player.value?.let {
            if (it.isPlaying) { // solo actualiza si cambia
                val newPlayer = it.copy(isPlaying = false)
                repo.updatePlayer(newPlayer)
                _player.value = newPlayer
            }
        }
    }

    fun resumeSong() {
        mediaPlayer?.start()
        _player.value?.let {
            if (!it.isPlaying) { // solo actualiza si cambia
                val newPlayer = it.copy(isPlaying = true)
                repo.updatePlayer(newPlayer)
                _player.value = newPlayer
            }
        }
    }


    fun playNext() {
            val current = _player.value ?: return
            val nextIndex = (current.currentSongIndex + 1) % _songs.value.size
            playSongAt(nextIndex)
        }

        fun playPrevious() {
            val current = _player.value ?: return
            val prevIndex = if (current.currentSongIndex - 1 < 0) {
                _songs.value.size - 1
            } else {
                current.currentSongIndex - 1
            }
            playSongAt(prevIndex)
        }

        fun stopSong() {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }

        override fun onCleared() {
            super.onCleared()
            stopSong()
        }
    }
