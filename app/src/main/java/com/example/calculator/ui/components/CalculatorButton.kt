package com.example.calculator.ui.components

import android.os.Build
import android.view.SoundEffectConstants
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.LightOrange
import com.example.calculator.ui.theme.Orange
import com.example.calculator.util.HapticFeedback

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val view = LocalView.current
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClick()
                view.playSoundEffect(SoundEffectConstants.CLICK)
                HapticFeedback().triggerHapticFeedback(context)
            }
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = Color.White,
            fontSize = 28.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CleanButton(
    symbol: String,
    modifier: Modifier,
) {
    val view = LocalView.current
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                view.playSoundEffect(SoundEffectConstants.CLICK)
                HapticFeedback().triggerHapticFeedback(context)
            }
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = Color.White,
            fontSize = 28.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun CalculatorLandscapeButton(
    symbol: String,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    val view = LocalView.current
    val context = LocalContext.current

    Box(modifier = Modifier
        .clip(CircleShape)
        .height(70.dp)
        .clickable {
            onClick()
            view.playSoundEffect(SoundEffectConstants.CLICK)
            HapticFeedback().triggerHapticFeedback(context)
        }
        .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbol,
            color = Color.White,
            fontSize = 28.sp
        )
    }
}


val operationButtonModifier1 = Modifier
    .background(Brush.verticalGradient(colors = listOf(LightOrange, Orange)))
    .border(1.dp, Color.White, shape = RoundedCornerShape(30.dp))


@Preview
@Composable
fun CalcButton() {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .width(50.dp)
            .aspectRatio(1f)
            .then(operationButtonModifier1),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "7",
            color = Color.White,
            fontSize = 25.sp
        )
    }

}