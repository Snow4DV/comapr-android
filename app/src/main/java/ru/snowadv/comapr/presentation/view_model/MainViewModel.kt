package ru.snowadv.comapr.presentation.view_model

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.domain.model.AuthUser
import ru.snowadv.comapr.presentation.use_case.AuthenticateUseCase
import ru.snowadv.comapr.presentation.use_case.SignInUseCase
import ru.snowadv.comapr.presentation.use_case.SignUpUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authenticateCase: AuthenticateUseCase,
    private val signInCase: SignInUseCase,
    private val signUpCase: SignUpUseCase,
) : ViewModel() {
    private val _user = mutableStateOf<AuthUser?>(null)
    val user: State<AuthUser?> = _user
    val authorized = _user.value != null

    private val _loading = mutableStateOf(false)
    private val loading: State<Boolean> = _loading

    private val eventChannel = Channel<UiEvent>()
    val eventFlow: Flow<UiEvent> = eventChannel.receiveAsFlow()


    private val navigationChannel = Channel<UiEvent>()
    val navigationFlow: Flow<UiEvent> = navigationChannel.receiveAsFlow()

    suspend fun authenticate() {
        withContext(Dispatchers.IO) {
            _loading.value = true
            updateAuthUser(authenticateCase())
        }
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
            }

            is Resource.Loading -> {
                _loading.value = true
            }

            is Resource.Success -> {
                resource.data?.let {
                    eventChannel.send(UiEvent.ShowSnackbar("Authorized successfully!"))
                    _user.value = it
                    _loading.value = false
                }
            }
        }
    }

    companion object {
        const val TAG = "MainViewModel"
    }
}