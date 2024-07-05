package com.example.calculator

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import com.example.calculator.ui.screens.CalculatorLandscapeScreen
import com.example.calculator.ui.screens.CalculatorPortraitScreen
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                window.statusBarColor = getColor(R.color.black)
                val configuration = LocalConfiguration.current

                val viewModel: CalculatorMainScreenViewModel by viewModels()
                val state = viewModel.state

                when(configuration.orientation){
                    Configuration.ORIENTATION_PORTRAIT -> {
                        CalculatorPortraitScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)

                        )
                    }
                    else -> {
                        CalculatorLandscapeScreen(
                            state = state,
                            onEvent = viewModel::onEvent,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black)
                        )
                    }
                }
            }
        }
    }
}

