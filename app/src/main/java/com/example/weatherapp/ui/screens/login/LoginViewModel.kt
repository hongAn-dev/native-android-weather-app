package com.example.weatherapp.ui.screens.login

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
open class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    open val uiState: StateFlow<LoginUiState> = _uiState

    fun login(email: String, password: String, onLoginSuccess: (String) -> Unit) {
        _uiState.value = LoginUiState.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            _uiState.value = result.fold(
                onSuccess = {
                    val cityName = "Vinh" // Hoặc gọi fused location service
                    onLoginSuccess(cityName)
                    LoginUiState.Success
                },
                onFailure = {
                    LoginUiState.Error(it.message ?: "Đăng nhập thất bại")
                }
            )
        }
    }

    fun getCityNameFromLocation(lat: Double, lon: Double, context: Context): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            addresses?.get(0)?.locality ?: "Unknown"
        } catch (e: Exception) {
            "Unknown"
        }
    }


}