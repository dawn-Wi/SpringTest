package com.dawn.springtest.service

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ActivityScoped
class SnackbarService {
    val snackbarState = SnackbarHostState()

    fun showSnackbar(message: String, duration: SnackbarDuration) {
        MainScope().launch {
            snackbarState.showSnackbar(
                message = message,
                duration = duration
            )
        }
    }

    fun showSnackbar(message: String, durationMillis: Long = 1000){
        MainScope().launch {
            showSnackbarForDuration(message, durationMillis)
        }
    }

    private suspend fun showSnackbarForDuration(message: String, durationMillis: Long) {
        val job = CoroutineScope(Dispatchers.Default).launch {
            snackbarState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Indefinite
            )
        }
        delay(timeMillis = durationMillis)
        job.cancel()
    }
}