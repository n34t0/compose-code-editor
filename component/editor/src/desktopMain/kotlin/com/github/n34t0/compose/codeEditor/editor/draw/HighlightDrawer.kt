package com.github.n34t0.compose.codeEditor.editor.draw

import androidx.compose.ui.graphics.drawscope.DrawScope

internal abstract class HighlightDrawer(
    private val zIndex: Int
) : Comparable<HighlightDrawer> {

    abstract fun draw(lineSegments: List<LineSegment>, drawScope: DrawScope)

    override fun compareTo(other: HighlightDrawer): Int {
        val comp = zIndex.compareTo(other.zIndex)
        return if (comp != 0) comp
        else this.hashCode() - other.hashCode()
    }
}

