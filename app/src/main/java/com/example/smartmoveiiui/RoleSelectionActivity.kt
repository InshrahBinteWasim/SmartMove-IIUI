package com.example.smartmoveiiui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.ui.onboarding.IiuiOnboardingColors

/**
 * Optional entry when skipping sign-in during development: picks [AppRole] and opens [MainActivity].
 */
class RoleSelectionActivity : ComponentActivity() {
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
                RoleSelectionRoute(
                    onBack = { finish() },
                    roleOptions = RoleOption.entries,
                    onRoleChosen = { role ->
                        startActivity(
                            Intent(this, MainActivity::class.java).apply {
                                putExtra(AppRole.EXTRA_NAME, role.name)
                            },
                        )
                        finish()
                    },
                )
            }
        }
    }

    private fun lightScrimArgb(): Int = android.graphics.Color.argb(0xE6, 0xFF, 0xFF, 0xFF)

    private fun darkScrimArgb(): Int = android.graphics.Color.argb(0x80, 0x33, 0x33, 0x33)
}

private enum class RoleOption(val labelRes: Int, val appRole: AppRole) {
    COMMUTER(R.string.role_commuter, AppRole.COMMUTER),
    STAFF(R.string.role_transport_staff, AppRole.TRANSPORT_STAFF),
    ADMIN(R.string.role_system_administrator, AppRole.SYSTEM_ADMINISTRATOR),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoleSelectionRoute(
    onBack: () -> Unit,
    onRoleChosen: (AppRole) -> Unit,
    roleOptions: List<RoleOption>,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.role_title),
                        fontWeight = FontWeight.SemiBold,
                    )
                },
                navigationIcon = {
                    TextButton(onClick = onBack, modifier = Modifier.padding(start = 4.dp)) {
                        Text(text = stringResource(id = R.string.onboarding_back))
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Text(
                    text = stringResource(id = R.string.role_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            items(roleOptions) { option ->
                OutlinedCard(
                    shape = RoundedCornerShape(18.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRoleChosen(option.appRole) },
                ) {
                    Column(
                        Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
                    ) {
                        Text(
                            text = stringResource(id = option.labelRes),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(18.dp)) }
        }
    }
}
