package com.dawn.springtest.ui.screen.signup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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

const val signupScreenRoute = "signup_screen_route"

fun NavGraphBuilder.signupScreen() {
    composable(route = signupScreenRoute) {
        SignupScreen()
    }
}

fun NavController.navigateToSignupScreen(navOptions: NavOptions? = null) {
    navigate(route = signupScreenRoute, navOptions = navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(
    viewModel: SignupViewModel = hiltViewModel()
) {
    val formState by viewModel.formState
    val userEmail = formState.userEmail
    val password = formState.password

    val isBusy by viewModel.isBusy.collectAsState()

    AnimatedVisibility(visible = isBusy) {
        AlertDialog(onDismissRequest = { }) {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("작업중...", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DisplaySection(headerText = "Signup") {
                InputField(
                    modifier = Modifier,
                    value = userEmail,
                    onValueChange = { viewModel.onEvent(SignupUiEvent.UserEmailChanged(it)) },
                    label = "이메일",
                    placeholder = "이메일을 입력하세요.",
                    singleLine = true,
                    inputValidator = InputValidator.Email
                )
                InputField(
                    modifier = Modifier,
                    value = password,
                    onValueChange = { viewModel.onEvent(SignupUiEvent.PasswordChanged(it)) },
                    label = "비밀번호",
                    placeholder = "비밀번호를 입력하세요.",
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    inputValidator = InputValidator.TextNoSpaces,
                    visualTransformation = PasswordVisualTransformation()
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = { viewModel.onEvent(SignupUiEvent.SubmitButtonPressed) },
                ) {
                    Text("등록")
                }
            }
        }
    }
}