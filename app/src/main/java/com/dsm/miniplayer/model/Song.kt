package com.dsm.firebaseauth.presentation.model

data class Song(
    val id: String = "",
    val title: String = "",
    val artistId: String = "",
    val url: String = "" //URL a Firebase Storage o HTTP
)
