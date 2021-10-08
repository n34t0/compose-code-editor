@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.github.n34t0.compose.codeEditor.editor

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.input.key.Key
import com.github.n34t0.compose.codeEditor.keyevent.KeyEventHandlerImpl
import com.github.n34t0.compose.codeEditor.keyevent.KeyModifier.ANY_MODIFIERS
import com.github.n34t0.compose.codeEditor.keyevent.KeyModifier.CTRL
import com.github.n34t0.compose.codeEditor.keyevent.KeyModifier.META
import com.github.n34t0.compose.codeEditor.keyevent.KeyModifier.NO_MODIFIERS
import com.github.n34t0.compose.codeEditor.keyevent.KeyModifier.SHIFT

fun KeyEventHandlerImpl.onAnyKey(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(keyModifiers = intArrayOf(ANY_MODIFIERS)) {
        action()
        false
    }
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEventHandlerImpl.onCtrlDown(action: () -> Unit): KeyEventHandlerImpl {
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
fun KeyEventHandlerImpl.onCtrlUp(action: () -> Unit): KeyEventHandlerImpl {
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
fun KeyEventHandlerImpl.onMetaDown(action: () -> Unit): KeyEventHandlerImpl {
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
fun KeyEventHandlerImpl.onMetaUp(action: () -> Unit): KeyEventHandlerImpl {
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
fun KeyEventHandlerImpl.onCtrlSpace(action: () -> Unit): KeyEventHandlerImpl {
    addKeyUpAction(Key.Spacebar, CTRL) {
        action()
        true
    }
    addKeyDownAction(Key.Spacebar, CTRL)
    addKeyTypeAction(' ', CTRL)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEventHandlerImpl.onCtrlF(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.F, CTRL) {
        action()
        false
    }
    addKeyTypeAction('f', CTRL)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEventHandlerImpl.onCtrlB(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.B, CTRL) {
        action()
        false
    }
    addKeyTypeAction('b', CTRL)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEventHandlerImpl.onMetaB(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.B, META) {
        action()
        false
    }
    addKeyTypeAction('b', META)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEventHandlerImpl.onTab(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.Tab) {
        action()
        true
    }
    addKeyTypeAction('\t')
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEventHandlerImpl.onShiftTab(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.Tab, SHIFT) {
        action()
        true
    }
    addKeyTypeAction('\t', SHIFT)
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEventHandlerImpl.onEnter(action: () -> Unit): KeyEventHandlerImpl {
    addKeyDownAction(Key.Enter) {
        action()
        true
    }
    addKeyTypeAction('\n')
    return this
}

@OptIn(ExperimentalComposeUiApi::class)
fun KeyEventHandlerImpl.onHome(action: () -> Boolean): KeyEventHandlerImpl {
    addKeyDownAction(Key.MoveHome) {
        action()
    }
    return this
}

fun KeyEventHandlerImpl.onPairChars(
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
fun KeyEventHandlerImpl.onBackspace(action: () -> Unit): KeyEventHandlerImpl {
    addKeyUpAction(Key.Backspace) {
        action()
        true
    }
    return this
}

fun KeyEventHandlerImpl.onCharacter(action: () -> Unit): KeyEventHandlerImpl {
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
