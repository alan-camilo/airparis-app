package fr.parisrespire.mpp.data.http

interface NetworkConnectivity {
    fun checkConnectivity(): Boolean
}
expect object NetworkConnectivityImpl: NetworkConnectivity {
    override fun checkConnectivity(): Boolean
}
