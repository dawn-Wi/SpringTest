package com.dawn.springtest.ui.screen.todolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.baec23.ludwig.component.section.DisplaySection
import com.baec23.ludwig.core.fadingLazy.FadingLazyColumn

const val todoListScreenRoute = "todo_list_screen_route"

fun NavGraphBuilder.todoListScreen() {
    composable(route = todoListScreenRoute) {
        TodoListScreen()
    }
}

fun NavController.navigateToTodoListScreen(navOptions: NavOptions? = null) {
    this.navigate(route = todoListScreenRoute, navOptions = navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    viewModel: TodoListViewModel = hiltViewModel()
) {
    val myTodoList by viewModel.myTodoList.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        DisplaySection(headerText = "TODO") {
            FadingLazyColumn(
                modifier = Modifier,
                contentPadding = PaddingValues(3.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                items(
                    myTodoList.size
                ) {
                    val todo =
                        myTodoList.sortedBy { todo -> todo.limitDateTime }
                            .reversed()[it]
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(6.dp),
                        onClick = {

                        }
                    ) {
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = todo.content,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = todo.limitDateTime,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        Text(
                            modifier = Modifier.padding(5.dp),
                            text = todo.tag,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
            Button(onClick = { viewModel.onEvent(TodoListUiEvent.AddTodoButtonPressed) }) {
                Text(text = "할 일 추가하기")
            }
        }
    }

}