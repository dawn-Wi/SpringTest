package com.dawn.springtest.ui.screen.tododetails

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baec23.ludwig.component.button.ButtonState
import com.baec23.ludwig.component.button.StatefulButton
import com.baec23.ludwig.component.section.DisplaySection

const val todoDetailsScreenRoute = "todoDetails_Screen_Route"

fun NavGraphBuilder.todoDetailsScreen() {
    composable(
        route = "$todoDetailsScreenRoute/{todoId}", arguments = listOf(
            navArgument("todoId") {
                type = NavType.IntType
                nullable = false
            }
        )
    ) {
        val todoId = it.arguments?.getInt("todoId")
        todoId?.let {
            val viewModel: TodoDetailsViewModel = hiltViewModel()
            viewModel.setCurrTodo(todoId = todoId.toString())
            TodoDetailsScreen(viewModel = viewModel, todoId = todoId)
        }
    }
}

fun NavController.navigateToTodoDetailsScreen(
    todoId: Int,
    navOptions: NavOptions? = null
) {
    val routeWithArgument = "$todoDetailsScreenRoute/$todoId"
    this.navigate(route = routeWithArgument, navOptions = navOptions)
}

@Composable
fun TodoDetailsScreen(
    viewModel: TodoDetailsViewModel = hiltViewModel(),
    todoId: Int
) {
    val currTodo by viewModel.currTodo.collectAsState()
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DisplaySection(headerText = "??? ??? ???") {
            Row() {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "????????????: "
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(3f),
                    text = currTodo.limitDateTime
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row() {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "??? ??? ??????: "
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(3f),
                    text = currTodo.content
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row() {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = "??????: "
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp)
                        .weight(3f),
                    text = currTodo.tag
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row() {
                if (currTodo.finish == "false") {
                    StatefulButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = "??????",
                    ) {
                        viewModel.onEvent(TodoDetailsUiEvent.EditButtonPressed(currTodo))
                    }
                } else {
                    StatefulButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        text = "??????",
                    ) {
                        viewModel.onEvent(TodoDetailsUiEvent.DeleteButtonPressed(currTodo))
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                StatefulButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = "??????",
                ) {
                    viewModel.onEvent(TodoDetailsUiEvent.ConfirmButtonPressed)
                }
            }
            StatefulButton(
                modifier = Modifier.fillMaxWidth(),
                text = "??? ??? ??????",
                state = if (currTodo.finish == "false") {
                    ButtonState.Enabled
                } else {
                    ButtonState.Disabled
                }
            ) {
                viewModel.onEvent(TodoDetailsUiEvent.FinishButtonPressed(currTodo))
            }
        }
    }
}