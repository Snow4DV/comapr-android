package ru.snowadv.comapr.presentation.view_model

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.snowadv.comapr.data.remote.ComaprApi
import ru.snowadv.comapr.presentation.EventAggregator
import javax.inject.Inject

@HiltViewModel
class CreateSessionViewModel @Inject constructor(
    private val api: ComaprApi,
    private val eventAggregator: EventAggregator
): ViewModel() {


}