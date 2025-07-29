package com.example.weatherapp.data.remote

import com.example.weatherapp.data.model.CitySearch
import com.example.weatherapp.data.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast")
    suspend fun getForecast(
        @Query("q") city:String,
        @Query("appid") apiKey:String,
        @Query("units") units:String = "metric"
    ): ForecastResponse

    @GET("geo/1.0/direct")
    suspend fun searchCity(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 5,
        @Query("appid") apiKey:String,
    ) : List<CitySearch>

}