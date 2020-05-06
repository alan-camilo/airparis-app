package fr.parisrespire.mpp.data.http

actual object NetworkConnectivityImpl: NetworkConnectivity {
    actual override fun checkConnectivity(): Boolean = true
}
