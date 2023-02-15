package com.dawn.springtest.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.dawn.springtest.ui.screen.login.loginScreen
import com.dawn.springtest.ui.screen.login.loginScreenRoute
import com.dawn.springtest.ui.screen.signup.signupScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    viewModel: AppViewModel = hiltViewModel()
) {
    val currNavScreenRoute by viewModel.currNavScreenRoute

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {},
        bottomBar = {}
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            NavHost(
                navController = viewModel.navController,
                startDestination = loginScreenRoute
            ){
                //TODO: 앞으로 화면 등록
                loginScreen()
                signupScreen()
            }
        }
    }
}