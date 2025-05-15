package ru.snowadv.comapr.presentation.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.snowadv.comapr.R

@Composable
fun CheckboxWithText(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    text: String
) {

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            text = text,
            fontSize = 19.sp
        )
    }
}


@Composable
fun TextWithIcon(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    contentDescription: String? = null,
    fontSize: TextUnit,
    color: Color = MaterialTheme.colorScheme.onSurface,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.let { mod -> onClick?.let { mod.clickable { it() } } ?: mod },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .height(with(LocalDensity.current) { fontSize.toDp() })
                .padding(end = 3.dp),
            tint = color,
        )
        Text(
            text = text,
            fontSize = fontSize,
            color = color
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBarWithBackButton(
    title: String,
    onBackClicked: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        ) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            IconButton(onClick = { onBackClicked() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "back"
                )
            }
        },
    )
}


@Preview
@Composable
fun TextWithIconPreview() {
    TextWithIcon(
        icon = Icons.Filled.Person,
        text = "Test user",
        fontSize = 23.sp,
        color = MaterialTheme.colorScheme.primary
    )
}


@Preview
@Composable
fun CheckboxWithTextPreview() {
    CheckboxWithText(
        checked = true,
        text = "Test user",
        onCheckedChange = {}
    )
}