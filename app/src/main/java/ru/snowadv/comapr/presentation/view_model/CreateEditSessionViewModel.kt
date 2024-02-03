package ru.snowadv.comapr.presentation.view_model

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
import kotlinx.coroutines.withContext
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.ui.session.modify.CreateEditSessionEvent
import ru.snowadv.comapr.presentation.ui.session.modify.CreateEditSessionScreenState
import javax.inject.Inject

@HiltViewModel
class CreateEditSessionViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val eventAggregator: EventAggregator,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId = savedStateHandle.get<String?>("sessionId")?.toLongOrNull()
    private val roadMapId = savedStateHandle.get<String?>("roadMapId")?.toLongOrNull()

    private val _state = mutableStateOf(
        CreateEditSessionScreenState(
            loading = false,
            newSession = (sessionId == null),
            roadMapId = roadMapId
        )
    )
    val state: State<CreateEditSessionScreenState> = _state

    fun saveOrCreateSession() {
        viewModelScope.launch {
            saveOrCreateSessionSuspend()
        }
    }
    private suspend fun saveOrCreateSessionSuspend() {
        val mapId = state.value.roadMapId ?: run {
            eventAggregator.eventChannel.send(UiEvent.ShowSnackbar("Select road map first"))
            return
        }
        val newSession = sessionId == null
        val flow = if (newSession) {
            dataRepository.createSession(
                public = state.value.public,
                startDate = state.value.startDate,
                groupChatUrl = state.value.groupChatUrl,
                roadMapId = mapId
            )
        } else {
            dataRepository.updateSession(
                public = state.value.public,
                startDate = state.value.startDate,
                groupChatUrl = state.value.groupChatUrl,
                roadMapId = mapId,
                id = sessionId!!
            )
        }

        withContext(Dispatchers.IO) {
            flow.onEach { resource ->
                when(resource) {
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            loading = false,
                        )
                        eventAggregator.eventChannel.send(UiEvent.ShowSnackbar(resource.message ?: "Unknown error"))
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            loading = true,
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            loading = false,
                        )
                        resource.data?.let {data ->
                            eventAggregator.navigationChannel.send(NavigationEvent.ToSession(data.id))
                        }
                    }
                }
            }.launchIn(this)
        }
    }

    fun loadRoadMaps() {
        viewModelScope.launch {
            dataRepository.fetchMapsNames().onEach { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(
                            loading = true,
                            roadMaps = resource.data?.associateBy { it.id } ?: emptyMap()
                        )
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            loading = false,
                            roadMaps = resource.data?.associateBy { it.id } ?: emptyMap()
                        )
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(
                            loading = false,
                            roadMaps = resource.data?.associateBy { it.id } ?: emptyMap()
                        )
                        eventAggregator.eventChannel.send(UiEvent.ShowSnackbar(resource.message ?: "Couldn't load resources"))
                    }
                }
            }.launchIn(this)
        }
    }

    fun onEvent(event: CreateEditSessionEvent) {
        when(event) {
            is CreateEditSessionEvent.ChangedRoadMapId -> {
                _state.value = _state.value.copy(
                    roadMapId = event.id
                )
            }
            is CreateEditSessionEvent.ChangedPublic -> {
                _state.value = _state.value.copy(
                    public = event.value
                )
            }
            is CreateEditSessionEvent.ChangedGroupChatUrl -> {
                _state.value = _state.value.copy(
                    groupChatUrl = event.url
                )
            }
            is CreateEditSessionEvent.ChangedStartDate -> {
                _state.value = _state.value.copy(
                    startDate = event.date
                )
            }
        }
    }
}