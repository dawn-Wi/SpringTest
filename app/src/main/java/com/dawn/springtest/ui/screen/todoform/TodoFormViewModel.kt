package com.dawn.springtest.ui.screen.todoform

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.dawn.springtest.model.Todo
import com.dawn.springtest.remote.TestSpring
import com.dawn.springtest.repository.UserRepository
import com.dawn.springtest.service.SnackbarService
import com.dawn.springtest.ui.screen.todolist.navigateToTodoListScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class TodoFormViewModel @Inject constructor(
    private val testSpring: TestSpring,
    private val snackbarService: SnackbarService,
    private val userRepository: UserRepository,
    private val navController: NavHostController,
) : ViewModel() {
    val limitDateTimeExpanded: MutableState<Boolean> = mutableStateOf(false)

    val dateExpanded: MutableState<Boolean> = mutableStateOf(false)
    val timeExpanded: MutableState<Boolean> = mutableStateOf(false)

    private val _isBusy = MutableStateFlow(false)
    val isBusy = _isBusy.asStateFlow()

    private val _selectedLimitDate = MutableStateFlow<LocalDate>(LocalDate.now().plusDays(1))
    val selectedLimitDate = _selectedLimitDate.asStateFlow()

    private val _selectedLimitTime =
        MutableStateFlow<LocalTime>(
            LocalDate.now().atStartOfDay().toLocalTime().plusHours(12)
        )
    val selectedLimitTime = _selectedLimitTime.asStateFlow()

    private val _content = MutableStateFlow("")
    val content = _content.asStateFlow()

    private val _tag = MutableStateFlow("공부")
    val tag = _tag.asStateFlow()


    fun onEvent(event: TodoFormUiEvent) {
        when (event) {
            is TodoFormUiEvent.IsDateExpanded -> {
                dateExpanded.value = event.expanded
            }

            is TodoFormUiEvent.IsTimeExpanded -> {
                timeExpanded.value = event.expanded
            }

            is TodoFormUiEvent.OnContentChanged -> {
                _content.value = event.content
            }

            is TodoFormUiEvent.OnSelectedLimitDateChanged -> {
                _selectedLimitDate.value = event.limitDate
            }

            is TodoFormUiEvent.OnSelectedLimitTimeChanged -> {
                _selectedLimitTime.value = event.limitTime
            }

            is TodoFormUiEvent.LimitDateTimeCardExpanded -> {
                limitDateTimeExpanded.value = event.expanded
            }

            is TodoFormUiEvent.OnSelectedTagChanged -> {
                _tag.value = event.tag
            }

            TodoFormUiEvent.OnSubmitPressed -> {
                _isBusy.value = true
                val toSubmit = generateTodo()
                viewModelScope.launch {
                    testSpring.submitTodo(toSubmit)
                    _isBusy.value = false
                    snackbarService.showSnackbar("저장 성공")
                    navController.navigateToTodoListScreen()
                }
            }
        }
    }

    private fun generateTodo(): Todo {
        val userEmail = userRepository.currUser!!.email
        return Todo(
            content = _content.value,
            limitDateTime = _selectedLimitDate.value.toString() + " / " + _selectedLimitTime.value.toString(),
            tag = _tag.value,
            ownerEmail = userEmail,
            finish = "false"
        )
    }
}

sealed class TodoFormUiEvent {
    data class OnSelectedLimitDateChanged(val limitDate: LocalDate) : TodoFormUiEvent()
    data class OnSelectedLimitTimeChanged(val limitTime: LocalTime) : TodoFormUiEvent()
    data class OnContentChanged(val content: String) : TodoFormUiEvent()
    data class LimitDateTimeCardExpanded(val expanded: Boolean) : TodoFormUiEvent()
    data class IsDateExpanded(val expanded: Boolean) : TodoFormUiEvent()
    data class IsTimeExpanded(val expanded: Boolean) : TodoFormUiEvent()
    data class OnSelectedTagChanged(val tag: String) : TodoFormUiEvent()
    object OnSubmitPressed : TodoFormUiEvent()
}