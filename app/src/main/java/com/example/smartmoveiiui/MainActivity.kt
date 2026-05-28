package com.example.smartmoveiiui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.ui.main.SmartMoveNavHost
import com.example.smartmoveiiui.ui.theme.SmartMoveIIUITheme
import com.example.smartmoveiiui.ui.theme.ThemePreference

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(lightScrim(), darkScrim()),
            navigationBarStyle = SystemBarStyle.auto(lightScrim(), darkScrim()),
        )

        val role = AppRole.fromIntentExtra(intent.getStringExtra(AppRole.EXTRA_NAME))

        setContent {
            var darkTheme by remember { mutableStateOf(ThemePreference.isDark(this)) }
            SmartMoveIIUITheme(darkTheme = darkTheme) {
                SmartMoveNavHost(
                    appRole = role,
                    isDarkTheme = darkTheme,
                    onToggleTheme = {
                        darkTheme = !darkTheme
                        ThemePreference.setDark(this, darkTheme)
                    }
                )
            }
        }
    }

    private fun lightScrim(): Int = android.graphics.Color.argb(0xE6, 0xF7, 0xFF, 0xFE)

    private fun darkScrim(): Int = android.graphics.Color.argb(0xE6, 0x07, 0x14, 0x11)
}
