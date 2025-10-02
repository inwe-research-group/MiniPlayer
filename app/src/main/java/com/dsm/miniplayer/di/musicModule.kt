package com.dsm.miniplayer.di

import com.dsm.miniplayer.repository.MusicRepository
import com.dsm.miniplayer.home.MusicViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val musicModule = module {
    single { MusicRepository() }
    viewModel { MusicViewModel(get()) }
}