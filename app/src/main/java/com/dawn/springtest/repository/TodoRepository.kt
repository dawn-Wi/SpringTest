package com.dawn.springtest.repository

import com.dawn.springtest.model.Todo
import com.dawn.springtest.remote.TestSpring
import dagger.hilt.android.scopes.ActivityScoped
import retrofit2.Response
import javax.inject.Inject

@ActivityScoped
class TodoRepository @Inject constructor(
    private val testSpring: TestSpring
){
    var myTodoList:List<Todo>? = null
        private set

    suspend fun getTodoListByUser(email: String): List<Todo>? {
        val response = testSpring.getTodoListByUser(email)
        return if(response.isSuccessful){
            myTodoList = response.body()
            myTodoList
        }else null
    }

    suspend fun getTodoById(todoId: String):Todo{
        var myTodo: Todo? = null
        if (myTodoList!!.isNotEmpty()){
            myTodo =myTodoList!!.filter {
                it.id==todoId
            }.first()
        }
        return myTodo!!
    }
}