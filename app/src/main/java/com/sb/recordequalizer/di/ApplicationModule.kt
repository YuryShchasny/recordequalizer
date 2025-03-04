package com.sb.recordequalizer.di

import com.sb.audio_processor.AudioEngine
import com.sb.audio_processor.NativeAudioEngine
import com.sb.core.coroutines.CoroutineManager
import com.sb.core.coroutines.CoroutineManagerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf<CoroutineManager>(::CoroutineManagerImpl).bind<CoroutineManager>()
    singleOf(::NativeAudioEngine).bind<AudioEngine>()
}
