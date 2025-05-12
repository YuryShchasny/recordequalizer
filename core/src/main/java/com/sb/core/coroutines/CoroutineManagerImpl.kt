package com.sb.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CoroutineManagerImpl : CoroutineManager {

    private val mainDispatcher = Dispatchers.Main
    private val ioDispatcher = Dispatchers.IO
    private val defaultDispatcher = Dispatchers.Default

    override fun launchIO(
        scope: CoroutineScope,
        onError: (Throwable) -> Unit,
        block: CoroutineBlock,
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            scope.launch(mainDispatcher) {
                onError(throwable)
            }
        }

        return scope.launch(
            context = exceptionHandler + ioDispatcher,
            block = block
        )
    }

    override fun launchMain(
        scope: CoroutineScope,
        onError: (Throwable) -> Unit,
        block: CoroutineBlock,
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            scope.launch(mainDispatcher) {
                onError(throwable)
            }
        }

        return scope.launch(
            context = exceptionHandler + mainDispatcher,
            block = block
        )
    }

    override fun launchDefault(
        scope: CoroutineScope,
        onError: (Throwable) -> Unit,
        block: CoroutineBlock,
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            scope.launch(mainDispatcher) {
                onError(throwable)
            }
        }

        return scope.launch(
            context = exceptionHandler + defaultDispatcher,
            block = block
        )
    }

    override fun launchCustom(
        scope: CoroutineScope,
        customDispatcher: CoroutineDispatcher,
        onError: (Throwable) -> Unit,
        block: CoroutineBlock,
    ): Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            scope.launch(mainDispatcher) {
                onError(throwable)
            }
        }

        return scope.launch(
            context = exceptionHandler + customDispatcher,
            block = block
        )
    }
}