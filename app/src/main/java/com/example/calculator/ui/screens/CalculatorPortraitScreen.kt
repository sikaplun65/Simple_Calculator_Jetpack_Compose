package com.example.calculator.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.calculator.ui.components.CalculatorButton
import com.example.calculator.ui.theme.LightOrange
import com.example.calculator.ui.theme.darkOrange
import com.example.calculator.util.ButtonModifiers.cleanButtonModifier
import com.example.calculator.util.ButtonModifiers.equalsButtonModifier
import com.example.calculator.util.ButtonModifiers.numberButtonModifier
import com.example.calculator.util.ButtonModifiers.operationButtonModifier

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CalculatorPortraitScreen(
    state: CalculatorState,
    modifier: Modifier = Modifier,
    onEvent: (CalculatorEvent) -> Unit,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            Row(
                modifier = Modifier
                    .weight(2.5f)
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp),
                verticalAlignment = Alignment.Bottom,
            ) {
                AutoSizeText(
                    state = state,
                    onEvent = onEvent
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    symbol = "AC",
                    modifier = cleanButtonModifier
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = { onEvent(CalculatorEvent.Clear) },
                                onLongPress = { onEvent(CalculatorEvent.FullClear) }
                            )
                        },
                    onClick = {}
                )


                CalculatorButton(
                    symbol = "( )",
                    modifier = equalsButtonModifier
                        .weight(1f),
                    onClick = {
                        onEvent(CalculatorEvent.Brackets)
                    }
                )

                CalculatorButton(
                    symbol = "%",
                    modifier = equalsButtonModifier
                        .weight(1f),
                    onClick = {
                        onEvent(CalculatorEvent.PercentCalculation)
                    }
                )

                CalculatorButton(
                    symbol = "÷",
                    modifier = operationButtonModifier
                        .weight(1f),
                    onClick = {
                        onEvent(CalculatorEvent.Operation(CalculatorOperation.Divide))
                    }
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    symbol = "7",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(7))
                    }
                )
                CalculatorButton(
                    symbol = "8",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(8))
                    }
                )
                CalculatorButton(
                    symbol = "9",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(9))
                    }
                )
                CalculatorButton(
                    symbol = "×",
                    modifier = operationButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Operation(CalculatorOperation.Multiply))
                    }
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    symbol = "4",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(4))
                    }
                )
                CalculatorButton(
                    symbol = "5",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(5))
                    }
                )
                CalculatorButton(
                    symbol = "6",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(6))
                    }
                )
                CalculatorButton(
                    symbol = "−",
                    modifier = operationButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Operation(CalculatorOperation.Subtract))
                    }
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    symbol = "1",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(1))
                    }
                )
                CalculatorButton(
                    symbol = "2",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(2))
                    }
                )
                CalculatorButton(
                    symbol = "3",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(3))
                    }
                )
                CalculatorButton(
                    symbol = "+",
                    modifier = operationButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Operation(CalculatorOperation.Addition))
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 2.dp)
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CalculatorButton(
                    symbol = "0",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Number(0))
                    }
                )
                CalculatorButton(
                    symbol = ",",
                    modifier = numberButtonModifier
                        .weight(weight = 1f),
                    onClick = {
                        onEvent(CalculatorEvent.Decimal)
                    }
                )
                CalculatorButton(
                    symbol = "±",
                    modifier = numberButtonModifier
                        .weight(weight = 1f)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Gray,
                                    Color.DarkGray
                                )
                            )
                        ),
                    onClick = {
                        onEvent(CalculatorEvent.NumberInversion)
                    }
                )
                CalculatorButton(
                    symbol = "=",
                    modifier = operationButtonModifier
                        .weight(weight = 1f)
                        .background(Brush.verticalGradient(colors = listOf(LightOrange, darkOrange))),
                    onClick = {
                        onEvent(CalculatorEvent.Calculate)
                    }
                )
            }
        }
    }
}

