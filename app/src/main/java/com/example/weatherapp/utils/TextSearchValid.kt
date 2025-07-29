package com.example.weatherapp.utils

fun isValidCityName(input: String): Boolean {
    val cityNameRegex = "^[a-zA-ZÀ-Ỹà-ỹ ,'-]+$".toRegex()
    return input.isNotBlank() && cityNameRegex.matches(input)
}
