package com.github.n34t0.compose.codeEditor.demo.window

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import com.github.n34t0.compose.codeEditor.ProjectFile
import java.io.File

@Stable
class CodeEditorWindowState(
    val projectFile: ProjectFile,
    private val onClose: (CodeEditorWindowState) -> Unit,
    val onGotoDeclaration: (String, String, Int) -> Unit
) {
    val title = "CodeEditor " +
        (projectFile.projectDir?.let { it.substringAfterLast(File.separatorChar) + " - " } ?: "") +
        projectFile.relativeFilePath

    val windowState = WindowState(width = 1280.dp, height = 768.dp)

    private lateinit var goto: (Int) -> Unit

    fun setGotoCallback(goto: (Int) -> Unit) {
        this.goto = goto
    }

    fun focus(caretOffset: Int) {
        if (this::goto.isInitialized) goto(caretOffset)
    }

    fun close() = onClose(this)
}
