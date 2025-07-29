package com.example.weatherapp.data.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
    suspend fun signUp(email: String, password: String): Result<Unit>
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
}