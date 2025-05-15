package ru.snowadv.comapr.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.domain.model.Challenge
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.ui.profile.ProfileScreenState
import ru.snowadv.comapr.presentation.ui.quiz.QuizScreenState
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val eventAggregator: EventAggregator,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId = requireNotNull(savedStateHandle.get<Long>("sessionId")) {
        "sessionId is null in savedStateHandle"
    }

    private val taskId = requireNotNull(savedStateHandle.get<Long?>("taskId")) {
        "taskId is null in savedStateHandle"
    }

    private val _state = MutableStateFlow(QuizScreenState())
    val state: Flow<QuizScreenState> = _state

    fun loadData() {
        viewModelScope.launch(Dispatchers.Default) {
            dataRepository.getChallengesForTask(taskId).onEach { resource ->
                _state.update { oldState ->
                    when (resource) {
                        is Resource.Error<List<Challenge>> -> oldState.copy(
                            loading = false,
                            error = true
                        )

                        is Resource.Loading<List<Challenge>> -> oldState.copy(
                            loading = true,
                            error = false
                        )

                        is Resource.Success<List<Challenge>> -> oldState.copy(
                            loading = false,
                            error = false,
                            challenges = resource.data ?: emptyList()
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    fun selectAnswer(id: Long, answer: String) {
        _state.update { state ->
            val answers = state.answers.toMutableMap().apply {
                put(id, answer)
            }

            state.copy(answers = answers)
        }
    }

    fun sendAnswers() {
        dataRepository.sendAnswers(sessionId, taskId, _state.value.answers).onEach { res ->
            when (res) {
                is Resource.Error<*> -> {
                    eventAggregator.eventChannel.send(
                        UiEvent.ShowSnackbar(
                            message = "Unable to send answers: ${res.message ?: "no error"}"
                        )
                    )
                    _state.update { it.copy(loading = false) }
                }

                is Resource.Loading<*> -> _state.update { it.copy(loading = true) }
                is Resource.Success<*> -> {
                    val message = if (res.data == true) {
                        eventAggregator.navigationChannel.send(NavigationEvent.PopBackStack)
                        "Task was finished successfully!"
                    } else {
                        "Too many errors, try again."
                    }
                    eventAggregator.eventChannel.send(UiEvent.ShowSnackbar(message))
                    _state.update { it.copy(loading = false) }
                }
            }

        }.launchIn(viewModelScope)
    }
}