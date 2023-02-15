package com.dawn.springtest.remote

import com.dawn.springtest.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TestSpring {
    @POST("sign-up")
    suspend fun saveUser(@Body user: User): Response<User>
}