package ru.snowadv.comapr.presentation.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.data.remote.ApiAuthenticator
import ru.snowadv.comapr.domain.model.AuthUser
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.use_case.AuthenticateUseCase
import ru.snowadv.comapr.presentation.use_case.SignInUseCase
import ru.snowadv.comapr.presentation.use_case.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticateCase: AuthenticateUseCase,
    private val signInCase: SignInUseCase,
    private val signUpCase: SignUpUseCase,
    private val authenticator: ApiAuthenticator,
    private val eventAggregator: EventAggregator
) : ViewModel() {
    private val _user = MutableStateFlow<AuthUser?>(null)
    val user: StateFlow<AuthUser?> = _user.asStateFlow()
    val authorized
        get() = _user.value != null

    private val _loading = mutableStateOf(false)
    val loading: State<Boolean> = _loading

    private val eventChannel = eventAggregator.eventChannel
    val eventFlow: Flow<UiEvent> = eventChannel.receiveAsFlow()


    private val navigationChannel = Channel<NavigationEvent>()
    val navigationFlow: Flow<NavigationEvent> = navigationChannel.receiveAsFlow()

    init {
        viewModelScope.launch{
            authenticate()
        }

        viewModelScope.launch {
            user.onEach {
                it?.let {
                    navigationChannel.send(NavigationEvent.ToHomeScreen())
                } /*?: run {
                    navigationChannel.send(NavigationEvent.ToLoginScreen())
                }*/
            }.launchIn(this)
        }
    }


    fun navigate(navigationEvent: NavigationEvent) {
        viewModelScope.launch {
            navigationChannel.send(navigationEvent)
        }
    }

    fun sendUiEvent(uiEvent: UiEvent) {
        viewModelScope.launch {
            eventChannel.send(uiEvent)
        }
    }
    private suspend fun authenticate() {
        _loading.value = true
        var result: Resource<AuthUser>
        withContext(Dispatchers.IO) {
            result = authenticateCase()
        }
        updateAuthUser(result)

    }

    fun signIn(username: String, password: String) {
        Log.e(TAG, "signIn: with $username $password")
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            updateAuthUser(signInCase(username, password))
        }
    }

    fun signUp(email: String, username: String, password: String) {
        Log.d(TAG, "signOut: with $username $password $email")
        viewModelScope.launch(Dispatchers.IO) {
            _loading.value = true
            updateAuthUser(signUpCase(email, username, password))
        }
    }

    private suspend fun updateAuthUser(resource: Resource<AuthUser>) {
        Log.d(TAG, "updateAuthUser: got ${resource.data} ${resource}")
        when (resource) {
            is Resource.Error -> {
                eventChannel.send(UiEvent.ShowSnackbar(resource.message ?: "Unknown error"))
                _loading.value = false
                navigationChannel.send(NavigationEvent.ToLoginScreen())
                // go to login screen on auth fail otherwise observers won't be notified with
                // another null value
            }

            is Resource.Loading -> {
                _loading.value = true
            }

            is Resource.Success -> {
                resource.data?.let {
                    //eventChannel.send(UiEvent.ShowSnackbar("Authorized successfully!"))
                    _user.value = it
                    authenticator.token = it.token
                    _loading.value = false
                }
            }
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}