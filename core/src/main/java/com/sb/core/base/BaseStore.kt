package com.sb.core.base

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.sb.core.coroutines.CoroutineBlock
import com.sb.core.coroutines.CoroutineManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

abstract class BaseStore : InstanceKeeper.Instance, KoinComponent {
    private val coroutineManager: CoroutineManager by inject()

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    fun launchIO(
        onError: (Throwable) -> Unit = {},
        safeAction: CoroutineBlock,
    ): Job = coroutineManager.launchIO(
        scope = scope,
        onError = onError,
        block = safeAction
    )

    fun launchMain(
        onError: (Throwable) -> Unit = {},
        safeAction: CoroutineBlock,
    ): Job = coroutineManager.launchMain(
        scope = scope,
        onError = onError,
        block = safeAction
    )

    fun launchCustom(
        customDispatcher: CoroutineDispatcher,
        onError: (Throwable) -> Unit = {},
        safeAction: CoroutineBlock,
    ): Job = coroutineManager.launchCustom(
        scope = scope,
        customDispatcher = customDispatcher,
        onError = onError,
        block = safeAction
    )

    override fun onDestroy() {
        scope.cancel()
    }
}