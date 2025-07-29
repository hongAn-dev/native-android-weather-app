package com.example.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>,
    val city: City
)

data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val wind: Wind,
    @SerializedName("dt_txt") val dtTxt: String
)

data class CityWeather(
    val name: String,
    val forecast:ForecastResponse,
)

data class Wind(
    val speed : Double
)


data class Main(
    val temp: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    val humidity: Int,
    @SerializedName("feels_like")val rain: Double
)

data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class City(
    val id: Long,
    val name: String,
    val coord: Coord,
    val country: String,
    val timezone: Int
)

data class Coord(
    val lat: Double,
    val lon: Double
)

