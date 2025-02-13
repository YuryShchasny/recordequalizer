package com.sb.recordequalizer.di

import com.sb.core.coroutines.CoroutineManager
import com.sb.core.coroutines.CoroutineManagerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::CoroutineManagerImpl).bind<CoroutineManager>()
}
