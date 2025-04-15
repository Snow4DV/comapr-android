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
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.ui.roadmap.single.RoadMapScreenState
import javax.inject.Inject


@HiltViewModel
class RoadMapViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val eventAggregator: EventAggregator,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val nodeId = savedStateHandle.get<String?>("nodeId")?.toLongOrNull()
    private val roadmapId = savedStateHandle.get<Long>("roadmapId") ?: error("Missing roadmap id")

    private val _state: MutableState<RoadMapScreenState> = mutableStateOf(RoadMapScreenState(loading = true, scrollToNodeId = nodeId))
    val state: State<RoadMapScreenState> = _state

    fun getRoadMap() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value.apply {
                dataRepository.fetchRoadMap(roadmapId).onEach {
                    val data = it.data ?: _state.value.roadMap
                    when(it) {
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(loading = true, roadMap = data)
                        }
                        is Resource.Error -> {
                            _state.value = _state.value.copy(loading = false, roadMap = data)
                            eventAggregator.eventChannel.send(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                        }
                        is Resource.Success -> {
                            _state.value = _state.value.copy(loading = false, roadMap = data)
                        }
                    }
                }.launchIn(this@launch)
            }
        }
    }
}