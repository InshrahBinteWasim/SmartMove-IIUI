package com.example.smartmoveiiui.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.AltRoute
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.R
import com.example.smartmoveiiui.navigation.FeatureCatalog
import com.example.smartmoveiiui.ui.theme.AccentMint
import com.example.smartmoveiiui.ui.theme.CardDark
import com.example.smartmoveiiui.ui.theme.CardLight
import com.example.smartmoveiiui.ui.theme.DangerRed
import com.example.smartmoveiiui.ui.theme.GlowGreen
import com.example.smartmoveiiui.ui.theme.PrimaryGreen
import com.example.smartmoveiiui.ui.theme.PrimaryGreenLight
import com.example.smartmoveiiui.ui.theme.TextSecondaryDark
import com.example.smartmoveiiui.ui.theme.TextSecondaryLight
import com.example.smartmoveiiui.ui.theme.WarningAmber
import kotlinx.coroutines.delay

private data class HomeTile(val featureId: String, val icon: ImageVector)
private data class RouteEta(val route: String, val bus: String, val driver: String, val eta: String, val status: String, val progress: Float)
private data class DashboardStat(val label: String, val value: String, val icon: ImageVector, val color: Color)

@Composable
fun CommuterHomeScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateToFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tiles = listOf(
        HomeTile(FeatureCatalog.Commuter.LIVE_TRACKING, Icons.Outlined.DirectionsBus),
        HomeTile(FeatureCatalog.Commuter.VIEW_SCHEDULE, Icons.Outlined.CalendarMonth),
        HomeTile(FeatureCatalog.Commuter.CHATBOT, Icons.Outlined.SmartToy),
        HomeTile(FeatureCatalog.Commuter.SUBMIT_QUERY, Icons.Outlined.MailOutline),
        HomeTile(FeatureCatalog.Commuter.MY_QUERIES, Icons.Outlined.Forum),
        HomeTile(FeatureCatalog.Commuter.NOTIFICATIONS, Icons.Outlined.NotificationsActive),
        HomeTile(FeatureCatalog.Commuter.ANNOUNCEMENTS, Icons.Outlined.Campaign),
        HomeTile(FeatureCatalog.Commuter.FAVORITES, Icons.Outlined.Star),
        HomeTile(FeatureCatalog.Commuter.ROLE_SWITCHER, Icons.Outlined.Group),
    )
    PremiumHomeScaffold(
        roleTitle = stringResource(id = R.string.home_subtitle_commuter),
        greeting = "Assalam-o-Alaikum, Inshrah",
        heroTitle = "Bus 07 is approaching G-10 stop",
        heroSubtitle = "ETA 4 min - Route: G-10 -> IIUI H-10",
        stats = listOf(
            DashboardStat("Active buses", "14", Icons.Outlined.DirectionsBus, PrimaryGreen),
            DashboardStat("Saved routes", "03", Icons.Outlined.Star, WarningAmber),
            DashboardStat("Alerts", "02", Icons.Outlined.NotificationsActive, DangerRed),
        ),
        routes = listOf(
            RouteEta("G-10 / F-10", "Bus 07", "Arshad Ali", "4 min", "On time", 0.76f),
            RouteEta("Faizabad", "Bus 11", "Nasir Mehmood", "11 min", "Heavy traffic", 0.52f),
            RouteEta("Rawalpindi Saddar", "Bus 05", "Tariq Usman", "18 min", "Delayed", 0.34f),
        ),
        tiles = tiles,
        onNavigateToFeature = onNavigateToFeature,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        modifier = modifier,
    )
}

@Composable
fun StaffHomeScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateToFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tiles = listOf(
        HomeTile(FeatureCatalog.Staff.LIVE_TRACKING, Icons.Outlined.DirectionsBus),
        HomeTile(FeatureCatalog.Staff.LIVE_BUS_MONITOR, Icons.Outlined.Analytics),
        HomeTile(FeatureCatalog.Staff.POST_ANNOUNCEMENTS, Icons.Outlined.Campaign),
        HomeTile(FeatureCatalog.Staff.ANNOUNCEMENT_HISTORY, Icons.Outlined.Schedule),
        HomeTile(FeatureCatalog.Staff.MANAGE_QUERIES, Icons.Outlined.Forum),
        HomeTile(FeatureCatalog.Staff.STAFF_PROFILE, Icons.Outlined.Person),
    )
    PremiumHomeScaffold(
        roleTitle = stringResource(id = R.string.home_subtitle_staff),
        greeting = "Transport operations",
        heroTitle = "14 buses reporting live location",
        heroSubtitle = "Low-cost mode: listeners reused, map refresh throttled",
        stats = listOf(
            DashboardStat("Fleet live", "14", Icons.Outlined.DirectionsBus, PrimaryGreen),
            DashboardStat("Queries", "08", Icons.Outlined.Forum, WarningAmber),
            DashboardStat("GPS alerts", "01", Icons.Outlined.NotificationsActive, DangerRed),
        ),
        routes = listOf(
            RouteEta("F-8 / F-10", "Bus 03", "Arshad Ali", "Live", "Tracking", 0.82f),
            RouteEta("Tramri", "Bus 14", "Nasir Mehmood", "Live", "Normal", 0.66f),
            RouteEta("Bhara Kahu", "Bus 11", "Tariq Usman", "Check", "Signal weak", 0.42f),
        ),
        tiles = tiles,
        onNavigateToFeature = onNavigateToFeature,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        modifier = modifier,
    )
}

@Composable
fun AdminHomeScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onNavigateToFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tiles = listOf(
        HomeTile(FeatureCatalog.Admin.MANAGE_USERS, Icons.Outlined.Group),
        HomeTile(FeatureCatalog.Admin.MANAGE_BUSES, Icons.Outlined.DirectionsBus),
        HomeTile(FeatureCatalog.Admin.MANAGE_SCHEDULE, Icons.Outlined.Schedule),
        HomeTile(FeatureCatalog.Admin.MANAGE_ROUTES, Icons.AutoMirrored.Outlined.AltRoute),
        HomeTile(FeatureCatalog.Admin.LIVE_TRACKING, Icons.Outlined.Analytics),
        HomeTile(FeatureCatalog.Admin.REPORTS_LOGS, Icons.Outlined.Analytics),
        HomeTile(FeatureCatalog.Admin.AUDIT_LOGS, Icons.Outlined.Schedule),
    )
    PremiumHomeScaffold(
        roleTitle = stringResource(id = R.string.home_subtitle_admin),
        greeting = "System control center",
        heroTitle = "SmartMove health score: 96%",
        heroSubtitle = "Firestore reads optimized with role-based dashboard slices",
        stats = listOf(
            DashboardStat("Users", "1.2k", Icons.Outlined.Group, PrimaryGreen),
            DashboardStat("Routes", "17", Icons.Outlined.Route, WarningAmber),
            DashboardStat("Reports", "05", Icons.Outlined.Analytics, AccentMint),
        ),
        routes = listOf(
            RouteEta("I-8 / I-10", "Bus 03", "Arshad Ali", "Live", "Healthy", 0.88f),
            RouteEta("Taxila", "Bus 14", "Nasir Mehmood", "Audit", "Schedule due", 0.58f),
            RouteEta("Chakwal", "Bus 11", "Tariq Usman", "Offline", "Review GPS", 0.26f),
        ),
        tiles = tiles,
        onNavigateToFeature = onNavigateToFeature,
        isDarkTheme = isDarkTheme,
        onToggleTheme = onToggleTheme,
        modifier = modifier,
    )
}

@Composable
private fun PremiumHomeScaffold(
    roleTitle: String,
    greeting: String,
    heroTitle: String,
    heroSubtitle: String,
    stats: List<DashboardStat>,
    routes: List<RouteEta>,
    tiles: List<HomeTile>,
    onNavigateToFeature: (String) -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(120)
        contentVisible = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(homeBackgroundBrush())
    ) {
        HomePattern()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(500)) + slideInVertically { -it / 4 },
                ) {
                    HomeTopBar(
                        roleTitle = roleTitle,
                        greeting = greeting,
                        isDarkTheme = isDarkTheme,
                        onToggleTheme = onToggleTheme
                    )
                }
            }
            item {
                AnimatedVisibility(
                    visible = contentVisible,
                    enter = fadeIn(tween(650)) + slideInVertically { it / 5 },
                ) {
                    LiveHeroCard(
                        title = heroTitle,
                        subtitle = heroSubtitle,
                onPrimaryAction = { onNavigateToFeature(tiles.first { it.featureId != FeatureCatalog.Commuter.ROLE_SWITCHER }.featureId) },
                    )
                }
            }
            item { StatStrip(stats = stats) }
            item {
                SectionTitle("Live routes", "Updated now")
            }
            items(routes, key = { it.route + it.bus }) { route ->
                RouteEtaCard(route)
            }
            item { SectionTitle("What do you need?", "Quick actions") }
            items(tiles, key = { it.featureId }) { tile ->
                FeatureEntryCard(
                    title = stringResource(id = FeatureCatalog.titleFor(tile.featureId)),
                    subtitle = stringResource(id = FeatureCatalog.subtitleFor(tile.featureId)),
                    icon = tile.icon,
                    onClick = {
                        if (tile.featureId == FeatureCatalog.Commuter.ROLE_SWITCHER) {
                            onNavigateToFeature(com.example.smartmoveiiui.navigation.HomeDestinations.ROLE_SWITCHER)
                        } else {
                            onNavigateToFeature(tile.featureId)
                        }
                    },
                )
            }
            item {
                AnnouncementPreview()
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun HomeTopBar(
    roleTitle: String,
    greeting: String,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = greeting,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = "SmartMove IIUI - $roleTitle",
                style = MaterialTheme.typography.bodyMedium,
                color = secondaryText(),
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                    contentDescription = "Change theme",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen),
                contentAlignment = Alignment.Center,
            ) {
                Text("IIUI", color = Color.White, style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun LiveHeroCard(
    title: String,
    subtitle: String,
    onPrimaryAction: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "homeHero")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "pulse",
    )
    val busProgress by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(tween(3000), RepeatMode.Reverse),
        label = "busProgress",
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = PrimaryGreen),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatusChip("Live", pulse)
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.14f),
                    contentColor = Color.White,
                ) {
                    Text(
                        "Peak 7:00-9:00 AM",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
            Spacer(Modifier.height(14.dp))
            Text(title, color = Color.White, style = MaterialTheme.typography.headlineMedium)
            Text(
                subtitle,
                color = Color.White.copy(alpha = 0.78f),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp),
            )
            Spacer(Modifier.height(14.dp))
            MiniMapPreview(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(145.dp),
                progress = busProgress,
            )
            Spacer(Modifier.height(14.dp))
            Button(
                onClick = onPrimaryAction,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = PrimaryGreen,
                ),
            ) {
                Icon(Icons.Outlined.DirectionsBus, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Open live map")
            }
        }
    }
}

@Composable
private fun StatStrip(stats: List<DashboardStat>) {
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
        stats.forEach { stat ->
            StatCard(stat = stat, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun StatCard(stat: DashboardStat, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(88.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor()),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Icon(stat.icon, contentDescription = null, tint = stat.color, modifier = Modifier.size(22.dp))
            Text(stat.value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(stat.label, style = MaterialTheme.typography.labelMedium, color = secondaryText(), maxLines = 1)
        }
    }
}

@Composable
private fun RouteEtaCard(route: RouteEta) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = cardColor()),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Outlined.DirectionsBus, contentDescription = null, tint = PrimaryGreen)
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(route.route, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text("${route.bus} - ${route.driver}", style = MaterialTheme.typography.bodyMedium, color = secondaryText())
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(route.eta, style = MaterialTheme.typography.titleMedium, color = PrimaryGreen, fontWeight = FontWeight.Bold)
                    Text(route.status, style = MaterialTheme.typography.labelMedium, color = statusColor(route.status))
                }
            }
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { route.progress },
                modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(50)),
                color = if (route.status.contains("delayed", ignoreCase = true)) WarningAmber else PrimaryGreenLight,
                trackColor = PrimaryGreen.copy(alpha = 0.10f),
            )
        }
    }
}

@Composable
private fun FeatureEntryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = cardColor()),
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(AccentMint.copy(alpha = 0.16f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = PrimaryGreen)
            }
            Column(
                modifier = Modifier.padding(start = 14.dp).weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = secondaryText(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String, chip: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Surface(shape = RoundedCornerShape(50), color = AccentMint.copy(alpha = 0.14f), contentColor = PrimaryGreen) {
            Text(chip, modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun AnnouncementPreview() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = WarningAmber.copy(alpha = 0.13f)),
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Campaign, contentDescription = null, tint = WarningAmber)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("Today 3:30 PM", style = MaterialTheme.typography.labelLarge, color = WarningAmber)
                Text(
                    "F-8 and G-9 buses may leave 8 minutes late due to traffic near Kashmir Highway.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

@Composable
private fun StatusChip(text: String, alpha: Float) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.16f))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(alpha)
                .clip(CircleShape)
                .background(GlowGreen),
        )
        Spacer(Modifier.width(7.dp))
        Text(text, color = Color.White, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun MiniMapPreview(modifier: Modifier, progress: Float) {
    Canvas(modifier = modifier.clip(RoundedCornerShape(22.dp)).background(Color.White.copy(alpha = 0.12f))) {
        val route = Path().apply {
            moveTo(size.width * 0.10f, size.height * 0.70f)
            cubicTo(size.width * 0.28f, size.height * 0.18f, size.width * 0.52f, size.height * 0.95f, size.width * 0.90f, size.height * 0.34f)
        }
        drawPath(route, color = Color.White.copy(alpha = 0.26f), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 18f, cap = StrokeCap.Round))
        drawPath(route, color = GlowGreen, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 6f, cap = StrokeCap.Round))

        val stops = listOf(Offset(size.width * 0.10f, size.height * 0.70f), Offset(size.width * 0.44f, size.height * 0.54f), Offset(size.width * 0.90f, size.height * 0.34f))
        stops.forEach {
            drawCircle(Color.White, radius = 10f, center = it)
            drawCircle(PrimaryGreen, radius = 5f, center = it)
        }

        val bus = Offset(
            x = size.width * (0.10f + (0.80f * progress)),
            y = size.height * (0.70f - (0.36f * progress) + kotlin.math.sin(progress * 6f) * 0.10f),
        )
        drawRoundRect(
            color = Color(0xFF073E34),
            topLeft = Offset(bus.x - 28f, bus.y - 16f),
            size = Size(56f, 30f),
            cornerRadius = CornerRadius(10f, 10f),
        )
        drawRect(Color(0xFFBCEFE2), topLeft = Offset(bus.x - 18f, bus.y - 8f), size = Size(26f, 9f))
        drawCircle(Color(0xFF111A17), radius = 5f, center = Offset(bus.x - 16f, bus.y + 15f))
        drawCircle(Color(0xFF111A17), radius = 5f, center = Offset(bus.x + 17f, bus.y + 15f))
    }
}

@Composable
private fun HomePattern() {
    Canvas(Modifier.fillMaxSize()) {
        for (x in 0..7) {
            for (y in 0..14) {
                drawCircle(
                    color = AccentMint.copy(alpha = 0.045f),
                    radius = 5f,
                    center = Offset(x * 72f + 20f, y * 72f + 30f),
                )
            }
        }
    }
}

@Composable
private fun homeBackgroundBrush(): Brush {
    return Brush.verticalGradient(
        listOf(
            if (isSystemInDarkTheme()) Color(0xFF071411) else Color(0xFFF7FFFE),
            if (isSystemInDarkTheme()) Color(0xFF0D201C) else Color(0xFFE1F5EE),
        )
    )
}

@Composable
private fun cardColor(): Color = if (isSystemInDarkTheme()) CardDark else CardLight.copy(alpha = 0.72f)

@Composable
private fun secondaryText(): Color = if (isSystemInDarkTheme()) TextSecondaryDark else TextSecondaryLight

private fun statusColor(status: String): Color = when {
    status.contains("delay", ignoreCase = true) -> WarningAmber
    status.contains("weak", ignoreCase = true) -> WarningAmber
    status.contains("offline", ignoreCase = true) -> DangerRed
    else -> PrimaryGreenLight
}
