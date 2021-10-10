package com.github.n34t0.compose.codeEditor.diagnostics

data class DiagnosticElement(
    val startLine: Int,
    val startCharacter: Int,
    val endLine: Int,
    val endCharacter: Int,
    val message: String,
    val severity: Severity
)
