package com.github.n34t0.compose.codeEditor

import androidx.compose.runtime.Stable
import com.github.n34t0.platform.Project
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

@Stable
internal class ProjectFileImpl(
    override val project: Project,
    override val projectDir: String?,
    override val absoluteFilePath: String
) : ProjectFile {
    private val filePath: Path = Path.of(absoluteFilePath)
    override val relativeFilePath: String = projectDir?.let {
        absoluteFilePath.substring(projectDir.length + 1)
    } ?: filePath.fileName.toString()

    init {
        projectDir?.let { require(absoluteFilePath.startsWith(projectDir)) }
    }

    override fun load(): String = filePath.readText()

    override fun save(text: String) = filePath.writeText(text)
}
