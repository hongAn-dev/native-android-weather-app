package com.example.weatherapp.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CityPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("weather_pref", Context.MODE_PRIVATE)

    private val gson = Gson()
    private val key = "saved_cities"
    private val currentCityKey = "current_city"

    // Lưu danh sách các thành phố
    fun saveCities(cities: List<String>) {
        val json = gson.toJson(cities)
        prefs.edit().putString(key, json).apply()
    }

    // Lấy danh sách đã lưu
    fun getSavedCities(): List<String> {
        val json = prefs.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(json, type)
    }

    // Xóa toàn bộ danh sách các địa điểm
    fun clearAllCities() {
        prefs.edit().remove(key).apply()
        Log.d("CityPreferences", "All cities cleared")
    }

    // Xoá thành phố khỏi danh sách
    fun deleteCity(city: String) {
        val current = getSavedCities().toMutableList()
        val updated = current.filterNot {
            normalizeCityName(it) == normalizeCityName(city)
        }
        saveCities(updated)
        Log.d("CityPreferences", "Deleted city: $city")
        logAllCities()
    }

    // Lưu tên thành phố hiện tại (định vị GPS)
    fun saveCurrentCity(city: String) {
        prefs.edit().putString(currentCityKey, city).apply()
    }

    // Lấy tên thành phố hiện tại
    fun getCurrentCity(): String? {
        return prefs.getString(currentCityKey, null)
    }

    private fun logAllCities() {
        val cities = getSavedCities()
        if (cities.isEmpty()) {
            Log.d("CityPreferences", "City list is EMPTY")
        } else {
            cities.forEachIndexed { index, city ->
                Log.d("CityPreferences", "City #$index: ${city.trim()}")
            }
        }
    }

    fun normalizeCityName(name: String): String {
        return name.trim().lowercase().replace("\\s+".toRegex(), "")
    }

}
