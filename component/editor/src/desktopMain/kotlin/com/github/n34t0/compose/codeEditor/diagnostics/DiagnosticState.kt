package com.github.n34t0.compose.codeEditor.diagnostics

import AppTheme
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import com.github.n34t0.compose.codeEditor.editor.draw.DrawState
import com.github.n34t0.compose.codeEditor.editor.draw.LineSegment
import com.github.n34t0.compose.codeEditor.editor.text.TextState
import kotlinx.coroutines.CoroutineScope
import mu.KotlinLogging
import com.github.n34t0.compose.stubs.diagnostic.DiagnosticStub
import com.github.n34t0.compose.stubs.diagnostic.SeverityStub

private val logger = KotlinLogging.logger {}

@Stable
class DiagnosticState(
    diagnostics: List<DiagnosticStub>,
    scope: CoroutineScope,
    textState: TextState,
    private val drawState: DrawState
) {

    val tooltipState = DiagnosticTooltipState(scope)

    val list = diagnostics.map {
        DiagnosticElementState(
            message = it.message,
            severity = when (it.severity) {
                SeverityStub.INFO -> Severity.INFO
                SeverityStub.WARNING -> Severity.WARNING
                SeverityStub.ERROR -> Severity.ERROR
            },
            startLine = it.startLine,
            startCharacter = it.startCharacter,
            endLine = it.endLine,
            endCharacter = it.endCharacter,
            textState = textState
        )
    }.sortedBy { it.severity }

    private val errorDrawer = drawState.createWavedLineDrawer(AppTheme.colors.severityError, zIndex = 19)
    private val warningDrawer = drawState.createBackgroundDrawer(AppTheme.colors.severityWarning, zIndex = 0)
    private val infoDrawer = drawState.createWavedLineDrawer(AppTheme.colors.severityInfo, zIndex = 18)

    init {
        drawState.putLineSegments(
            infoDrawer,
            derivedStateOf { mapToLineSegments(Severity.INFO) }
        )
        drawState.putLineSegments(
            warningDrawer,
            derivedStateOf { mapToLineSegments(Severity.WARNING) }
        )
        drawState.putLineSegments(
            errorDrawer,
            derivedStateOf { mapToLineSegments(Severity.ERROR) }
        )
    }

    private fun mapToLineSegments(severity: Severity): List<LineSegment> {
        return list
            .filter { it.severity == severity }
            .flatMap {
                drawState.getLineSegments(
                    startCharacter = it.startCharacter,
                    endCharacter = it.endCharacter,
                    startLine = it.startLine,
                    endLine = it.endLine
                )
            }
    }

    fun getDiagnosticMessagesForOffset(
        offset: Int,
        delimiter: String = ". ",
        ending: String = "."
    ): String =
        if (offset == -1) ""
        else {
            val filteredList = list
                .filter { offset >= it.startTextOffset && offset <= it.endTextOffset }
            when (filteredList.size) {
                0 -> ""
                1 -> filteredList[0].message
                else -> filteredList.joinToString(delimiter) { it.message } + ending
            }
        }

}
