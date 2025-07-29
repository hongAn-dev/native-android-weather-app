package com.example.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherEntity::class], exportSchema = false, version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDAO() : WeatherDAO
}