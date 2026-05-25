package com.example.smartmoveiiui.ui.splash

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.R
import com.example.smartmoveiiui.ui.theme.AccentMint
import com.example.smartmoveiiui.ui.theme.GlowGreen
import com.example.smartmoveiiui.ui.theme.PrimaryGreen
import com.example.smartmoveiiui.ui.theme.PrimaryGreenLight
import com.example.smartmoveiiui.ui.theme.SmartMoveIIUITheme
import com.example.smartmoveiiui.ui.theme.WarningAmber
import kotlinx.coroutines.delay

@Composable
fun SmartMoveSplashScreen(
    onContinue: () -> Unit = {},
) {
    var stage by remember { mutableIntStateOf(0) }
    val stages = listOf(
        "Checking IIUI transport network",
        "Syncing cached routes",
        "Preparing live ETA engine",
        "SmartMove is ready",
    )

    LaunchedEffect(Unit) {
        while (stage < stages.lastIndex) {
            delay(780)
            stage += 1
        }
    }

    val infinite = rememberInfiniteTransition(label = "splash")
    val logoPulse by infinite.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(tween(1150), RepeatMode.Reverse),
        label = "logoPulse",
    )
    val liveAlpha by infinite.animateFloat(
        initialValue = 0.32f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(620), RepeatMode.Reverse),
        label = "liveAlpha",
    )
    val busProgress by infinite.animateFloat(
        initialValue = 0.06f,
        targetValue = 0.88f,
        animationSpec = infiniteRepeatable(tween(3100), RepeatMode.Reverse),
        label = "busProgress",
    )
    val radar by infinite.animateFloat(
        initialValue = 0.45f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(1500), RepeatMode.Restart),
        label = "radar",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(splashBrush())
            .clickable(onClick = onContinue)
    ) {
        SplashPattern()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                SplashStatusChip("HCI #1 Live", liveAlpha, GlowGreen)
                SplashStatusChip("Low-cost sync", 1f, WarningAmber)
            }

            Spacer(modifier = Modifier.height(22.dp))

            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(122.dp)
                        .scale(radar)
                        .alpha(1.35f - radar)
                        .clip(CircleShape)
                        .background(GlowGreen.copy(alpha = 0.12f))
                )
                Box(
                    modifier = Modifier
                        .size(116.dp)
                        .scale(logoPulse)
                        .blur(22.dp)
                        .clip(CircleShape)
                        .background(GlowGreen.copy(alpha = 0.20f))
                )
                Image(
                    painter = painterResource(id = R.drawable.iiui_logo),
                    contentDescription = "IIUI logo",
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "SmartMove IIUI",
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "DIRECTORATE OF TRANSPORT",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 4.dp),
            )

            Spacer(modifier = Modifier.height(18.dp))

            InteractiveCampusHero(progress = busProgress)

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                MetricPill(Icons.Outlined.DirectionsBus, "14 active", Modifier.weight(1f))
                MetricPill(Icons.Outlined.Schedule, "ETA 4 min", Modifier.weight(1f))
                MetricPill(Icons.Outlined.Route, "17 routes", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.weight(1f))

            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = if (isSystemInDarkTheme()) 0.70f else 0.88f),
                tonalElevation = 0.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedContent(targetState = stages[stage], label = "splashStage") { text ->
                        Text(
                            text = text,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (stage + 1) / stages.size.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50)),
                        color = PrimaryGreen,
                        trackColor = AccentMint.copy(alpha = 0.18f),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Tap anywhere to continue",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }
    }
}

@Composable
private fun InteractiveCampusHero(progress: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(265.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        if (isSystemInDarkTheme()) Color(0xFF123129) else Color(0xFFE1F5EE),
                        if (isSystemInDarkTheme()) Color(0xFF0D201C) else Color(0xFFF7FFFE),
                    )
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val buildingColor = Color(0xFFE29A62)
            drawRoundRect(
                color = buildingColor.copy(alpha = 0.95f),
                topLeft = Offset(size.width * 0.17f, size.height * 0.16f),
                size = Size(size.width * 0.66f, size.height * 0.40f),
                cornerRadius = CornerRadius(18f, 18f),
            )
            drawRoundRect(
                color = PrimaryGreen.copy(alpha = 0.88f),
                topLeft = Offset(size.width * 0.40f, size.height * 0.30f),
                size = Size(size.width * 0.20f, size.height * 0.26f),
                cornerRadius = CornerRadius(60f, 60f),
            )
            repeat(6) { i ->
                drawRoundRect(
                    color = Color(0xFFBCEFE2).copy(alpha = 0.76f),
                    topLeft = Offset(size.width * (0.22f + i * 0.095f), size.height * 0.24f),
                    size = Size(22f, 20f),
                    cornerRadius = CornerRadius(4f, 4f),
                )
            }

            val route = Path().apply {
                moveTo(size.width * 0.10f, size.height * 0.82f)
                cubicTo(size.width * 0.30f, size.height * 0.54f, size.width * 0.55f, size.height * 0.98f, size.width * 0.92f, size.height * 0.62f)
            }
            drawPath(route, Color.White.copy(alpha = 0.82f), style = Stroke(width = 24f, cap = StrokeCap.Round))
            drawPath(route, PrimaryGreenLight, style = Stroke(width = 7f, cap = StrokeCap.Round))

            val stops = listOf(
                Offset(size.width * 0.10f, size.height * 0.82f),
                Offset(size.width * 0.43f, size.height * 0.71f),
                Offset(size.width * 0.92f, size.height * 0.62f),
            )
            stops.forEach {
                drawCircle(Color.White, radius = 13f, center = it)
                drawCircle(PrimaryGreen, radius = 6f, center = it)
            }

            val bus = Offset(
                x = size.width * (0.10f + 0.82f * progress),
                y = size.height * (0.82f - 0.20f * progress + kotlin.math.sin(progress * 7f) * 0.09f),
            )
            drawRoundRect(
                color = Color(0xFF073E34),
                topLeft = Offset(bus.x - 36f, bus.y - 19f),
                size = Size(72f, 38f),
                cornerRadius = CornerRadius(12f, 12f),
            )
            drawRect(Color(0xFFBCEFE2), topLeft = Offset(bus.x - 22f, bus.y - 10f), size = Size(34f, 11f))
            drawCircle(Color(0xFF101816), radius = 6f, center = Offset(bus.x - 20f, bus.y + 19f))
            drawCircle(Color(0xFF101816), radius = 6f, center = Offset(bus.x + 22f, bus.y + 19f))
        }

        Image(
            painter = painterResource(id = R.drawable.bus_splash),
            contentDescription = "IIUI bus illustration",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 18.dp)
                .fillMaxWidth(0.74f)
                .alpha(0.96f),
            contentScale = ContentScale.Fit,
        )

        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(14.dp),
            shape = RoundedCornerShape(50),
            color = Color.White.copy(alpha = 0.86f),
            contentColor = PrimaryGreen,
        ) {
            Text("Bus 07 - G-10", modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun SplashStatusChip(text: String, alpha: Float, color: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.72f))
            .padding(horizontal = 11.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(Modifier.size(8.dp).alpha(alpha).clip(CircleShape).background(color))
        Spacer(Modifier.width(7.dp))
        Text(text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun MetricPill(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(16.dp),
        color = AccentMint.copy(alpha = 0.14f),
        contentColor = PrimaryGreen,
    ) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.material3.Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun SplashPattern() {
    Canvas(Modifier.fillMaxSize()) {
        val spacing = 64f
        for (x in 0..(size.width / spacing).toInt() + 1) {
            for (y in 0..(size.height / spacing).toInt() + 1) {
                drawCircle(
                    color = AccentMint.copy(alpha = 0.045f),
                    radius = 5f,
                    center = Offset(x * spacing, y * spacing),
                )
                drawCircle(
                    color = AccentMint.copy(alpha = 0.035f),
                    radius = 14f,
                    center = Offset(x * spacing + spacing / 2, y * spacing + spacing / 2),
                    style = Stroke(width = 1.4f),
                )
            }
        }
    }
}

@Composable
private fun splashBrush(): Brush = Brush.verticalGradient(
    listOf(
        if (isSystemInDarkTheme()) Color(0xFF071411) else Color(0xFFF7FFFE),
        if (isSystemInDarkTheme()) Color(0xFF0D201C) else Color(0xFFE1F5EE),
    )
)

@Preview(showBackground = true)
@Composable
private fun SmartMoveSplashScreenPreview() {
    SmartMoveIIUITheme {
        SmartMoveSplashScreen()
    }
}
