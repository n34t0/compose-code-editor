package io.github.n34t0.compose.codeEditor.editor

import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.areAnyPressed
import androidx.compose.ui.input.pointer.isCtrlPressed
import androidx.compose.ui.input.pointer.isMetaPressed
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.unit.IntOffset
import io.github.n34t0.compose.codeEditor.ProjectFile
import io.github.n34t0.compose.codeEditor.codecompletion.CodeCompletionState
import io.github.n34t0.compose.codeEditor.diagnostics.DiagnosticElement
import io.github.n34t0.compose.codeEditor.diagnostics.DiagnosticState
import io.github.n34t0.compose.codeEditor.editor.draw.DrawState
import io.github.n34t0.compose.codeEditor.editor.text.TextState
import io.github.n34t0.compose.codeEditor.editor.tooltip.EditorTooltipState
import io.github.n34t0.compose.codeEditor.gtd.GtdState
import io.github.n34t0.compose.codeEditor.keyevent.KeyEventHandlerImpl
import io.github.n34t0.compose.codeEditor.search.SearchState
import io.github.n34t0.compose.codeEditor.statusbar.BusyState
import io.github.n34t0.compose.fork.DesktopPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Stable
internal class EditorState(
    val projectFile: ProjectFile,
    private val scope: CoroutineScope,
    onOuterGotoDeclaration: (String, String, Int) -> Unit
) {
    val busyState = BusyState()
    val textState = TextState(projectFile.load())
    val scrollState = ScrollState(0)
    val layoutOffset = OffsetState()
    val cursorPosition = OffsetState()
    val ccState = CodeCompletionState(scope, textState::paste)
    val drawState = DrawState(textState)
    val diagnosticState = DiagnosticState(scope, textState, drawState)
    val searchState = SearchState(textState, drawState)
    val tooltipState = EditorTooltipState(textState, layoutOffset)
    val gtdState = GtdState(
        scope = scope,
        projectFile = projectFile,
        localGoto = ::localGotoDeclaration,
        outerGoto = onOuterGotoDeclaration,
        tooltipState = tooltipState,
        busyState = busyState
    )

    val focusRequester = FocusRequester()
    private var ctrlPressed = false
    private var metaPressed = false
    private val isMacOS = DesktopPlatform.Current == DesktopPlatform.MacOS
    private fun modKeyPressed() = (isMacOS && metaPressed) || ctrlPressed

    private val cursorPositionAdjustedWithScroll by derivedStateOf {
        cursorPosition.value.copy(y = cursorPosition.value.y + scrollState.value)
    }

    val diagnosticMessagesUnderCaret by derivedStateOf {
        diagnosticState.getDiagnosticMessagesForOffset(textState.caretOffset)
    }

    val diagnosticMessagesUnderCursor by derivedStateOf {
        diagnosticState.getDiagnosticMessagesForOffset(
            textState.getOffsetForPosition(cursorPositionAdjustedWithScroll),
            ".\n"
        )
    }

    @OptIn(ExperimentalComposeUiApi::class)
    val cursorPointerIcon by derivedStateOf {
        if (textState.highlightedReferenceRanges.isNotEmpty()) PointerIcon.Hand
        else PointerIcon.Text
    }

    fun setDiagnostics(list: List<DiagnosticElement>) = diagnosticState.updateList(list)

    fun onScroll(offset: Float) {
        val intOffset = offset.roundToInt()
        if (scrollState.value != intOffset) {
            layoutOffset.setY(-intOffset)
            scope.launch {
                scrollState.scrollTo(intOffset)
            }
            tooltipState.hide()
        }
    }

    fun onLineNumbersWidthChange(width: Int) {
        if (layoutOffset.value.x != width) layoutOffset.setX(width)
    }

    fun onClick(pointerEvent: PointerEvent) {
        if (pointerEvent.buttons.areAnyPressed) {
            ccState.hide()
            diagnosticState.tooltipState.hide()
            tooltipState.hide()
            if (pointerEvent.buttons.isPrimaryPressed) {
                if (isMacOS) {
                    if (pointerEvent.keyboardModifiers.isMetaPressed) {
                        metaPressed = true
                        gtdState.goto()
                    } else {
                        metaPressed = false
                    }
                } else {
                    if (pointerEvent.keyboardModifiers.isCtrlPressed) {
                        ctrlPressed = true
                        gtdState.goto()
                    } else {
                        ctrlPressed = false
                    }
                }
            }
        }
    }

    fun onCursorMove(offset: IntOffset) {
        cursorPosition.set(offset)
        tooltipState.hide()
        if (modKeyPressed()) {
            val caretOffset = textState.getOffsetForPosition(cursorPositionAdjustedWithScroll)
            if (!gtdState.initialElementContains(caretOffset)) {
                gtdState.runGtdDataGetter(
                    text = textState.text,
                    offset = caretOffset,
                    range = textState.getElementTextRange(caretOffset),
                    highlight = {
                        if (it != null) textState.highlightReferences(it)
                        else textState.clearReferences()
                    }
                )
            }
        } else {
            gtdState.stop()
            textState.clearReferences()
        }
    }

    private fun localGotoDeclaration(offset: Int) {
        textState.caretOffset = offset
    }

    val previewKeyEventHandler = KeyEventHandlerImpl("Editor preview")
        .addPreEventHandler(ccState.getPreviewKeyEventHandler())
        .addPreEventHandler(searchState.getPreviewKeyEventHandler())
        .onAnyKey {
            gtdState.stop()
            diagnosticState.tooltipState.hide()
            tooltipState.hide()
        }.apply {
            if (isMacOS) {
                this
                    .onMetaDown {
                        metaPressed = true
                    }
                    .onMetaUp {
                        metaPressed = false
                        textState.clearReferences()
                    }
                    .onMetaB {
                        if (!textState.isSelected()) { // todo: add support for selected text
                            gtdState.goto(
                                textState.text,
                                textState.caretOffset,
                                textState.getElementTextRange(textState.caretOffset)
                            )
                        }
                    }
            } else {
                this
                    .onCtrlDown {
                        ctrlPressed = true
                    }
                    .onCtrlUp {
                        ctrlPressed = false
                        textState.clearReferences()
                    }
                    .onCtrlB {
                        if (!textState.isSelected()) { // todo: add support for selected text
                            gtdState.goto(
                                textState.text,
                                textState.caretOffset,
                                textState.getElementTextRange(textState.caretOffset)
                            )
                        }
                    }
            }
        }
        .onCtrlSpace {
            if (!textState.isSelected()) {  // todo: add support for selected text
                ccState.start(getPrefix = textState::getPrefixBeforeCaret) {
                    projectFile.getCodeCompletionList(textState.text, textState.caretOffset)
                }
            }
        }
        .onCtrlF {
            searchState.show(textState.getSelectedText(), focusRequester)
        }
        .onTab(textState::insertIndent)
        .onShiftTab(textState::removeIndent)
        .onEnter(textState::newLineWithIndent)
        .onHome(textState::moveCaretToLineStartWithIndent)
        .also {
            textState.pairChars.forEach { (openChar, closeChar) ->
                it.onPairChars(openChar, closeChar, textState::insertPair, textState::checkForRepeating)
            }
        }

    val keyEventHandler = KeyEventHandlerImpl("Editor post")
        .addPreEventHandler(ccState.getPreKeyEventHandler())
        .onBackspace {
            ccState.restart(textState::getPrefixBeforeCaret) {
                projectFile.getCodeCompletionList(textState.text, textState.caretOffset)
            }
        }
        .onCharacter {
            ccState.startWithDelay(getPrefix = textState::getPrefixBeforeCaret) {
                projectFile.getCodeCompletionList(textState.text, textState.caretOffset)
            }
        }
        .addPostEventHandler(ccState.getPostKeyEventHandler())

}
