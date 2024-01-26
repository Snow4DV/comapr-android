package ru.snowadv.comapr.presentation.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.screen.session.list.SessionsScreenState
import javax.inject.Inject


@HiltViewModel
class SessionsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val eventAggregator: EventAggregator
): ViewModel() {

    private val _state: MutableState<SessionsScreenState> = mutableStateOf(SessionsScreenState(loading = true))
    val state: State<SessionsScreenState> = _state

    fun getSessions() {
        viewModelScope.launch {
            state.value.apply {
                dataRepository.fetchSessions().onEach {
                    val data = it.data ?: _state.value.sessions
                    when(it) {
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(
                                loading = true,
                                sessions = data
                            )
                        }
                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                sessions = data
                            )
                            eventAggregator.eventChannel.send(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                        }
                        is Resource.Success -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                sessions = data
                            )
                        }
                    }
                }.launchIn(this@launch)
            }
        }
    }

}