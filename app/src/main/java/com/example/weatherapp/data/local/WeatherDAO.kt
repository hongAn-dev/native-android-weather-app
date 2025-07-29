package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertWeather(weather: WeatherEntity)

    @Query("SELECT * FROM weather_table ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestWeather(): WeatherEntity?

    @Query("DELETE FROM weather_table")
    suspend fun clearAll()

}