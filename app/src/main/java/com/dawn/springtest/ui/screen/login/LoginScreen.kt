package com.dawn.springtest.ui.screen.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.baec23.ludwig.component.inputfield.InputField
import com.baec23.ludwig.component.inputfield.InputValidator
import com.baec23.ludwig.component.section.DisplaySection
import kotlin.system.exitProcess

const val loginScreenRoute = "login_screen_route"

fun NavGraphBuilder.loginScreen() {
    composable(route = loginScreenRoute) {
        LoginScreen()
    }
}

fun NavController.navigateToLoginScreen(navOptions: NavOptions? = null) {
    navigate(route = loginScreenRoute, navOptions = navOptions)
}

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val formState by viewModel.formState
    val userEmail = formState.userEmail
    val password = formState.password
    val isFormValid by viewModel.isFormValid

    var backPressedTime: Long = 0;
    BackHandler(
        enabled = true, onBack = {
            if (System.currentTimeMillis() > backPressedTime + 2000) {
                backPressedTime = System.currentTimeMillis()
            } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
                exitProcess(0)
            }
        })

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DisplaySection(headerText = "?????????") {
                InputField(
                    modifier = Modifier,
                    value = userEmail,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.UserEmailChanged(it)) },
                    label = "?????????",
                    placeholder = "???????????? ???????????????.",
                    singleLine = true,
                    inputValidator = InputValidator.Email
                )
                InputField(
                    modifier = Modifier,
                    value = password,
                    onValueChange = { viewModel.onEvent(LoginUiEvent.PasswordChanged(it)) },
                    label = "????????????",
                    placeholder = "??????????????? ???????????????.",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    inputValidator = InputValidator.TextNoSpaces,
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { viewModel.onEvent(LoginUiEvent.SignupButtonPressed) },
                ) {
                    Text("????????????")
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { viewModel.onEvent(LoginUiEvent.LoginButtonPressed) },
                    enabled = isFormValid,
                ) {
                    Text("?????????")
                }
            }
        }
    }
}