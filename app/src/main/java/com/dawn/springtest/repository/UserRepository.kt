package com.dawn.springtest.repository

import com.dawn.springtest.model.Todo
import com.dawn.springtest.model.User
import com.dawn.springtest.remote.TestSpring
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserRepository @Inject constructor(
    private val testSpring: TestSpring
) {
    var currUser: User? = null
        private set

    suspend fun tryLogin(user: User): Boolean{
        val response = testSpring.tryLogin(user)
        return if (response.isSuccessful){
            currUser = response.body()
            true
        }else false
    }


}