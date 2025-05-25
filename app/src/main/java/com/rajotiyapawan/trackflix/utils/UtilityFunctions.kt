package com.rajotiyapawan.trackflix.utils

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Modifier.noRippleClick(delayMillis: Long = 500L, onClick: () -> Unit): Modifier {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return this.then(
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() }, indication = null
        ) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime >= delayMillis) {
                lastClickTime = currentTime
                onClick()
            }
        }
    )
}