package com.example.smartmoveiiui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.ui.onboarding.IiuiOnboardingColors

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(lightScrimArgb(), darkScrimArgb()),
            navigationBarStyle = SystemBarStyle.light(lightScrimArgb(), darkScrimArgb()),
        )

        setContent {
            MaterialTheme(
                colorScheme = IiuiOnboardingColors,
                typography = Typography(),
            ) {
                AuthScreen(
                    onAuthenticated = { role ->
                        val intent = Intent(this, MainActivity::class.java).apply {
                            putExtra(AppRole.EXTRA_NAME, role.name)
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        }
                        startActivity(intent)
                        finish()
                    },
                )
            }
        }
    }

    private fun lightScrimArgb(): Int = android.graphics.Color.argb(0xE6, 0xFF, 0xFF, 0xFF)

    private fun darkScrimArgb(): Int = android.graphics.Color.argb(0x80, 0x33, 0x33, 0x33)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthScreen(
    onAuthenticated: (AppRole) -> Unit,
) {
    var tab by remember { mutableIntStateOf(0) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf(AppRole.COMMUTER) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.auth_title),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = stringResource(id = R.string.auth_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            TabRow(selectedTabIndex = tab) {
                Tab(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    text = { Text(stringResource(id = R.string.auth_tab_login)) },
                )
                Tab(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    text = { Text(stringResource(id = R.string.auth_tab_signup)) },
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.auth_email)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.auth_password)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            )

            Text(
                text = stringResource(id = R.string.auth_role_section),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = role == AppRole.COMMUTER,
                    onClick = { role = AppRole.COMMUTER },
                    label = { Text(stringResource(id = R.string.role_commuter)) },
                )
                FilterChip(
                    selected = role == AppRole.TRANSPORT_STAFF,
                    onClick = { role = AppRole.TRANSPORT_STAFF },
                    label = { Text(stringResource(id = R.string.role_transport_staff)) },
                )
                FilterChip(
                    selected = role == AppRole.SYSTEM_ADMINISTRATOR,
                    onClick = { role = AppRole.SYSTEM_ADMINISTRATOR },
                    label = { Text(stringResource(id = R.string.role_system_administrator)) },
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onAuthenticated(role) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = if (tab == 0) {
                        stringResource(id = R.string.auth_sign_in)
                    } else {
                        stringResource(id = R.string.auth_create_account)
                    },
                )
            }

            Text(
                text = stringResource(id = R.string.auth_mock_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
