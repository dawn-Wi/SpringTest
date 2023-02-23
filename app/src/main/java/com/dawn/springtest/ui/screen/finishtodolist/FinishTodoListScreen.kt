package com.dawn.springtest.ui.screen.finishtodolist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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


const val finishTodoListScreenRoute = "finish_Todo_list_screen_route"

fun NavGraphBuilder.finishTodoListScreen() {
    composable(route = finishTodoListScreenRoute) {
        FinishTodoListScreen()
    }
}

fun NavController.navigateToFinishTodoListScreen(navOptions: NavOptions? = null) {
    this.navigate(route = finishTodoListScreenRoute, navOptions = navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishTodoListScreen(
    viewModel: FinishTodoListViewModel = hiltViewModel()
) {
    val myTodoList by viewModel.myTodoList.collectAsState()

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp)
    ) {
        DisplaySection(headerText = "FINISH TODO") {
            FadingLazyColumn(
                modifier = Modifier
                    .weight(13f),
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
                            .height(120.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(6.dp),
                        onClick = {
                            viewModel.onEvent(
                                FinishTodoListUiEvent.FinishTodoDetailsCardPressed(
                                    todo
                                )
                            )
                        }
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(2f),
                            text = todo.content,
                            fontSize = 35.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            modifier = Modifier
                                .padding(5.dp)
                                .weight(1f),
                            text = "마감기한: " + todo.limitDateTime,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                        if (todo.tag.length > 1) {
                            Text(
                                modifier = Modifier
                                    .padding(5.dp)
                                    .weight(1f),
                                text = "#" + todo.tag,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                }
            }
            ExtendedFloatingActionButton(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.End),
                text = { Text(text = "로그아웃") },
                icon = { Icons.Default.ExitToApp },
                shape = RoundedCornerShape(30.dp),
                onClick = { viewModel.onEvent(FinishTodoListUiEvent.LogoutPressed) })
        }
    }
}