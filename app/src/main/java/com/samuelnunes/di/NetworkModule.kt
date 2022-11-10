package com.samuelnunes.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.samuelnunes.data.Constants
import com.samuelnunes.data.Constants.CATS_API_BASE_URL
import com.samuelnunes.data.local.AppDatabase
import com.samuelnunes.data.local.dao.CatsDao
import com.samuelnunes.data.remote.api.TheCatApi
import com.samuelnunes.data.repository.CatsRepository
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.network.EnumConverter.EnumConverterFactory
import com.samuelnunes.utility_tool_kit.network.FlowAdapter.FlowCallAdapterFactory
import com.samuelnunes.utility_tool_kit.network.LiveDataAdapter.LiveDataCallAdapterFactory
import com.samuelnunes.utility_tool_kit.network.NetworkConnectivityObserver
import com.samuelnunes.utility_tool_kit.network.naturalAdapter.ResourceCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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
        .addCallAdapterFactory(ResourceCallAdapterFactory.create())
        .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
        .addCallAdapterFactory(FlowCallAdapterFactory.create())
        .addConverterFactory(EnumConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))

    @Singleton
    @Provides
    fun providerTheCatApiService(builder: Retrofit.Builder): TheCatApi =
        builder.baseUrl(CATS_API_BASE_URL).build().create()

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, Constants.TABLE_NAME_CATS)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase): CatsDao = db.catsDao()

    @Singleton
    @Provides
    fun provideNetworkConnection(@ApplicationContext appContext: Context): NetworkConnectivityObserver = NetworkConnectivityObserver(appContext)

    @Singleton
    @Provides
    fun providerTheCatsRepository(theCatApi: TheCatApi, dao: CatsDao): ICatsRepository = CatsRepository(theCatApi, dao)
}