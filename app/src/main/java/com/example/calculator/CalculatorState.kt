package com.example.calculator

data class CalculatorState(
    val firstOperand: String = "",
    val secondOperand: String = "",
    val firstOperandInBrackets: String = "",
    val secondOperandInBrackets: String = "",
    val operation: CalculatorOperation? = null,
    val operationInBrackets: CalculatorOperation? = null,
    val isInBrackets: Boolean = false,
)
