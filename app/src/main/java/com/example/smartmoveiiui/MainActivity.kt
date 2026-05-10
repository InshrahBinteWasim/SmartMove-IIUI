package com.example.smartmoveiiui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.ui.Modifier
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.ui.main.SmartMoveNavHost
import com.example.smartmoveiiui.ui.onboarding.IiuiOnboardingColors

/**
 * Post-auth shell: shows exactly one role home (commuter, transport staff, or admin) with
 * navigation to feature areas — no dashboard widgets on the home itself.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val role = AppRole.fromIntentExtra(intent.getStringExtra(AppRole.EXTRA_NAME))

        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = IiuiOnboardingColors,
                typography = Typography(),
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SmartMoveNavHost(
                        appRole = role,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}
