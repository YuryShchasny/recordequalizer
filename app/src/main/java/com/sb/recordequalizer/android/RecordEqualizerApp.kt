package com.sb.recordequalizer.android

import android.app.Application
import com.sb.recordequalizer.di.appModule
import com.sb.recordequalizer.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RecordEqualizerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        System.loadLibrary("audio_processor")
        startKoin {
            androidContext(this@RecordEqualizerApp)
            modules(appModule, dataModule)
        }
    }
}