package com.dawn.springtest.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.dawn.springtest.service.SnackbarService
import com.dawn.springtest.ui.comp.BottomNavBarItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val navController : NavHostController,
    val snackbarService: SnackbarService
): ViewModel(){
    private val _currNavScreenRoute: MutableState<String?> = mutableStateOf(null)
    val currNavScreenRoute: State<String?> = _currNavScreenRoute

    fun onEvent(event: AppUiEvent){
        when(event){
            is AppUiEvent.BottomNavBarButtonPressed -> navController.navigate(event.pressedItem.route)
        }
    }
    init {
        MainScope().launch {
            navController.currentBackStackEntryFlow.collect {
                _currNavScreenRoute.value = it.destination.route
            }
        }
    }
}

sealed class AppUiEvent{

    data class BottomNavBarButtonPressed(val pressedItem: BottomNavBarItem) : AppUiEvent()
}