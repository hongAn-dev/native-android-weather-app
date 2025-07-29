package com.example.weatherapp.ui.screens.signup

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.auth.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<Result<Unit>?>(null)

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            result.fold(
                onSuccess = {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _signUpState.value = Result.success(Unit)
                        } else {
                            _signUpState.value = Result.failure(task.exception ?: Exception("Gửi email xác thực thất bại"))
                        }
                    }
                },
                onFailure = {
                    _signUpState.value = Result.failure(it)
                }
            )
        }
    }

    fun checkEmailVerify(onVerify: () -> Unit, onNotVerify: () -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        user?.reload()?.addOnCompleteListener {
            if (user.isEmailVerified){
                onVerify()
            }else{
                onNotVerify()
            }
        }
    }



}