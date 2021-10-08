package com.github.n34t0.compose.codeEditor.diagnostics

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.github.n34t0.compose.codeEditor.LogMarkers
import com.github.n34t0.compose.codeEditor.editor.tooltip.Tooltip
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DiagnosticTooltip(
    message: String,
    tooltipState: DiagnosticTooltipState
) {
    logger.trace(LogMarkers.recomposition) { "Recomposition DiagnosticTooltip" }

    tooltipState.setMessage(message)

    if (tooltipState.isVisible) {
        Tooltip(
            message = tooltipState.message,
            positionProvider = tooltipState.placement.positionProvider(),
            onDismissRequest = tooltipState::hide
        )
    }
}

