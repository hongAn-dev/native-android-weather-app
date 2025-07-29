package com.example.weatherapp.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Loading)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    fun loadCachedWeather() {
        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading
            try {
                val weather = repository.getCachedWeather()
                if(weather != null){
                    _uiState.value = WeatherUiState.Success(weather)
                }else{
                    _uiState.value = WeatherUiState.Empty
                }
            }catch (e: Exception){
                _uiState.value = WeatherUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun saveWeather(weather: WeatherEntity){
        viewModelScope.launch {
            repository.saveWeather(weather)
            _uiState.value = WeatherUiState.Success(weather)
        }
    }

}