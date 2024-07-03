package com.example.calculator

fun getExpressionWithSpaces(state: CalculatorState, isErrorCalculate: Boolean): String {

    val blocks = mutableListOf<String>()
    val errorMessage = "Error! You can't divide by 0"

    val firstOperand = state.firstOperand
    val secondOperand = state.secondOperand

    if (!firstOperand.contains("−")) {
        blocks.add(addSpaces(firstOperand))
    } else {
        blocks.add("−")
        blocks.add(addSpaces(firstOperand.substring(1, firstOperand.length)))
    }

    if (state.operation != null) {
        blocks.add(addOperation(state.operation))
    }

    if (!state.isSecondOperandNegative) {
        blocks.add(addSpaces(secondOperand))
    } else {
        blocks.add("(−")
        blocks.add(addSpaces(secondOperand.substring(1, secondOperand.length)))
        blocks.add(")")
    }

    if (state.isInBrackets) {
        val firstNumber = state.firstOperandInBrackets
        val secondNumber = state.secondOperandInBrackets

        blocks.add("(")

        if (!state.isFirstOperandInBracketsNegative) {
            blocks.add(addSpaces(firstNumber))
        } else {
            blocks.add("(−")
            blocks.add(addSpaces(firstNumber.substring(1, firstNumber.length)))
            blocks.add(")")
        }

        if (state.operationInBrackets != null) {
            blocks.add(addOperation(state.operationInBrackets))
        }

        if (!state.isSecondOperandInBracketsNegative) {
            blocks.add(addSpaces(secondNumber))
        } else {
            blocks.add("(−")
            blocks.add(addSpaces(secondNumber.substring(1, secondNumber.length)))
            blocks.add(")")
        }

        blocks.add(")")
    }

    return if (isErrorCalculate) errorMessage else blocks.joinToString("")
}

private fun addOperation(operation: CalculatorOperation): String =
    when (operation) {
        CalculatorOperation.Addition -> "+"
        CalculatorOperation.Divide -> "÷"
        CalculatorOperation.Multiply -> "×"
        CalculatorOperation.Subtract -> "−"
    }


private fun addSpaces(number: String): String {

    var inputNumber = number
    val outputNumber = mutableListOf<String>()
    val integerNum = StringBuilder()
    var decimalNumber = ""

    val commaIndex = number.indexOf(',')

    if (commaIndex != -1) {
        inputNumber = number.substring(0, commaIndex)
        decimalNumber = number.substring(commaIndex, number.length)
    }

    var counter = 0
    for (digit in inputNumber.reversed()) {
        counter++
        if (counter == 4) {
            integerNum.append(" ")
            integerNum.append(digit)
            counter = 1
        } else {
            integerNum.append(digit)
        }
    }

    outputNumber.add(integerNum.toString().reversed())
    outputNumber.add(decimalNumber)

    return outputNumber.joinToString("")
}
