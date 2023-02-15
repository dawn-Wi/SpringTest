package com.dawn.springtest.repository

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


}