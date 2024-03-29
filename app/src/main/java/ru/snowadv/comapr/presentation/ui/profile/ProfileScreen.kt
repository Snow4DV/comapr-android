package ru.snowadv.comapr.presentation.ui.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LeadingIconTab
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.TabRow
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import ru.snowadv.comapr.R
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.domain.model.Role
import ru.snowadv.comapr.domain.model.User
import ru.snowadv.comapr.domain.model.UserAndSessions
import ru.snowadv.comapr.presentation.ui.session.list.SessionsList
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import ru.snowadv.comapr.presentation.view_model.ProfileViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    mainViewModel: MainViewModel
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(true) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            profileViewModel.loadDataIfDidntLoadBefore()
        }
    }

    val state = remember { profileViewModel.state }
    val pullRefreshState =
        rememberPullRefreshState(state.value.loading, { profileViewModel.getProfile() })

    Box(
        modifier = modifier
            .pullRefresh(pullRefreshState)
            .fillMaxSize()
    ) {
        state.value.data?.let {
            ProfileScreenContent(
                modifier = Modifier.fillMaxSize(),
                data = it,
                onSessionClick = {
                    mainViewModel.navigate(NavigationEvent.ToSession(it.id))
                }
            )
        }

        PullRefreshIndicator(
            refreshing = state.value.loading,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}

enum class ProfileTab(val imageVector: ImageVector, val textResId: Int, val navRoute: String) {
    Profile(Icons.Filled.Person, R.string.profile, "profile"),
    Sessions(Icons.Filled.List, R.string.sessions, "sessions")
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    data: UserAndSessions,
    onSessionClick: (MapSession) -> Unit
) {
    val pagerState = rememberPagerState { 2 }

    val scrollCoroutineScope = rememberCoroutineScope()

    val propertiesScroll = rememberScrollState()

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                modifier = Modifier.size(70.dp),
                imageVector = Icons.Filled.Person,
                contentDescription = "profile icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = data.user.username,
                fontSize = 50.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        ) {
            ProfileTab.entries.forEachIndexed { index, tab ->
                LeadingIconTab(
                    icon = {
                        Icon(
                            imageVector = tab.imageVector,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    text = {
                        Text(
                            stringResource(id = tab.textResId),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scrollCoroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                )
            }
        }

        HorizontalPager(
            modifier = modifier
                .fillMaxSize(),
            state = pagerState
        ) {
            when (it) {
                0 -> {
                    UserInfoTab(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(propertiesScroll),
                        user = data.user
                    )
                }

                1 -> {
                    SessionsList(
                        modifier = Modifier.fillMaxSize(),
                        sessions = data.sessions,
                        onClickSession = onSessionClick
                    )
                }
            }
        }


    }


}

@Composable
fun UserInfoTab(
    modifier: Modifier = Modifier,
    user: User
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .padding(top = 10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Row(modifier = Modifier.padding(15.dp)) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    contentDescription = null,
                    modifier = Modifier
                        .height(with(LocalDensity.current) { 25.sp.toDp() })
                        .padding(end = 5.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "${stringResource(R.string.email_profile)}${user.email}",
                    fontSize = 17.sp
                )
            }
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(modifier = Modifier.padding(15.dp)) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .height(with(LocalDensity.current) { 25.sp.toDp() })
                        .padding(end = 5.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "${stringResource(R.string.id_profile)}${user.id}",
                    fontSize = 17.sp
                )
            }
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(modifier = Modifier.padding(15.dp)) {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = null,
                    modifier = Modifier
                        .height(with(LocalDensity.current) { 25.sp.toDp() })
                        .padding(end = 5.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = "${stringResource(R.string.role_profile)}${stringResource(id = user.role.displayNameResId)}",
                    fontSize = 17.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreenContent(
        modifier = Modifier
            .width(420.dp)
            .height(905.dp),
        data = UserAndSessions(
            User(1, "snowadv", "testemail@example.com", Role.ROLE_ADMIN),
            emptyList()
        ),
        onSessionClick = {}
    )
}