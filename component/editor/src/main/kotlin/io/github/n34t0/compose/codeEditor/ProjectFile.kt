package io.github.n34t0.compose.codeEditor

import androidx.compose.runtime.Stable
import io.github.n34t0.platform.CodeCompletionElement
import io.github.n34t0.platform.GotoDeclarationData
import io.github.n34t0.platform.Project
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Stable
class ProjectFile(
    val project: Project,
    val projectDir: String?,
    val absoluteFilePath: String
) {
    private val filePath: Path = Path.of(absoluteFilePath)
    val relativeFilePath: String = projectDir?.let {
        absoluteFilePath.substring(projectDir.length + 1)
    } ?: filePath.fileName.toString()

    init {
        projectDir?.let { require(absoluteFilePath.startsWith(projectDir)) }
    }

    fun load(): String = filePath.readText()

    fun save(text: String) = filePath.writeText(text)

    fun getCodeCompletionList(text: String, caretOffset: Int): List<CodeCompletionElement> {
        save(text)
        return project.getCodeCompletion(absoluteFilePath, caretOffset)
    }

    fun getGotoDeclarationData(text: String, caretOffset: Int): GotoDeclarationData {
        save(text)
        return project.gotoDeclaration(absoluteFilePath, caretOffset)
    }
}
