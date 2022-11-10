package com.samuelnunes.utility_tool_kit.session

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.io.Serializable
import kotlin.reflect.KClass

open class SharedPreferencesSession(private val context: Context) : ISession {

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    private fun SharedPreferences.Editor.put(pair: Pair<String, Serializable?>) {
        val (key, value) = pair
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> putString(key, Gson().toJson(value))
        }
    }

    private fun getSharedPreferences(): SharedPreferences =
        context.getSharedPreferences("${context.packageName}->${javaClass.simpleName}", Context.MODE_PRIVATE)

    override fun clear(key: String) {
        getSharedPreferences().editMe { it.remove(key) }
    }

    override fun clearAll() =
        getSharedPreferences().editMe { it.clear() }

    override fun setData(key: String, value: Serializable?, saveDataNull: Boolean) {
        if (value != null || saveDataNull) {
            getSharedPreferences().editMe { it.put(key to value) }
        }
    }

    override fun <T : Serializable> getObject(key: String, kclass: KClass<T>): T? = try {
        Gson().fromJson(getString(key), kclass.javaObjectType)
    } catch (e: Exception) {
        null
    }

    override fun <T : Serializable> getObject(key: String, jclass: Class<T>): T? = try {
        Gson().fromJson(getString(key), jclass)
    } catch (e: Exception) {
        null
    }

    override fun getString(key: String, default: String): String =
        getString(key) ?: default

    override fun getString(key: String): String? =
        getSharedPreferences().getString(key, null)

    override fun getInt(key: String, default: Int): Int =
        getSharedPreferences().getInt(key, default)

    override fun getBoolean(key: String, default: Boolean): Boolean =
        getSharedPreferences().getBoolean(key, default)

    override fun getLong(key: String, default: Long): Long =
        getSharedPreferences().getLong(key, default)

    override fun getFloat(key: String, default: Float): Float =
        getSharedPreferences().getFloat(key, default)
}