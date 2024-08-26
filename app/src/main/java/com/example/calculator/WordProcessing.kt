package com.example.calculator

fun getExpressionWithSpaces(state: CalculatorState): String {
    val expressionSB = StringBuilder()
    val errorMessage = "Error! You can't divide by 0"

    expressionSB.append(addSpaces(state.firstOperand))

    state.operation?.let { operation ->
        expressionSB.append(addOperation(operation))
        expressionSB.append(getOperand(state.isSecondOperandNegative, state.secondOperand))

        if (state.isInBrackets) {
            expressionSB.append("(")
            expressionSB.append(
                getOperand(
                    state.isFirstOperandInBracketsNegative,
                    state.firstOperandInBrackets
                )
            )

            state.operationInBrackets?.let { operationInBrackets ->
                expressionSB.append(addOperation(operationInBrackets))
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
        expressionSB.toString().replace(".", ",")
    }
}

private fun getOperand(isNegative: Boolean, operand: String): String {
    return if (isNegative) {
        "(${addSpaces(operand)})"
    } else {
        addSpaces(operand)
    }
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
    val decimalPart = if (partsOfNumber.size > 1) ".${partsOfNumber[1]}" else ""

    val outputNumber = StringBuilder()

    for (i in integerPart.indices.reversed()) {
        if (i != integerPart.lastIndex && (integerPart.lastIndex - i) % 3 == 0) {
            outputNumber.append(" ")
        }
        outputNumber.append(integerPart[i])
    }

    outputNumber.append(minus).reverse().append(decimalPart)

    return outputNumber.toString()
}
