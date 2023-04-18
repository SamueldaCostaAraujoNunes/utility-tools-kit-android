package com.samuelnunes.utility_tool_kit.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit

inline fun buildRetrofit(builderAction: Retrofit.Builder.() -> Unit): Retrofit =
    Retrofit.Builder().apply(builderAction).build()

inline fun buildClient(builderAction: OkHttpClient.Builder.() -> Unit): OkHttpClient =
    OkHttpClient.Builder().apply(builderAction).build()

fun Retrofit.Builder.client(builderAction: OkHttpClient.Builder.() -> Unit) {
    client(buildClient(builderAction))
}

inline fun buildGson(builderAction: GsonBuilder.() -> Unit): Gson =
    GsonBuilder().apply(builderAction).create()

