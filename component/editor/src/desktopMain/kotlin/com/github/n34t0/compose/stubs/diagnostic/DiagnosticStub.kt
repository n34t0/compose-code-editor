package com.github.n34t0.compose.stubs.diagnostic

data class DiagnosticStub(
    val startLine: Int,
    val startCharacter: Int,
    val endLine: Int,
    val endCharacter: Int,
    val message: String,
    val severity: SeverityStub
)

