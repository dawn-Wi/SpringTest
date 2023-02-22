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
            TodoDetailsUiEvent.EditButtonPressed -> TODO()
            TodoDetailsUiEvent.ConfirmButtonPressed -> {
                navController.navigateUp()
            }
            TodoDetailsUiEvent.FinishButtonPressed -> TODO()
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
    object EditButtonPressed: TodoDetailsUiEvent()
    object ConfirmButtonPressed: TodoDetailsUiEvent()
    object FinishButtonPressed: TodoDetailsUiEvent()
}