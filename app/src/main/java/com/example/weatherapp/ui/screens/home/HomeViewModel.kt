package com.example.weatherapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.model.CitySearch
import com.example.weatherapp.data.model.CityWeather
import com.example.weatherapp.data.model.ForecastResponse
import com.example.weatherapp.data.remote.WeatherApiService
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.utils.toWeatherEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val api : WeatherApiService
) : ViewModel() {

    private val _forecast = MutableStateFlow<ForecastResponse?>(null)
    val forecast: StateFlow<ForecastResponse?> = _forecast

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _cityWeather = MutableStateFlow<List<CityWeather>>(emptyList())
    val cityWeather: StateFlow<List<CityWeather>> = _cityWeather

    suspend fun loadWeather(city: String) {
        _loading.value = true
        _error.value = null
        try {
            val result = weatherRepository.fetchWeatherFromApi(city)
            _forecast.value = result

            weatherRepository.saveWeather(
                weather = result.toWeatherEntity()
            )
        } catch (e: Exception) {
            _error.value = e.message
        } finally {
            _loading.value = false
        }
    }

    suspend fun addCityByName(cityName: String) {
        try {
            val forecast = weatherRepository.fetchWeatherFromApi(cityName)
            val newCity = CityWeather(name = forecast.city.name, forecast = forecast)

            val currentList = _cityWeather.value.toMutableList()
            if (currentList.none { it.name.equals(newCity.name, ignoreCase = true) }) {
                currentList.add(newCity)
                _cityWeather.value = currentList
            }
        } catch (e: Exception) {
            _error.value = e.message
        }
    }

    fun removeCityByName(cityName: String){
        val currentList = _cityWeather.value.toMutableList()
        val deleteCity = currentList.filterNot { it.name.equals(cityName, ignoreCase = true) }
        _cityWeather.value = deleteCity
    }

    // search city

    var suggestions = MutableStateFlow<List<CitySearch>>(emptyList())
        private set

    fun searchCity(query: String) {
        viewModelScope.launch {
            try {
                val result = api.searchCity(
                    cityName = query,
                    limit = 5,
                    apiKey = "c90eb0ae6b2cd7e62134fe9c5d5902ac"
                )
                suggestions.value = result
            } catch (e: Exception) {
                suggestions.value = emptyList()
            }
        }
    }


}