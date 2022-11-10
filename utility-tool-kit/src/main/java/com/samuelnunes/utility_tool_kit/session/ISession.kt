package com.samuelnunes.utility_tool_kit.session

import java.io.Serializable
import kotlin.reflect.KClass

interface ISession {

    fun clear(key: String)
    fun clearAll()

    fun setData(key: String, value: Serializable?, saveDataNull: Boolean = true)

    fun <T : Serializable> getObject(key: String, kclass: KClass<T>): T?
    fun <T : Serializable> getObject(key: String, jclass: Class<T>): T?

    fun getString(key: String): String?
    fun getString(key: String, default: String = ""): String
    fun getInt(key: String, default: Int = 0): Int
    fun getBoolean(key: String, default: Boolean = false): Boolean
    fun getLong(key: String, default: Long = 0L): Long
    fun getFloat(key: String, default: Float = 0F): Float
}