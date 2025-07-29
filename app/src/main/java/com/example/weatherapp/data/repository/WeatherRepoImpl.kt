package com.example.weatherapp.data.repository

import com.example.weatherapp.data.local.WeatherDAO
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.model.ForecastResponse
import com.example.weatherapp.data.remote.WeatherApiService
import javax.inject.Inject

class WeatherRepoImpl @Inject constructor(
    private val dao: WeatherDAO,
    private val api: WeatherApiService
) : WeatherRepository {

    override suspend fun getCachedWeather(): WeatherEntity? {
        return dao.getLatestWeather()
    }

    override suspend fun saveWeather(weather: WeatherEntity) {
        return dao.insertWeather(weather)
    }

    override suspend fun clearWeather() {
        return dao.clearAll()
    }

    override suspend fun fetchWeatherFromApi(city: String): ForecastResponse {
        return api.getForecast(
            city = city,
            apiKey = "c90eb0ae6b2cd7e62134fe9c5d5902ac",
            units = "metric",
        )
    }
}