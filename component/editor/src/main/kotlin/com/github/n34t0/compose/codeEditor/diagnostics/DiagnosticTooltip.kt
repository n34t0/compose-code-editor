package com.github.n34t0.compose.codeEditor.diagnostics

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import com.github.n34t0.compose.codeEditor.editor.tooltip.Tooltip

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
internal fun DiagnosticTooltip(
    message: String,
    tooltipState: DiagnosticTooltipState
) {
    tooltipState.setMessage(message)

    if (tooltipState.isVisible) {
        Tooltip(
            message = tooltipState.message,
            positionProvider = tooltipState.placement.positionProvider(),
            onDismissRequest = tooltipState::hide
        )
    }
}

