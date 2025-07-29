package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.model.ForecastResponse

interface WeatherRepository {
    suspend fun getCachedWeather(): WeatherEntity?
    suspend fun saveWeather(weather: WeatherEntity)
    suspend fun clearWeather()
    suspend fun fetchWeatherFromApi(city:String): ForecastResponse
}