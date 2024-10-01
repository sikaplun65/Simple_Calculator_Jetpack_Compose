package com.example.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode

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
            CalculatorEvent.AddPercentageIcon -> addPercentage()
            CalculatorEvent.Brackets -> enterBrackets()
        }
    }

    private fun addPercentage() {
        val isPercentageApplicable = (state.operation == null && state.firstOperand.isNotEmpty()) ||
                (state.secondOperand.isNotEmpty()) ||
                (state.operationInBrackets == null && state.firstOperandInBrackets.isNotEmpty()) ||
                (state.secondOperandInBrackets.isNotEmpty())

        if (isPercentageApplicable) {
            state = state.copy(isPercentage = true)
        }
    }

    private fun enterBrackets() {

        if (state.operation == null || state.secondOperand.isNotEmpty()) return
        calculate()
        state = state.copy(
            isInBrackets = !state.isInBrackets,
            secondOperand = state.firstOperandInBrackets.ifEmpty { "" },
            firstOperandInBrackets = "",
            operationInBrackets = null
        )
        if (state.secondOperand.isNotEmpty()) {
            val number = state.secondOperand.toDouble()
            if (number < 0) {
                state = state.copy(isSecondOperandNegative = true)
            }
        }
    }

    private fun calculate() {
        val (firstOperand, secondOperand, operation) = getOperandsAndOperation() ?: return

        if (operation == CalculatorOperation.Divide && isOperandZero(secondOperand.toString())) {
            state = state.copy(isErrorCalculate = true)
            return
        }

        val result = performCalculation(operation, firstOperand, secondOperand)

        updateStateWithResult(result)

        resetNegativeFlags()
        isChangeDigit = false
    }

    private fun getOperandsAndOperation(): Triple<BigDecimal, BigDecimal, CalculatorOperation>? {
        if (state.isPercentage) {
            performPercentCalculation()
        }

        val (firstOp, secondOp, op) =
            if (state.isInBrackets) {
                if (state.firstOperandInBrackets.isEmpty() || state.secondOperandInBrackets.isEmpty()) return null
                Triple(
                    first = state.firstOperandInBrackets.toBigDecimal(),
                    second = state.secondOperandInBrackets.toBigDecimal(),
                    third = state.operationInBrackets!!
                )
            } else {
                if (state.firstOperand.isEmpty() || state.secondOperand.isEmpty()) return null
                Triple(
                    first = state.firstOperand.toBigDecimal(),
                    second = state.secondOperand.toBigDecimal(),
                    third = state.operation!!
                )
            }

        return Triple(firstOp, secondOp, op)
    }

    private fun updateStateWithResult(result: String) {
        state = if (state.isInBrackets) {
            state.copy(
                firstOperandInBrackets = result,
                operationInBrackets = null,
                secondOperandInBrackets = ""
            )
        } else {
            state.copy(
                firstOperand = result,
                operation = null,
                secondOperand = ""
            )
        }
    }

    private fun resetNegativeFlags() {
        if (state.secondOperand.isEmpty()) {
            state = state.copy(isSecondOperandNegative = false)
        }

        if (state.firstOperandInBrackets.isNotEmpty()) {
            state = state.copy(
                isFirstOperandInBracketsNegative = state.firstOperandInBrackets.contains('-')
            )
        }

        if (state.secondOperandInBrackets.isEmpty()) {
            state = state.copy(isSecondOperandInBracketsNegative = false)
        }
    }

    private fun performCalculation(
        operation: CalculatorOperation,
        firstOperand: BigDecimal,
        secondOperand: BigDecimal
    ): String {
        val result = when (operation) {
            CalculatorOperation.Addition -> firstOperand.plus(secondOperand)
            CalculatorOperation.Divide -> firstOperand.setScale(NUMBER_OF_DECIMAL_PLACES)
                .div(secondOperand)

            CalculatorOperation.Multiply -> firstOperand.times(secondOperand)
            CalculatorOperation.Subtract -> firstOperand.minus(secondOperand)
        }.setScale(NUMBER_OF_DECIMAL_PLACES, RoundingMode.CEILING)

        return result.toPlainString().dropLastWhile { it == '0' }.dropLastWhile { it == '.' }
    }

    private fun enterOperation(operation: CalculatorOperation) {
        if (state.isErrorCalculate) return
        calculate()
        if (state.isErrorCalculate) return //to prevent replacement of an operation in case of an error
        when {
            state.isInBrackets -> {
                if (state.firstOperandInBrackets.isEmpty() || state.firstOperandInBrackets == "0.") return
                state = state.copy(operationInBrackets = operation)
            }

            !state.isInBrackets -> {
                if (state.firstOperand.isEmpty() || state.firstOperand == "0.") return
                state = state.copy(operation = operation)
            }
        }
        isChangeDigit = true
    }

    private fun enterDigit(digit: Int) {
        if (!isChangeDigit || state.isErrorCalculate) return

        val (operation, firstOperand, secondOperand) =
            if (state.isInBrackets) {
                listOf(
                    state.operationInBrackets,
                    state.firstOperandInBrackets,
                    state.secondOperandInBrackets
                )
            } else {
                listOf(state.operation, state.firstOperand, state.secondOperand)
            }

        val currentOperand = (if (operation == null) firstOperand else secondOperand).toString()
        val isFirstOperand = operation == null
        val operandLength = currentOperand.length

        if (operandLength < MAX_LENGTH_DIGIT) {

            val newValue = if (currentOperand == "0") digit.toString() else currentOperand + digit

            state =
                if (state.isInBrackets) {
                    if (isFirstOperand) {
                        state.copy(firstOperandInBrackets = newValue)
                    } else {
                        state.copy(secondOperandInBrackets = newValue)
                    }
                } else {
                    if (isFirstOperand) {
                        state.copy(firstOperand = newValue)
                    } else {
                        state.copy(secondOperand = newValue)
                    }
                }
        }
    }

    private fun enterDecimal() {
        if (!isChangeDigit || state.isErrorCalculate) return

        val (operation, firstOperand, secondOperand) =
            if (state.isInBrackets) {
                listOf(
                    state.operationInBrackets,
                    state.firstOperandInBrackets,
                    state.secondOperandInBrackets
                )
            } else {
                listOf(state.operation, state.firstOperand, state.secondOperand)
            }

        val currentOperand = (if (operation == null) firstOperand else secondOperand).toString()

        if (!currentOperand.contains(".")) {

            val newValue = if (currentOperand.isEmpty()) "0." else "$currentOperand."

            state =
                if (state.isInBrackets) {
                    if (operation == null) {
                        state.copy(firstOperandInBrackets = newValue)
                    } else {
                        state.copy(secondOperandInBrackets = newValue)
                    }
                } else {
                    if (operation == null) {
                        state.copy(firstOperand = newValue)
                    } else {
                        state.copy(secondOperand = newValue)
                    }
                }
        }
    }

    private fun performDeletion() {
        if (!isChangeDigit || state.isErrorCalculate) return
        if (state.isPercentage) {
            state = state.copy(isPercentage = false)
            return
        }

        val isInBrackets = state.isInBrackets
        val currentOperands = if (isInBrackets) {
            listOf(state.firstOperandInBrackets, state.secondOperandInBrackets)
        } else {
            listOf(state.firstOperand, state.secondOperand)
        }

        var (firstOperand, secondOperand) = currentOperands
        val isOperationPresent =
            if (isInBrackets) state.operationInBrackets != null else state.operation != null

        when {
            secondOperand.isNotEmpty() -> {
                state = if (isInBrackets) {
                    state.copy(secondOperandInBrackets = secondOperand.dropLast(1))
                } else {
                    state.copy(secondOperand = secondOperand.dropLast(1))
                }
                secondOperand =
                    if (isInBrackets) state.secondOperandInBrackets else state.secondOperand
                handleZeroAndNegativeState(isInBrackets, isOperationPresent, secondOperand)
            }

            isOperationPresent -> {
                state = if (isInBrackets) {
                    state.copy(operationInBrackets = null)
                } else {
                    state.copy(operation = null)
                }
            }

            firstOperand.isNotEmpty() -> {
                state = if (isInBrackets) {
                    state.copy(firstOperandInBrackets = firstOperand.dropLast(1))
                } else {
                    state.copy(firstOperand = firstOperand.dropLast(1))
                }
                firstOperand =
                    if (isInBrackets) state.firstOperandInBrackets else state.firstOperand
                handleZeroAndNegativeState(isInBrackets, false, firstOperand)
            }
        }
    }

    private fun handleZeroAndNegativeState(
        isInBrackets: Boolean,
        isOperationPresent: Boolean,
        operand: String
    ) {
        if ((operand == "-" || operand == "0" || operand == "-0")) {
            state =
                if (isInBrackets) {
                    if (isOperationPresent) {
                        state.copy(
                            secondOperandInBrackets = operand.dropLastWhile { it == '0' }
                                .dropLastWhile { it == '-' },
                            isSecondOperandInBracketsNegative = false
                        )
                    } else {
                        state.copy(
                            firstOperandInBrackets = operand.dropLastWhile { it == '0' }
                                .dropLastWhile { it == '-' },
                            isFirstOperandInBracketsNegative = false
                        )
                    }
                } else {
                    if (isOperationPresent) {
                        state.copy(
                            secondOperand = operand.dropLastWhile { it == '0' }
                                .dropLastWhile { it == '-' },
                            isSecondOperandNegative = false
                        )
                    } else {
                        state.copy(
                            firstOperand = operand.dropLastWhile { it == '0' }
                                .dropLastWhile { it == '-' },
                        )
                    }
                }
        }
    }

    private fun performPercentCalculation() {
        if (!isChangeDigit || state.isErrorCalculate) return

        val (firstOperand, secondOperand, operation, isInBrackets) =
            if (state.isInBrackets) {
                listOf(
                    state.firstOperandInBrackets,
                    state.secondOperandInBrackets,
                    state.operationInBrackets,
                    true
                )
            } else {
                listOf(state.firstOperand, state.secondOperand, state.operation, false)
            }

        if (firstOperand.toString().isEmpty() || isOperandZero(firstOperand.toString())) return

        val onePercentFirstNum = firstOperand.toString().toDouble() / 100

        state = when {
            operation == null -> {
                updateFirstOperandInPercentCalculation(onePercentFirstNum, isInBrackets as Boolean)
            }

            secondOperand.toString().isEmpty() || isOperandZero(secondOperand.toString()) -> return

            else -> {
                val secondNum = secondOperand.toString().toDouble()
                updateSecondOperandInPercentCalculation(
                    onePercentFirstNum,
                    secondNum,
                    operation as CalculatorOperation,
                    isInBrackets as Boolean
                )
            }
        }
        state = state.copy(isPercentage = false)
    }

    private fun updateFirstOperandInPercentCalculation(
        onePercentFirstNum: Double,
        isInBrackets: Boolean
    ): CalculatorState {
        return if (isInBrackets) {
            state.copy(firstOperandInBrackets = getStringFromNumber(onePercentFirstNum.toBigDecimal()))
        } else {
            state.copy(firstOperand = getStringFromNumber(onePercentFirstNum.toBigDecimal()))
        }
    }

    private fun updateSecondOperandInPercentCalculation(
        onePercentFirstNum: Double,
        secondNum: Double,
        operation: CalculatorOperation?,
        isInBrackets: Boolean
    ): CalculatorState {
        return if (operation == CalculatorOperation.Multiply || operation == CalculatorOperation.Divide) {
            val onePercentOfSecondNumber = secondNum / 100
            when (isInBrackets) {
                true -> state.copy(
                    secondOperandInBrackets = getStringFromNumber(
                        onePercentOfSecondNumber.toBigDecimal()
                    )
                )

                false -> state.copy(secondOperand = getStringFromNumber(onePercentOfSecondNumber.toBigDecimal()))
            }
        } else {
            val percentageOfSecondNumber = onePercentFirstNum * secondNum
            when (isInBrackets) {
                true -> state.copy(
                    secondOperandInBrackets = getStringFromNumber(
                        percentageOfSecondNumber.toBigDecimal()
                    )
                )

                false -> state.copy(secondOperand = getStringFromNumber(percentageOfSecondNumber.toBigDecimal()))
            }
        }
    }

    private fun inversionPositiveDigitToNegativeDigitAndViceVersa() {

        fun invertOperand(operand: String): Pair<String, Boolean> {
            val number = -operand.toBigDecimal()
            return getStringFromNumber(number) to (number < BigDecimal.ZERO)
        }

        when {
            state.isInBrackets -> {
                when {
                    state.operationInBrackets == null && state.firstOperandInBrackets.isNotEmpty() && !isOperandZero(
                        state.firstOperandInBrackets
                    ) -> {
                        val (newFirstOperand, isNegative) = invertOperand(state.firstOperandInBrackets)
                        state = state.copy(
                            firstOperandInBrackets = newFirstOperand,
                            isFirstOperandInBracketsNegative = isNegative
                        )
                    }

                    state.operationInBrackets != null && state.secondOperandInBrackets.isNotEmpty() && !isOperandZero(
                        state.secondOperandInBrackets
                    ) -> {
                        val (newSecondOperand, isNegative) = invertOperand(state.secondOperandInBrackets)
                        state = state.copy(
                            secondOperandInBrackets = newSecondOperand,
                            isSecondOperandInBracketsNegative = isNegative
                        )
                    }
                }
            }

            !state.isInBrackets -> {
                when {
                    state.operation == null && state.firstOperand.isNotEmpty() && !isOperandZero(
                        state.firstOperand
                    ) -> {
                        val (newFirstOperand, _) = invertOperand(state.firstOperand)
                        state = state.copy(firstOperand = newFirstOperand)
                    }

                    state.operation != null && state.secondOperand.isNotEmpty() && !isOperandZero(
                        state.secondOperand
                    ) -> {
                        val (newSecondOperand, isNegative) = invertOperand(state.secondOperand)
                        state = state.copy(
                            secondOperand = newSecondOperand,
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

    private fun getStringFromNumber(number: BigDecimal): String =
        number
            .setScale(NUMBER_OF_DECIMAL_PLACES, RoundingMode.CEILING)
            .toString()
            .dropLastWhile { it == '0' }
            .dropLastWhile { it == '.' }

    private fun isOperandZero(number: String): Boolean =
        number.trimEnd('0').trimEnd('.').run { this == "0" || this == "" }

    companion object {
        private const val MAX_LENGTH_DIGIT = 12
        private const val NUMBER_OF_DECIMAL_PLACES = 9
    }
}