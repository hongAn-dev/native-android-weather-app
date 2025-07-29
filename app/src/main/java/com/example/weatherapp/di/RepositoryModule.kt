package com.example.weatherapp.di

import com.example.weatherapp.data.repository.WeatherRepoImpl
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepoImpl
    ) : WeatherRepository

}