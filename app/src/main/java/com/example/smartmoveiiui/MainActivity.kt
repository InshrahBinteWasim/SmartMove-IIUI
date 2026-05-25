package com.example.smartmoveiiui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.ui.main.SmartMoveNavHost
import com.example.smartmoveiiui.ui.theme.SmartMoveIIUITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(lightScrim(), darkScrim()),
            navigationBarStyle = SystemBarStyle.auto(lightScrim(), darkScrim()),
        )

        val role = AppRole.fromIntentExtra(intent.getStringExtra(AppRole.EXTRA_NAME))

        setContent {
            SmartMoveIIUITheme {
                SmartMoveNavHost(appRole = role)
            }
        }
    }

    private fun lightScrim(): Int = android.graphics.Color.argb(0xE6, 0xF7, 0xFF, 0xFE)

    private fun darkScrim(): Int = android.graphics.Color.argb(0xE6, 0x07, 0x14, 0x11)
}
