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
    var isErrorCalculate by mutableStateOf(false)
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
        if (state.secondOperand.isNotEmpty()){
            val number = getNumberFromString(state.secondOperand)
            if (number < 0){
                state = state.copy(isSecondOperandNegative = true)
            }
        }
    }

    private fun calculate() {
        var firstOperand by Delegates.notNull<Double>()
        var secondOperand by Delegates.notNull<Double>()
        var operation by Delegates.notNull<CalculatorOperation>()

        if (state.isInBrackets) {
            if (state.firstOperandInBrackets.isEmpty() || state.secondOperandInBrackets.isEmpty()) return
            firstOperand = getNumberFromString(state.firstOperandInBrackets)
            secondOperand = getNumberFromString(state.secondOperandInBrackets)
            operation = state.operationInBrackets!!
        } else {
            if (state.firstOperand.isEmpty() || state.secondOperand.isEmpty()) return
            firstOperand = getNumberFromString(state.firstOperand)
            secondOperand = getNumberFromString(state.secondOperand)
            operation = state.operation!!
        }

        if (operation == CalculatorOperation.Divide && secondOperand == 0.0) {
            isErrorCalculate = true
            return
        }

        val result = performCalculate(operation, firstOperand, secondOperand)


        state = if (state.isInBrackets) state.copy(
            firstOperandInBrackets = convertNumberToString(result),
            operationInBrackets = null,
            secondOperandInBrackets = ""
        ) else state.copy(
            firstOperand = convertNumberToString(result),
            operation = null,
            secondOperand = ""
        )

        if (state.secondOperand.isEmpty()){
            state = state.copy(isSecondOperandNegative = false)
        }
        if (state.secondOperandInBrackets.isEmpty()){
            state = state.copy(isSecondOperandInBracketsNegative = false)
        }

        isChangeDigit = false
    }

    private fun performCalculate(
        operation: CalculatorOperation,
        firstOperand: Double,
        secondOperand: Double
    ): BigDecimal = when (operation) {
        CalculatorOperation.Addition -> BigDecimal.valueOf(firstOperand).plus(BigDecimal.valueOf(secondOperand))
        CalculatorOperation.Divide -> BigDecimal.valueOf(firstOperand/secondOperand)//.div(BigDecimal.valueOf(secondOperand))
        CalculatorOperation.Multiply -> BigDecimal.valueOf(firstOperand).times(BigDecimal.valueOf(secondOperand))
        CalculatorOperation.Subtract -> BigDecimal.valueOf(firstOperand).minus(BigDecimal.valueOf(secondOperand))
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (isErrorCalculate) return
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
        if (!isChangeDigit || isErrorCalculate) return

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
        if (!isChangeDigit || isErrorCalculate) return
        when {
            state.isInBrackets -> {
                when {
                    state.operationInBrackets != null && !state.secondOperandInBrackets.contains(",") -> {
                        state =
                            state.copy(secondOperandInBrackets = if (state.secondOperandInBrackets.isEmpty()) "0," else state.secondOperandInBrackets + ",")
                    }

                    state.operationInBrackets == null && !state.firstOperandInBrackets.contains(",") -> {
                        state =
                            state.copy(firstOperandInBrackets = if (state.firstOperandInBrackets.isEmpty()) "0," else state.firstOperandInBrackets + ",")
                    }

                }
            }

            !state.isInBrackets -> {
                when {
                    state.operation != null && !state.secondOperand.contains(",") -> {
                        state =
                            state.copy(secondOperand = if (state.secondOperand.isEmpty()) "0," else state.secondOperand + ",")
                    }

                    state.operation == null && !state.firstOperand.contains(",") -> {
                        state =
                            state.copy(firstOperand = if (state.firstOperand.isEmpty()) "0," else state.firstOperand + ",")
                    }
                }
            }
        }
    }

    private fun performDeletion() {
        if (!isChangeDigit || isErrorCalculate) return

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

    private fun convertNumberToString(digit: BigDecimal): String =
        digit.setScale(3, RoundingMode.HALF_EVEN) // 3 decimal places
            .toString()
            .dropLastWhile { it == '0' }
            .dropLastWhile { it == '.' }
            .replace(oldValue = ".", newValue = ",", ignoreCase = true)

    private fun getNumberFromString(str: String): Double =
        str.replace(oldValue = ",", newValue = ".", ignoreCase = true).toDouble()

    private fun performPercentCalculation() {
        if (!isChangeDigit || isErrorCalculate) return

        when {
            state.isInBrackets -> {
                if (state.firstOperandInBrackets.isEmpty() || state.firstOperandInBrackets == "0,") return
                val onePercentFirstNum = getNumberFromString(state.firstOperandInBrackets) / 100

                state = when (state.operationInBrackets) {
                    null -> {
                        state.copy(
                            firstOperandInBrackets = convertNumberToString(
                                BigDecimal.valueOf(
                                    onePercentFirstNum
                                )
                            )
                        )
                    }

                    else -> {
                        if (state.secondOperandInBrackets.isEmpty() || state.secondOperandInBrackets == "0,") return
                        val secondNum = getNumberFromString(state.secondOperandInBrackets)

                        if (state.operationInBrackets == CalculatorOperation.Multiply || state.operationInBrackets == CalculatorOperation.Divide)
                            state.copy(
                                secondOperandInBrackets = convertNumberToString(
                                    BigDecimal.valueOf(
                                        secondNum / 100
                                    )
                                )
                            )
                        else
                            state.copy(
                                secondOperandInBrackets = convertNumberToString(
                                    BigDecimal.valueOf(
                                        onePercentFirstNum * secondNum
                                    )
                                )
                            )
                    }
                }
            }

            !state.isInBrackets -> {
                if (state.firstOperand.isEmpty() || state.firstOperand == "0,") return
                val onePercentFirstNum = getNumberFromString(state.firstOperand) / 100

                state = when (state.operation) {
                    null -> {
                        state.copy(
                            firstOperand = convertNumberToString(
                                BigDecimal.valueOf(
                                    onePercentFirstNum
                                )
                            )
                        )
                    }

                    else -> {
                        if (state.secondOperand.isEmpty() || state.secondOperand == "0,") return
                        val secondNum = getNumberFromString(state.secondOperand)

                        if (state.operation == CalculatorOperation.Multiply || state.operation == CalculatorOperation.Divide)
                            state.copy(
                                secondOperand = convertNumberToString(
                                    BigDecimal.valueOf(
                                        secondNum / 100
                                    )
                                )
                            )
                        else
                            state.copy(
                                secondOperand = convertNumberToString(
                                    BigDecimal.valueOf(
                                        onePercentFirstNum * secondNum
                                    )
                                )
                            )

                    }
                }
            }
        }
    }

    private fun inversionPositiveDigitToNegativeDigitAndViceVersa() {
        when {
            state.isInBrackets -> {
                when {
                    state.operationInBrackets == null && state.firstOperandInBrackets.isNotEmpty() && state.firstOperandInBrackets != "0," -> {
                        val number = -getNumberFromString(state.firstOperandInBrackets)
                        val isNegative = number < 0
                        state = state.copy(
                            firstOperandInBrackets = convertNumberToString(
                                BigDecimal.valueOf(number)
                            ),
                            isFirstOperandInBracketsNegative = isNegative
                        )
                    }

                    state.operationInBrackets != null && state.secondOperandInBrackets.isNotEmpty() && state.secondOperandInBrackets != "0," -> {
                        val number = -getNumberFromString(state.secondOperandInBrackets)
                        val isNegative = number < 0
                        state = state.copy(
                            secondOperandInBrackets = convertNumberToString(
                                BigDecimal.valueOf(number)
                            ),
                            isSecondOperandInBracketsNegative = isNegative
                        )
                    }
                }
            }

            !state.isInBrackets -> {
                when {
                    state.operation == null && state.firstOperand.isNotEmpty() && state.firstOperand != "0," -> {
                        val number = -getNumberFromString(state.firstOperand)
                        state =
                            state.copy(firstOperand = convertNumberToString(BigDecimal.valueOf(number)))
                    }

                    state.operation != null && state.secondOperand.isNotEmpty() && state.secondOperand != "0," -> {
                        val number = -getNumberFromString(state.secondOperand)
                        val isNegative = number < 0
                        state =
                            state.copy(
                                secondOperand = convertNumberToString(BigDecimal.valueOf(number)),
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
                if (isErrorCalculate) state.copy(secondOperandInBrackets = "")
                else state.copy(
                    firstOperandInBrackets = "",
                    secondOperandInBrackets = "",
                    operationInBrackets = null
                )
            }

            else -> {
                if (isErrorCalculate) state.copy(secondOperand = "") else CalculatorState()
            }
        }
        isChangeDigit = true
        isErrorCalculate = false
    }



    private fun fullClearScreen() {
        state = CalculatorState()
        isChangeDigit = true
        isErrorCalculate = false
    }

    companion object {
        const val MAX_LENGTH_DIGIT = 12
    }
}