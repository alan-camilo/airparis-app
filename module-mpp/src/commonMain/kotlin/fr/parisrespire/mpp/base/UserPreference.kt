package fr.parisrespire.mpp.base

expect object UserPreference {

    fun getBoolean(key: String, defValue: Boolean): Boolean

    fun getLong(key: String, defValue: Long): Long

    fun getString(key: String, defValue: String?): String?

    fun set(key: String, value: Boolean)

    fun set(key: String, value: Long)

    fun set(key: String, value: String?)
}
