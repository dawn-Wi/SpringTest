package com.dawn.springtest.remote

import com.dawn.springtest.model.Todo
import com.dawn.springtest.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TestSpring {
    @POST("users/sign-up")
    suspend fun saveUser(@Body userForm: User): Response<User>

    @POST("users/login")
    suspend fun tryLogin(@Body userForm: User): Response<User>

    @GET("todo")
    suspend fun getTodoListByUser(@Query("email")email: String): Response<List<Todo>>

    @POST("todo")
    suspend fun submitTodo(@Body toAdd: Todo): Response<Todo>

}