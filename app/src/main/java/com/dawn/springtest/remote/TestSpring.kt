package com.dawn.springtest.remote

import com.dawn.springtest.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TestSpring {
    @POST("users/sign-up")
    suspend fun saveUser(@Body userForm: User): Response<User>

}