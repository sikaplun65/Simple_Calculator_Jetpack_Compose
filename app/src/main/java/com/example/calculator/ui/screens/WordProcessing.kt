package com.example.calculator.ui.screens

fun getExpressionWithSpaces(inputStr: String, isErrorCalculate: Boolean): String {

    val blocks = mutableListOf<String>()
    var expression = inputStr
    var number = ""
    val errorMessage = "Error! You can't divide by 0"
    var lastBracket = ""

    while (expression.isNotEmpty()) {
        if (expression[0] == '-') {
            blocks.add(expression[0].toString())
            expression = expression.substring(1, expression.length)
        }

        if (expression[0] == '('){
            blocks.add(expression[0].toString())
            lastBracket = expression[expression.length - 1].toString()
            expression = expression.substring(1,expression.length - 1)
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
    if (lastBracket.isNotEmpty()){
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

//@Composable
//fun calculateFontSize(text: String): TextUnit {
//
//    val screenWidth = LocalConfiguration.current.screenWidthDp.sp
//    var fontSize = screenWidth / 4
//    var textSize = fontSize * text.length
//
//    while (textSize > screenWidth * 1.5) {
//        fontSize *= 0.999
//        textSize = fontSize * text.length
//    }
//
//    return fontSize
//}
//
//val Int.dpTextUnit: TextUnit
//    @Composable get() = with(LocalDensity.current) { this@dpTextUnit.dp.toSp() }