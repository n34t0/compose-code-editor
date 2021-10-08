package com.github.n34t0.compose.codeEditor

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.application
import com.github.n34t0.compose.stubs.ProjectDescription
import com.github.n34t0.compose.stubs.clearProjectList
import com.github.n34t0.compose.stubs.projectList

fun main() = application {

    val filesToOpen: List<ProjectDescription> = projectList

    val scope = rememberCoroutineScope() // todo: to hack the window focus

    val applicationState = remember {
        CodeEditorApplicationState(
            onClose = {
                clearProjectList()
                exitApplication()
            },
            scope = scope // todo: to hack the window focus
        ).apply {
            for (projectDescription in filesToOpen) {
                open(projectDescription.projectDir, projectDescription.file)
            }
        }
    }

    CodeEditorApplication(applicationState.windows)
}

