package com.dawn.springtest.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import com.dawn.springtest.ui.comp.BottomNavBar
import com.dawn.springtest.ui.comp.bottomNavBarItem
import com.dawn.springtest.ui.screen.edittodo.editTodoScreen
import com.dawn.springtest.ui.screen.finishtodolist.finishTodoListScreen
import com.dawn.springtest.ui.screen.login.loginScreen
import com.dawn.springtest.ui.screen.login.loginScreenRoute
import com.dawn.springtest.ui.screen.signup.signupScreen
import com.dawn.springtest.ui.screen.signup.signupScreenRoute
import com.dawn.springtest.ui.screen.tododetails.todoDetailsScreen
import com.dawn.springtest.ui.screen.todoform.todoFormScreen
import com.dawn.springtest.ui.screen.todolist.todoListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    viewModel: AppViewModel = hiltViewModel()
) {
    val currNavScreenRoute by viewModel.currNavScreenRoute

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {},
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarService.snackbarState) },
        bottomBar = {
            if (currNavScreenRoute != loginScreenRoute) {
                if (currNavScreenRoute != signupScreenRoute){
                    BottomNavBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        items = bottomNavBarItem,
                        currNavScreenRoute = currNavScreenRoute,
                        onBottomNavBarButtonPressed = {
                            viewModel.onEvent(AppUiEvent.BottomNavBarButtonPressed(it))
                        }
                    )
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(start = 16.dp, end = 16.dp)
        ) {
            NavHost(
                navController = viewModel.navController,
                startDestination = loginScreenRoute
            ) {
                //TODO: 앞으로 화면 등록
                loginScreen()
                signupScreen()
                todoListScreen()
                todoFormScreen()
                todoDetailsScreen()
                finishTodoListScreen()
                editTodoScreen()
            }
        }
    }
}