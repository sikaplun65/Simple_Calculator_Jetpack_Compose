package com.example.calculator

fun getExpressionWithSpaces(state: CalculatorState): String {

    val expressionSB = StringBuilder()
    val errorMessage = "Error! You can't divide by 0"

    expressionSB.append(addSpaces(state.firstOperand))

    if (state.operation != null) {
        expressionSB.append(addOperation(state.operation))

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

                expressionSB.append(
                    getOperand(
                        state.isSecondOperandInBracketsNegative,
                        state.secondOperandInBrackets
                    )
                )
            }

            expressionSB.append(")")
        }
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

    val isNumberNegative = number.startsWith('-')
    val minus = if (isNumberNegative) "−" else ""
    val startIndex = if (isNumberNegative) 1 else 0

    val partsOfNumber = number.substring(startIndex).split(".")
    val integerPart = partsOfNumber[0]
    val decimalPart = if (partsOfNumber.size > 1) "." + partsOfNumber[1] else ""

    val outputNumber = StringBuilder(minus)
    val integerNum = StringBuilder()

    for (i in integerPart.indices.reversed()) {
        if (i != integerPart.length - 1 && (integerPart.length - i - 1) % 3 == 0) {
            integerNum.append(" ")
        }
        integerNum.append(integerPart[i])
    }

    outputNumber.append(integerNum.toString().reversed()).append(decimalPart)

    return outputNumber.toString()
}
