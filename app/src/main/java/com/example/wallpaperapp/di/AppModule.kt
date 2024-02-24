package com.example.wallpaperapp.di

import android.app.Application
import android.content.Context
import androidx.core.app.ActivityCompat
import com.example.wallpaperapp.Utils.Constants.BASE_URL
import com.example.wallpaperapp.data.api.PicSumApi
import com.example.wallpaperapp.data.api.SharedPreferenceWallpaperCache
import com.example.wallpaperapp.data.api.WallpaperCache
import com.example.wallpaperapp.data.api.WallpaperRepositoryImpl
import com.example.wallpaperapp.domain.repository.WallpaperRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    companion object{
        @Provides
        @Singleton
        fun provideRetrofitApi(): PicSumApi {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PicSumApi::class.java)
        }

        @Provides
        @Singleton
        fun provideContext(application: Application): Context {
            return application.applicationContext // Use application context for better practices
        }
    }

    @Binds
    @Singleton
    fun provideWallpaperRepository(repositoryImpl: WallpaperRepositoryImpl): WallpaperRepository

    @Binds
    @Singleton
    fun provideWallpaperCache(cache: SharedPreferenceWallpaperCache): WallpaperCache


}