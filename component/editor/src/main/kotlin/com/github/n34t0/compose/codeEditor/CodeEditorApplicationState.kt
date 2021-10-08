package com.github.n34t0.compose.codeEditor

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import com.github.n34t0.compose.codeEditor.window.CodeEditorWindowState
import com.github.n34t0.compose.stubs.CcwStub
import com.github.n34t0.compose.stubs.isDebug
import com.github.n34t0.ipw.IntellijPlatformWrapper
import com.github.n34t0.ipw.IpwProject
import com.github.n34t0.ipw.impl.Wrapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Stable
class CodeEditorApplicationState(
    private val onClose: () -> Unit,
    private val scope: CoroutineScope // todo: to hack the window focus
) {
    private val ccw: IntellijPlatformWrapper = if (!isDebug) Wrapper() else CcwStub()

    private val projectDirToProject = hashMapOf<String, IpwProject>()
    private val filePathToWindow = hashMapOf<String, CodeEditorWindowState>()

    private val _windows = mutableStateListOf<CodeEditorWindowState>()
    val windows: List<CodeEditorWindowState> get() = _windows

    init {
        ccw.init()
    }

    fun open(projectDir: String? = null, filePath: String) {
        getOrOpenWindow(projectDir, filePath)
    }

    fun focus(projectDir: String, relativeFilePath: String, caretOffset: Int) {
        val absoluteFilePath = ProjectFile.getAbsoluteFilePath(projectDir, relativeFilePath)
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

    private fun getOrOpenWindow(projectDir: String?, filePath: String): CodeEditorWindowState {
        return filePathToWindow.getOrPut(filePath) {
            val project = getOrOpenProject(projectDir, filePath)
            val projectFile = ProjectFile(project, projectDir, filePath)
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

    private fun getOrOpenProject(projectDir: String?, filePath: String): IpwProject {
        return projectDir?.let {
            projectDirToProject.getOrPut(projectDir) {
                val project = ccw.openProject(projectDir)
                // warming up
                project.getCodeCompletion(filePath, 0)
                project.gotoDeclaration(filePath, 0)
                project
            }
        } ?: ccw.openFile(filePath)
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
            ccw.stop()
            onClose()
        }
    }

    private fun isProjectOpen(project: IpwProject): Boolean =
        _windows.any { it.projectFile.project == project }
}
