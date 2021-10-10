package com.github.n34t0.compose.codeEditor.demo

import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.application

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

