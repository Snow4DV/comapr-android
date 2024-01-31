package ru.snowadv.comapr.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.ui.profile.ProfileScreenState
import ru.snowadv.comapr.presentation.use_case.GetUserProfileUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val eventAggregator: EventAggregator,
    private val getProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _state = mutableStateOf(ProfileScreenState(loading = true))
    val state: State<ProfileScreenState> = _state

    private val loaded = mutableStateOf(false)

    fun loadDataIfDidntLoadBefore() {
        if(!loaded.value) {
            loaded.value = true
            getProfile()
        }
    }
    fun getProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            getProfileUseCase().onEach {profileResponse ->
                when (profileResponse) {
                    is Resource.Error -> {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(loading = false)
                            eventAggregator.eventChannel.send(
                                UiEvent.ShowSnackbar(
                                    profileResponse.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                    is Resource.Loading, is Resource.Success -> {
                        withContext(Dispatchers.Main) {
                            _state.value = _state.value.copy(
                                loading = profileResponse is Resource.Loading,
                                data = profileResponse.data ?: _state.value.data
                            )
                        }
                    }
                }
            }.launchIn(this)
        }
    }



}