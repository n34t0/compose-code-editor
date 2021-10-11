package com.github.n34t0.compose.codeEditor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.n34t0.compose.codeEditor.diagnostics.DiagnosticElement
import com.github.n34t0.platform.Project
import com.github.n34t0.platform.Platform

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

expect fun createProjectFile(
    project: Project,
    projectDir: String?,
    absoluteFilePath: String
): ProjectFile

expect fun createPlatformInstance(): Platform
