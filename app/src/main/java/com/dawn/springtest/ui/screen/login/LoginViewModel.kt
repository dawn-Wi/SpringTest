package com.dawn.springtest.ui.screen.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.dawn.springtest.ui.screen.signup.navigateToSignupScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navController: NavHostController,
) : ViewModel() {

    private val _formState: MutableState<LoginFormState> = mutableStateOf(LoginFormState())
    val formState: State<LoginFormState> = _formState
    private val _isFormValid: MutableState<Boolean> = mutableStateOf(false)
    val isFormValid: State<Boolean> = _isFormValid

    private fun checkIfFormIsValid() {
        val form by _formState
        var isValid = true
        if (form.userEmail.isEmpty() || !form.userEmail.contains("@")) {
            isValid = false
        } else if (form.password.isEmpty() || form.password.length < 4) {
            isValid = false
        }
        _isFormValid.value = isValid
    }

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UserEmailChanged -> {
                _formState.value = _formState.value.copy(
                    userEmail = event.userEmail
                )
                checkIfFormIsValid()
            }

            is LoginUiEvent.PasswordChanged -> {
                _formState.value = _formState.value.copy(
                    password = event.password
                )
                checkIfFormIsValid()
            }
            LoginUiEvent.SignupButtonPressed ->{
                navController.navigateToSignupScreen()
            }
            LoginUiEvent.LoginButtonPressed -> TODO()
        }
    }

}

data class LoginFormState(
    val userEmail: String = "",
    val password: String = ""
)

sealed class LoginUiEvent {
    data class UserEmailChanged(val userEmail: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    object SignupButtonPressed: LoginUiEvent()
    object LoginButtonPressed : LoginUiEvent()
}