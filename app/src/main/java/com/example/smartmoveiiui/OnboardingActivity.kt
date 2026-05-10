package com.example.smartmoveiiui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.smartmoveiiui.ui.onboarding.IiuiOnboardingExperience

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(lightScrim(), darkScrim()),
            navigationBarStyle = SystemBarStyle.light(lightScrim(), darkScrim()),
        )

        setContent {
            IiuiOnboardingExperience(
                onSkip = { navigateToHomeAndFinish() },
                onAlreadyHaveAccount = { navigateToHomeAndFinish() },
                onFinishOnboarding = { navigateToHomeAndFinish() },
            )
        }
    }

    private fun navigateToHomeAndFinish() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun lightScrim(): Int = android.graphics.Color.argb(0xE6, 0xFF, 0xFF, 0xFF)

    private fun darkScrim(): Int = android.graphics.Color.argb(0x80, 0x33, 0x33, 0x33)
}
