package com.sb.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

typealias CoroutineBlock = suspend CoroutineScope.() -> Unit

interface CoroutineManager {
    fun launchIO(
        scope: CoroutineScope,
        onError: (Throwable) -> Unit,
        block: CoroutineBlock,
    ): Job

    fun launchMain(
        scope: CoroutineScope,
        onError: (Throwable) -> Unit,
        block: CoroutineBlock,
    ): Job

    fun launchCustom(
        scope: CoroutineScope,
        customDispatcher: CoroutineDispatcher,
        onError: (Throwable) -> Unit,
        block: CoroutineBlock,
    ): Job
}