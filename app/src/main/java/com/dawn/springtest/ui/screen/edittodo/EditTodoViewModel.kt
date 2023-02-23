package com.dawn.springtest.ui.screen.edittodo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.dawn.springtest.model.Todo
import com.dawn.springtest.remote.TestSpring
import com.dawn.springtest.repository.TodoRepository
import com.dawn.springtest.repository.UserRepository
import com.dawn.springtest.service.SnackbarService
import com.dawn.springtest.ui.screen.todoform.TodoFormUiEvent
import com.dawn.springtest.ui.screen.todolist.navigateToTodoListScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class EditTodoViewModel @Inject constructor(
    private val testSpring: TestSpring,
    private val snackbarService: SnackbarService,
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
    private val navController: NavHostController,
) : ViewModel() {

    private val _currTodo = MutableStateFlow(Todo())
    val currTodo = _currTodo

    private val _isBusy = MutableStateFlow(false)
    val isBusy = _isBusy.asStateFlow()

    val limitDateTimeExpanded: MutableState<Boolean> = mutableStateOf(false)

    val dateExpanded: MutableState<Boolean> = mutableStateOf(false)
    val timeExpanded: MutableState<Boolean> = mutableStateOf(false)

    private val _selectedLimitDate = MutableStateFlow<LocalDate>(LocalDate.now().plusDays(1))
    val selectedLimitDate = _selectedLimitDate.asStateFlow()

    private val _selectedLimitTime =
        MutableStateFlow<LocalTime>(
            LocalDate.now().atStartOfDay().toLocalTime().plusHours(12)
        )
    val selectedLimitTime = _selectedLimitTime.asStateFlow()

    private val _content = MutableStateFlow(_currTodo.value.content)
    val content = _content.asStateFlow()

    private val _tag = MutableStateFlow("공부")
    val tag = _tag.asStateFlow()

    fun onEvent(event: EditTodoUiEvent) {
        when (event) {
            is EditTodoUiEvent.IsDateExpanded -> {
                dateExpanded.value = event.expanded
            }

            is EditTodoUiEvent.IsTimeExpanded -> {
                timeExpanded.value = event.expanded
            }

            is EditTodoUiEvent.LimitDateTimeCardExpanded -> {
                limitDateTimeExpanded.value = event.expanded
            }

            is EditTodoUiEvent.OnContentChanged -> {
                _content.value = event.content
            }

            is EditTodoUiEvent.OnSelectedLimitDateChanged -> {
                _selectedLimitDate.value = event.limitDate
            }

            is EditTodoUiEvent.OnSelectedLimitTimeChanged -> {
                _selectedLimitTime.value = event.limitTime
            }

            is EditTodoUiEvent.OnSelectedTagChanged -> {
                _tag.value = event.tag
            }

            EditTodoUiEvent.OnSubmitPressed -> {
                _isBusy.value = true
                val toSubmit = generateTodo()
                viewModelScope.launch {
                    testSpring.editTodo(toSubmit)
                    _isBusy.value = false
                    snackbarService.showSnackbar("저장 성공")
                    navController.navigateToTodoListScreen()
                }
            }
        }
    }

    fun setCurrTodo(todoId: String) {
        viewModelScope.launch {
            _currTodo.emit(
                todoRepository.getTodoById(todoId = todoId)
            )
            settingTodo()
        }
    }

    private fun generateTodo(): Todo {
        val userEmail = userRepository.currUser!!.email
        return Todo(
            id=_currTodo.value.id,
            content = _content.value,
            limitDateTime = _selectedLimitDate.value.toString()+" / "+_selectedLimitTime.value.toString(),
            tag = _tag.value,
            ownerEmail = userEmail,
            finish = "false"
        )
    }

    private fun settingTodo(){
        _content.value = _currTodo.value.content
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd ")!!
        val format2 = DateTimeFormatter.ofPattern(" HH:mm")!!
        val strToLocalDate = LocalDate.parse(_currTodo.value.limitDateTime.split("/")[0],format)!!
        val strToLocalTime = LocalTime.parse(_currTodo.value.limitDateTime.split("/")[1],format2)!!
        _selectedLimitDate.value = strToLocalDate
        _selectedLimitTime.value = strToLocalTime
        _tag.value=_currTodo.value.tag
    }
}

sealed class EditTodoUiEvent {
    data class OnSelectedLimitDateChanged(val limitDate: LocalDate) : EditTodoUiEvent()
    data class OnSelectedLimitTimeChanged(val limitTime: LocalTime) : EditTodoUiEvent()
    data class OnContentChanged(val content: String) : EditTodoUiEvent()
    data class LimitDateTimeCardExpanded(val expanded: Boolean) : EditTodoUiEvent()
    data class IsDateExpanded(val expanded: Boolean) : EditTodoUiEvent()
    data class IsTimeExpanded(val expanded: Boolean) : EditTodoUiEvent()
    data class OnSelectedTagChanged(val tag: String) : EditTodoUiEvent()
    object OnSubmitPressed : EditTodoUiEvent()
}