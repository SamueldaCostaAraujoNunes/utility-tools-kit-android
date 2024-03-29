package com.samuelnunes.di

import android.content.Context
import com.samuelnunes.data.Constants
import com.samuelnunes.data.Constants.CATS_API_BASE_URL
import com.samuelnunes.data.local.AppDatabase
import com.samuelnunes.data.local.dao.BreedDao
import com.samuelnunes.data.local.dao.CatsDao
import com.samuelnunes.data.local.dao.ImageDao
import com.samuelnunes.data.remote.api.TheCatApi
import com.samuelnunes.data.repository.CatsRepository
import com.samuelnunes.domain.repository.ICatsRepository
import com.samuelnunes.utility_tool_kit.database.dao.buildRoomDatabase
import com.samuelnunes.utility_tool_kit.network.EnumConverter.EnumConverterFactory
import com.samuelnunes.utility_tool_kit.network.FlowAdapter.FlowCallAdapterFactory
import com.samuelnunes.utility_tool_kit.network.LiveDataAdapter.LiveDataCallAdapterFactory
import com.samuelnunes.utility_tool_kit.network.NetworkConnectivityObserver
import com.samuelnunes.utility_tool_kit.network.buildRetrofit
import com.samuelnunes.utility_tool_kit.network.client
import com.samuelnunes.utility_tool_kit.network.naturalAdapter.ResourceCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providerRetrofitBuilder(): Retrofit = buildRetrofit {
        addCallAdapterFactory(ResourceCallAdapterFactory.create())
        addCallAdapterFactory(LiveDataCallAdapterFactory.create())
        addCallAdapterFactory(FlowCallAdapterFactory.create())
        addConverterFactory(EnumConverterFactory.create())
        addConverterFactory(GsonConverterFactory.create())
        baseUrl(CATS_API_BASE_URL)

        client {
            readTimeout(30L, TimeUnit.SECONDS)
            connectTimeout(30L, TimeUnit.SECONDS)
        }

    }

    @Singleton
    @Provides
    fun providerTheCatApiService(retrofit: Retrofit): TheCatApi = retrofit.create()

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context): AppDatabase =
        buildRoomDatabase(context, AppDatabase::class.java, Constants.TABLE_NAME_CATS) {
            fallbackToDestructiveMigration()
        }

    @Singleton
    @Provides
    fun provideCatsDao(db: AppDatabase): CatsDao = db.catsDao()

    @Singleton
    @Provides
    fun provideBreedDao(db: AppDatabase): BreedDao = db.breedDao()

    @Singleton
    @Provides
    fun provideImageDao(db: AppDatabase): ImageDao = db.imageDao()

    @Singleton
    @Provides
    fun provideNetworkConnection(@ApplicationContext appContext: Context): NetworkConnectivityObserver =
        NetworkConnectivityObserver(appContext)

    @Singleton
    @Provides
    fun providerTheCatsRepository(
        theCatApi: TheCatApi,
        catsDao: CatsDao,
        breedDao: BreedDao,
        imageDao: ImageDao
    ): ICatsRepository = CatsRepository(theCatApi, catsDao, breedDao, imageDao)
}