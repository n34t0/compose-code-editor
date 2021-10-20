package com.github.n34t0.compose.codeEditor.codecompletion.filters

import com.github.n34t0.compose.codeEditor.codecompletion.CodeCompletionIndexedElement

internal interface Filter {
    fun matches(
        element: CodeCompletionIndexedElement,
        prefix: String,
        ignoreCase: Boolean = true
    ): Boolean
}
