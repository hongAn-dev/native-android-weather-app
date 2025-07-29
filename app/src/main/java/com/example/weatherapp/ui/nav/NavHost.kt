package com.example.weatherapp.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.weatherapp.ui.screens.detail.DetailScreen
import com.example.weatherapp.ui.screens.forgotpassword.ForgotPassWordScreen
import com.example.weatherapp.ui.screens.home.HomeScreen
import com.example.weatherapp.ui.screens.location.LocationScreen
import com.example.weatherapp.ui.screens.login.LoginScreen
import com.example.weatherapp.ui.screens.signup.SignUpScreen

@Composable
fun WeatherNavGraph(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { cityName ->
                    navController.navigate(Screen.Home.createRoute(cityName)) {
                        popUpTo(Screen.Login.route) {inclusive = true}
                    }
                },
                onNavToSignUp = {
                    navController.navigate(Screen.SignUp.route)
                },
                onNavToForgotPassword = {
                    navController.navigate(Screen.ForgotPassword.route)
                }
            )
        }

        composable(route = Screen.SignUp.route){
            SignUpScreen (
                onBackToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(route = Screen.ForgotPassword.route){
            ForgotPassWordScreen(
                onBackToLogin = {
                    navController.navigate(Screen.Login.route)
                }
            )
        }

        composable(
            route = Screen.Home.route,
            arguments = listOf(navArgument("city") {type = NavType.StringType})
            ){ backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("city") ?: "Hanoi"

            HomeScreen(
                cityName = cityName,
                onLogOut = {navController.navigate(Screen.Login.route)},
                navController = navController
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("city") { type = NavType.StringType })
        ) { backStackEntry ->
            val cityName = backStackEntry.arguments?.getString("city") ?: "Hanoi"

            DetailScreen(
                cityName = cityName,
                onBack = {
                    navController.navigate(Screen.Home.createRoute(cityName))
                }
            )
        }

        composable(route = Screen.Location.route)  {
            LocationScreen(navController = navController)
        }
    }
}