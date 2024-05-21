package com.example.calculator

sealed class CalculatorOperation(val operation: String){
    object Addition: CalculatorOperation("+")
    object Subtract: CalculatorOperation("-")
    object Divide: CalculatorOperation("÷")
    object Multiply: CalculatorOperation("×")
}
