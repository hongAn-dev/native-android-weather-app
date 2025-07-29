package com.example.weatherapp.di

import com.example.weatherapp.data.remote.WeatherApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {

    @Provides
    fun provideBaseUrl() = "https://api.openweathermap.org/data/2.5/"

    @Provides
    @Singleton
    fun provideRetrofit(BASE_URL:String): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApiService =
        retrofit.create(WeatherApiService::class.java)

}