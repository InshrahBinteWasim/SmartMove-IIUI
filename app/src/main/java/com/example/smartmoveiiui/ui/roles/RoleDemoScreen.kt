package com.example.smartmoveiiui.ui.roles

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.model.AppRole
import com.example.smartmoveiiui.ui.theme.AccentMint
import com.example.smartmoveiiui.ui.theme.CardDark
import com.example.smartmoveiiui.ui.theme.CardLight
import com.example.smartmoveiiui.ui.theme.GlowGreen
import com.example.smartmoveiiui.ui.theme.PrimaryGreen
import com.example.smartmoveiiui.ui.theme.PrimaryGreenLight
import com.example.smartmoveiiui.ui.theme.TextSecondaryDark
import com.example.smartmoveiiui.ui.theme.TextSecondaryLight
import com.example.smartmoveiiui.ui.theme.WarningAmber

private data class RoleCard(
    val role: AppRole,
    val title: String,
    val subtitle: String,
    val badge: String,
    val icon: ImageVector,
    val color: Color,
)

@Composable
fun RoleDemoScreen(
    onBack: () -> Unit,
    onSelectRole: (AppRole) -> Unit,
    modifier: Modifier = Modifier,
) {
    val roles = listOf(
        RoleCard(
            role = AppRole.COMMUTER,
            title = "Student / Employee",
            subtitle = "Live bus tracking, schedules, MoveBot, queries, feedback",
            badge = "#085041",
            icon = Icons.Outlined.Group,
            color = PrimaryGreen,
        ),
        RoleCard(
            role = AppRole.TRANSPORT_STAFF,
            title = "Transport Staff",
            subtitle = "Fleet monitor, query replies, announcements, duty profile",
            badge = "#27500A",
            icon = Icons.Outlined.DirectionsBus,
            color = Color(0xFF27500A),
        ),
        RoleCard(
            role = AppRole.SYSTEM_ADMINISTRATOR,
            title = "System Administrator",
            subtitle = "Users, routes, buses, schedules, reports, audit logs",
            badge = "#173404",
            icon = Icons.Outlined.AdminPanelSettings,
            color = Color(0xFF173404),
        ),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        if (isSystemInDarkTheme()) Color(0xFF071411) else Color(0xFFF7FFFE),
                        if (isSystemInDarkTheme()) Color(0xFF0D201C) else Color(0xFFE1F5EE),
                    )
                )
            )
    ) {
        RolePattern()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Text("Demo role switcher", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                        Text("Open every SmartMove workspace quickly during viva", style = MaterialTheme.typography.bodyMedium, color = secondaryText())
                    }
                }
            }
            item { RoleHero() }
            roles.forEach { role ->
                item {
                    RoleEntryCard(roleCard = role, onClick = { onSelectRole(role.role) })
                }
            }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = AccentMint.copy(alpha = 0.14f),
                    contentColor = PrimaryGreen,
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Demo strategy", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            "This avoids creating three Firebase accounts during presentation while still showing role-based access, HCI consistency, and complete navigation.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun RoleHero() {
    val infinite = rememberInfiniteTransition(label = "roleHero")
    val alpha by infinite.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(750), RepeatMode.Reverse),
        label = "alpha",
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(9.dp).alpha(alpha).clip(CircleShape).background(GlowGreen))
                Spacer(Modifier.width(8.dp))
                Text("Role-aware prototype", color = GlowGreen, style = MaterialTheme.typography.labelLarge)
            }
            Spacer(Modifier.height(14.dp))
            Text("Three dashboards, one polished demo path", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text(
                "Student, transport staff, and admin workflows stay visually consistent while using role-specific accents.",
                color = Color.White.copy(alpha = 0.78f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 6.dp),
            )
        }
    }
}

@Composable
private fun RoleEntryCard(roleCard: RoleCard, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(102.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = if (isSystemInDarkTheme()) CardDark else CardLight.copy(alpha = 0.74f)),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(17.dp))
                    .background(roleCard.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(roleCard.icon, contentDescription = null, tint = roleCard.color)
            }
            Spacer(Modifier.width(13.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(roleCard.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Outlined.VerifiedUser, contentDescription = null, tint = PrimaryGreenLight, modifier = Modifier.size(17.dp))
                }
                Text(roleCard.subtitle, style = MaterialTheme.typography.bodyMedium, color = secondaryText())
            }
            Surface(shape = RoundedCornerShape(50), color = roleCard.color.copy(alpha = 0.12f), contentColor = roleCard.color) {
                Text(roleCard.badge, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp), style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun RolePattern() {
    Canvas(Modifier.fillMaxSize()) {
        repeat(8) { x ->
            repeat(14) { y ->
                drawCircle(
                    color = AccentMint.copy(alpha = 0.04f),
                    radius = 5f,
                    center = Offset(x * 72f + 26f, y * 72f + 28f),
                )
            }
        }
    }
}

@Composable
private fun secondaryText(): Color = if (isSystemInDarkTheme()) TextSecondaryDark else TextSecondaryLight
