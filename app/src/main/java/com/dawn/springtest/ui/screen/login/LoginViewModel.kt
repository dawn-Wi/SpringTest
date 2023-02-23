package com.dawn.springtest.ui.screen.login

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.dawn.springtest.model.User
import com.dawn.springtest.repository.UserRepository
import com.dawn.springtest.service.SnackbarService
import com.dawn.springtest.ui.screen.signup.navigateToSignupScreen
import com.dawn.springtest.ui.screen.todolist.navigateToTodoListScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val snackbarService: SnackbarService,
    private val userRepository: UserRepository,
    private val navController: NavHostController,
) : ViewModel() {

    private val _formState: MutableState<LoginFormState> = mutableStateOf(LoginFormState())
    val formState: State<LoginFormState> = _formState
    private val _isFormValid: MutableState<Boolean> = mutableStateOf(false)
    val isFormValid: State<Boolean> = _isFormValid

    private val _isBusy = MutableStateFlow(false)
    val isBusy = _isBusy.asStateFlow()

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

            LoginUiEvent.SignupButtonPressed -> {
                navController.navigateToSignupScreen()
            }

            LoginUiEvent.LoginButtonPressed -> {
                _isBusy.value = true
                val tryLoginUser = generateUser()
                viewModelScope.launch {
                    if (userRepository.tryLogin(tryLoginUser)) {
                        snackbarService.showSnackbar("로그인 성공")
                        navController.navigateToTodoListScreen()
                    }
                    _isBusy.value = false
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

data class LoginFormState(
    val userEmail: String = "",
    val password: String = ""
)

sealed class LoginUiEvent {
    data class UserEmailChanged(val userEmail: String) : LoginUiEvent()
    data class PasswordChanged(val password: String) : LoginUiEvent()
    object SignupButtonPressed : LoginUiEvent()
    object LoginButtonPressed : LoginUiEvent()
}