package com.example.calculator

fun getExpressionWithSpaces(state: CalculatorState, isErrorCalculate: Boolean): String {

    val blocks = StringBuilder()
    val errorMessage = "Error! You can't divide by 0"

    blocks.append(addSpaces(state.firstOperand))

    if (state.operation != null) {
        blocks.append(addOperation(state.operation))
    }

    blocks.append(
        getOperand(state.isSecondOperandNegative, state.secondOperand)
    )

    if (state.isInBrackets) {

        blocks.append("(")

        blocks.append(
            getOperand(state.isFirstOperandInBracketsNegative, state.firstOperandInBrackets)
        )

        if (state.operationInBrackets != null) {
            blocks.append(addOperation(state.operationInBrackets))
        }

        blocks.append(
            getOperand(state.isSecondOperandInBracketsNegative, state.secondOperandInBrackets)
        )

        blocks.append(")")
    }

    return if (isErrorCalculate) errorMessage else blocks.toString()
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
    val minus = if (isNumberNegative)"−" else ""
    var inputNumber = if (isNumberNegative) number.substring(1,number.length) else number
    val outputNumber = StringBuilder()
    val integerNum = StringBuilder()
    var decimalNumber = ""

    val commaIndex = number.indexOf(',')
    if (commaIndex != -1) {
        inputNumber = number.substring(if (isNumberNegative) 1 else 0, commaIndex)
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

    outputNumber.append(minus).append(integerNum.toString().reversed()).append(decimalNumber)

    return outputNumber.toString()
}
