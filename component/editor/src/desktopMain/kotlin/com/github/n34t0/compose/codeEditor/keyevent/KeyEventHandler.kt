package com.github.n34t0.compose.codeEditor.keyevent

import androidx.compose.ui.input.key.KeyEvent

fun interface KeyEventHandler {
    fun onKeyEvent(event: KeyEvent): Boolean
}