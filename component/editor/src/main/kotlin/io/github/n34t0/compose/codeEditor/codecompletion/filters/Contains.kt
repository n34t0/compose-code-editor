package io.github.n34t0.compose.codeEditor.codecompletion.filters

import io.github.n34t0.compose.codeEditor.codecompletion.CodeCompletionIndexedElement

internal class Contains : Filter {
    override fun matches(
        element: CodeCompletionIndexedElement,
        prefix: String,
        ignoreCase: Boolean
    ): Boolean = element.element.name.contains(prefix, ignoreCase)
}
