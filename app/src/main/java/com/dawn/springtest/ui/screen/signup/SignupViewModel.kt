package com.dawn.springtest.ui.screen.signup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.dawn.springtest.model.User
import com.dawn.springtest.remote.TestSpring
import com.dawn.springtest.service.SnackbarService
import com.dawn.springtest.ui.screen.login.LoginFormState
import com.dawn.springtest.ui.screen.login.navigateToLoginScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val testSpring: TestSpring,
    private val snackbarService: SnackbarService,
    private val navController: NavHostController
) : ViewModel() {

    private val _formState: MutableState<LoginFormState> = mutableStateOf(LoginFormState())
    val formState: State<LoginFormState> = _formState

    private val _isBusy = MutableStateFlow(false)
    val isBusy = _isBusy.asStateFlow()

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

    fun onEvent(event: SignupUiEvent) {
        when (event) {
            is SignupUiEvent.UserEmailChanged -> {
                _formState.value = _formState.value.copy(
                    userEmail = event.userEmail
                )
                checkIfFormIsValid()
            }

            is SignupUiEvent.PasswordChanged -> {
                _formState.value = _formState.value.copy(
                    password = event.password
                )
                checkIfFormIsValid()
            }

            SignupUiEvent.SubmitButtonPressed -> {
                _isBusy.value = true
                val toSave = generateUser()
                viewModelScope.launch {
                    if (testSpring.saveUser(toSave).isSuccessful){
                        _isBusy.value = false
                        navController.navigateToLoginScreen()
                    }
                    else{
                        _isBusy.value = false
                        snackbarService.showSnackbar("이미 존재하는 회원입니다.")
                        _formState.value = _formState.value.copy(
                            userEmail = "",
                            password = ""
                        )
                    }
                }
            }
        }
    }

    private fun generateUser(): User {
        return User(
            email = _formState.value.userEmail.lowercase(),
            password = _formState.value.password
        )
    }

}

data class SignupFormState(
    val userEmail: String = "",
    val password: String = ""
)

sealed class SignupUiEvent {
    data class UserEmailChanged(val userEmail: String) : SignupUiEvent()
    data class PasswordChanged(val password: String) : SignupUiEvent()
    object SubmitButtonPressed : SignupUiEvent()
}