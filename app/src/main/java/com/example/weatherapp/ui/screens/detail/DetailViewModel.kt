package com.example.weatherapp.ui.screens.detail

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor (
    private val weatherRepository: WeatherRepository
) : ViewModel() {

}