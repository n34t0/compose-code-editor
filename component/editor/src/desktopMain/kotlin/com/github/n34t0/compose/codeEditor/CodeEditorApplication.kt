package com.github.n34t0.compose.codeEditor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.github.n34t0.compose.codeEditor.window.CodeEditorWindow
import com.github.n34t0.compose.codeEditor.window.CodeEditorWindowState
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@Composable
fun CodeEditorApplication(
    windowList: List<CodeEditorWindowState>
) {
    logger.trace(LogMarkers.recomposition) { "Recomposition CodeEditorApplication" }

    for (window in windowList) {
        key(window) {
            CodeEditorWindow(window)
        }
    }
}
