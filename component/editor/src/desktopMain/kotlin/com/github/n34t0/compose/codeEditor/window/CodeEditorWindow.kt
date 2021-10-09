package com.github.n34t0.compose.codeEditor.window

import AppTheme
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import com.github.n34t0.compose.codeEditor.LogMarkers
import com.github.n34t0.compose.codeEditor.editor.Editor
import com.github.n34t0.compose.codeEditor.editor.EditorState
import com.github.n34t0.compose.codeEditor.search.SearchBar
import com.github.n34t0.compose.codeEditor.statusbar.StatusBar
import mu.KotlinLogging
import com.github.n34t0.compose.stubs.diagnostic.DiagnosticStub
import com.github.n34t0.compose.stubs.getDiagnostics

private val logger = KotlinLogging.logger {}

@Composable
fun CodeEditorWindow(
    codeEditorWindowState: CodeEditorWindowState
) = Window(
    title = codeEditorWindowState.title,
    state = codeEditorWindowState.windowState,
    onCloseRequest = codeEditorWindowState::close
) {
    logger.trace(LogMarkers.recomposition) { "Recomposition CodeEditorWindows" }

    val diagnostics: List<DiagnosticStub> = getDiagnostics(codeEditorWindowState.projectFile.absoluteFilePath)

    val scope = rememberCoroutineScope()
    val editorState = rememberSaveable {
        EditorState(
            projectFile = codeEditorWindowState.projectFile,
            diagnostics = diagnostics,
            scope = scope,
            onGotoOuterDeclaration = codeEditorWindowState.onGotoDeclaration,
            gotoCallbackSetter = codeEditorWindowState::setGotoCallback
        )
    }

    DesktopMaterialTheme(
        colors = AppTheme.colors.material,
        typography = AppTheme.typography.material
    ) {
        Surface {
            Column(Modifier.fillMaxSize()) {
                SearchBar(editorState.searchState)
                Editor(editorState, Modifier.weight(1f))
                StatusBar(editorState.diagnosticMessagesUnderCaret, editorState.busyState)
            }
        }
    }
}
