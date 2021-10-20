package com.github.n34t0.compose.codeEditor.codecompletion.filters

import com.github.n34t0.compose.codeEditor.codecompletion.CodeCompletionIndexedElement

internal class Matches : Filter {
    override fun matches(
        element: CodeCompletionIndexedElement,
        prefix: String,
        ignoreCase: Boolean
    ) = element.element.name.equals(prefix, ignoreCase)
}
