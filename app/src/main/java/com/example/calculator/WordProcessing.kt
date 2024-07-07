package com.example.calculator

fun getExpressionWithSpaces(state: CalculatorState): String {

    val expressionSB = StringBuilder()
    val errorMessage = "Error! You can't divide by 0"

    expressionSB.append(addSpaces(state.firstOperand))

    if (state.operation != null) {
        expressionSB.append(addOperation(state.operation))
    }

    expressionSB.append(
        getOperand(state.isSecondOperandNegative, state.secondOperand)
    )

    if (state.isInBrackets) {

        expressionSB.append("(")

        expressionSB.append(
            getOperand(state.isFirstOperandInBracketsNegative, state.firstOperandInBrackets)
        )

        if (state.operationInBrackets != null) {
            expressionSB.append(addOperation(state.operationInBrackets))
        }

        expressionSB.append(
            getOperand(state.isSecondOperandInBracketsNegative, state.secondOperandInBrackets)
        )

        expressionSB.append(")")
    }

    return if (state.isErrorCalculate) {
        errorMessage
    } else {
        expressionSB.toString().replace(oldValue = ".", newValue = ",", ignoreCase = true)
    }
}

private fun getOperand(isNegativeOperand: Boolean, operand: String): String {
    val sb = StringBuilder()

    if (!isNegativeOperand) {
        sb.append(addSpaces(operand))
    } else {
        sb.append("(")
        sb.append(addSpaces(operand))
        sb.append(")")
    }
    return sb.toString()
}

private fun addOperation(operation: CalculatorOperation): String =
    when (operation) {
        CalculatorOperation.Addition -> "+"
        CalculatorOperation.Divide -> "÷"
        CalculatorOperation.Multiply -> "×"
        CalculatorOperation.Subtract -> "−"
    }

private fun addSpaces(number: String): String {
    val isNumberNegative = number.contains('-')
    val minus = if (isNumberNegative) "−" else ""
    var inputNumber = if (isNumberNegative) number.substring(1, number.length) else number
    val outputNumber = StringBuilder()
    val integerNum = StringBuilder()
    var decimalNumber = ""

    val pointIndex = number.indexOf('.')
    if (pointIndex != -1) {
        val index = if (isNumberNegative) 1 else 0
        inputNumber = number.substring(index, pointIndex)
        decimalNumber = number.substring(pointIndex, number.length)
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

    outputNumber.append(minus).append(integerNum.toString().reversed()).append(decimalNumber)

    return outputNumber.toString()
}
