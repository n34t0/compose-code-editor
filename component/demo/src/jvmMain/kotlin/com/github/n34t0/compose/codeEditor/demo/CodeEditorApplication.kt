package com.github.n34t0.compose.codeEditor.demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import com.github.n34t0.compose.codeEditor.demo.window.CodeEditorWindow
import com.github.n34t0.compose.codeEditor.demo.window.CodeEditorWindowState

@Composable
fun CodeEditorApplication(
    windowList: List<CodeEditorWindowState>
) {
    for (window in windowList) {
        key(window) {
            CodeEditorWindow(window)
        }
    }
}
