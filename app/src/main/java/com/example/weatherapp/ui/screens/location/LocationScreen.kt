package com.example.weatherapp.ui.screens.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherapp.R
import com.example.weatherapp.data.local.CityPreferences
import com.example.weatherapp.ui.nav.Screen
import com.example.weatherapp.ui.screens.home.HomeViewModel
import com.example.weatherapp.utils.isValidCityName
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    val gradientBackground = Brush.verticalGradient(
        listOf(Color(0xFF56CCF2), Color(0xFF2F80ED))
    )
    val cityWeathers by viewModel.cityWeather.collectAsState()
    val context = LocalContext.current
    val prefs = remember {CityPreferences(context)}
    val existingCities = prefs.getSavedCities()
    val currentCity = prefs.getCurrentCity()

    // tim theo từ
    val suggestions by viewModel.suggestions.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect (Unit) {
        prefs.getSavedCities().forEach {city ->
            viewModel.addCityByName(city)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBackground)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Weather",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        if (isValidCityName(it) || it.isEmpty()) { // Cho phép xóa text
                            searchQuery = it
                            if (it.length >= 2) {
                                viewModel.searchCity(it)
                                expanded = true
                            } else {
                                expanded = false
                            }
                        }
                    },
                    placeholder = {
                        Text("Enter your location ...")
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    trailingIcon = {
                        if (searchQuery.isNotEmpty() && !isValidCityName(searchQuery)) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Invalid format",
                                tint = Color.Red
                            )
                        }
                    }
                )

                DropdownMenu(
                    expanded = expanded && suggestions.isNotEmpty(),
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    suggestions.forEach { city ->
                        DropdownMenuItem(
                            text = { Text("${city.name}, ${city.country}") },
                            onClick = {
                                searchQuery = city.name
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    val newCity = searchQuery.trim()
                    if (newCity.isNotEmpty() && !existingCities.contains(newCity)) {
                        scope.launch {
                            val updateCities = existingCities + newCity
                            prefs.saveCities(updateCities)
                            viewModel.addCityByName(searchQuery)
                            searchQuery = ""
                            expanded = false
                        }
                    }
                }
            ) {
                Text("add")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        cityWeathers.forEachIndexed { index, cityWeather ->
            val item = cityWeather.forecast.list.firstOrNull()
            val weather = item?.weather?.firstOrNull()
            val temp = item?.main?.temp?.toInt()
            val max = item?.main?.tempMax?.toInt()
            val min = item?.main?.tempMin?.toInt()
            val time = getCurrentTimeForCity(cityWeather.forecast.city.timezone)

            LocationWeatherItem(
                city = cityWeather.name,
                isCurrentLocation = cityWeather.name.equals(currentCity, ignoreCase = true),
                time = time,
                condition = weather?.description ?: "Không rõ",
                temperature = "${temp ?: "--"}°",
                highLow = "C:${max ?: "--"}° T:${min ?: "--"}°",
                onRemove = {
                    scope.launch {
                        prefs.deleteCity(cityWeather.name)
                        viewModel.removeCityByName(cityWeather.name)
                    }
                },
                navController = navController
            )
            Spacer(modifier = Modifier.height(12.dp))
        }


        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Find more about weather data and map location",
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 14.sp
        )
    }
}

@Composable
fun LocationWeatherItem(
    city: String,
    isCurrentLocation: Boolean,
    time: String,
    condition: String,
    temperature: String,
    highLow: String,
    onRemove: () -> Unit,
    navController: NavController
) {
    val backgroundRes = when {
        condition.contains("rain", ignoreCase = true) -> R.drawable.rainny_bg
        condition.contains("sunny", ignoreCase = true) -> R.drawable.cloudy_sunny_bg
        condition.contains("cloud", ignoreCase = true) -> R.drawable.cloudy_bg
        else -> R.drawable.cloudy_bg_1
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable {
                navController.navigate(Screen.Home.createRoute(city))
            }
    ) {
        // Background image
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(Color(0x66000000), BlendMode.Darken),
            modifier = Modifier.matchParentSize()
        )

        // Remove button (top right)
        if (!isCurrentLocation) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.White
                )
            }
        }

        // Weather info
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(city, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                if (isCurrentLocation) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "My location • 🏠 Home",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp
                    )
                }
            }

            Text(time, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
            Text(condition, color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    temperature,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(highLow, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
            }
        }
    }
}

fun getCurrentTimeForCity(timezoneOffset: Int): String {
    val currentTime = System.currentTimeMillis() + (timezoneOffset * 1000L)
    val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("UTC")
    return sdf.format(Date(currentTime))
}

