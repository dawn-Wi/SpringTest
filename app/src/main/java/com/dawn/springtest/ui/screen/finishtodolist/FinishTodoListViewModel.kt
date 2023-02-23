package com.dawn.springtest.ui.screen.finishtodolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.dawn.springtest.model.Todo
import com.dawn.springtest.remote.TestSpring
import com.dawn.springtest.repository.TodoRepository
import com.dawn.springtest.repository.UserRepository
import com.dawn.springtest.ui.screen.login.navigateToLoginScreen
import com.dawn.springtest.ui.screen.tododetails.navigateToTodoDetailsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishTodoListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val todoRepository: TodoRepository,
    private val navController: NavHostController
) : ViewModel() {
    private val _myTodoList = MutableStateFlow<List<Todo>>(listOf())
    val myTodoList = _myTodoList.asStateFlow()

    fun onEvent(event: FinishTodoListUiEvent) {
        when (event) {
            is FinishTodoListUiEvent.FinishTodoDetailsCardPressed -> {
                navController.navigateToTodoDetailsScreen(todoId = event.todo.id.toInt())
            }

            FinishTodoListUiEvent.LogoutPressed -> {
                navController.navigateToLoginScreen()
            }
        }
    }

    private fun getTodoListByUser() {
        val userEmail = userRepository.currUser!!.email
        viewModelScope.launch {
            _myTodoList.value = todoRepository.getTodoListByUser(userEmail)!!.filter {
                it.finish == "true"
            }
        }
    }

    init {
        getTodoListByUser()
    }
}

sealed class FinishTodoListUiEvent {
    data class FinishTodoDetailsCardPressed(val todo: Todo) : FinishTodoListUiEvent()
    object LogoutPressed : FinishTodoListUiEvent()
}