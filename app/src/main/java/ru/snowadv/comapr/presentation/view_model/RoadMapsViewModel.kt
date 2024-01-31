package ru.snowadv.comapr.presentation.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import ru.snowadv.comapr.presentation.ui.roadmap.list.RoadMapsScreenState
import javax.inject.Inject


@HiltViewModel
class RoadMapsViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val eventAggregator: EventAggregator
): ViewModel() {

    private val _state: MutableState<RoadMapsScreenState> = mutableStateOf(RoadMapsScreenState(loading = true))
    val state: State<RoadMapsScreenState> = _state

    private val loaded = mutableStateOf(false)

    fun setStatusIdAndCategoryIdFilters(statusId: Int?, categoryId: Long?) {
        _state.value = _state.value.copy(
            filterStatusId = statusId,
            filterCategoryId = categoryId
        )
        getRoadMaps()
    }

    fun loadDataIfDidntLoadBefore() {
        if(!loaded.value) {
            loaded.value = true
            getRoadMaps()
        }
    }
    fun getRoadMaps() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value.apply {
                dataRepository.fetchMaps(filterStatusId, filterCategoryId).onEach {
                    val data = it.data ?: _state.value.roadMaps
                    when(it) {
                        is Resource.Loading -> {
                            _state.value = _state.value.copy(
                                loading = true,
                                roadMaps = data
                            )
                        }
                        is Resource.Error -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                roadMaps = data
                            )
                            eventAggregator.eventChannel.send(UiEvent.ShowSnackbar(it.message ?: "Unknown error"))
                        }
                        is Resource.Success -> {
                            _state.value = _state.value.copy(
                                loading = false,
                                roadMaps = data
                            )
                        }
                    }
                }.launchIn(this@launch)
            }
        }
    }

}