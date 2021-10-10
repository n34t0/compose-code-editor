package com.github.n34t0.compose.codeEditor.demo

import com.github.n34t0.compose.codeEditor.diagnostics.DiagnosticElement
import com.github.n34t0.compose.codeEditor.diagnostics.Severity
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteExisting
import kotlin.io.path.pathString
import kotlin.io.path.writeText

val projectList: List<ProjectDescription> = listOf(

    ProjectDescription(
        projectDirName = "diagnostic",
        relativeFilePath = "main.kt",
        code = """
            import java.util.*
            
            fun main() {
                vl list = ArrayList<String>();
                list.add(3);
                if (true) {
                    println(list[3]);
                    return
                } else {
                    {
                        val str = "str"
                    }
                    return
                }
                return
            }
        """.trimIndent(),
        diagnostics = listOf(
            DiagnosticElement(6, 8, 6, 12, "Condition is always 'true' ", Severity.INFO),
            DiagnosticElement(4, 4, 4, 6, "Unresolved reference: vl", Severity.ERROR),
            DiagnosticElement(4, 12, 4, 13, "Expecting an element", Severity.WARNING),
            DiagnosticElement(4, 23, 4, 24, "Expecting an element", Severity.WARNING),
            DiagnosticElement(4, 30, 4, 31, "Expecting an element", Severity.WARNING),
            DiagnosticElement(5, 4, 5, 8, "Unresolved reference: list", Severity.ERROR),
            DiagnosticElement(7, 16, 7, 20, "Unresolved reference: list", Severity.ERROR),
            DiagnosticElement(15, 4, 15, 10, "Unreachable code", Severity.WARNING),
            DiagnosticElement(11, 16, 11, 19, "Variable 'str' is never used", Severity.INFO),
            DiagnosticElement(10, 8, 12, 9, "The lambda expression is unused. If you mean a block, you can use 'run { ... }'",
                Severity.WARNING)
        )
    ),

    ProjectDescription(
        projectDirName = "tempProject",
        relativeFilePath = "project${File.separator}main.kt",
        code = """
            package project
            
            import java.util.*
            
            fun main() {
                val stub = Stub()
                stub.
                val ops1 = Ops()
                val ops2 = Ops()
                ops1..ops2
                ops1[4]
                ops1(ops1())
                val b = ops1 == ops2
            }
            """.trimIndent(),
    ),

    ProjectDescription(
        projectDirName = "tempProject",
        relativeFilePath = "project${File.separator}stub.kt",
        code = """
            package project
            
            class Stub {
                val value = 42
            }
            
            class Ops {
                operator fun rangeTo(o: Ops) {}
                operator fun get(i: Int) {}
                operator fun invoke(i: Int): Int = i
                override operator fun equals(o: Any?): Boolean = true
            }
            """.trimIndent(),
    ),

    ProjectDescription(
        relativeFilePath = "tempMain.kt"
    )

)

fun clearProjectList() = projectList.forEach(ProjectDescription::delete)

fun getDiagnostics(file: String) = projectList.firstOrNull { it.file == file }?.diagnostics ?: emptyList()

class ProjectDescription(
    val projectDirName: String? = null,
    val relativeFilePath: String,
    val code: String = "",
    val diagnostics: List<DiagnosticElement> = emptyList()
) {

    private val projectDirPath: Path = Path.of(System.getProperty("java.io.tmpdir"), projectDirName ?: "")
    private val filePath: Path = projectDirPath.resolve(relativeFilePath)
    val file: String = filePath.pathString
    val projectDir: String? = if (projectDirName == null) null else projectDirPath.pathString

    init {
        projectDirPath.createDirectories()
        filePath.parent.createDirectories()
        filePath.writeText(code)
    }

    fun delete() {
        filePath.deleteExisting()
        var parentPath = filePath.parent
        while (parentPath != projectDirPath) {
            if (parentPath.isEmpty()) {
                parentPath.deleteExisting()
                parentPath = parentPath.parent
            } else {
                break
            }
        }
        if (projectDirName != null && projectDirPath.isEmpty()) projectDirPath.deleteExisting()
    }

    private fun Path.isEmpty(): Boolean {
        return Files.list(this).use {
            it.findFirst().isEmpty
        }
    }
}
