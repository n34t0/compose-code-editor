@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package io.github.n34t0.compose.codeEditor.editor

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import io.github.n34t0.compose.codeEditor.keyevent.KeyEventHandlerImpl
import io.github.n34t0.compose.codeEditor.keyevent.KeyModifier.ANY_MODIFIERS
import io.github.n34t0.compose.codeEditor.keyevent.KeyModifier.CTRL
import io.github.n34t0.compose.codeEditor.keyevent.KeyModifier.META
import io.github.n34t0.compose.codeEditor.keyevent.KeyModifier.NO_MODIFIERS
import io.github.n34t0.compose.codeEditor.keyevent.KeyModifier.SHIFT

internal fun KeyEventHandlerImpl.onAnyKey(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(keyModifiers = intArrayOf(ANY_MODIFIERS)) {
        action()
        false
    }
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onCtrlDown(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.CtrlLeft, CTRL) {
        action()
        false
    }
    addKeyDownAction(Key.CtrlRight, CTRL) {
        action()
        false
    }
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onCtrlUp(action: () -> Unit): KeyEventHandlerImpl {
    addKeyUpAction(Key.CtrlLeft) {
        action()
        false
    }
    addKeyUpAction(Key.CtrlRight) {
        action()
        false
    }
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onMetaDown(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.MetaLeft, META) {
        action()
        false
    }
    addKeyDownAction(Key.MetaRight, META) {
        action()
        false
    }
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onMetaUp(action: () -> Unit): KeyEventHandlerImpl {
    addKeyUpAction(Key.MetaLeft) {
        action()
        false
    }
    addKeyUpAction(Key.MetaRight) {
        action()
        false
    }
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onCtrlSpace(action: () -> Unit): KeyEventHandlerImpl {
    addKeyUpAction(Key.Spacebar, CTRL) {
        action()
        true
    }
    addKeyDownAction(Key.Spacebar, CTRL)
    addKeyTypeAction(' ', CTRL)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onCtrlF(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.F, CTRL) {
        action()
        false
    }
    addKeyTypeAction('f', CTRL)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onCtrlB(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.B, CTRL) {
        action()
        false
    }
    addKeyTypeAction('b', CTRL)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onMetaB(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.B, META) {
        action()
        false
    }
    addKeyTypeAction('b', META)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onTab(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.Tab) {
        action()
        true
    }
    addKeyTypeAction('\t')
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onShiftTab(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.Tab, SHIFT) {
        action()
        true
    }
    addKeyTypeAction('\t', SHIFT)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onEnter(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.Enter) {
        action()
        true
    }
    addKeyTypeAction('\n')
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onHome(action: () -> Boolean): KeyEventHandlerImpl {
    addKeyDownAction(Key.MoveHome) {
        action()
    }
    return this
}

internal fun KeyEventHandlerImpl.onPairChars(
    openChar: Char,
    closeChar: Char,
    insertPair: (Char) -> Unit,
    checkForRepeating: (Char, Boolean) -> Boolean
): KeyEventHandlerImpl {
    if (openChar != closeChar) {
        addKeyTypeAction(openChar, NO_MODIFIERS, SHIFT) {
            insertPair(openChar)
            true
        }
        addKeyTypeAction(closeChar, NO_MODIFIERS, SHIFT) {
            checkForRepeating(closeChar, true)
            true
        }
    } else {
        addKeyTypeAction(openChar, NO_MODIFIERS, SHIFT) {
            if (!checkForRepeating(openChar, false)) {
                insertPair(openChar)
            }
            true
        }
    }
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
internal fun KeyEventHandlerImpl.onBackspace(action: () -> Unit): KeyEventHandlerImpl {
    addKeyUpAction(Key.Backspace) {
        action()
        true
    }
    return this
}

internal fun KeyEventHandlerImpl.onCharacter(action: () -> Unit): KeyEventHandlerImpl {
    addKeyUpAction(keyModifiers = intArrayOf(NO_MODIFIERS, SHIFT)) {
        val keyChar = it.nativeKeyEvent.keyChar
        // todo: add support for other operators like ::
        if (keyChar.isLetterOrDigit() || keyChar == '.' || keyChar == '_') {
            action()
            true
        } else {
            false
        }
    }
    return this
}
