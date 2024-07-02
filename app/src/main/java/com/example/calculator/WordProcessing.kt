package com.example.calculator

fun getExpressionWithSpaces(state: CalculatorState, isErrorCalculate: Boolean): String {

    var expression = getExpression(state)
    val blocks = mutableListOf<String>()
    var number = ""
    val errorMessage = "Error! You can't divide by 0"
    var secondLastBracket = ""
    var lastBracket = ""
    var bracketCounter = 0

    while (expression.isNotEmpty()) {
        if (expression[0] == '-') {
            blocks.add(expression[0].toString())
            expression = expression.substring(1, expression.length)
        }

        if (expression[0] == '(') {
            bracketCounter++
            blocks.add(expression[0].toString())
            lastBracket = expression[expression.length - 1].toString()
            expression = expression.substring(1, expression.length - 1)
            if (bracketCounter == 2) {
                secondLastBracket = ")"
            }
        }

        val operatorInd =
            expression.indexOfFirst { it == '-' || it == '+' || it == '×' || it == '÷' }

        if (operatorInd != -1) {
            number = expression.substring(0, operatorInd)

            blocks.add(addSpaces(number))
            blocks.add(expression[operatorInd].toString())

            expression = expression.substring(operatorInd + 1, expression.length)
        } else {
            number = expression.substring(0, expression.length)
            blocks.add(addSpaces(number))
            expression = ""
        }
    }
    if (secondLastBracket.isNotEmpty()) {
        blocks.add(secondLastBracket)
    }
    if (lastBracket.isNotEmpty()) {
        blocks.add(lastBracket)
    }
    return if (isErrorCalculate) errorMessage else blocks.joinToString("")
}

private fun addSpaces(number: String): String {

    var inputNumber = number
    var outputNumber = ""
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
    outputNumber = integerNum.toString().reversed()

    if (decimalNumber.isNotEmpty()) {
        outputNumber += decimalNumber
    }
    return outputNumber
}

fun getExpression(
    state: CalculatorState
): String {

    val sb = StringBuilder(state.firstOperand).append(state.operation?.operation ?: "")

    if (!state.isInBrackets) {
        if (!state.isSecondOperandNegative) {
            sb.append(state.secondOperand)
        } else {
            sb.append("(").append(state.secondOperand).append(")")
        }
    } else {
        sb.append("(")

        if (!state.isFirstOperandInBracketsNegative) {
            sb.append(state.firstOperandInBrackets)
        } else {
            sb.append("(").append(state.firstOperandInBrackets).append(")")
        }

        sb.append(state.operationInBrackets?.operation ?: "")

        if (!state.isSecondOperandInBracketsNegative) {
            sb.append(state.secondOperandInBrackets)
        } else {
            sb.append("(").append(state.secondOperandInBrackets).append(")")
        }

        sb.append(")")
    }

    return sb.toString()
}
