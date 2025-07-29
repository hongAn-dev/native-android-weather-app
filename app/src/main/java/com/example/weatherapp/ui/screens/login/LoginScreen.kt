package com.example.weatherapp.ui.screens.login

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weatherapp.R
import com.example.weatherapp.data.local.CityPreferences
import com.example.weatherapp.utils.FusedLocationProviderClient

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: (String) -> Unit,
    onNavToSignUp: () -> Unit,
    onNavToForgotPassword: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }

    val context = LocalContext.current
    val fusedLocationHelper = remember { FusedLocationProviderClient() }
    val prefs = remember { CityPreferences(context) }


    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            if (granted) {
                fusedLocationHelper.getCurrentLocation(context) { location ->
                    val lat = location?.latitude
                    val lon = location?.longitude

                    val cityName = if (lat != null && lon != null) {
                        viewModel.getCityNameFromLocation(lat, lon, context).also { city ->
                            Toast.makeText(context, "Location: $city", Toast.LENGTH_SHORT).show()
                            prefs.saveCurrentCity(city) // ✅ lưu vị trí hiện tại
                        }
                    } else {
                        prefs.getCurrentCity() ?: "hanoi"
                    }

                    onLoginSuccess(cityName)
                }
            } else {
                Toast.makeText(context, "Permission for location denied", Toast.LENGTH_SHORT).show()
                onLoginSuccess("hanoi")
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF56CCF2), Color(0xFF2F80ED))
                )
            )
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.img),
            contentDescription = "Logo",
            modifier = Modifier
                .height(100.dp)
                .align(Alignment.Start)
        )

        Spacer(Modifier.height(16.dp))

        Text("Welcome Back", style = MaterialTheme.typography.headlineMedium)
        Text("Login to continue", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = setEmail,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = setPassword,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Forgot Password?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.End).clickable {
                onNavToForgotPassword()
            }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login(email, password) {
                    launcher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            TextButton(onClick = onNavToSignUp) {
                Text("Don't have an account? Sign up here")
            }
        }

        if (uiState is LoginUiState.Error) {
            val error = (uiState as LoginUiState.Error).message
            Spacer(Modifier.height(16.dp))
            Text(text = error, color = MaterialTheme.colorScheme.error)
        }
    }
}
