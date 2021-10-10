package com.github.n34t0.compose.codeEditor

import com.github.n34t0.platform.CodeCompletionElement
import com.github.n34t0.platform.GotoDeclarationData
import com.github.n34t0.platform.Project

interface ProjectFile {
    val project: Project
    val projectDir: String?
    val absoluteFilePath: String
    val relativeFilePath: String

    fun load(): String
    fun save(text: String)

    fun getCodeCompletionList(text: String, caretOffset: Int): List<CodeCompletionElement> {
        save(text)
        return project.getCodeCompletion(absoluteFilePath, caretOffset)
    }

    fun getGotoDeclarationData(text: String, caretOffset: Int): GotoDeclarationData {
        save(text)
        return project.gotoDeclaration(absoluteFilePath, caretOffset)
    }
}
