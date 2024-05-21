package com.example.calculator.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.unit.dp
import com.example.calculator.ui.theme.LightBlue
import com.example.calculator.ui.theme.LightOrange
import com.example.calculator.ui.theme.Orange

object ButtonModifiers {
    val numberButtonModifier = Modifier
        .background(Brush.verticalGradient(colors = listOf(LightGray, DarkGray)))
        .aspectRatio(ratio = 1F)

    val operationButtonModifier = Modifier
        .background(Brush.verticalGradient(colors = listOf(LightOrange, Orange)))
        .aspectRatio(ratio = 1F)

    val calculationButtonModifier = Modifier
        .background(Brush.verticalGradient(colors = listOf(LightBlue, Blue)))
        .aspectRatio(ratio = 1f)

    val equalsButtonModifier = Modifier
        .background(Brush.verticalGradient(colors = listOf(LightBlue, Blue)))
        .aspectRatio(ratio = 1f)
}