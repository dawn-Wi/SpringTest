package com.dawn.springtest.ui.screen.edittodo

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.baec23.ludwig.component.button.LabelledValueButton
import com.baec23.ludwig.component.button.StatefulButton
import com.baec23.ludwig.component.datepicker.DatePicker
import com.baec23.ludwig.component.section.DisplaySection
import com.baec23.ludwig.component.timepicker.TimePicker
import com.dawn.springtest.ui.screen.todoform.toAnnotatedString
import java.time.LocalDate
import java.time.LocalTime

const val editTodoScreenRoute = "editTodo_Screen_Route"

fun NavGraphBuilder.editTodoScreen() {
    composable(
        route = "$editTodoScreenRoute/{todoId}", arguments = listOf(
            navArgument("todoId") {
                type = NavType.IntType
                nullable = false
            }
        )
    ) {
        val todoId = it.arguments?.getInt("todoId")
        todoId?.let {
            val viewModel: EditTodoViewModel = hiltViewModel()
            viewModel.setCurrTodo(todoId = todoId.toString())
            EditTodoScreen(viewModel = viewModel, todoId = todoId)
        }
    }
}

fun NavController.navigateToEditTodoScreen(
    todoId: Int,
    navOptions: NavOptions? = null
) {
    val routeWithArgument = "$editTodoScreenRoute/$todoId"
    this.navigate(route = routeWithArgument, navOptions = navOptions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoScreen(
    viewModel: EditTodoViewModel = hiltViewModel(),
    todoId: Int
) {
    val currTodo by viewModel.currTodo.collectAsState()

    val isBusy by viewModel.isBusy.collectAsState()
    val limitDateTimeExpanded by viewModel.limitDateTimeExpanded

    val dateExpanded by viewModel.dateExpanded
    val timeExpanded by viewModel.timeExpanded

    val limitDate by viewModel.selectedLimitDate.collectAsState()
    val limitTime by viewModel.selectedLimitTime.collectAsState()

    val content by viewModel.content.collectAsState()

    val valueFontSize = MaterialTheme.typography.titleMedium.fontSize
    val valueFontColor = MaterialTheme.colorScheme.onPrimaryContainer
    val labelFontSize = MaterialTheme.typography.labelMedium.fontSize
    val labelFontColor = Color.DarkGray

    val tag by viewModel.tag.collectAsState()
    val items = listOf("공부", "약속", "할일")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(tag) }

    AnimatedVisibility(visible = isBusy) {
        AlertDialog(onDismissRequest = { }) {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("작업중...", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        DisplaySection(headerText = "할 일 만들기") {

            LabelledValueButton(
                onClick = {
                    viewModel.onEvent(EditTodoUiEvent.LimitDateTimeCardExpanded(true))
                    viewModel.onEvent(EditTodoUiEvent.IsDateExpanded(true))
                    viewModel.onEvent(EditTodoUiEvent.IsTimeExpanded(true))
                }, label = { Text("마감기한") },
                value = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = limitDate.toAnnotatedString(
                                valueFontSize,
                                valueFontColor,
                                labelFontSize,
                                labelFontColor
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = limitTime.toAnnotatedString(
                                valueFontSize,
                                valueFontColor,
                                labelFontSize,
                                labelFontColor
                            )
                        )
                    }
                })
            DateTimePickerDialog(
                isShowing = limitDateTimeExpanded,
                onCancel = { viewModel.onEvent(EditTodoUiEvent.LimitDateTimeCardExpanded(false)) },
                initialDate = limitDate,
                initialTime = limitTime,
                dateExpanded = dateExpanded,
                timeExpanded = timeExpanded,
                onUiEvent = { viewModel.onEvent(it) },
                onDateTimeSelected = { selectedDate, selectedTime ->
                    viewModel.onEvent(
                        EditTodoUiEvent.OnSelectedLimitDateChanged(
                            selectedDate
                        )
                    )
                    viewModel.onEvent(
                        EditTodoUiEvent.OnSelectedLimitTimeChanged(
                            selectedTime
                        )
                    )
                    viewModel.onEvent(EditTodoUiEvent.LimitDateTimeCardExpanded(false))
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(5.dp),
                value = content,
                minLines = 1,
                maxLines = 5,
                onValueChange = { viewModel.onEvent(EditTodoUiEvent.OnContentChanged(it)) },
                label = { Text(text = "> 내가 해야 할 일") }
            )
            DisplaySection(headerText = "태그") {
                items.forEach { text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    onOptionSelected(text)
                                    viewModel.onEvent(EditTodoUiEvent.OnSelectedTagChanged(text))
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                viewModel.onEvent(EditTodoUiEvent.OnSelectedTagChanged(text))
                            }
                        )
                        Text(
                            modifier = Modifier,
                            text = text,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
            StatefulButton(
                modifier = Modifier.fillMaxWidth(),
                text = "수정완료",
            ) {
                viewModel.onEvent(EditTodoUiEvent.OnSubmitPressed)
            }
        }
    }

}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerDialog(
    isShowing: Boolean,
    onCancel: () -> Unit,
    initialDate: LocalDate,
    initialTime: LocalTime,
    dateExpanded: Boolean,
    timeExpanded: Boolean,
    onDateTimeSelected: (LocalDate, LocalTime) -> Unit,
    onUiEvent: (EditTodoUiEvent) -> Unit
) {
    var selectedDate by remember { mutableStateOf(initialDate) }
    var selectedTime by remember { mutableStateOf(initialTime) }

    AnimatedVisibility(visible = isShowing) {
        AlertDialog(onDismissRequest = { }) {
            AnimatedVisibility(
                visible = dateExpanded, //t
                enter = scaleIn(),
                exit = fadeOut()
            ) {
                DatePicker(
                    onCancelled = onCancel,
                    onDateSelectionFinalized = {
                        selectedDate = it
                        onUiEvent(EditTodoUiEvent.IsDateExpanded(false))
                    },
                    shouldFinalizeOnSelect = true,
                    initialDate = initialDate
                )
            }
            AnimatedVisibility(
                visible = !dateExpanded && timeExpanded, //f t
                enter = scaleIn(),
                exit = fadeOut()
            ) {
                Card(shape = RoundedCornerShape(6.dp)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        DisplaySection(
                            headerText = "${selectedDate.year}년 ${selectedDate.monthValue}월 ${selectedDate.dayOfMonth}일",
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            TimePicker(
                                initialTime = selectedTime,
                                onTimeChanged = { selectedTime = it })
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                StatefulButton(text = "완료") {
                                    onDateTimeSelected(selectedDate, selectedTime)
                                    onUiEvent(EditTodoUiEvent.IsTimeExpanded(false))
                                }
                                StatefulButton(text = "취소") {
                                    onUiEvent(EditTodoUiEvent.IsDateExpanded(true))
                                    onUiEvent(EditTodoUiEvent.IsTimeExpanded(true))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}