package com.zaram.orcamentos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.zaram.orcamentos.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var screen by remember { mutableStateOf("welcome") }

            when (screen) {
                "welcome" -> WelcomeScreen(onNext = { screen = "form" })
                "form" -> FormScreen(onSubmit = { screen = "success" })
                "success" -> SuccessScreen()
            }
        }
    }
}
