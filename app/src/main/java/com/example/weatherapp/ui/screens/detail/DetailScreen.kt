package com.example.weatherapp.ui.screens.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.ui.screens.home.HomeViewModel
import com.example.weatherapp.ui.screens.home.WeatherInfoItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun DetailScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    cityName: String,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.loadWeather(cityName)
    }

    val forecast by viewModel.forecast.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val dailyForecasts = forecast?.list
        ?.filter { it.dtTxt.contains("12:00:00") }
        ?.drop(1)
        ?.take(5)
    val tomorrow = dailyForecasts?.firstOrNull()
    val weather = tomorrow?.weather?.firstOrNull()


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
        IconButton(
            onClick = {
                onBack()
            },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0x33212121)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(
                        model = "https://openweathermap.org/img/wn/${weather?.icon}@2x.png",
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                    )
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Spacer(modifier = Modifier.height(50.dp))
                        Text("Tomorrow", color = Color.White, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "${tomorrow?.main?.temp?.toInt()}°C",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            weather?.description ?: "Unknown",
                            color = Color.White,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    WeatherInfoItem(R.drawable.rain, "${tomorrow?.main?.rain ?: 0}%", "Rain")
                    WeatherInfoItem(
                        R.drawable.wind,
                        "${tomorrow?.wind?.speed ?: 0.0} km/h",
                        "Wind speed"
                    )
                    WeatherInfoItem(
                        R.drawable.humidity,
                        "${tomorrow?.main?.humidity ?: 0}%",
                        "Humidity"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        dailyForecasts?.forEach() { item ->

            val day = SimpleDateFormat("EEE", Locale.getDefault()).format(Date(item.dt * 1000L))
            val icon = item.weather.firstOrNull()?.icon
            val tempMax = item.main.tempMax
            val tempMin = item.main.tempMin

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color(0x33121212), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(day, color = Color.White, modifier = Modifier.weight(1f))
                AsyncImage(
                    model = "https://openweathermap.org/img/wn/${icon}@2x.png",
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text("${tempMax.toInt()}°", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("${tempMin.toInt()}°", color = Color.White.copy(alpha = 0.6f))
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color(0x33121212), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Day", color = Color.White, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier
                .width(25.dp)
                .height(32.dp)) // chỗ icon
            Spacer(modifier = Modifier.width(16.dp))
            Text("Update after 12:00 PM", color = Color.White.copy(alpha = 0.6f))
        }
    }
}