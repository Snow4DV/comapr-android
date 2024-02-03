package ru.snowadv.comapr.presentation.ui.session.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.snowadv.comapr.R
import ru.snowadv.comapr.core.util.SampleData
import ru.snowadv.comapr.domain.model.MapSession
import ru.snowadv.comapr.presentation.ui.common.TextWithIcon
import java.time.format.DateTimeFormatter

@Composable
fun SessionItem(
    modifier: Modifier = Modifier,
    session: MapSession,
    onClickSession: ((MapSession) -> Unit)? = null,
    showChatUrl: Boolean = false,
    onUrlClick: (String) -> Unit = {},
    onJoinSessionClick: (() -> Unit)? = null,
    onStartSessionClick: (() -> Unit)? = null,
    onEndSessionClick: (() -> Unit)? = null,
    onLeaveSessionClick: (() -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .padding(5.dp)
            .let { mod -> onClickSession?.let { mod.clickable { it(session) } } ?: mod },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .height(with(LocalDensity.current) { 23.sp.toDp() })
                        .padding(end = 5.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = session.creator.username,
                    fontSize = 23.sp,
                    color = MaterialTheme.colorScheme.primary
                )

            }
            TextWithIcon(
                text = "${stringResource(R.string.map_sessions_list)}${session.roadMap.name}",
                fontSize = 18.sp,
                icon = Icons.Outlined.Place
            )
            TextWithIcon(
                text = "${stringResource(R.string.joined_users)}${session.users.size}",
                fontSize = 18.sp,
                icon = ImageVector.vectorResource(R.drawable.people_outlined)
            )
            TextWithIcon(
                text = "${stringResource(R.string.starting_at)}${
                    session.startDate.format(
                        DateTimeFormatter.ofPattern("yyyy.MM.DD hh:mm")
                    )
                }",
                fontSize = 18.sp,
                icon = ImageVector.vectorResource(R.drawable.clock_outlined)
            )
            session.groupChatUrl?.let { chatUrl ->
                if (showChatUrl) {
                    val urlBeginPattern =
                        remember { Regex("""(http://)|(https://)""") }
                    val resLink = chatUrl.replace(urlBeginPattern, "")
                    Row(
                        modifier = Modifier.clickable {
                            onUrlClick(chatUrl)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.link_filled),
                            contentDescription = null,
                            modifier = Modifier
                                .height(with(LocalDensity.current) { 18.sp.toDp() })
                                .padding(end = 5.dp),
                            tint = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            text = resLink,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    TextWithIcon(
                        text = stringResource(R.string.with_group_chat),
                        fontSize = 18.sp,
                        icon = ImageVector.vectorResource(R.drawable.chat_outlined)
                    )
                }
            }
            if(onJoinSessionClick != null && !session.joined) {
                Button(onClick = onJoinSessionClick, modifier = Modifier.align(Alignment.End)) {
                    Text(stringResource(R.string.join))
                }
            } else if(onStartSessionClick != null && session.isCreator && session.state == MapSession.State.LOBBY) {
                Button(onClick = onStartSessionClick, modifier = Modifier.align(Alignment.End)) {
                    Text(stringResource(R.string.start_session))
                }
            } else if(onEndSessionClick != null && session.isCreator && session.state == MapSession.State.STARTED) {
                Button(onClick = onEndSessionClick, modifier = Modifier.align(Alignment.End)) {
                    Text(stringResource(R.string.end_session))
                }
            } else if(onLeaveSessionClick != null && session.joined && !session.isCreator) {
                Button(onClick = onLeaveSessionClick, modifier = Modifier.align(Alignment.End)) {
                    Text(stringResource(R.string.leave_session))
                }
            }

        }
    }


}


@Preview
@Composable
fun SessionItemPreview() {
    SessionItem(
        modifier = Modifier
            .width(420.dp),
        session = SampleData.session,
        onClickSession = {}
    )
}

