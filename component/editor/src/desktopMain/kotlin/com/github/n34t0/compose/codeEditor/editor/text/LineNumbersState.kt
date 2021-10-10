package com.github.n34t0.compose.codeEditor.editor.text

import androidx.compose.runtime.Stable

@Stable
internal class LineNumbersState {

    private var lineNumbersText = "1\n"
    private var prevLineCount = 1

    fun getLineNumbersText(newLineCount: Int): String {
        lineNumbersText = when {
            newLineCount == prevLineCount -> lineNumbersText

            newLineCount > prevLineCount ->
                appendLineNumbers(prevLineCount, newLineCount)

            else ->
                trimLineNumbers(prevLineCount, newLineCount)
        }
        prevLineCount = newLineCount
        return lineNumbersText
    }

    private fun appendLineNumbers(prevLineCount: Int, newLineCount: Int): String {
        val sb = StringBuilder(lineNumbersText)
        ((prevLineCount + 1)..newLineCount).forEach {
            sb.append(it).append('\n')
        }
        return sb.toString()
    }

    private fun trimLineNumbers(prevLineCount: Int, newLineCount: Int): String {
        val newLineCountNumDigits = getNumDigits(newLineCount)
        var prevLineCountNumDigits = getNumDigits(prevLineCount)
        val numCharsToTrim =
            when {
                newLineCountNumDigits == prevLineCountNumDigits -> {
                    // number of lines * chars per line
                    (prevLineCount - newLineCount) * (newLineCountNumDigits + 1)
                }

                else -> {
                    var numLines = 1
                    repeat(prevLineCountNumDigits - 1) { numLines *= 10 }
                    var charsNum = (prevLineCount - numLines + 1) * (prevLineCountNumDigits + 1)
                    prevLineCountNumDigits--
                    while (prevLineCountNumDigits > newLineCountNumDigits) {
                        charsNum += (numLines - numLines / 10) * (prevLineCountNumDigits + 1)
                        numLines /= 10
                        prevLineCountNumDigits--
                    }
                    charsNum + (numLines - newLineCount - 1) * (newLineCountNumDigits + 1)
                }
            }
        return lineNumbersText.substring(0, lineNumbersText.length - numCharsToTrim)
    }

    private fun getNumDigits(i: Int): Int {
        var j = 1
        var d = 10
        while (i >= d) {
            j++
            d *= 10
        }
        return j
    }
}
