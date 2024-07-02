package com.example.calculator.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.calculator.CalculatorEvent
import com.example.calculator.CalculatorOperation
import com.example.calculator.CalculatorState
import com.example.calculator.getExpressionWithSpaces
import com.example.calculator.getExpression
import com.example.calculator.ui.components.CalculatorLandscapeButton
import com.example.calculator.ui.theme.Orange
import com.example.calculator.ui.theme.darkOrange
import com.example.calculator.util.ButtonModifiers

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CalculatorLandscapeScreen(
    state: CalculatorState,
    modifier: Modifier = Modifier,
    onEvent: (CalculatorEvent) -> Unit,
    isErrorCalculate: Boolean
) {
    val widthBetweenBlocs = 8.dp

    Box(modifier = modifier){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(1.5f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                AutoSizeText(
                    text = getExpressionWithSpaces(state, isErrorCalculate),
                    onEvent = onEvent,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(0.8f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorLandscapeButton(
                    symbol = "7",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(7))
                    }
                )
                CalculatorLandscapeButton(
                    symbol = "8",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(8))
                    }
                )
                CalculatorLandscapeButton(
                    symbol = "9",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(9))
                    }
                )

                Spacer(modifier = Modifier.width(widthBetweenBlocs))

                CalculatorLandscapeButton(
                    symbol = "÷",
                    modifier = ButtonModifiers.operationButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Operation(CalculatorOperation.Divide))
                    }
                )

                Spacer(modifier = Modifier.width(widthBetweenBlocs))

                CalculatorLandscapeButton(
                    symbol = "AC",
                    modifier = ButtonModifiers.equalsButtonModifier
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { onEvent(CalculatorEvent.Clear) },
                                onLongPress = { onEvent(CalculatorEvent.FullClear) }
                            )
                        },
                    onClick = {}
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp)
                    .weight(0.8f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorLandscapeButton(
                    symbol = "4",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(4))
                    }
                )
                CalculatorLandscapeButton(
                    symbol = "5",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(5))
                    }
                )
                CalculatorLandscapeButton(
                    symbol = "6",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(6))
                    }
                )

                Spacer(modifier = Modifier.width(widthBetweenBlocs))

                CalculatorLandscapeButton(
                    symbol = "×",
                    modifier = ButtonModifiers.operationButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Operation(CalculatorOperation.Multiply))
                    }
                )

                Spacer(modifier = Modifier.width(widthBetweenBlocs))

                CalculatorLandscapeButton(
                    symbol = "(  )",
                    modifier = ButtonModifiers.equalsButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Brackets)
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.8f)
                    .padding(start = 5.dp, end = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorLandscapeButton(
                    symbol = "1",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(1))
                    }
                )
                CalculatorLandscapeButton(
                    symbol = "2",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(2))
                    }
                )
                CalculatorLandscapeButton(
                    symbol = "3",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(3))
                    }
                )

                Spacer(modifier = Modifier.width(widthBetweenBlocs))

                CalculatorLandscapeButton(
                    symbol = "−",
                    modifier = ButtonModifiers.operationButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Operation(CalculatorOperation.Subtract))
                    }
                )

                Spacer(modifier = Modifier.width(widthBetweenBlocs))

                CalculatorLandscapeButton(
                    symbol = "%",
                    modifier = ButtonModifiers.equalsButtonModifier
                        .weight(1f),
                    onClick = {
                        onEvent(CalculatorEvent.PercentCalculation)
                    }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.95f)
                    .padding(start = 5.dp, end = 5.dp, bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorLandscapeButton(
                    symbol = "0",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(0))
                    }
                )
                CalculatorLandscapeButton(
                    symbol = ",",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Decimal)
                    }
                )
                CalculatorLandscapeButton(
                    symbol = "±",
                    modifier = ButtonModifiers.numberButtonModifier
                        .weight(weight = 1f)
                        .background(Brush.verticalGradient(colors = listOf(Color.Gray, Color.DarkGray))),
                    onClick = {
                        onEvent(CalculatorEvent.NumberInversion)
                    }
                )

                Spacer(modifier = Modifier.width(widthBetweenBlocs))

                CalculatorLandscapeButton(
                    symbol = "+",
                    modifier = ButtonModifiers.operationButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Operation(CalculatorOperation.Addition))
                    }
                )

                Spacer(modifier = Modifier.width(widthBetweenBlocs))

                CalculatorLandscapeButton(
                    symbol = "=",
                    modifier = ButtonModifiers.operationButtonModifier
                        .weight(weight = 1f)
                        .background(Brush.verticalGradient(colors = listOf(Orange, darkOrange))),
                    onClick = {
                        onEvent(CalculatorEvent.Calculate)
                    }
                )
            }
        }
    }
}