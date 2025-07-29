package com.example.weatherapp.ui.screens.forgotpassword

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp

@Composable
fun ForgotPassWordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF56CCF2), Color(0xFF2F80ED))
                )
            )
            .padding(horizontal = 32.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
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

            Text("Forgot Password", style = MaterialTheme.typography.headlineMedium, color = Color.Black)
            Text("Enter your email to reset your password", style = MaterialTheme.typography.bodyMedium, color = Color.Black)

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.sendResetPasswordEmail(
                        email = email,
                        onSuccess = {
                            showDialog = true
                        },
                        onFailure = {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Send link to reset password")
            }

            Spacer(Modifier.height(16.dp))
        }

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ExitToApp,
                contentDescription = "Return",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("Return", color = Color.White)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Email sent") },
            text = { Text("Please check your email to reset your password.") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onBackToLogin()
                }) {
                    Text("Return to login")
                }
            }
        )
    }
}
