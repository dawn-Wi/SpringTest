package com.dawn.springtest.ui.screen.signup

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.dawn.springtest.model.User
import com.dawn.springtest.remote.TestSpring
import com.dawn.springtest.repository.UserRepository
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
    private val navController: NavHostController
): ViewModel(){

    private val _formState: MutableState<LoginFormState> = mutableStateOf(LoginFormState())
    val formState: State<LoginFormState> = _formState

    private val _isBusy = MutableStateFlow(false)
    val isBusy = _isBusy.asStateFlow()

    fun onEvent(event: SignupUiEvent){
        when(event){
            is SignupUiEvent.UserEmailChanged -> {
                _formState.value = _formState.value.copy(
                    userEmail = event.userEmail
                )
            }
            is SignupUiEvent.PasswordChanged -> {
                _formState.value = _formState.value.copy(
                    password = event.password
                )
            }
            SignupUiEvent.SubmitButtonPressed -> {
                _isBusy.value = true
                val toSave = generateUser()
                viewModelScope.launch {
                    testSpring.saveUser(toSave)
                    _isBusy.value = false
                    navController.navigateToLoginScreen()
                }
            }
        }
    }

    private fun generateUser(): User{
        return User(
            email = _formState.value.userEmail,
            password = _formState.value.password
        )
    }

}

data class SignupFormState(
    val userEmail: String = "",
    val password: String = ""
)

sealed class SignupUiEvent{
    data class UserEmailChanged(val userEmail: String): SignupUiEvent()
    data class PasswordChanged(val password: String): SignupUiEvent()
    object SubmitButtonPressed : SignupUiEvent()
}