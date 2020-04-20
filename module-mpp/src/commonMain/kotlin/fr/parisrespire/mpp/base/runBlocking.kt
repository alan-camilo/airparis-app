package fr.parisrespire.mpp.base

internal expect fun runBlocking(block: suspend () -> Unit)
