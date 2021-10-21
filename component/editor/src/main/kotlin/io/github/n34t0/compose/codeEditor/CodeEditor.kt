package io.github.n34t0.compose.codeEditor

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import io.github.n34t0.compose.codeEditor.diagnostics.DiagnosticElement
import io.github.n34t0.compose.codeEditor.editor.Editor
import io.github.n34t0.compose.codeEditor.editor.EditorState
import io.github.n34t0.compose.codeEditor.search.SearchBar
import io.github.n34t0.compose.codeEditor.statusbar.StatusBar
import io.github.n34t0.compose.stubs.PlatformStub
import io.github.n34t0.compose.stubs.isStub
import io.github.n34t0.platform.Project
import io.github.n34t0.platform.impl.IntellijPlatformWrapper

/**
 * This composable provides basic code editing functionality with support for autosuggestion of code,
 * search and go to declaration.
 *
 * @param projectFile The [ProjectFile] that contains information about the project and the file being edited.
 * @param modifier optional [Modifier].
 * @param diagnostics List of diagnostic messages to be rendered in the code.
 * @param onTextChange Called when code text is changed.
 */
@Composable
fun CodeEditor(
    projectFile: ProjectFile,
    modifier: Modifier,
    diagnostics: List<DiagnosticElement>,
    onTextChange: (String) -> Unit,
) {
    val scope = rememberCoroutineScope()

    val editorState = rememberSaveable {
        EditorState(
            projectFile = projectFile,
            scope = scope,
            onOuterGotoDeclaration = { _, _, _ -> }
        )
    }

    editorState.setDiagnostics(diagnostics)

    MaterialTheme(
        colors = AppTheme.colors.material,
        typography = AppTheme.typography.material
    ) {
        Surface {
            Column(modifier) {
                SearchBar(editorState.searchState)
                Editor(editorState, onTextChange, Modifier.weight(1f))
                StatusBar(editorState.diagnosticMessagesUnderCaret, editorState.busyState)
            }
        }
    }
}

/**
 * Returns an instance of the [ProjectFile].
 *
 * @param project The project to which the file belongs.
 * @param projectDir Path to the project folder.
 * @param absoluteFilePath Absolute path to the file.
 */
fun createProjectFile(
    project: Project,
    projectDir: String?,
    absoluteFilePath: String
) = ProjectFile(project, projectDir, absoluteFilePath)

/**
 * Returns an instance of the [Platform].
 */
fun createPlatformInstance() = if (!isStub) IntellijPlatformWrapper() else PlatformStub()
