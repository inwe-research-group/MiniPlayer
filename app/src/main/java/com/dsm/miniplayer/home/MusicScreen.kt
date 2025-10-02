package com.dsm.miniplayer.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dsm.firebaseauth.presentation.model.Song
import org.koin.androidx.compose.koinViewModel

@Composable
fun MusicScreen(viewModel: MusicViewModel = koinViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            "🎵 Mi Spotify",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        // Lista de canciones
        SongList(viewModel, Modifier.weight(1f))

        // Controles de reproducción
        PlayerSection(viewModel)
    }
}

@Composable
fun SongList(viewModel: MusicViewModel, modifier: Modifier = Modifier) {
    val songs by viewModel.songs.collectAsState() // solo canciones

    LazyColumn(modifier = modifier) {
        itemsIndexed(songs) { index, song ->
            Text(
                text = song.title,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.playSongAt(index) }
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun PlayerSection(viewModel: MusicViewModel) {
    val songs by viewModel.songs.collectAsState()
    val player by viewModel.player.collectAsState() // solo player

    player?.let {
        val song = songs.getOrNull(it.currentSongIndex)
        PlayerControls(
            song = song,
            isPlaying = it.isPlaying,
            onPlayPause = {
                if (it.isPlaying) viewModel.pauseSong()
                else viewModel.resumeSong()
            },
            onNext = { viewModel.playNext() },
            onPrevious = { viewModel.playPrevious() }
        )
    }
}

