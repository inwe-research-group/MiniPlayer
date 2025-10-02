package com.dsm.miniplayer

import android.app.Application
import com.dsm.miniplayer.di.musicModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MiniPlayerApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MiniPlayerApp)
            modules(
                musicModule
            )
        }
    }
}