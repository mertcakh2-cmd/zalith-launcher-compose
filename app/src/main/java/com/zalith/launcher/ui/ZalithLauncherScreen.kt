package com.zalith.launcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zalith.launcher.data.LauncherAppItem
import com.zalith.launcher.data.LauncherCatalog

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZalithLauncherScreen() {
    val apps = LauncherCatalog.apps()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF08111F),
                        Color(0xFF0B1526),
                        Color(0xFF121D31)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            TopBar()

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Search apps, docs, worlds") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF101B2B),
                    unfocusedContainerColor = Color(0xFF101B2B),
                    focusedIndicatorColor = Color(0xFF7C9CFF),
                    unfocusedIndicatorColor = Color(0xFF233148),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = Color(0xFF7C9CFF)
                ),
                shape = RoundedCornerShape(18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionChip("Builds", true)
                QuickActionChip("Docs", false)
                QuickActionChip("Launch", false)
            }

            DeepWikiPanel()

            Text(
                text = "Favorites",
                color = Color(0xFFEAF4FF),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                apps.forEach { item ->
                    AppTile(item)
                }
            }
        }
    }
}

@Composable
private fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Zalith",
                color = Color(0xFFEAF4FF),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Launcher Core",
                color = Color(0xFF9CB8D8),
                fontSize = 12.sp
            )
        }

        Surface(
            shape = CircleShape,
            color = Color(0xFF11223B),
            tonalElevation = 0.dp,
            modifier = Modifier
                .size(44.dp)
                .border(1.dp, Color(0xFF2E3D5B), CircleShape)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "09:42",
                    fontSize = 10.sp,
                    color = Color(0xFFEAF4FF),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun QuickActionChip(label: String, selected: Boolean) {
    FilterChip(
        selected = selected,
        onClick = {},
        label = { Text(label) },
        modifier = Modifier.height(38.dp),
        leadingIcon = if (selected) {
            {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else null,
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF7C9CFF),
            containerColor = Color(0xFF101B2B),
            labelColor = Color(0xFFEAF4FF),
            selectedLabelColor = Color.White
        )
    )
}

@Composable
private fun DeepWikiPanel() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF101B2B))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "DeepWiki",
                    color = Color(0xFFEAF4FF),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFF1E3352)
                ) {
                    Text(
                        text = "Live",
                        color = Color(0xFF78E3C5),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = "New model context synced • 4 research threads ready • deployment card updated",
                color = Color(0xFFB8C9E3),
                fontSize = 12.sp,
                lineHeight = 18.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                InfoPill("Docs", "17")
                InfoPill("Builds", "09")
                InfoPill("Memories", "43")
            }
        }
    }
}

@Composable
private fun InfoPill(label: String, value: String) {
    Surface(
        color = Color(0xFF15283F),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.weight(1f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                color = Color(0xFFEAF4FF),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = label,
                color = Color(0xFFA7BCD8),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun AppTile(item: LauncherAppItem) {
    Card(
        modifier = Modifier
            .width(118.dp)
            .height(120.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = item.tint.copy(alpha = 0.18f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(item.tint.copy(alpha = 0.28f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.tint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = item.title,
                    color = Color(0xFFEAF4FF),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(
                    text = item.subtitle,
                    color = Color(0xFFB4C8E5),
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420, heightDp = 860)
@Composable
private fun ZalithLauncherPreview() {
    ZalithLauncherTheme {
        ZalithLauncherScreen()
    }
}
