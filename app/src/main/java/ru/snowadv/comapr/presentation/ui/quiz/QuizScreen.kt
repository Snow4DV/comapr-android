package ru.snowadv.comapr.presentation.ui.quiz

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.icons.Icons
import androidx.compose.material.TabRow
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.snowadv.comapr.R
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.domain.model.Challenge
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.Role
import ru.snowadv.comapr.domain.model.User
import ru.snowadv.comapr.domain.model.UserAndSessions
import ru.snowadv.comapr.presentation.ui.common.SimpleTopBarWithBackButton
import ru.snowadv.comapr.presentation.ui.session.list.SessionsList
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.ProfileViewModel
import ru.snowadv.comapr.presentation.view_model.QuizViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    quizViewModel: QuizViewModel = hiltViewModel(),
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            quizViewModel.loadData()
        }
    }

    val state = quizViewModel.state.collectAsState(QuizScreenState())
    val pullRefreshState =
        rememberPullRefreshState(state.value.loading, { quizViewModel.loadData() })

    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        state.value.let { state ->
            when {
                !state.loading && !state.error -> {
                    QuizScreenContent(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        onAnswerClick = { id, answer -> quizViewModel.selectAnswer(id, answer) },
                        onSendAnswersClick = { quizViewModel.sendAnswers() },
                    )
                }

                state.error -> Text("Unable to load challenges")
            }
        }

        PullRefreshIndicator(
            refreshing = state.value.loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuizScreenContent(
    modifier: Modifier = Modifier,
    state: QuizScreenState,
    onAnswerClick: (Long, String) -> Unit = { _, _ -> },
    onSendAnswersClick: () -> Unit = {},
) {

    Scaffold(modifier = modifier, topBar = {
        SimpleTopBarWithBackButton(
            title = "Quiz",
            onBackClicked = {},
        )
    }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        ) {
            Column(modifier = modifier) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, top = 10.dp)
                ) {
                    items(state.challenges.size) { index ->
                        val challenge = state.challenges[index]

                        QuizChallenge(
                            modifier = Modifier.fillMaxWidth(),
                            challenge = challenge,
                            onAnswerSelected = { answer -> onAnswerClick(challenge.id, answer) },
                            selectedAnswer = state.answers[challenge.id],
                        )
                    }

                    item {
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onSendAnswersClick,
                        ) {
                            Text(
                                text = "Send answers",
                                color = Color.White,
                                fontSize = 16.sp,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizChallenge(
    modifier: Modifier = Modifier,
    challenge: Challenge,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit = {},
) {
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
    ) {
        Row(
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.0f, fill = true)) {
                Text(
                    text = challenge.description,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                challenge.answers.forEach { answer ->
                    val backgroundColor = if (answer == selectedAnswer) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                    Button(
                        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onAnswerSelected(answer) },
                    ) {
                        Text(
                            text = answer,
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun QuizScreenPreview() {
    QuizScreenContent(
        modifier = Modifier
            .width(420.dp)
            .height(905.dp)
            .background(MaterialTheme.colorScheme.surface),
        state = QuizScreenState(
            challenges = listOf(
                Challenge(
                    id = 1,
                    description = "Which type of cache mode doesnt lazy from kotlin have?",
                    answers = listOf("Publication", "Synchronized", "None", "Automatic")
                ),
                Challenge(
                    id = 2,
                    description = "Which component doesn't declare in manifest in android development?",
                    answers = listOf("Service", "Activity", "Widget", "Content provider"),
                ),
            ),
            loading = false,
            answers = mapOf(1L to "Automatic"),
        ),
    )
}

@Preview
@Composable
fun QuizChallengePreview() {
    QuizChallenge(
        modifier = Modifier
            .width(420.dp)
            .height(100.dp),
        challenge = Challenge(
            id = 1,
            description = "Which type of cache mode doesnt lazy from kotlin have?",
            answers = listOf("Publication", "Synchronized", "None", "Automatic")
        ),
        selectedAnswer = "Automatic",
    )
}