package com.github.n34t0.compose.codeEditor.demo.window

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import com.github.n34t0.compose.codeEditor.CodeEditor
import com.github.n34t0.compose.codeEditor.demo.getDiagnostics
import com.github.n34t0.compose.codeEditor.diagnostics.DiagnosticElement

@Composable
fun CodeEditorWindow(
    codeEditorWindowState: CodeEditorWindowState
) = Window(
    title = codeEditorWindowState.title,
    state = codeEditorWindowState.windowState,
    onCloseRequest = codeEditorWindowState::close
) {

    val diagnostics: List<DiagnosticElement> = getDiagnostics(codeEditorWindowState.projectFile.absoluteFilePath)

    CodeEditor(
        projectFile = codeEditorWindowState.projectFile,
        modifier = Modifier.fillMaxSize(),
        diagnostics = diagnostics
    )
}
