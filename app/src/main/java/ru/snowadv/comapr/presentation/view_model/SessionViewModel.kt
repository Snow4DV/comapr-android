package ru.snowadv.comapr.presentation.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.ui.session.single.SessionScreenState
import javax.inject.Inject


@HiltViewModel
class SessionViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val eventAggregator: EventAggregator,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val sessionId = savedStateHandle.get<Long>("sessionId") ?: error("No session id!")

    private val _state: MutableState<SessionScreenState> = mutableStateOf(SessionScreenState(loading = true))
    val state: State<SessionScreenState> = _state



    fun markTask(taskId: Long, state: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.markTask(sessionId, taskId, state).onEach {  res ->
                updateSession(res)
            }.launchIn(this@launch)
        }
    }
    fun startSession() {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.startSession(sessionId).onEach {  res ->
                updateSession(res)
            }.launchIn(this@launch)

        }
    }
    fun endSession() {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.endSession(sessionId).onEach {  res ->
                updateSession(res)
            }.launchIn(this@launch)

        }
    }
    fun leaveSession() {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.leaveSession(sessionId).onEach {  res ->
                updateSession(res)
            }.launchIn(this@launch)

        }
    }
    fun joinSession() {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.joinSession(sessionId).onEach {  res ->
                updateSession(res)
            }.launchIn(this@launch)

        }
    }
    fun getSession() {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.getSession(sessionId).onEach {  res ->
                updateSession(res)
            }.launchIn(this@launch)

        }
    }

    fun openChallenges(taskId: Long) {
        viewModelScope.launch {
            eventAggregator.navigationChannel.send(NavigationEvent.ToQuizScreen(sessionId, taskId))
        }
    }

    private suspend fun updateSession(
        res: Resource<MapSession>
    ) {
        state.value.apply {
            when (res) {
                is Resource.Error -> {
                    _state.value = copy(
                        loading = false
                    )
                    eventAggregator.eventChannel.send(
                        UiEvent.ShowSnackbar(
                            res.message ?: "Unknown error"
                        )
                    )
                }

                is Resource.Loading -> {
                    _state.value = copy(
                        loading = true
                    )
                }

                is Resource.Success -> {
                    _state.value = copy(
                        loading = false,
                        session = res.data,
                        joined = res.data?.joined ?: false
                    )
                }
            }
        }
    }

}