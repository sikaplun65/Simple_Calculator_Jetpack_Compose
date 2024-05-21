package com.example.calculator.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class HapticFeedback {

    @RequiresApi(Build.VERSION_CODES.Q)
    fun triggerHapticFeedback(context: Context){
        val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)

        vibrator?.let {
            if (it.hasVibrator()){
                val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                it.vibrate(effect)
            }
        }
    }
}