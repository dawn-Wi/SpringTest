package com.dawn.springtest.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val navController : NavHostController,
): ViewModel(){
    private val _currNavScreenRoute: MutableState<String?> = mutableStateOf(null)
    val currNavScreenRoute: State<String?> = _currNavScreenRoute

    fun onEvent(event: AppUiEvent){
        when(event){

            else -> {}
        }
    }
}

sealed class AppUiEvent{

}