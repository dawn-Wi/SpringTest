package com.dawn.springtest.ui.screen.todolist

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dawn.springtest.model.Todo
import com.dawn.springtest.remote.TestSpring
import com.dawn.springtest.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val testSpring: TestSpring,
    private val userRepository: UserRepository,
): ViewModel(){
    private val _myTodoList = MutableStateFlow<List<Todo>>(listOf())
    val myTodoList = _myTodoList.asStateFlow()

    fun onEvent(event: TodoListUiEvent){
        when(event){

            else -> {}
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
}

sealed class TodoListUiEvent{

}