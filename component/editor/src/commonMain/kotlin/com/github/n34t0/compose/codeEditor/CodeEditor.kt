package com.github.n34t0.compose.codeEditor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.n34t0.compose.codeEditor.diagnostics.DiagnosticElement

@Composable
fun CodeEditor(
    projectFile: ProjectFile,
    modifier: Modifier = Modifier,
    diagnostics: List<DiagnosticElement> = emptyList(),
    onTextChange: (String) -> Unit = {}
) = CodeEditorImpl(projectFile, modifier, diagnostics, onTextChange)

internal expect fun CodeEditorImpl(
    projectFile: ProjectFile,
    modifier: Modifier,
    diagnostics: List<DiagnosticElement>,
    onTextChange: (String) -> Unit
)
