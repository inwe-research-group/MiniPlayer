package com.dsm.miniplayer.repository

import com.dsm.firebaseauth.presentation.model.Player
import com.dsm.firebaseauth.presentation.model.Song
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class MusicRepository {
    private val db = FirebaseDatabase.getInstance().reference

    suspend fun getSongs(): List<Song> {
        val snapshot = db.child("songs").get().await()
        return snapshot.children.mapNotNull { it.getValue(Song::class.java) }
    }

    suspend fun getPlayer(): Player? {
        val snapshot = db.child("player").get().await()
        return snapshot.getValue(Player::class.java)
    }

    fun updatePlayer(player: Player) {
        db.child("player").setValue(player)
    }
}