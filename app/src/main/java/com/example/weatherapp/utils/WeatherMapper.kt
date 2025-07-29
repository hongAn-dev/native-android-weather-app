package com.example.weatherapp.utils

import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.model.ForecastResponse

fun ForecastResponse.toWeatherEntity() : WeatherEntity {
    val current = list.first() // lay thoi tiet hien tai
    val weather = current.weather.first()
    return WeatherEntity(
        city = city.name,
        temperature = current.main.temp,
        description = weather.description,
        icon = weather.icon
    )
}

