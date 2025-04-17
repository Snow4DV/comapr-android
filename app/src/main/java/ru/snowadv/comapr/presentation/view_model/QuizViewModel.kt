package ru.snowadv.comapr.presentation.view_model

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.snowadv.comapr.core.util.Resource
import ru.snowadv.comapr.core.util.UiEvent
import ru.snowadv.comapr.domain.repository.DataRepository
import ru.snowadv.comapr.presentation.EventAggregator
import ru.snowadv.comapr.presentation.ui.profile.ProfileScreenState
import ru.snowadv.comapr.presentation.ui.quiz.QuizScreenState
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val eventAggregator: EventAggregator,
    private val dataRepository: DataRepository
) : ViewModel() {

    private val _state = MutableStateFlow(QuizScreenState())
    val state: Flow<QuizScreenState> = _state

    fun loadData() {

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

    }
}