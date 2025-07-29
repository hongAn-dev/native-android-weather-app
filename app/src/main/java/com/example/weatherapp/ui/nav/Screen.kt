package com.example.weatherapp.ui.nav

sealed class Screen(val route: String) {
    object Login:Screen("login")
    object SignUp:Screen("signup")
    object ForgotPassword:Screen("forgotpassword")
    object Home : Screen("home/{city}") {
        fun createRoute(city: String) = "home/$city"
    }
    object Detail : Screen("detail/{city}") {
        fun createRoute(city: String) = "detail/$city"
    }
    object Location:Screen("location")
}