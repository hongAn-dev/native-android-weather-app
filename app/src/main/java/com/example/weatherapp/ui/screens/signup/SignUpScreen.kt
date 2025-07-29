package com.example.weatherapp.ui.screens.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
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

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onBackToLogin: () -> Unit
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showVerifyDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF56CCF2), Color(0xFF2F80ED))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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

            Text("Welcome!", style = MaterialTheme.typography.headlineMedium)
            Text("Sign up to continue", style = MaterialTheme.typography.bodyMedium)

            Spacer(Modifier.height(24.dp))

            Text(text = "Create Account", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when {
                        email.isBlank() || password.isBlank() || confirmPassword.isBlank() -> {
                            Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                        }
                        !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            Toast.makeText(context, "Invalid email format", Toast.LENGTH_SHORT).show()
                        }
                        password != confirmPassword -> {
                            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            viewModel.signUp(email, password)
                            showVerifyDialog = true
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            Spacer(Modifier.height(16.dp))
        }

        // Return button at bottom-left corner
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBackToLogin() }) {
                Icon(Icons.Default.ExitToApp, contentDescription = "Return", tint = Color.White)
            }
            Text(
                text = "Return",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onBackToLogin() }
            )
        }
    }

    if (showVerifyDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Email Verification") },
            text = { Text("A verification email has been sent. Please check your inbox and click the button below after verifying.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.checkEmailVerify(
                        onVerify = {
                            showVerifyDialog = false
                            onBackToLogin()
                        },
                        onNotVerify = {
                            Toast.makeText(context, "Email not verified", Toast.LENGTH_SHORT).show()
                        }
                    )
                }) {
                    Text("I have verified")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showVerifyDialog = false
                }) {
                    Text("Later")
                }
            }
        )
    }
}
