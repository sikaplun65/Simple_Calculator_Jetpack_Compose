package com.example.calculator

data class CalculatorState(
    val firstOperand: String = "",
    val secondOperand: String = "",
    val firstOperandInBrackets: String = "",
    val secondOperandInBrackets: String = "",
    val operation: CalculatorOperation? = null,
    val operationInBrackets: CalculatorOperation? = null,
    val isInBrackets: Boolean = false,
    val isSecondOperandNegative: Boolean = false,
    val isFirstOperandInBracketsNegative: Boolean = false,
    val isSecondOperandInBracketsNegative: Boolean = false,
    val isErrorCalculate: Boolean = false,
    val isPercentage: Boolean = false
)
