package com.example.calculator.ui.components

import android.os.Build
import android.view.SoundEffectConstants
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        contentAlignment = Alignment.Center,
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