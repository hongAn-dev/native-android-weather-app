package com.example.weatherapp.ui.screens

import com.example.weatherapp.data.local.WeatherEntity

// sealed class để biểu diễn tất cả trạng thái của UI: Loading, Thành công, Lỗi, Không có dữ liệu
sealed class WeatherUiState {

    object Loading : WeatherUiState()
    data class Success(val weather: WeatherEntity) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
    object Empty: WeatherUiState()
}