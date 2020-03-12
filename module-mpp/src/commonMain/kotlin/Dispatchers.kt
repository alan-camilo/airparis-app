package dispatchers

import kotlinx.coroutines.CoroutineDispatcher

internal expect val Main: CoroutineDispatcher

internal expect val Background: CoroutineDispatcher

internal expect val IO: CoroutineDispatcher
