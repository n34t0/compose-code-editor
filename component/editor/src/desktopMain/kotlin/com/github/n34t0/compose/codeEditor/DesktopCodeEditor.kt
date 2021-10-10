package com.github.n34t0.compose.codeEditor

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.github.n34t0.compose.codeEditor.diagnostics.DiagnosticElement
import com.github.n34t0.compose.codeEditor.diagnostics.DiagnosticState
import com.github.n34t0.compose.codeEditor.editor.Editor
import com.github.n34t0.compose.codeEditor.editor.EditorState
import com.github.n34t0.compose.codeEditor.search.SearchBar
import com.github.n34t0.compose.codeEditor.statusbar.StatusBar

@Composable
internal actual fun CodeEditorImpl(
    projectFile: ProjectFile,
    modifier: Modifier,
    diagnostics: List<DiagnosticElement>,
    onTextChange: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val editorState = rememberSaveable {
        EditorState(
            projectFile = projectFile,
            scope = scope,
            onOuterGotoDeclaration = { _, _, _ -> }
        )
    }

    editorState.setDiagnostics(diagnostics)

    MaterialTheme(
        colors = AppTheme.colors.material,
        typography = AppTheme.typography.material
    ) {
        Surface {
            Column(modifier) {
                SearchBar(editorState.searchState)
                Editor(editorState, onTextChange, Modifier.weight(1f))
                StatusBar(editorState.diagnosticMessagesUnderCaret, editorState.busyState)
            }
        }
    }
}
