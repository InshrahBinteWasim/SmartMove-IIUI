package com.example.smartmoveiiui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.smartmoveiiui.ui.splash.SmartMoveSplashScreen
import com.example.smartmoveiiui.ui.theme.SmartMoveIIUITheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : ComponentActivity() {

    private var navigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SmartMoveIIUITheme {
                SmartMoveSplashScreen(
                    onContinue = { navigateToOnboarding() }
                )
            }
        }

        lifecycleScope.launch {
            delay(4300)
            navigateToOnboarding()
        }
    }

    private fun navigateToOnboarding() {
        if (navigated) return
        navigated = true
        startActivity(Intent(this, OnboardingActivity::class.java))
        finish()
    }
}
