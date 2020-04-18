package parisrespire.base

internal expect fun runBlocking(block: suspend () -> Unit)
