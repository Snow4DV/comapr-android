package ru.snowadv.comapr.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GroupHeader(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    titleSize: TextUnit = 19.sp,
    descriptionSize: TextUnit = 16.sp
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(6.dp)
    ) {
        Text(
            text = title,
            fontSize = titleSize,
            fontWeight = FontWeight.Medium
        )
        description?.let {
            Text(
                text = it,
                fontSize = descriptionSize,
                maxLines = 2
            )
        }
    }

}
