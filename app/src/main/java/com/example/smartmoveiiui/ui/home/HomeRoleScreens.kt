package com.example.smartmoveiiui.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.DirectionsBus
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.R
import com.example.smartmoveiiui.navigation.FeatureCatalog

private data class HomeTile(val featureId: String, val icon: ImageVector)

@Composable
fun CommuterHomeScreen(
    onNavigateToFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tiles = listOf(
        HomeTile(FeatureCatalog.Commuter.LIVE_TRACKING, Icons.Outlined.DirectionsBus),
        HomeTile(FeatureCatalog.Commuter.VIEW_SCHEDULE, Icons.Outlined.CalendarMonth),
        HomeTile(FeatureCatalog.Commuter.CHATBOT, Icons.Outlined.SmartToy),
        HomeTile(FeatureCatalog.Commuter.SUBMIT_QUERY, Icons.Outlined.MailOutline),
        HomeTile(FeatureCatalog.Commuter.ANNOUNCEMENTS, Icons.Outlined.Campaign),
    )
    HomeScaffold(
        roleSubtitle = stringResource(id = R.string.home_subtitle_commuter),
        modifier = modifier,
        tiles = tiles,
        onNavigateToFeature = onNavigateToFeature,
    )
}

@Composable
fun StaffHomeScreen(
    onNavigateToFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tiles = listOf(
        HomeTile(FeatureCatalog.Staff.LIVE_TRACKING, Icons.Outlined.DirectionsBus),
        HomeTile(FeatureCatalog.Staff.POST_ANNOUNCEMENTS, Icons.Outlined.Campaign),
        HomeTile(FeatureCatalog.Staff.MANAGE_QUERIES, Icons.Outlined.Forum),
    )
    HomeScaffold(
        roleSubtitle = stringResource(id = R.string.home_subtitle_staff),
        modifier = modifier,
        tiles = tiles,
        onNavigateToFeature = onNavigateToFeature,
    )
}

@Composable
fun AdminHomeScreen(
    onNavigateToFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tiles = listOf(
        HomeTile(FeatureCatalog.Admin.MANAGE_USERS, Icons.Outlined.Group),
        HomeTile(FeatureCatalog.Admin.MANAGE_BUSES, Icons.Outlined.DirectionsBus),
        HomeTile(FeatureCatalog.Admin.MANAGE_SCHEDULE, Icons.Outlined.Schedule),
        HomeTile(FeatureCatalog.Admin.MANAGE_ROUTES, Icons.Outlined.AltRoute),
        HomeTile(FeatureCatalog.Admin.LIVE_TRACKING, Icons.Outlined.DirectionsBus),
    )
    HomeScaffold(
        roleSubtitle = stringResource(id = R.string.home_subtitle_admin),
        modifier = modifier,
        tiles = tiles,
        onNavigateToFeature = onNavigateToFeature,
    )
}

@Composable
private fun HomeScaffold(
    roleSubtitle: String,
    tiles: List<HomeTile>,
    onNavigateToFeature: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = roleSubtitle,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
            Text(
                text = stringResource(id = R.string.home_section_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 4.dp),
            )
        }
        items(tiles, key = { it.featureId }) { tile ->
            FeatureEntryCard(
                title = stringResource(id = FeatureCatalog.titleFor(tile.featureId)),
                subtitle = stringResource(id = FeatureCatalog.subtitleFor(tile.featureId)),
                icon = tile.icon,
                onClick = { onNavigateToFeature(tile.featureId) },
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun FeatureEntryCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    val clickLabel = stringResource(id = R.string.home_open_feature_cd, title)
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .clickable(onClick = onClick, onClickLabel = clickLabel),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(
                modifier = Modifier.padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
