package com.example.smartmoveiiui.ui.onboarding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.R
import kotlinx.coroutines.launch

val IiuiOnboardingColors = lightColorScheme(
    primary = Color(0xFF103F34),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE4F8EF),
    onPrimaryContainer = Color(0xFF103F34),
    secondary = Color(0xFF23745E),
    onSecondary = Color.White,
    tertiary = Color(0xFFFFB454),
    onTertiary = Color(0xFF391C00),
    background = Color(0xFFFFFDFB),
    onBackground = Color(0xFF122621),
    surface = Color(0xFFFFFDFB),
    onSurface = Color(0xFF122621),
    surfaceVariant = Color(0xFFE5EFEA),
    onSurfaceVariant = Color(0xFF4D645C),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IiuiOnboardingExperience(
    onSkip: () -> Unit,
    onAlreadyHaveAccount: () -> Unit,
    onFinishOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scope = rememberCoroutineScope()

    MaterialTheme(colorScheme = IiuiOnboardingColors, typography = Typography()) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = modifier.fillMaxSize(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
                    .padding(horizontal = 18.dp)
                    .padding(top = 10.dp, bottom = 16.dp),
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(26.dp),
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 18.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = stringResource(id = R.string.onboarding_welcome_title),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                            )
                            TextButton(onClick = onSkip) {
                                Text(
                                    text = stringResource(id = R.string.onboarding_skip),
                                    color = MaterialTheme.colorScheme.secondary,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                            pageSpacing = 0.dp,
                            beyondViewportPageCount = 1,
                        ) { page ->
                            when (page) {
                                0 -> OnboardingPageWelcome()
                                1 -> OnboardingPageTrackLive()
                                else -> OnboardingPageStayOriented()
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        FlowPagerDots(
                            currentIndex = pagerState.currentPage,
                            count = pageCount,
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val isLastPage = pagerState.currentPage >= pageCount - 1
                        val primaryLabel =
                            if (isLastPage) stringResource(id = R.string.onboarding_get_started)
                            else stringResource(id = R.string.onboarding_next_continue)

                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    if (isLastPage) {
                                        onFinishOnboarding()
                                    } else {
                                        scope.launch {
                                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                        }
                                    }
                                }
                                .semantics {
                                    role = Role.Button
                                    contentDescription = primaryLabel
                                },
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = primaryLabel,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = onAlreadyHaveAccount,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        ) {
                            Text(
                                text = stringResource(id = R.string.cta_already_have_account),
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FlowPagerDots(
    currentIndex: Int,
    count: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(count) { index ->
            val active = index == currentIndex
            val width by animateDpAsState(
                targetValue = if (active) 34.dp else 8.dp,
                label = "dotWidth",
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .width(width)
                    .height(8.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        color = if (active) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                    ),
            )
        }
    }
}

@Composable
private fun OnboardingPageWelcome(modifier: Modifier = Modifier) {
    val scroll = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        WelcomeCampusIllustration()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(id = R.string.ob_p0_headline),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(id = R.string.ob_p0_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun OnboardingPageTrackLive(modifier: Modifier = Modifier) {
    val scroll = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        TrackBusIllustration()
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = stringResource(id = R.string.ob_p1_headline),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(id = R.string.ob_p1_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.52f),
            shape = RoundedCornerShape(20.dp),
        ) {
            Column(
                Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                FeatureBullet(
                    iconPainter = painterResource(id = R.drawable.ic_feature_gps_live),
                    title = stringResource(id = R.string.ob_feature_gps_title),
                    subtitle = stringResource(id = R.string.ob_feature_gps_sub),
                )
                FeatureBullet(
                    iconPainter = painterResource(id = R.drawable.ic_feature_eta_smart),
                    title = stringResource(id = R.string.ob_feature_eta_title),
                    subtitle = stringResource(id = R.string.ob_feature_eta_sub),
                )
                FeatureBullet(
                    iconPainter = painterResource(id = R.drawable.ic_feature_routes_map),
                    title = stringResource(id = R.string.ob_feature_routes_title),
                    subtitle = stringResource(id = R.string.ob_feature_routes_sub),
                )
            }
        }
    }
}

@Composable
private fun OnboardingPageStayOriented(modifier: Modifier = Modifier) {
    val scroll = rememberScrollState()
    Column(
        modifier = modifier.verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        CampusMapJourneyIllustration()
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(id = R.string.ob_p2_headline),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(id = R.string.ob_p2_body),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun FeatureBullet(
    iconPainter: Painter,
    title: String,
    subtitle: String,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier.size(42.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun WelcomeCampusIllustration(modifier: Modifier = Modifier) {
    val deep = MaterialTheme.colorScheme.primary
    val mint = MaterialTheme.colorScheme.secondary
    val skyTop = Color(0xFFEAF6F1)
    val skyBot = Color(0xFFC5E2D6)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(228.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.verticalGradient(listOf(skyTop, skyBot))),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x66FFB454), Color(0x00FFB454)),
                    center = Offset(size.width * 0.84f, size.height * 0.2f),
                    radius = size.minDimension * 0.38f,
                ),
                radius = 38.dp.toPx(),
                center = Offset(size.width * 0.84f, size.height * 0.2f),
            )
            drawCircle(
                color = Color(0xFFFFD699).copy(alpha = 0.55f),
                radius = 12.dp.toPx(),
                center = Offset(size.width * 0.84f, size.height * 0.2f),
            )

            val hill = Path().apply {
                moveTo(0f, size.height * 0.58f)
                cubicTo(
                    size.width * 0.12f,
                    size.height * 0.42f,
                    size.width * 0.28f,
                    size.height * 0.52f,
                    size.width * 0.42f,
                    size.height * 0.46f,
                )
                cubicTo(
                    size.width * 0.58f,
                    size.height * 0.4f,
                    size.width * 0.72f,
                    size.height * 0.52f,
                    size.width,
                    size.height * 0.44f,
                )
                lineTo(size.width, size.height * 0.72f)
                lineTo(0f, size.height * 0.72f)
                close()
            }
            drawPath(hill, deep.copy(alpha = 0.38f), style = Fill)

            val b1 = Size(size.width * 0.07f, size.height * 0.22f)
            drawRoundRect(
                color = deep.copy(alpha = 0.55f),
                topLeft = Offset(size.width * 0.1f, size.height * 0.36f),
                size = b1,
                cornerRadius = CornerRadius(4.dp.toPx()),
            )
            drawRoundRect(
                color = deep.copy(alpha = 0.48f),
                topLeft = Offset(size.width * 0.22f, size.height * 0.32f),
                size = Size(size.width * 0.09f, size.height * 0.26f),
                cornerRadius = CornerRadius(4.dp.toPx()),
            )
            drawRoundRect(
                color = deep.copy(alpha = 0.5f),
                topLeft = Offset(size.width * 0.62f, size.height * 0.34f),
                size = Size(size.width * 0.08f, size.height * 0.24f),
                cornerRadius = CornerRadius(4.dp.toPx()),
            )

            val roadTop = size.height * 0.72f
            drawRoundRect(
                color = Color(0xFF0E2A23).copy(alpha = 0.85f),
                topLeft = Offset(0f, roadTop),
                size = Size(size.width, size.height - roadTop),
                cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx()),
            )
            val laneY = roadTop + (size.height - roadTop) * 0.42f
            drawLine(
                color = Color(0xFF62E090).copy(alpha = 0.45f),
                start = Offset(size.width * 0.08f, laneY),
                end = Offset(size.width * 0.92f, laneY),
                strokeWidth = 3.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 14f)),
                cap = StrokeCap.Round,
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_onboarding_bus),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-12).dp)
                .size(width = 112.dp, height = 72.dp),
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 14.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White.copy(alpha = 0.88f))
                .padding(horizontal = 10.dp, vertical = 6.dp),
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_illustration_campus_badge),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = mint,
            )
        }
    }
}

@Composable
private fun TrackBusIllustration(modifier: Modifier = Modifier) {
    val canopy = MaterialTheme.colorScheme.secondary
    val deepGreen = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(28.dp))
                .background(Brush.verticalGradient(listOf(canopy.copy(alpha = 0.22f), Color(0xFFD8EDE7)))),
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val w = size.width
                val h = size.height
                val path = Path().apply {
                    moveTo(-w * 0.06f, h * 0.68f)
                    cubicTo(w * 0.18f, h * 0.22f, w * 0.46f, h * 0.86f, w * 0.92f, h * 0.26f)
                }
                drawPath(
                    path = path,
                    color = deepGreen.copy(alpha = 0.28f),
                    style = Stroke(
                        width = 3.2.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(18f, 14f)),
                    ),
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_onboarding_bus),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(140.dp),
            )

            Surface(
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(999.dp),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .padding(top = 14.dp, end = 18.dp)
                    .align(Alignment.TopEnd),
            ) {
                Text(
                    text = stringResource(id = R.string.onboarding_eta_sample),
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
private fun CampusMapJourneyIllustration(modifier: Modifier = Modifier) {
    val route = MaterialTheme.colorScheme.primary
    val park = MaterialTheme.colorScheme.secondary
    val mintField = Color(0xFFE8F5EF)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(228.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Brush.verticalGradient(listOf(mintField, Color(0xFFD4EAE2)))),
    ) {
        Canvas(Modifier.fillMaxSize()) {
            drawRoundRect(
                color = park.copy(alpha = 0.14f),
                topLeft = Offset(size.width * 0.06f, size.height * 0.1f),
                size = Size(size.width * 0.88f, size.height * 0.78f),
                cornerRadius = CornerRadius(22.dp.toPx()),
            )

            val p = Path().apply {
                moveTo(size.width * 0.14f, size.height * 0.74f)
                lineTo(size.width * 0.32f, size.height * 0.62f)
                lineTo(size.width * 0.5f, size.height * 0.38f)
                lineTo(size.width * 0.68f, size.height * 0.5f)
                lineTo(size.width * 0.86f, size.height * 0.28f)
            }
            drawPath(
                p,
                route.copy(alpha = 0.9f),
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
            )

            val stops = listOf(
                Offset(size.width * 0.14f, size.height * 0.74f),
                Offset(size.width * 0.5f, size.height * 0.38f),
                Offset(size.width * 0.86f, size.height * 0.28f),
            )
            stops.forEachIndexed { index, o ->
                drawCircle(Color.White, 10.dp.toPx(), o)
                drawCircle(
                    color = route,
                    radius = 10.dp.toPx(),
                    center = o,
                    style = Stroke(width = 2.5.dp.toPx()),
                )
                if (index == stops.lastIndex) {
                    drawCircle(Color(0xFFFFB454).copy(alpha = 0.35f), 20.dp.toPx(), o)
                }
            }

            repeat(9) { i ->
                val x = size.width * (0.1f + i * 0.09f)
                drawCircle(
                    park.copy(alpha = 0.55f),
                    4.5.dp.toPx(),
                    Offset(x, size.height * 0.2f + (i % 3) * 5.dp.toPx()),
                )
            }

            drawRoundRect(
                color = Color.White.copy(alpha = 0.65f),
                topLeft = Offset(size.width * 0.58f, size.height * 0.08f),
                size = Size(size.width * 0.32f, size.height * 0.16f),
                cornerRadius = CornerRadius(12.dp.toPx()),
            )
        }

        Text(
            text = stringResource(id = R.string.onboarding_illustration_live_route_label),
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 22.dp, top = 18.dp),
            style = MaterialTheme.typography.labelLarge,
            color = route,
            fontWeight = FontWeight.Bold,
        )

        Image(
            painter = painterResource(id = R.drawable.ic_onboarding_bus),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-20).dp, y = (88).dp)
                .size(76.dp),
        )
    }
}
