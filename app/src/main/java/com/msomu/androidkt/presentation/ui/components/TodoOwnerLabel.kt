package com.msomu.androidkt.presentation.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

internal fun todoOwnerLabel(userId: Int): String = "User #$userId"

@Composable
fun TodoOwnerLabel(
    userId: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = todoOwnerLabel(userId),
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
    )
}
