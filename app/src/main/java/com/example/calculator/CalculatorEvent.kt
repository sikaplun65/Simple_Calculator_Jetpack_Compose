package com.example.calculator

sealed class CalculatorEvent {
    data class Number(val digit: Int) : CalculatorEvent()
    data class Operation(val operation: CalculatorOperation) : CalculatorEvent()
    object Brackets: CalculatorEvent()
    object Calculate: CalculatorEvent()
    object Clear: CalculatorEvent()
    object FullClear: CalculatorEvent()
    object Delete: CalculatorEvent()
    object Decimal: CalculatorEvent()
    object NumberInversion: CalculatorEvent()
    object AddPercentageIcon: CalculatorEvent()
}
