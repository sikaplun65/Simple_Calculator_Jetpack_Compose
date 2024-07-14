package com.example.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.properties.Delegates

class CalculatorMainScreenViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set

    private var isChangeDigit = true

    fun onEvent(event: CalculatorEvent) {
        when (event) {
            is CalculatorEvent.Number -> enterDigit(event.digit)
            is CalculatorEvent.Operation -> enterOperation(event.operation)
            CalculatorEvent.Calculate -> calculate()
            CalculatorEvent.Clear -> clearScreen()
            CalculatorEvent.FullClear -> fullClearScreen()
            CalculatorEvent.Decimal -> enterDecimal()
            CalculatorEvent.Delete -> performDeletion()
            CalculatorEvent.NumberInversion -> inversionPositiveDigitToNegativeDigitAndViceVersa()
            CalculatorEvent.PercentCalculation -> performPercentCalculation()
            CalculatorEvent.Brackets -> enterBrackets()
        }
    }

    private fun enterBrackets() {

        if (state.operation == null || state.secondOperand.isNotEmpty()) return
        calculate()
        state = state.copy(
            isInBrackets = !state.isInBrackets,
            secondOperand = state.firstOperandInBrackets.ifEmpty { "" },
            firstOperandInBrackets = ""
        )
        if (state.secondOperand.isNotEmpty()) {
            val number = state.secondOperand.toDouble()
            if (number < 0) {
                state = state.copy(isSecondOperandNegative = true)
            }
        }
    }

    private fun calculate() {
        var firstOperand by Delegates.notNull<BigDecimal>()
        var secondOperand by Delegates.notNull<BigDecimal>()
        var operation by Delegates.notNull<CalculatorOperation>()

        if (state.isInBrackets) {
            if (state.firstOperandInBrackets.isEmpty() || state.secondOperandInBrackets.isEmpty()) return

            firstOperand = state.firstOperandInBrackets.toBigDecimal()
            secondOperand = state.secondOperandInBrackets.toBigDecimal()
            operation = state.operationInBrackets!!
        } else {
            if (state.firstOperand.isEmpty() || state.secondOperand.isEmpty()) return

            firstOperand = state.firstOperand.toBigDecimal()
            secondOperand = state.secondOperand.toBigDecimal()
            operation = state.operation!!
        }

        if (operation == CalculatorOperation.Divide && secondOperand == BigDecimal.valueOf(0)) {
            state = state.copy(isErrorCalculate = true)
            return
        }

        val result = getStringFromNumber(performCalculate(operation,firstOperand,secondOperand))

        state = if (state.isInBrackets) state.copy(
            firstOperandInBrackets = result,
            operationInBrackets = null,
            secondOperandInBrackets = ""
        ) else state.copy(
            firstOperand = result,
            operation = null,
            secondOperand = ""
        )

        if (state.secondOperand.isEmpty()) {
            state = state.copy(isSecondOperandNegative = false)
        }
        if (state.firstOperandInBrackets.isNotEmpty()) {
            state =
                if (state.firstOperandInBrackets.contains('-')) {
                    state.copy(isFirstOperandInBracketsNegative = true)
                } else {
                    state.copy(isFirstOperandInBracketsNegative = false)
                }
        }
        if (state.secondOperandInBrackets.isEmpty()) {
            state = state.copy(isSecondOperandInBracketsNegative = false)
        }

        isChangeDigit = false
    }

    private fun performCalculate(
        operation: CalculatorOperation,
        firstOperand: BigDecimal,
        secondOperand: BigDecimal
    ): BigDecimal = when (operation) {
        CalculatorOperation.Addition -> firstOperand.plus(secondOperand)
        CalculatorOperation.Divide -> firstOperand.setScale(9).div(secondOperand)
        CalculatorOperation.Multiply -> firstOperand.times(secondOperand)
        CalculatorOperation.Subtract -> firstOperand.minus(secondOperand)
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.isErrorCalculate) return
        calculate()
        when {
            state.isInBrackets -> {
                if (state.firstOperandInBrackets.isEmpty() || state.firstOperandInBrackets == "0,") return
                state = state.copy(operationInBrackets = operation)
            }

            !state.isInBrackets -> {
                if (state.firstOperand.isEmpty() || state.firstOperand == "0,") return
                state = state.copy(operation = operation)
            }
        }
        isChangeDigit = true
    }

    private fun enterDigit(digit: Int) {
        if (!isChangeDigit || state.isErrorCalculate) return

        when {
            state.isInBrackets -> {
                when {
                    state.operationInBrackets == null && state.firstOperandInBrackets.length < MAX_LENGTH_DIGIT -> {
                        state = if (state.firstOperandInBrackets == "0") state.copy(
                            firstOperandInBrackets = digit.toString()
                        ) else state.copy(
                            firstOperandInBrackets = state.firstOperandInBrackets + digit
                        )
                    }

                    state.operationInBrackets != null && state.secondOperandInBrackets.length < MAX_LENGTH_DIGIT -> {
                        state = if (state.secondOperandInBrackets == "0") state.copy(
                            secondOperandInBrackets = digit.toString()
                        ) else state.copy(
                            secondOperandInBrackets = state.secondOperandInBrackets + digit
                        )
                    }
                }
            }

            !state.isInBrackets -> {
                when {
                    state.operation == null && state.firstOperand.length < MAX_LENGTH_DIGIT -> {
                        state = if (state.firstOperand == "0") state.copy(
                            firstOperand = digit.toString()
                        ) else state.copy(
                            firstOperand = state.firstOperand + digit
                        )
                    }

                    state.operation != null && state.secondOperand.length < MAX_LENGTH_DIGIT -> {
                        state = if (state.secondOperand == "0") state.copy(
                            secondOperand = digit.toString()
                        ) else state.copy(
                            secondOperand = state.secondOperand + digit
                        )
                    }
                }
            }
        }
    }

    private fun enterDecimal() {
        if (!isChangeDigit || state.isErrorCalculate) return
        when {
            state.isInBrackets -> {
                when {
                    state.operationInBrackets != null && !state.secondOperandInBrackets.contains(".") -> {
                        state =
                            state.copy(secondOperandInBrackets = if (state.secondOperandInBrackets.isEmpty()) "0." else state.secondOperandInBrackets + ".")
                    }

                    state.operationInBrackets == null && !state.firstOperandInBrackets.contains(".") -> {
                        state =
                            state.copy(firstOperandInBrackets = if (state.firstOperandInBrackets.isEmpty()) "0." else state.firstOperandInBrackets + ".")
                    }

                }
            }

            !state.isInBrackets -> {
                when {
                    state.operation != null && !state.secondOperand.contains(".") -> {
                        state =
                            state.copy(secondOperand = if (state.secondOperand.isEmpty()) "0." else state.secondOperand + ".")
                    }

                    state.operation == null && !state.firstOperand.contains(".") -> {
                        state =
                            state.copy(firstOperand = if (state.firstOperand.isEmpty()) "0." else state.firstOperand + ".")
                    }
                }
            }
        }
    }

    private fun performDeletion() {
        if (!isChangeDigit || state.isErrorCalculate) return

        if (state.isInBrackets) {
            when {
                state.secondOperandInBrackets.isNotEmpty() -> {
                    state = state.copy(
                        secondOperandInBrackets = state.secondOperandInBrackets.dropLast(1)
                    )
                    if (state.secondOperandInBrackets == "-" || state.firstOperandInBrackets == "0") {
                        state = state.copy(
                            secondOperandInBrackets = state.secondOperandInBrackets.dropLast(1),
                            isSecondOperandInBracketsNegative = false
                        )
                    }
                }

                state.operationInBrackets != null -> state =
                    state.copy(operationInBrackets = null)

                state.firstOperandInBrackets.isNotEmpty() -> {
                    state =
                        state.copy(firstOperandInBrackets = state.firstOperandInBrackets.dropLast(1))
                    if (state.firstOperandInBrackets == "-" || state.firstOperandInBrackets == "0") {
                        state = state.copy(
                            firstOperandInBrackets = state.firstOperandInBrackets.dropLast(1),
                            isFirstOperandInBracketsNegative = false
                        )
                    }
                    return
                }
            }
            if (state.firstOperandInBrackets.isEmpty()) {
                state = state.copy(isInBrackets = !state.isInBrackets)
            }
        } else {
            when {
                state.secondOperand.isNotEmpty() -> {
                    state = state.copy(secondOperand = state.secondOperand.dropLast(1))
                    if (state.secondOperand == "-" || state.secondOperand == "0") {
                        state = state.copy(
                            secondOperand = state.secondOperand.dropLast(1),
                            isSecondOperandNegative = false
                        )
                    }
                }

                state.operation != null -> state = state.copy(operation = null)

                state.firstOperand.isNotEmpty() -> {
                    state = state.copy(firstOperand = state.firstOperand.dropLast(1))
                    if (state.firstOperand == "-" || state.firstOperand == "0") {
                        state = state.copy(firstOperand = state.firstOperand.dropLast(1))
                    }
                }
            }
        }
    }

    private fun performPercentCalculation() {
        if (!isChangeDigit || state.isErrorCalculate) return

        when {
            state.isInBrackets -> {
                if (state.firstOperandInBrackets.isEmpty() || state.firstOperandInBrackets == "0.") return

                val onePercentFirstNum = state.firstOperandInBrackets.toDouble() / 100

                state = when (state.operationInBrackets) {
                    null -> {
                        state.copy(
                            firstOperandInBrackets = getStringFromNumber(onePercentFirstNum.toBigDecimal()))
                    }

                    else -> {
                        if (state.secondOperandInBrackets.isEmpty() || state.secondOperandInBrackets == "0.") return

                        val secondNum = state.secondOperandInBrackets.toDouble()

                        if (state.operationInBrackets == CalculatorOperation.Multiply || state.operationInBrackets == CalculatorOperation.Divide) {

                            val onePercentOfSecondNumber = secondNum / 100

                            state.copy(
                                secondOperandInBrackets = getStringFromNumber(onePercentOfSecondNumber.toBigDecimal())
                            )
                        }else {

                            val percentageOfSecondNumber = onePercentFirstNum * secondNum

                            state.copy(
                                secondOperandInBrackets = getStringFromNumber(percentageOfSecondNumber.toBigDecimal())
                            )
                        }
                    }
                }
            }

            !state.isInBrackets -> {
                if (state.firstOperand.isEmpty() || state.firstOperand == "0.") return

                val onePercentFirstNum = state.firstOperand.toDouble() / 100

                state = when (state.operation) {
                    null -> {
                        state.copy(
                            firstOperand = getStringFromNumber(onePercentFirstNum.toBigDecimal())
                        )
                    }

                    else -> {
                        if (state.secondOperand.isEmpty() || state.secondOperand == "0.") return

                        val secondNum = state.secondOperand.toDouble()

                        if (state.operation == CalculatorOperation.Multiply || state.operation == CalculatorOperation.Divide) {

                            val onePercentOfSecondNumber = secondNum / 100

                            state.copy(
                                secondOperand = getStringFromNumber(onePercentOfSecondNumber.toBigDecimal())
                            )
                        } else {

                            val percentageOfSecondNumber = onePercentFirstNum * secondNum

                            state.copy(
                                secondOperand = getStringFromNumber(percentageOfSecondNumber.toBigDecimal())
                            )
                        }
                    }
                }
            }
        }
    }

    private fun inversionPositiveDigitToNegativeDigitAndViceVersa() {
        when {
            state.isInBrackets -> {
                when {
                    state.operationInBrackets == null && state.firstOperandInBrackets.isNotEmpty() && state.firstOperandInBrackets != "0." -> {
                        val number = -state.firstOperandInBrackets.toBigDecimal()
                        val isNegative = number < BigDecimal.valueOf(0)
                        state = state.copy(
                            firstOperandInBrackets = getStringFromNumber(number),
                            isFirstOperandInBracketsNegative = isNegative
                        )
                    }

                    state.operationInBrackets != null && state.secondOperandInBrackets.isNotEmpty() && state.secondOperandInBrackets != "0." -> {
                        val number = -state.secondOperandInBrackets.toBigDecimal()
                        val isNegative = number < BigDecimal(0)
                        state = state.copy(
                            secondOperandInBrackets = getStringFromNumber(number),
                            isSecondOperandInBracketsNegative = isNegative
                        )
                    }
                }
            }

            !state.isInBrackets -> {
                when {
                    state.operation == null && state.firstOperand.isNotEmpty() && state.firstOperand != "0." -> {
                        val number = -state.firstOperand.toBigDecimal()
                        state =
                            state.copy(
                                firstOperand = getStringFromNumber(number),
                            )
                    }

                    state.operation != null && state.secondOperand.isNotEmpty() && state.secondOperand != "0." -> {
                        val number = -state.secondOperand.toBigDecimal()
                        val isNegative = number < BigDecimal.valueOf(0)
                        state =
                            state.copy(
                                secondOperand = getStringFromNumber(number),
                                isSecondOperandNegative = isNegative
                            )
                    }
                }
            }
        }
    }

    private fun clearScreen() {
        if (state.isInBrackets && state.firstOperandInBrackets.isEmpty()) {
            state = state.copy(isInBrackets = !state.isInBrackets)
            return
        }
        state = when {
            state.isInBrackets -> {
                if (state.isErrorCalculate) state.copy(secondOperandInBrackets = "")
                else state.copy(
                    firstOperandInBrackets = "",
                    secondOperandInBrackets = "",
                    operationInBrackets = null,
                    isFirstOperandInBracketsNegative = false,
                    isSecondOperandInBracketsNegative = false,
                )
            }

            else -> {
                if (state.isErrorCalculate) state.copy(secondOperand = "") else CalculatorState()
            }
        }
        isChangeDigit = true
        state = state.copy(isErrorCalculate = false)
    }

    private fun fullClearScreen() {
        state = CalculatorState()
        isChangeDigit = true
    }

    private fun getStringFromNumber(number: BigDecimal): String {
        return number
            .setScale(9,RoundingMode.CEILING)
            .toString()
            .dropLastWhile { it == '0' }
            .dropLastWhile { it == '.' }
    }

    companion object {
        const val MAX_LENGTH_DIGIT = 12
    }
}