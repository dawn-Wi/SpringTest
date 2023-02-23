package com.dawn.springtest.ui.screen.tododetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.dawn.springtest.model.Todo
import com.dawn.springtest.remote.TestSpring
import com.dawn.springtest.repository.TodoRepository
import com.dawn.springtest.repository.UserRepository
import com.dawn.springtest.service.SnackbarService
import com.dawn.springtest.ui.screen.edittodo.navigateToEditTodoScreen
import com.dawn.springtest.ui.screen.finishtodolist.navigateToFinishTodoListScreen
import com.dawn.springtest.ui.screen.todolist.navigateToTodoListScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodoDetailsViewModel @Inject constructor(
    private val testSpring: TestSpring,
    private val snackbarService: SnackbarService,
    private val todoRepository: TodoRepository,
    private val navController: NavHostController
): ViewModel(){
    private val _currTodo = MutableStateFlow(Todo())
    val currTodo = _currTodo

    fun onEvent(event : TodoDetailsUiEvent){
        when(event){
            is TodoDetailsUiEvent.FinishButtonPressed -> {
                viewModelScope.launch{
                    testSpring.finishTodo(event.selectedTodo)
                    navController.navigateToTodoListScreen()
                }
            }
            is TodoDetailsUiEvent.DeleteButtonPressed -> {
                viewModelScope.launch {
                    testSpring.deleteTodo(event.deleteTodo)
                    navController.navigateToFinishTodoListScreen()
                }
            }
            TodoDetailsUiEvent.ConfirmButtonPressed -> {
                navController.navigateUp()
            }

            is TodoDetailsUiEvent.EditButtonPressed ->{
                navController.navigateToEditTodoScreen(todoId = event.editTodo.id.toInt())
            }
        }
    }

    fun setCurrTodo(todoId: String){
        viewModelScope.launch {
            _currTodo.emit(
                todoRepository.getTodoById(todoId = todoId)
            )
        }
    }
}

sealed class TodoDetailsUiEvent{
    data class FinishButtonPressed(val selectedTodo:Todo): TodoDetailsUiEvent()
    data class DeleteButtonPressed(val deleteTodo: Todo): TodoDetailsUiEvent()
    data class EditButtonPressed(val editTodo: Todo): TodoDetailsUiEvent()
    object ConfirmButtonPressed: TodoDetailsUiEvent()
}