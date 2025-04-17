package ru.snowadv.comapr.presentation.ui.quiz

import ru.snowadv.comapr.domain.model.Challenge

data class QuizScreenState(
    val challenges: List<Challenge> = emptyList(),
    val answers: Map<Long, String> = emptyMap(),
    val loading: Boolean = true,
)