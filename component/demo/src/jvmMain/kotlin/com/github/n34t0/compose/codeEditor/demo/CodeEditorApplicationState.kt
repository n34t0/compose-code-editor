package com.github.n34t0.compose.codeEditor.demo

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import com.github.n34t0.compose.codeEditor.ProjectFileImpl
import com.github.n34t0.compose.codeEditor.demo.window.CodeEditorWindowState
import com.github.n34t0.platform.Platform
import com.github.n34t0.platform.Project
import com.github.n34t0.platform.impl.IntellijPlatformWrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@Stable
class CodeEditorApplicationState(
    private val onClose: () -> Unit,
    private val scope: CoroutineScope // todo: to hack the window focus
) {
    private val platform: Platform = IntellijPlatformWrapper() // todo: maybe add factory

    private val projectDirToProject = hashMapOf<String, Project>()
    private val filePathToWindow = hashMapOf<String, CodeEditorWindowState>()

    private val _windows = mutableStateListOf<CodeEditorWindowState>()
    val windows: List<CodeEditorWindowState> get() = _windows

    init {
        platform.init()
    }

    fun open(projectDir: String? = null, filePath: String) {
        getOrOpenWindow(projectDir, filePath)
    }

    fun focus(projectDir: String, relativeFilePath: String, caretOffset: Int) {
        val absoluteFilePath = getAbsoluteFilePath(projectDir, relativeFilePath)
        val windowState = getOrOpenWindow(projectDir, absoluteFilePath)
        // todo: window focus hack
        _windows -= windowState
        scope.launch {
            delay(10)
            _windows += windowState
            delay(10)
            windowState.focus(caretOffset)
        }
    }

    private fun getAbsoluteFilePath(projectDir: String, relativeFilePath: String): String =
        projectDir + File.separator + relativeFilePath

    private fun getOrOpenWindow(projectDir: String?, filePath: String): CodeEditorWindowState {
        return filePathToWindow.getOrPut(filePath) {
            val project = getOrOpenProject(projectDir, filePath)
            val projectFile = ProjectFileImpl(project, projectDir, filePath)
            val windowState = CodeEditorWindowState(
                projectFile = projectFile,
                onClose = ::close,
                onGotoDeclaration = ::focus
            )
            _windows += windowState
            filePathToWindow += filePath to windowState
            windowState
        }
    }

    private fun getOrOpenProject(projectDir: String?, filePath: String): Project {
        return projectDir?.let {
            projectDirToProject.getOrPut(projectDir) {
                val project = platform.openProject(projectDir)
                // warming up
                project.getCodeCompletion(filePath, 0)
                project.gotoDeclaration(filePath, 0)
                project
            }
        } ?: platform.openFile(filePath)
    }

    private fun close(windowState: CodeEditorWindowState) {
        _windows -= windowState
        filePathToWindow -= windowState.projectFile.absoluteFilePath

        windowState.projectFile.project.let { project ->
            if (!isProjectOpen(project)) {
                project.closeProject()
                projectDirToProject.entries.removeIf { it.value == project }
            }
        }

        if (_windows.isEmpty()) {
            platform.stop()
            onClose()
        }
    }

    private fun isProjectOpen(project: Project): Boolean =
        _windows.any { it.projectFile.project == project }
}
