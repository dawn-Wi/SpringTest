package com.dawn.springtest.ui.screen.todolist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.dawn.springtest.model.Todo
import com.dawn.springtest.remote.TestSpring
import com.dawn.springtest.repository.UserRepository
import com.dawn.springtest.ui.screen.todoform.navigateToTodoFormScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val testSpring: TestSpring,
    private val userRepository: UserRepository,
    private val navController: NavHostController
): ViewModel(){
    private val _myTodoList = MutableStateFlow<List<Todo>>(listOf())
    val myTodoList = _myTodoList.asStateFlow()

    fun onEvent(event: TodoListUiEvent){
        when(event){
            TodoListUiEvent.AddTodoButtonPressed -> {
                navController.navigateToTodoFormScreen()
            }
        }
    }

    private fun getTodoListByUser(){
        val userEmail = userRepository.currUser!!.email
        viewModelScope.launch {
            val response = testSpring.getTodoListByUser(userEmail)
            if(response.isSuccessful){
                _myTodoList.value = response.body()!!
            }
        }
    }

    init {
        getTodoListByUser()
    }
}

sealed class TodoListUiEvent{
    object AddTodoButtonPressed : TodoListUiEvent()
}