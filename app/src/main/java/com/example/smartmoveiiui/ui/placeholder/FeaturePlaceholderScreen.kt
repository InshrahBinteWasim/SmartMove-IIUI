package com.example.smartmoveiiui.ui.placeholder

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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.SmartToy
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
import androidx.compose.ui.graphics.drawscope.Stroke
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

private data class DetailRow(val title: String, val subtitle: String, val meta: String, val icon: ImageVector, val tone: Color)

@Composable
fun FeaturePlaceholderScreen(
    featureId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val title = stringResource(id = FeatureCatalog.titleFor(featureId))
    val detail = remember(featureId) { featureDetail(featureId) }
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(featureId) {
        visible = false
        delay(80)
        visible = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(screenBrush())
    ) {
        DetailPattern()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                DetailTopBar(title = title, onBack = onBack)
            }
            item {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(tween(450)) + slideInVertically { it / 5 },
                ) {
                    FeatureHero(detail = detail)
                }
            }
            if (featureId.contains("tracking")) {
                item { LiveMapDetail() }
            } else {
                item { OperationalPreview(detail = detail) }
            }
            item { FeatureInteractionPanel(featureId) }
            item { SectionLabel("Realistic demo data", "HCI #6 recognition") }
            items(detail.rows, key = { it.title }) { row ->
                DetailInfoCard(row)
            }
            item {
                StateGallery()
                CostOptimizationCard(featureId)
                Spacer(Modifier.height(18.dp))
            }
        }
    }
}

@Composable
private fun DetailTopBar(title: String, onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.nav_back))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("SmartMove IIUI module preview", style = MaterialTheme.typography.bodyMedium, color = secondaryText())
        }
    }
}

@Composable
private fun FeatureHero(detail: FeatureDetail) {
    val infinite = rememberInfiniteTransition(label = "detailHero")
    val blink by infinite.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse),
        label = "blink",
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(detail.icon, contentDescription = null, tint = Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(detail.kicker, color = GlowGreen, style = MaterialTheme.typography.labelLarge)
                        Text(detail.status, color = Color.White.copy(alpha = 0.78f), style = MaterialTheme.typography.bodyMedium)
                    }
                }
                LiveDot(alpha = blink, label = "Live")
            }
            Spacer(Modifier.height(16.dp))
            Text(detail.headline, style = MaterialTheme.typography.headlineMedium, color = Color.White)
            Text(
                detail.supporting,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.78f),
                modifier = Modifier.padding(top = 5.dp),
            )
        }
    }
}

@Composable
private fun LiveMapDetail() {
    val infinite = rememberInfiniteTransition(label = "map")
    val busProgress by infinite.animateFloat(
        initialValue = 0.08f,
        targetValue = 0.88f,
        animationSpec = infiniteRepeatable(tween(3600), RepeatMode.Reverse),
        label = "busProgress",
    )
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = cardColor()),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Brush.verticalGradient(listOf(Color(0xFFE1F5EE), Color(0xFFBFE9DD))))
            ) {
                CampusRouteMap(progress = busProgress)
                Surface(
                    modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                    shape = RoundedCornerShape(50),
                    color = Color.White.copy(alpha = 0.88f),
                    contentColor = PrimaryGreen,
                ) {
                    Text("Static map preview + throttled live marker", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium)
                }
            }
            Spacer(Modifier.height(14.dp))
            BottomSheetPreview()
        }
    }
}

@Composable
private fun BottomSheetPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.DirectionsBus, contentDescription = null, tint = PrimaryGreen)
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Bus 07 - G-10 / F-10", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text("Driver: Arshad Ali - Next stop: IIUI Gate 2", style = MaterialTheme.typography.bodyMedium, color = secondaryText())
            }
            Text("4 min", color = PrimaryGreen, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { 0.76f },
            modifier = Modifier.fillMaxWidth().height(7.dp).clip(RoundedCornerShape(50)),
            color = PrimaryGreenLight,
            trackColor = PrimaryGreen.copy(alpha = 0.10f),
        )
    }
}

@Composable
private fun OperationalPreview(detail: FeatureDetail) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor()),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(detail.icon, contentDescription = null, tint = PrimaryGreen)
                Spacer(Modifier.width(10.dp))
                Text(detail.previewTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(12.dp))
            detail.previewLines.forEach { line ->
                Row(modifier = Modifier.padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(AccentMint))
                    Spacer(Modifier.width(9.dp))
                    Text(line, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}

@Composable
private fun FeatureInteractionPanel(featureId: String) {
    when (featureId) {
        FeatureCatalog.Commuter.ROUTE_DETAIL -> RouteTimelinePanel()
        FeatureCatalog.Commuter.VIEW_SCHEDULE,
        FeatureCatalog.Commuter.SCHEDULE_DETAIL,
        FeatureCatalog.Admin.MANAGE_SCHEDULE -> ScheduleFilterPanel()
        FeatureCatalog.Commuter.CHATBOT -> ChatPreviewPanel()
        FeatureCatalog.Commuter.SUBMIT_QUERY -> QueryFormPanel()
        FeatureCatalog.Commuter.MY_QUERIES,
        FeatureCatalog.Staff.MANAGE_QUERIES -> QueryQueuePanel()
        FeatureCatalog.Commuter.ANNOUNCEMENTS -> AnnouncementInboxPanel()
        FeatureCatalog.Staff.POST_ANNOUNCEMENTS -> PostAnnouncementPanel()
        FeatureCatalog.Staff.ANNOUNCEMENT_HISTORY -> AnnouncementHistoryPanel()
        FeatureCatalog.Commuter.NOTIFICATION_SETTINGS -> NotificationTogglePanel()
        FeatureCatalog.Commuter.FEEDBACK -> FeedbackRatingPanel()
        FeatureCatalog.Admin.MANAGE_USERS -> UserRoleManagerPanel()
        FeatureCatalog.Admin.MANAGE_SCHEDULE -> ScheduleManagerPanel()
        FeatureCatalog.Admin.REPORTS_LOGS,
        FeatureCatalog.Admin.AUDIT_LOGS -> ReportsMiniChartPanel()
        FeatureCatalog.Admin.MANAGE_ROUTES -> RouteManagerPanel()
        FeatureCatalog.Admin.MANAGE_BUSES,
        FeatureCatalog.Staff.LIVE_BUS_MONITOR -> FleetHealthPanel()
        else -> HciAnnotationPanel()
    }
}

@Composable
private fun RouteTimelinePanel() {
    InteractionCard("Route timeline", "HCI #2 real-world match") {
        listOf("G-10 Markaz", "F-10 Gate", "IIUI Gate 2").forEachIndexed { index, stop ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 5.dp)) {
                Box(
                    Modifier.size(26.dp).clip(CircleShape).background(if (index == 0) PrimaryGreen else AccentMint.copy(alpha = 0.22f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("${index + 1}", color = if (index == 0) Color.White else PrimaryGreen, style = MaterialTheme.typography.labelMedium)
                }
                Spacer(Modifier.width(10.dp))
                Column(Modifier.weight(1f)) {
                    Text(stop, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(if (index == 0) "Bus 07 arriving now" else "Estimated after ${index * 5 + 4} min", style = MaterialTheme.typography.bodyMedium, color = secondaryText())
                }
            }
        }
    }
}

@Composable
private fun ScheduleFilterPanel() {
    InteractionCard("Schedule filters", "HCI #7 efficiency") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilterChipMock("Male", true, Modifier.weight(1f))
            FilterChipMock("Female", false, Modifier.weight(1f))
            FilterChipMock("Friday", false, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        ScheduleSlot("06:00 AM", "08:15 AM", "Morning pickup", PrimaryGreen)
        ScheduleSlot("01:40 PM", "03:30 PM", "Afternoon return", WarningAmber)
        ScheduleSlot("07:20 PM", "09:00 PM", "Evening service", AccentMint)
    }
}

@Composable
private fun QueryFormPanel() {
    InteractionCard("Query form preview", "HCI #5 error prevention") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilterChipMock("Late bus", true, Modifier.weight(1f))
            FilterChipMock("Missed stop", false, Modifier.weight(1f))
            FilterChipMock("Other", false, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        FormFieldMock("Route", "Rawalpindi Saddar - Bus 05")
        FormFieldMock("Message", "Bus has not reached Committee Chowk stop.")
        AttachmentPickerMock("Attach proof", "Photo, PDF, or document - optional")
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
        ) {
            Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Submit query")
        }
    }
}

@Composable
private fun QueryQueuePanel() {
    InteractionCard("Query queue", "HCI #1 status") {
        QueueRow("Q-1042", "Bus 05 late near Committee Chowk", "Open", WarningAmber)
        QueueRow("Q-1038", "Stop location clarification", "Replied", PrimaryGreen)
        QueueRow("Q-1035", "Driver contact needed", "Closed", AccentMint)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilterChipMock("Open", true, Modifier.weight(1f))
            FilterChipMock("Assigned", false, Modifier.weight(1f))
            FilterChipMock("Closed", false, Modifier.weight(1f))
        }
    }
}

@Composable
private fun AnnouncementInboxPanel() {
    InteractionCard("Announcement inbox", "HCI #8 minimalist") {
        AnnouncementRow("Delay alert", "F-8 and G-9 buses delayed 8 minutes", "New", WarningAmber)
        AnnouncementRow("Route change", "Tramri stop shifted for today", "High", DangerRed)
        AnnouncementRow("Schedule posted", "Saturday pickup timings updated", "Seen", PrimaryGreen)
    }
}

@Composable
private fun PostAnnouncementPanel() {
    InteractionCard("Post announcement", "HCI #5 prevention") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilterChipMock("Delay", true, Modifier.weight(1f))
            FilterChipMock("Cancel", false, Modifier.weight(1f))
            FilterChipMock("Route", false, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        FormFieldMock("Audience", "Favorite users of F-8, G-9, and G-10")
        FormFieldMock("Message", "Bus 07 will arrive 8 minutes late due to traffic.")
        AttachmentPickerMock("Attach notice", "Optional PDF/image for official circular")
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
        ) {
            Icon(Icons.Outlined.Campaign, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Publish update")
        }
    }
}

@Composable
private fun AnnouncementHistoryPanel() {
    InteractionCard("Delivery history", "HCI #1 status") {
        DeliveryRow("F-8 delay notice", 0.94f, "246 read", PrimaryGreen)
        DeliveryRow("Kahuta stop shift", 0.62f, "18 pending", WarningAmber)
        DeliveryRow("Gate 2 reminder", 0.35f, "Scheduled", AccentMint)
    }
}

@Composable
private fun ChatPreviewPanel() {
    InteractionCard("MoveBot chat", "HCI #10 help") {
        ChatBubble("Which bus goes to F-10?", fromUser = true)
        ChatBubble("Bus 07 and Bus 03 cover G-10/F-10. Bus 07 arrives in 4 min.", fromUser = false)
        ChatBubble("Show next female schedule.", fromUser = true)
        ChatBubble("12:40 PM -> 02:30 PM. Friday special: 01:30 PM -> 03:20 PM.", fromUser = false)
    }
}

@Composable
private fun NotificationTogglePanel() {
    InteractionCard("Notification controls", "HCI #3 control") {
        ToggleRowMock("Delay alerts", "Favorite routes only", true)
        ToggleRowMock("GPS stop reminder", "300m before saved stop", true)
        ToggleRowMock("Non-critical night alerts", "Muted during quiet hours", false)
    }
}

@Composable
private fun FeedbackRatingPanel() {
    InteractionCard("Trip feedback", "HCI #9 recovery") {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            repeat(5) { index ->
                Text(if (index < 4) "*" else "*", color = if (index < 4) WarningAmber else secondaryText(), style = MaterialTheme.typography.headlineMedium)
            }
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilterChipMock("On time", true, Modifier.weight(1f))
            FilterChipMock("Clean", true, Modifier.weight(1f))
            FilterChipMock("Safe", true, Modifier.weight(1f))
        }
        Spacer(Modifier.height(10.dp))
        FormFieldMock("Suggestion", "Add stop photo for first semester students.")
        AttachmentPickerMock("Attach image", "Optional bus/stop photo")
    }
}

@Composable
private fun UserRoleManagerPanel() {
    InteractionCard("User role management", "HCI #4 consistency") {
        UserRoleRow("Inshrah Binte Wasim", "Student / Employee", "#085041", PrimaryGreen)
        UserRoleRow("Arshad Ali", "Transport Staff", "#27500A", Color(0xFF27500A))
        UserRoleRow("System Admin", "Administrator", "#173404", Color(0xFF173404))
        Spacer(Modifier.height(8.dp))
        FormFieldMock("Pending approval", "name.bsse4518@iiu.edu.pk - verify IIUI domain")
    }
}

@Composable
private fun ScheduleManagerPanel() {
    InteractionCard("Schedule manager", "HCI #5 prevention") {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilterChipMock("Male", true, Modifier.weight(1f))
            FilterChipMock("Female", false, Modifier.weight(1f))
            FilterChipMock("Friday", true, Modifier.weight(1f))
        }
        Spacer(Modifier.height(12.dp))
        FormFieldMock("Route", "G-10 / F-10")
        FormFieldMock("Slot", "01:40 PM -> 03:30 PM")
        FormFieldMock("Validation", "Prevents overlapping bus assignments")
    }
}

@Composable
private fun ReportsMiniChartPanel() {
    InteractionCard("Analytics snapshot", "HCI #1 status") {
        MiniBar("On-time trips", 0.82f, PrimaryGreen)
        MiniBar("Resolved queries", 0.74f, AccentMint)
        MiniBar("Delay risk", 0.38f, WarningAmber)
        Spacer(Modifier.height(8.dp))
        AttachmentPickerMock("Export report", "PDF/CSV export prepared for admin")
    }
}

@Composable
private fun QueueRow(id: String, title: String, status: String, tone: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = RoundedCornerShape(12.dp), color = tone.copy(alpha = 0.13f), contentColor = tone) {
            Text(id, modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp), style = MaterialTheme.typography.labelMedium)
        }
        Spacer(Modifier.width(10.dp))
        Text(title, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        Text(status, style = MaterialTheme.typography.labelLarge, color = tone)
    }
}

@Composable
private fun AnnouncementRow(title: String, body: String, meta: String, tone: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(34.dp).clip(CircleShape).background(tone.copy(alpha = 0.15f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Campaign, contentDescription = null, tint = tone, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(body, style = MaterialTheme.typography.bodyMedium, color = secondaryText())
        }
        Text(meta, style = MaterialTheme.typography.labelLarge, color = tone)
    }
}

@Composable
private fun DeliveryRow(label: String, value: Float, meta: String, tone: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(meta, style = MaterialTheme.typography.labelLarge, color = tone)
        }
        Spacer(Modifier.height(5.dp))
        LinearProgressIndicator(
            progress = { value },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
            color = tone,
            trackColor = tone.copy(alpha = 0.13f),
        )
    }
}

@Composable
private fun UserRoleRow(name: String, role: String, badge: String, tone: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(38.dp).clip(RoundedCornerShape(13.dp)).background(tone.copy(alpha = 0.14f)), contentAlignment = Alignment.Center) {
            Icon(Icons.Outlined.Group, contentDescription = null, tint = tone, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(role, style = MaterialTheme.typography.bodyMedium, color = secondaryText())
        }
        Surface(shape = RoundedCornerShape(50), color = tone.copy(alpha = 0.12f), contentColor = tone) {
            Text(badge, modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp), style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun RouteManagerPanel() {
    InteractionCard("Route manager", "HCI #4 standards") {
        FormFieldMock("Route name", "G-10 / F-10")
        FormFieldMock("Stops", "G-10 Markaz, F-10 Gate, IIUI Gate 2")
        AttachmentPickerMock("Upload stop list", "Optional CSV/PDF route sheet")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            FilterChipMock("Active", true, Modifier.weight(1f))
            FilterChipMock("Peak", true, Modifier.weight(1f))
            FilterChipMock("Cached", true, Modifier.weight(1f))
        }
    }
}

@Composable
private fun FleetHealthPanel() {
    InteractionCard("Fleet health", "HCI #1 status") {
        MiniBar("Bus 07 GPS", 0.94f, PrimaryGreen)
        MiniBar("Bus 11 traffic", 0.58f, WarningAmber)
        MiniBar("Bus 14 signal", 0.32f, DangerRed)
    }
}

@Composable
private fun AttachmentPickerMock(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(PrimaryGreen.copy(alpha = 0.08f))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(AccentMint.copy(alpha = 0.20f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.MailOutline, contentDescription = null, tint = PrimaryGreen)
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = secondaryText())
        }
        Surface(shape = RoundedCornerShape(50), color = PrimaryGreen, contentColor = Color.White) {
            Text("Upload", modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun HciAnnotationPanel() {
    InteractionCard("Design annotations", "Nielsen heuristics") {
        Text("Visible states, route-specific content, clear recovery actions, and low-cost data loading patterns are intentionally shown for FYP evaluation.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun InteractionCard(title: String, chip: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor()),
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Surface(shape = RoundedCornerShape(50), color = AccentMint.copy(alpha = 0.14f), contentColor = PrimaryGreen) {
                    Text(chip, modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp), style = MaterialTheme.typography.labelMedium)
                }
            }
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun FilterChipMock(text: String, selected: Boolean, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(38.dp),
        shape = RoundedCornerShape(50),
        color = if (selected) PrimaryGreen else AccentMint.copy(alpha = 0.12f),
        contentColor = if (selected) Color.White else PrimaryGreen,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun ScheduleSlot(start: String, end: String, label: String, tone: Color) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(9.dp).clip(CircleShape).background(tone))
        Spacer(Modifier.width(10.dp))
        Text("$start -> $end", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(label, style = MaterialTheme.typography.labelLarge, color = secondaryText())
    }
}

@Composable
private fun FormFieldMock(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(AccentMint.copy(alpha = 0.10f))
            .padding(12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = PrimaryGreen)
        Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun ChatBubble(text: String, fromUser: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = if (fromUser) Arrangement.End else Arrangement.Start) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = if (fromUser) PrimaryGreen else AccentMint.copy(alpha = 0.16f),
            contentColor = if (fromUser) Color.White else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(0.82f),
        ) {
            Text(text, modifier = Modifier.padding(horizontal = 12.dp, vertical = 9.dp), style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ToggleRowMock(title: String, subtitle: String, enabled: Boolean) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = secondaryText())
        }
        Box(
            modifier = Modifier
                .width(46.dp)
                .height(28.dp)
                .clip(RoundedCornerShape(50))
                .background(if (enabled) PrimaryGreen else secondaryText().copy(alpha = 0.25f))
                .padding(3.dp),
            contentAlignment = if (enabled) Alignment.CenterEnd else Alignment.CenterStart,
        ) {
            Box(Modifier.size(22.dp).clip(CircleShape).background(Color.White))
        }
    }
}

@Composable
private fun MiniBar(label: String, value: Float, tone: Color) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Text("${(value * 100).toInt()}%", style = MaterialTheme.typography.labelLarge, color = tone)
        }
        Spacer(Modifier.height(5.dp))
        LinearProgressIndicator(
            progress = { value },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)),
            color = tone,
            trackColor = tone.copy(alpha = 0.13f),
        )
    }
}

@Composable
private fun DetailInfoCard(row: DetailRow) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = cardColor()),
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(row.tone.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(row.icon, contentDescription = null, tint = row.tone)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(row.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(row.subtitle, style = MaterialTheme.typography.bodyMedium, color = secondaryText(), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Text(row.meta, style = MaterialTheme.typography.labelLarge, color = row.tone)
        }
    }
}

@Composable
private fun StateGallery() {
    SectionLabel("Interaction states", "HCI #9 recovery")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        StatePill("Loading", Icons.Outlined.Schedule, WarningAmber, Modifier.weight(1f))
        StatePill("Empty", Icons.Outlined.ErrorOutline, secondaryText(), Modifier.weight(1f))
        StatePill("Success", Icons.Outlined.CheckCircle, PrimaryGreen, Modifier.weight(1f))
    }
}

@Composable
private fun StatePill(label: String, icon: ImageVector, tone: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(16.dp),
        color = tone.copy(alpha = 0.12f),
        contentColor = tone,
    ) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun CostOptimizationCard(featureId: String) {
    val message = when {
        featureId.contains("tracking") -> "Uses cached route shapes, one active listener per selected bus, and animated marker interpolation between sparse GPS updates."
        featureId.contains("schedule") -> "Schedules are cache-first and refresh lazily, avoiding repeated Firestore reads during demo navigation."
        else -> "The screen is designed for paginated reads, role-based queries, and local optimistic UI states."
    }
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 14.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = AccentMint.copy(alpha = 0.14f)),
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text("Free-tier optimization", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = PrimaryGreen)
            Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 4.dp))
        }
    }
}

@Composable
private fun SectionLabel(title: String, chip: String) {
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
private fun LiveDot(alpha: Float, label: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.15f))
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(8.dp).alpha(alpha).clip(CircleShape).background(GlowGreen))
        Spacer(Modifier.width(7.dp))
        Text(label, color = Color.White, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun CampusRouteMap(progress: Float) {
    Canvas(Modifier.fillMaxSize()) {
        val roadColor = Color(0xFF085041).copy(alpha = 0.18f)
        repeat(6) { index ->
            val y = size.height * (0.18f + index * 0.13f)
            drawLine(roadColor, Offset(0f, y), Offset(size.width, y + 20f), strokeWidth = 18f, cap = StrokeCap.Round)
        }
        val route = Path().apply {
            moveTo(size.width * 0.12f, size.height * 0.78f)
            cubicTo(size.width * 0.28f, size.height * 0.28f, size.width * 0.52f, size.height * 0.92f, size.width * 0.88f, size.height * 0.24f)
        }
        drawPath(route, Color.White.copy(alpha = 0.82f), style = Stroke(width = 20f, cap = StrokeCap.Round))
        drawPath(route, PrimaryGreen, style = Stroke(width = 7f, cap = StrokeCap.Round))
        val stops = listOf(
            Offset(size.width * 0.12f, size.height * 0.78f),
            Offset(size.width * 0.38f, size.height * 0.54f),
            Offset(size.width * 0.64f, size.height * 0.58f),
            Offset(size.width * 0.88f, size.height * 0.24f),
        )
        stops.forEach {
            drawCircle(Color.White, 11f, it)
            drawCircle(PrimaryGreen, 5f, it)
        }
        val bus = Offset(
            x = size.width * (0.12f + 0.76f * progress),
            y = size.height * (0.78f - 0.54f * progress + kotlin.math.sin(progress * 8f) * 0.09f),
        )
        drawRoundRect(Color(0xFF073E34), Offset(bus.x - 30f, bus.y - 17f), Size(60f, 34f), CornerRadius(11f, 11f))
        drawRect(Color(0xFFBCEFE2), Offset(bus.x - 18f, bus.y - 8f), Size(30f, 10f))
        drawCircle(Color(0xFF101816), 5f, Offset(bus.x - 17f, bus.y + 17f))
        drawCircle(Color(0xFF101816), 5f, Offset(bus.x + 18f, bus.y + 17f))
    }
}

@Composable
private fun DetailPattern() {
    Canvas(Modifier.fillMaxSize()) {
        for (x in 0..7) {
            for (y in 0..14) {
                drawCircle(AccentMint.copy(alpha = 0.04f), radius = 5f, center = Offset(x * 72f + 24f, y * 72f + 26f))
            }
        }
    }
}

@Composable
private fun screenBrush(): Brush = Brush.verticalGradient(
    listOf(
        if (isSystemInDarkTheme()) Color(0xFF071411) else Color(0xFFF7FFFE),
        if (isSystemInDarkTheme()) Color(0xFF0D201C) else Color(0xFFE1F5EE),
    )
)

@Composable
private fun cardColor(): Color = if (isSystemInDarkTheme()) CardDark else CardLight.copy(alpha = 0.72f)

@Composable
private fun secondaryText(): Color = if (isSystemInDarkTheme()) TextSecondaryDark else TextSecondaryLight

private data class FeatureDetail(
    val icon: ImageVector,
    val kicker: String,
    val status: String,
    val headline: String,
    val supporting: String,
    val previewTitle: String,
    val previewLines: List<String>,
    val rows: List<DetailRow>,
)

private fun featureDetail(featureId: String): FeatureDetail {
    return when (featureId) {
        FeatureCatalog.Commuter.LIVE_TRACKING, FeatureCatalog.Staff.LIVE_TRACKING, FeatureCatalog.Admin.LIVE_TRACKING -> FeatureDetail(
            icon = Icons.Outlined.DirectionsBus,
            kicker = "Live Bus Tracking",
            status = "Last sync: 12 sec ago",
            headline = "Animated bus movement with smart ETA",
            supporting = "Designed like a lightweight Google Maps/Careem view using cached route shapes and sparse GPS updates.",
            previewTitle = "Map preview",
            previewLines = listOf("Bus 07 nearing G-10", "Bus 11 delayed near Faizabad", "Bus 05 departing Saddar"),
            rows = listOf(
                DetailRow("G-10 / F-10", "Bus 07 - Arshad Ali", "4 min", Icons.Outlined.LocationOn, PrimaryGreen),
                DetailRow("Faizabad", "Bus 11 - Nasir Mehmood", "11 min", Icons.Outlined.DirectionsBus, WarningAmber),
                DetailRow("Rawalpindi Saddar", "Bus 05 - Tariq Usman", "18 min", Icons.Outlined.Route, DangerRed),
            )
        )

        FeatureCatalog.Commuter.ROUTE_DETAIL -> FeatureDetail(
            icon = Icons.Outlined.Route,
            kicker = "Route Detail",
            status = "Bottom sheet interaction",
            headline = "Stops, driver, ETA, and route confidence in one panel",
            supporting = "The route detail layout uses a compact bottom-sheet mental model so students stay oriented while checking stops.",
            previewTitle = "G-10 / F-10 route sheet",
            previewLines = listOf("Current bus: Bus 07", "Driver: Arshad Ali", "Stops: G-10 Markaz -> F-10 Gate -> IIUI H-10"),
            rows = listOf(
                DetailRow("G-10 Markaz", "Next pickup - blinking GPS dot", "4 min", Icons.Outlined.LocationOn, PrimaryGreen),
                DetailRow("F-10 Gate", "Second stop after current pickup", "9 min", Icons.Outlined.Route, AccentMint),
                DetailRow("IIUI Gate 2", "Arrival near campus", "18 min", Icons.Outlined.DirectionsBus, WarningAmber),
            )
        )

        FeatureCatalog.Commuter.VIEW_SCHEDULE, FeatureCatalog.Admin.MANAGE_SCHEDULE -> FeatureDetail(
            icon = Icons.Outlined.CalendarMonth,
            kicker = "Smart Schedule",
            status = "Mon-Sat academic transport",
            headline = "Route-wise schedule browsing",
            supporting = "Male/female timing cards, Friday special slot, delay chips, and offline cached schedules.",
            previewTitle = "Official-inspired timings",
            previewLines = listOf("Male: 06:00 AM -> 08:15 AM", "Female: 12:40 PM -> 02:30 PM", "Friday female slot: 01:30 PM -> 03:20 PM"),
            rows = listOf(
                DetailRow("F-8 / F-10", "Morning pickup window", "06:00", Icons.Outlined.Schedule, PrimaryGreen),
                DetailRow("I-8 / I-10", "Afternoon departure", "01:40", Icons.Outlined.CalendarMonth, WarningAmber),
                DetailRow("Bhara Kahu", "Evening route", "05:40", Icons.Outlined.Route, AccentMint),
            )
        )

        FeatureCatalog.Commuter.SCHEDULE_DETAIL -> FeatureDetail(
            icon = Icons.Outlined.Schedule,
            kicker = "Schedule Detail",
            status = "Animated countdown ready",
            headline = "Compare today’s trip slots before leaving",
            supporting = "Schedule detail cards show gender-specific timing, Friday exceptions, and delay chips without forcing recall.",
            previewTitle = "Today - G-10 / F-10",
            previewLines = listOf("Male: 01:40 PM -> 03:30 PM", "Female: 12:40 PM -> 02:30 PM", "Friday special: 01:30 PM -> 03:20 PM"),
            rows = listOf(
                DetailRow("Morning pickup", "06:00 AM -> 08:15 AM", "Done", Icons.Outlined.CheckCircle, PrimaryGreen),
                DetailRow("Afternoon return", "01:40 PM -> 03:30 PM", "Next", Icons.Outlined.Schedule, WarningAmber),
                DetailRow("Evening service", "07:20 PM -> 09:00 PM", "Later", Icons.Outlined.CalendarMonth, AccentMint),
            )
        )

        FeatureCatalog.Commuter.CHATBOT -> FeatureDetail(
            icon = Icons.Outlined.SmartToy,
            kicker = "MoveBot",
            status = "AI assistance preview",
            headline = "Ask transport questions naturally",
            supporting = "Demo-friendly chat UI for route timings, delays, stops, and complaint guidance.",
            previewTitle = "Sample conversation",
            previewLines = listOf("You: Which bus goes to G-10?", "MoveBot: Bus 07 covers G-10/F-10.", "MoveBot: Next expected arrival is 4 min."),
            rows = listOf(
                DetailRow("Route help", "Find routes by area or stop", "Fast", Icons.Outlined.Route, PrimaryGreen),
                DetailRow("Delay guidance", "Explains delay notices", "Live", Icons.Outlined.Campaign, WarningAmber),
                DetailRow("Query drafting", "Turns complaint into clear ticket", "Smart", Icons.Outlined.MailOutline, AccentMint),
            )
        )

        FeatureCatalog.Commuter.SUBMIT_QUERY -> FeatureDetail(
            icon = Icons.Outlined.MailOutline,
            kicker = "Submit Query",
            status = "Guided form preview",
            headline = "Report transport issues in under a minute",
            supporting = "Category chips, route picker, priority selection, and success confirmation reduce confusion during stressful moments.",
            previewTitle = "Form composition",
            previewLines = listOf("Category: Late bus", "Route: Rawalpindi Saddar", "Priority: Normal - avoids false emergency reports"),
            rows = listOf(
                DetailRow("Route", "Rawalpindi Saddar - Bus 05", "Chosen", Icons.Outlined.Route, PrimaryGreen),
                DetailRow("Issue type", "Late bus / missed stop / driver concern", "Chip", Icons.Outlined.Forum, WarningAmber),
                DetailRow("Submit state", "Success animation and ticket number", "Ready", Icons.AutoMirrored.Outlined.Send, AccentMint),
            )
        )

        FeatureCatalog.Commuter.MY_QUERIES, FeatureCatalog.Staff.MANAGE_QUERIES -> FeatureDetail(
            icon = Icons.Outlined.MailOutline,
            kicker = "Query System",
            status = "Two-way student support",
            headline = "Track every query from sent to resolved",
            supporting = "A status timeline makes replies visible and helps users recover from unanswered or rejected requests.",
            previewTitle = "Ticket workflow",
            previewLines = listOf("Category: Late bus", "Route: Rawalpindi Saddar", "Status: Assigned to transport staff"),
            rows = listOf(
                DetailRow("Q-1042", "Bus 05 late near Committee Chowk", "Open", Icons.Outlined.Forum, WarningAmber),
                DetailRow("Q-1038", "Stop location clarification", "Replied", Icons.Outlined.CheckCircle, PrimaryGreen),
                DetailRow("Q-1035", "Driver contact needed", "Closed", Icons.AutoMirrored.Outlined.Send, AccentMint),
            )
        )

        FeatureCatalog.Commuter.ANNOUNCEMENTS, FeatureCatalog.Staff.POST_ANNOUNCEMENTS -> FeatureDetail(
            icon = Icons.Outlined.Campaign,
            kicker = "Announcements",
            status = "High priority alerts",
            headline = "Delay and cancellation notices in one calm inbox",
            supporting = "Role-aware posting for staff and readable alert cards for students.",
            previewTitle = "Today notices",
            previewLines = listOf("G-9 route delayed 8 minutes", "Kahuta route uses alternate stop", "Evening buses leave from Gate 2"),
            rows = listOf(
                DetailRow("Delay alert", "F-8 and G-9 routes", "3:30 PM", Icons.Outlined.Campaign, WarningAmber),
                DetailRow("Route change", "Tramri stop shifted temporarily", "New", Icons.Outlined.Route, DangerRed),
                DetailRow("Schedule posted", "Saturday pickup timings updated", "Seen", Icons.Outlined.CheckCircle, PrimaryGreen),
            )
        )

        FeatureCatalog.Staff.ANNOUNCEMENT_HISTORY -> FeatureDetail(
            icon = Icons.Outlined.Campaign,
            kicker = "Announcement History",
            status = "Delivery visibility",
            headline = "See which notices were sent, read, or need follow-up",
            supporting = "History protects staff from duplicate posting and gives examiners a clear audit-friendly communication flow.",
            previewTitle = "Recent broadcasts",
            previewLines = listOf("F-8 delay notice delivered to 246 students", "Kahuta alternate stop notice pending 18 reads", "Evening departure reminder scheduled"),
            rows = listOf(
                DetailRow("F-8 delay", "Sent to affected favorite-route users", "246", Icons.Outlined.Campaign, PrimaryGreen),
                DetailRow("Kahuta stop shift", "Requires follow-up announcement", "18", Icons.Outlined.ErrorOutline, WarningAmber),
                DetailRow("Gate 2 reminder", "Scheduled for 05:20 PM", "Queued", Icons.Outlined.Schedule, AccentMint),
            )
        )

        FeatureCatalog.Commuter.NOTIFICATIONS -> FeatureDetail(
            icon = Icons.Outlined.NotificationsActive,
            kicker = "Notification Inbox",
            status = "GPS alerts enabled",
            headline = "Smart alerts without notification overload",
            supporting = "Students see only route-relevant alerts, with toggles for delays, cancellations, GPS proximity, and quiet hours.",
            previewTitle = "Settings snapshot",
            previewLines = listOf("Delay alerts: On", "GPS stop reminder: 300m before stop", "Quiet hours: 10:00 PM -> 6:00 AM"),
            rows = listOf(
                DetailRow("Bus 07 arriving", "G-10 stop - approximately 4 minutes", "Live", Icons.Outlined.NotificationsActive, PrimaryGreen),
                DetailRow("Delay warning", "Faizabad route slowed near Murree Road", "New", Icons.Outlined.ErrorOutline, WarningAmber),
                DetailRow("Schedule reminder", "Evening departure at 05:40 PM", "Today", Icons.Outlined.Schedule, AccentMint),
            )
        )

        FeatureCatalog.Commuter.NOTIFICATION_SETTINGS -> FeatureDetail(
            icon = Icons.Outlined.Settings,
            kicker = "Alert Controls",
            status = "User control and freedom",
            headline = "Choose exactly which alerts interrupt you",
            supporting = "Students can tune delay, cancellation, GPS proximity, announcement, and quiet-hour behavior.",
            previewTitle = "Toggle states",
            previewLines = listOf("Delay alerts: Enabled", "GPS stop reminders: Enabled at 300m", "Quiet hours: Enabled from 10 PM"),
            rows = listOf(
                DetailRow("Delay alerts", "Only for favorite routes", "On", Icons.Outlined.NotificationsActive, PrimaryGreen),
                DetailRow("GPS reminders", "Notify before reaching saved stop", "300m", Icons.Outlined.LocationOn, AccentMint),
                DetailRow("Quiet hours", "Mute non-critical alerts overnight", "10 PM", Icons.Outlined.Schedule, WarningAmber),
            )
        )

        FeatureCatalog.Staff.LIVE_BUS_MONITOR -> FeatureDetail(
            icon = Icons.Outlined.Analytics,
            kicker = "Live Bus Monitor",
            status = "Fleet health view",
            headline = "Operational GPS alerts without heavy polling",
            supporting = "Staff monitor shows bus health, last GPS ping, occupancy hint, and weak-signal alerts using reused listeners.",
            previewTitle = "Fleet monitor",
            previewLines = listOf("Bus 07: healthy - 12 sec ago", "Bus 11: traffic delay - 36 sec ago", "Bus 14: weak GPS signal - review"),
            rows = listOf(
                DetailRow("Bus 07", "G-10 / F-10 - Arshad Ali", "Live", Icons.Outlined.DirectionsBus, PrimaryGreen),
                DetailRow("Bus 11", "Faizabad - Nasir Mehmood", "Slow", Icons.Outlined.Analytics, WarningAmber),
                DetailRow("Bus 14", "Bhara Kahu - Tariq Usman", "Weak", Icons.Outlined.ErrorOutline, DangerRed),
            )
        )

        FeatureCatalog.Staff.STAFF_PROFILE -> FeatureDetail(
            icon = Icons.Outlined.Settings,
            kicker = "Staff Profile",
            status = "Duty status controls",
            headline = "Transport staff identity and assigned responsibilities",
            supporting = "Profile screen clarifies assigned buses, duty status, contact visibility, and role permissions.",
            previewTitle = "Staff account",
            previewLines = listOf("Name: Arshad Ali", "Assigned bus: Bus 07", "Duty: Morning and afternoon G-10/F-10"),
            rows = listOf(
                DetailRow("Duty status", "Available for live route support", "On", Icons.Outlined.CheckCircle, PrimaryGreen),
                DetailRow("Assigned route", "G-10 / F-10", "Bus 07", Icons.Outlined.Route, AccentMint),
                DetailRow("Permissions", "Post delay notices and reply queries", "Staff", Icons.Outlined.Group, WarningAmber),
            )
        )

        FeatureCatalog.Commuter.FAVORITES -> FeatureDetail(
            icon = Icons.Outlined.Star,
            kicker = "Saved Routes",
            status = "Fast access mode",
            headline = "Favorite stops and routes with quick ETAs",
            supporting = "One-tap route access prevents repeated searching and keeps the most important bus status visible.",
            previewTitle = "Saved commute",
            previewLines = listOf("Primary route: G-10 / F-10", "Favorite stop: G-10 Markaz", "Backup route: I-8 / I-10"),
            rows = listOf(
                DetailRow("G-10 Markaz", "Bus 07 - Arshad Ali", "4 min", Icons.Outlined.Star, PrimaryGreen),
                DetailRow("F-10 Gate", "Bus 03 - morning pickup", "06:00", Icons.Outlined.LocationOn, AccentMint),
                DetailRow("IIUI Gate 2", "Evening departure point", "05:40", Icons.Outlined.Route, WarningAmber),
            )
        )

        FeatureCatalog.Commuter.PROFILE_SETTINGS -> FeatureDetail(
            icon = Icons.Outlined.Settings,
            kicker = "Profile Settings",
            status = "Light and dark ready",
            headline = "Personalized campus transport account",
            supporting = "Theme, language, role visibility, privacy, and default route controls in one clean settings screen.",
            previewTitle = "Preference controls",
            previewLines = listOf("Language: English primary / Urdu secondary", "Theme: Follows system mode", "Default route: G-10 / F-10"),
            rows = listOf(
                DetailRow("Inshrah Binte Wasim", "BS Software Engineering - IIUI", "Student", Icons.Outlined.Group, PrimaryGreen),
                DetailRow("Default route", "G-10 / F-10", "Saved", Icons.Outlined.Route, AccentMint),
                DetailRow("Privacy", "Location used only during active tracking", "Clear", Icons.Outlined.CheckCircle, WarningAmber),
            )
        )

        FeatureCatalog.Commuter.FEEDBACK -> FeatureDetail(
            icon = Icons.Outlined.CheckCircle,
            kicker = "Feedback",
            status = "Trip rating ready",
            headline = "Close the loop after every journey",
            supporting = "Rating chips, issue tags, and success states help IIUI transport improve without long forms.",
            previewTitle = "Rating flow",
            previewLines = listOf("Trip: Bus 07 - G-10", "Rating: 4.8 / 5", "Tags: Clean bus, on-time, safe driving"),
            rows = listOf(
                DetailRow("Bus 07", "Clean bus and punctual driver", "4.8", Icons.Outlined.CheckCircle, PrimaryGreen),
                DetailRow("Bus 11", "Traffic delay but announcement was clear", "4.2", Icons.Outlined.Campaign, WarningAmber),
                DetailRow("Suggestion", "Add stop photo for new students", "Open", Icons.Outlined.MailOutline, AccentMint),
            )
        )

        FeatureCatalog.Admin.MANAGE_USERS -> FeatureDetail(
            icon = Icons.Outlined.Group,
            kicker = "User Roles",
            status = "Admin module",
            headline = "Role-based account management",
            supporting = "Student, staff, and administrator access grouped to prevent accidental data exposure.",
            previewTitle = "Role controls",
            previewLines = listOf("Commuter accounts: 1,180", "Transport staff: 18", "Pending approvals: 07"),
            rows = listOf(
                DetailRow("Inshrah Binte Wasim", "Student - BSSE", "Active", Icons.Outlined.Group, PrimaryGreen),
                DetailRow("Arshad Ali", "Transport staff", "Active", Icons.Outlined.DirectionsBus, AccentMint),
                DetailRow("Pending request", "name.bsse4518@iiu.edu.pk", "Review", Icons.Outlined.ErrorOutline, WarningAmber),
            )
        )

        FeatureCatalog.Admin.REPORTS_LOGS -> FeatureDetail(
            icon = Icons.Outlined.Analytics,
            kicker = "System Reports",
            status = "Executive dashboard",
            headline = "Transport analytics for routes, usage, and service quality",
            supporting = "Reports use aggregated snapshots instead of raw repeated reads, which keeps Firebase free-tier friendly.",
            previewTitle = "Today’s analytics",
            previewLines = listOf("Peak route: G-10/F-10", "Average delay: 6.4 minutes", "Resolved queries: 82%"),
            rows = listOf(
                DetailRow("Fleet utilization", "14 active buses during peak hours", "88%", Icons.Outlined.DirectionsBus, PrimaryGreen),
                DetailRow("Delay trend", "Faizabad and Saddar routes affected", "6.4m", Icons.Outlined.Analytics, WarningAmber),
                DetailRow("Satisfaction", "Trip ratings from feedback module", "4.6", Icons.Outlined.CheckCircle, AccentMint),
            )
        )

        FeatureCatalog.Admin.AUDIT_LOGS -> FeatureDetail(
            icon = Icons.Outlined.Schedule,
            kicker = "Audit Logs",
            status = "Admin accountability",
            headline = "Trace every role, schedule, and route change",
            supporting = "Audit logs make the system feel production-grade and help teachers see governance, not just UI.",
            previewTitle = "Security trail",
            previewLines = listOf("Admin updated Bus 03 capacity", "Schedule edited for Friday female slot", "Staff role approved for transport office"),
            rows = listOf(
                DetailRow("Schedule update", "Friday female slot changed to 01:30 PM", "Now", Icons.Outlined.Schedule, PrimaryGreen),
                DetailRow("Role approval", "Transport staff account verified", "12m", Icons.Outlined.Group, AccentMint),
                DetailRow("Route edit", "Tramri stop order changed", "1h", Icons.Outlined.Route, WarningAmber),
            )
        )

        else -> FeatureDetail(
            icon = Icons.Outlined.Analytics,
            kicker = "Admin Tools",
            status = "System preview",
            headline = "Operational controls for SmartMove IIUI",
            supporting = "Premium frontend-ready module with realistic states and Firebase-conscious interaction patterns.",
            previewTitle = "System snapshot",
            previewLines = listOf("Fleet registry loaded from cache", "Route manager supports lazy pagination", "Audit logs grouped by day"),
            rows = listOf(
                DetailRow("Bus 03", "F-8 / F-10 route", "Active", Icons.Outlined.DirectionsBus, PrimaryGreen),
                DetailRow("Bus 14", "Bhara Kahu route", "Service", Icons.Outlined.Route, WarningAmber),
                DetailRow("Audit log", "Schedule changed by admin", "Now", Icons.Outlined.Analytics, AccentMint),
            )
        )
    }
}
