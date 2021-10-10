package com.github.n34t0.compose.codeEditor.editor.text

import com.github.n34t0.compose.codeEditor.AppTheme
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun LineNumbers(
    textFieldState: EditorTextFieldState,
    scrollState: ScrollState,
    onWidthChange: (Int) -> Unit = {}
) {
    val lineNumbersState = remember { LineNumbersState() }

    with(LocalDensity.current) {
        Surface(
            color = AppTheme.colors.backgroundMedium,
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = lineNumbersState.getLineNumbersText(textFieldState.lineCount),
                    modifier = Modifier
                        .fillMaxHeight()
                        .widthIn(min = 25.dp)
                        .drawBehind { // right border
                            clipRect {
                                drawLine(
                                    brush = SolidColor(AppTheme.colors.borderMedium),
                                    cap = StrokeCap.Square,
                                    start = Offset(x = size.width, y = 0f),
                                    end = Offset(x = size.width, y = size.height)
                                )
                            }
                        }
                        .padding(Paddings.lineNumbersPadding)
                        .verticalScroll(scrollState)
                        .onSizeChanged {
                            onWidthChange(it.width
                                + (Paddings.lineNumbersHorizontalPaddingSum
                                + Paddings.textFieldLeftPadding).roundToPx())
                        },
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Right
                )
            }
        }
    }

}
