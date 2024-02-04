package ru.snowadv.comapr.presentation.ui.session.modify

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.snowadv.comapr.R
import ru.snowadv.comapr.core.util.NavigationEvent
import ru.snowadv.comapr.core.util.millisToZonedDateTime
import ru.snowadv.comapr.presentation.ui.common.CheckboxWithText
import ru.snowadv.comapr.presentation.ui.common.SimpleTopBarWithBackButton
import ru.snowadv.comapr.presentation.ui.common.TimePickerDialog
import ru.snowadv.comapr.presentation.view_model.CreateEditSessionViewModel
import ru.snowadv.comapr.presentation.view_model.MainViewModel
import java.time.format.DateTimeFormatter


@Composable
fun CreateEditSessionScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    createEditSessionViewModel: CreateEditSessionViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        createEditSessionViewModel.loadRoadMaps()
    }
    CreateEditSessionScreenContent(
        modifier = modifier,
        onEvent = { createEditSessionViewModel.onEvent(it) },
        state = createEditSessionViewModel.state.value,
        onBackClicked = {mainViewModel.navigate(NavigationEvent.PopBackStack)},
        onSaveClick = {createEditSessionViewModel.saveOrCreateSession()}
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditSessionScreenContent(
    modifier: Modifier = Modifier,
    onEvent: (CreateEditSessionEvent) -> Unit,
    state: CreateEditSessionScreenState,
    onBackClicked: () -> Unit,
    onSaveClick: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = state.startDate.toInstant().toEpochMilli(),
        initialDisplayMode = DisplayMode.Picker
    )
    val timePickerState = rememberTimePickerState(
        initialHour = state.startDate.hour,
        initialMinute = state.startDate.minute
    )

    val datePickerDialogVisibility = remember { mutableStateOf(false) }
    val timePickerDialogVisibility = remember { mutableStateOf(false) }

    val roadMapListExpanded = remember { mutableStateOf(false) }

    val selectedRoadmap = state.roadMapId?.let { state.roadMaps[it] }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }

    fun updateDateAndTime() {
        val dateMillis = datePickerState.selectedDateMillis
        val timeMillis = timePickerState.hour * 3600_000 + timePickerState.minute * 60_000
        if (dateMillis == null) return
        val date = millisToZonedDateTime(dateMillis + timeMillis)
        onEvent(CreateEditSessionEvent.ChangedStartDate(date))
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SimpleTopBarWithBackButton(
                title = stringResource(if (state.newSession) R.string.new_session else R.string.edit_session),
                onBackClicked = onBackClicked
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSaveClick,
            ) {
                Icon(Icons.Filled.Check, "Save session")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 10.dp, top = 3.dp)
                        .size(30.dp),
                    imageVector = Icons.Outlined.Place,
                    contentDescription = "Communication"
                )
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = roadMapListExpanded.value,
                    onExpandedChange = { roadMapListExpanded.value = !roadMapListExpanded.value },
                ) {

                    TextField(
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        readOnly = true,
                        value = selectedRoadmap?.name ?: stringResource(R.string.not_selected),
                        onValueChange = {},
                        label = { Text(stringResource(id = R.string.road_map)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roadMapListExpanded.value) },
                    )
                    ExposedDropdownMenu(
                        expanded = roadMapListExpanded.value,
                        onDismissRequest = { roadMapListExpanded.value = false },
                    ) {
                        state.roadMaps.values.forEach { roadMap ->
                            DropdownMenuItem(
                                text = { Text(roadMap.name) },
                                onClick = {
                                    onEvent(CreateEditSessionEvent.ChangedRoadMapId(roadMap.id))
                                    roadMapListExpanded.value = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                            )
                        }
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 0.dp, top = 3.dp)
                        .size(30.dp),
                    painter = painterResource(R.drawable.visibility_filled),
                    contentDescription = "Visibility"
                )
                CheckboxWithText(
                    checked = state.public,
                    onCheckedChange = { onEvent(CreateEditSessionEvent.ChangedPublic(it)) },
                    text = stringResource(R.string.make_session_public)
                )
            }


            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 10.dp, top = 3.dp)
                        .size(30.dp),
                    painter = painterResource(R.drawable.chat_outlined),
                    contentDescription = "Communication"
                )
                TextField(
                    singleLine = true,
                    label = { Text(text = stringResource(R.string.group_chat_url)) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = state.groupChatUrl,
                    onValueChange = { onEvent(CreateEditSessionEvent.ChangedGroupChatUrl(it)) }
                )
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = 5.dp, end = 10.dp, top = 3.dp)
                        .size(30.dp),
                    painter = painterResource(R.drawable.clock_outlined),
                    contentDescription = "Communication"
                )
                Text(
                    text = state.startDate.format(dateFormatter),
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { datePickerDialogVisibility.value = true }
                )
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable { timePickerDialogVisibility.value = true },
                    text = state.startDate.format(timeFormatter),
                    fontSize = 19.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }

    if (datePickerDialogVisibility.value) {
        DatePickerDialog(
            onDismissRequest = { datePickerDialogVisibility.value = false },
            confirmButton = {
                updateDateAndTime()
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }



    if (timePickerDialogVisibility.value) {
        TimePickerDialog(
            title = stringResource(R.string.select_session_s_start_time),
            onCancel = { timePickerDialogVisibility.value = false },
            onConfirm = {
                updateDateAndTime()
                timePickerDialogVisibility.value = false
            }
        ) {
            TimePicker(state = timePickerState)
        }

    }
}


@Preview
@Composable
fun CreateEditSessionScreenContentPreview() {
    CreateEditSessionScreenContent(
        modifier = Modifier
            .width(420.dp)
            .height(905.dp),
        onEvent = {},
        state = CreateEditSessionScreenState(),
        onSaveClick = {},
        onBackClicked = {}
    )
}