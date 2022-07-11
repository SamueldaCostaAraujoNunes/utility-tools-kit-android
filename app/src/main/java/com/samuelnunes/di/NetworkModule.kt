package com.samuelnunes.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.samuelnunes.data.remote.Constants.CATS_API_BASE_URL
import com.samuelnunes.data.remote.api.TheCatApi
import com.samuelnunes.data.repository.CatsRepository
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.network.FlowCallAdapterFactory
import com.samuelnunes.utility_tool_kit.network.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providerGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun providerRetrofitBuilder(gson: Gson): Retrofit.Builder = Retrofit.Builder()
        .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
        .addCallAdapterFactory(FlowCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))

    @Singleton
    @Provides
    fun providerTheCatApiService(builder: Retrofit.Builder): TheCatApi =
        builder.baseUrl(CATS_API_BASE_URL).build().create(TheCatApi::class.java)

    @Singleton
    @Provides
    fun providerTheCatsRepository(theCatApi: TheCatApi): ICatsRepository = CatsRepository(theCatApi)

}