package io.github.n34t0.compose.codeEditor.codecompletion.filters

import io.github.n34t0.compose.codeEditor.codecompletion.CodeCompletionIndexedElement

internal class StartsWith : Filter {
    override fun matches(
        element: CodeCompletionIndexedElement,
        prefix: String,
        ignoreCase: Boolean
    ) = element.element.name.startsWith(prefix, ignoreCase)
}
