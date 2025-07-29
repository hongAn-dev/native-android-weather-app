package com.example.weatherapp.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.data.local.CityPreferences
import com.example.weatherapp.data.model.ForecastResponse
import com.example.weatherapp.ui.nav.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    cityName: String,
    onLogOut: () -> Unit,
    navController: NavController
) {
    val forecast by viewModel.forecast.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val context = LocalContext.current
    val cityPrefs = remember {CityPreferences(context)}

    LaunchedEffect(Unit) {
        viewModel.loadWeather(cityName)
    }

    when {
        loading -> CircularProgressIndicator()
        error != null -> Text("Lỗi : $error")
        forecast != null -> {
            Text("Temp: ${forecast!!.list.first().main.temp}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF56CCF2), Color(0xFF2F80ED))
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = {
                    val currentCities = cityPrefs.getSavedCities().toMutableList()
                    if(!currentCities.contains(cityName)){
                        currentCities.add(cityName)
                    }
                    cityPrefs.saveCities(currentCities)
                    navController.navigate("location")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Back to Location",
                    tint = Color.White
                )
            }

            TextButton(
                onClick = onLogOut
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Logout", color = Color.White, fontSize = 18.sp)
            }
        }
        Spacer(modifier = Modifier.padding(top = 40.dp))
        CurrentWeatherSection(forecast)
        Spacer(modifier = Modifier.height(24.dp))
        HourlyForecastSection(forecast, onDetail = { cityName ->
            navController.navigate(Screen.Detail.createRoute(cityName))
        })
    }
}

@Composable
fun CurrentWeatherSection(forecast: ForecastResponse?) {

    val current = forecast?.list?.firstOrNull()
    val weather = current?.weather?.firstOrNull()
    val city = forecast?.city

    // Format lại giờ theo timezone
    val timezoneOffset = forecast?.city?.timezone ?: 0
    val localTimeFormatted = getCurrentTimeForCity(timezoneOffset)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(weather?.main ?: "UnKnown", color = Color.White, fontSize = 30.sp)
        AsyncImage(
            model = "https://openweathermap.org/img/wn/${weather?.icon}@2x.png",
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        if (city != null) { Text(city.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold) }
        Spacer(modifier = Modifier.height(8.dp))
        Text(localTimeFormatted, color = Color.White, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "${current?.main?.temp?.toInt()}°C",
            fontSize = 64.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            "H:${current?.main?.tempMax?.toInt()} L:${current?.main?.tempMin?.toInt()}",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x33212121)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherInfoItem(R.drawable.rain, "${current?.main?.rain ?: 0}%", "Rain")
                WeatherInfoItem(R.drawable.wind, "${current?.wind?.speed} Km/h", "Wind")
                WeatherInfoItem(R.drawable.humidity, "${current?.main?.humidity ?: 0}%", "Humidity")
            }
        }
    }
}

@Composable
fun WeatherInfoItem(icon: Int, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .padding(bottom = 8.dp),
            tint = Color.Unspecified
        )
        Text(value, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Medium)
        Text(label, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun HourlyForecastSection(forecast: ForecastResponse?, onDetail: (String) -> Unit) {

    forecast?.list?.take(24)?.let { hourlyList ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { }
                ) {
                    Text(
                        text = "Today",
                        color = Color.Yellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                TextButton(
                    onClick = { onDetail(forecast.city.name) }
                ) {
                    Text(
                        text = "Next 5 Days >",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                hourlyList.forEach { item ->
                    val hourText = item.dtTxt.substringAfter(" ").substringBefore(":") + ":00"
                    val temp = item.main.temp.toInt()
                    val iconCode = item.weather.firstOrNull()?.icon
                    val iconUrl = "https://openweathermap.org/img/wn/${iconCode}@2x.png"

                    Column(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .background(Color(0x33FFFFFF), RoundedCornerShape(16.dp))
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(hourText, color = Color.White, fontSize = 14.sp)

                        // Nếu dùng Coil:
                        AsyncImage(
                            model = iconUrl,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )

                        Text(
                            "$temp°",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

fun getCurrentTimeForCity(timezoneOffset: Int): String {
    val currentTime = System.currentTimeMillis() + (timezoneOffset * 1000L)
    val sdf = SimpleDateFormat("EEEE, dd/MM/yyyy - HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date(currentTime))
}
