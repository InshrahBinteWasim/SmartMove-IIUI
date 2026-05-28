package com.example.smartmoveiiui.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.SmartToy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.smartmoveiiui.data.SmartMoveRepository
import com.example.smartmoveiiui.ui.theme.AccentMint
import com.example.smartmoveiiui.ui.theme.BackgroundDark
import com.example.smartmoveiiui.ui.theme.BackgroundLight
import com.example.smartmoveiiui.ui.theme.PrimaryGreen
import com.example.smartmoveiiui.ui.theme.SurfaceDark
import com.example.smartmoveiiui.ui.theme.SurfaceLight

private data class ChatMessage(val text: String, val fromUser: Boolean)

@Composable
fun MoveBotChatScreen(
    onBack: () -> Unit,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages = remember {
        mutableStateListOf(
            ChatMessage("Assalam-o-Alaikum. Ask me about buses, routes, schedules, delays, or queries.", false)
        )
    }
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(chatBackground())
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        ChatHeader(onBack, isDarkTheme, onToggleTheme)

        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)) }
            items(messages) { message ->
                MessageBubble(message)
            }
            item { Spacer(Modifier.height(8.dp)) }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Message MoveBot") },
                minLines = 1,
                maxLines = 3,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryGreen,
                    cursorColor = PrimaryGreen
                )
            )
            Spacer(Modifier.width(8.dp))
            IconButton(
                onClick = {
                    val question = input.trim()
                    if (question.isBlank()) return@IconButton
                    messages.add(ChatMessage(question, true))
                    input = ""
                    SmartMoveRepository.askMoveBot(question) { answer ->
                        messages.add(ChatMessage(answer, false))
                    }
                },
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(PrimaryGreen)
            ) {
                Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = "Send", tint = Color.White)
            }
        }
    }
}

@Composable
private fun ChatHeader(onBack: () -> Unit, isDarkTheme: Boolean, onToggleTheme: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(AccentMint.copy(alpha = 0.18f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.SmartToy, contentDescription = null, tint = PrimaryGreen)
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text("MoveBot", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Online", style = MaterialTheme.typography.labelMedium, color = PrimaryGreen)
        }
        IconButton(onClick = onToggleTheme) {
            Icon(
                if (isDarkTheme) Icons.Outlined.LightMode else Icons.Outlined.DarkMode,
                contentDescription = "Change theme",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.fromUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(0.78f),
            shape = RoundedCornerShape(
                topStart = 18.dp,
                topEnd = 18.dp,
                bottomStart = if (message.fromUser) 18.dp else 4.dp,
                bottomEnd = if (message.fromUser) 4.dp else 18.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = if (message.fromUser) PrimaryGreen else MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
                color = if (message.fromUser) Color.White else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun chatBackground(): Brush = Brush.verticalGradient(
    listOf(
        if (isSystemInDarkTheme()) BackgroundDark else BackgroundLight,
        if (isSystemInDarkTheme()) SurfaceDark else SurfaceLight
    )
)
