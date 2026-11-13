package com.zalith.launcher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zalith.launcher.ui.theme.ZalithLauncherTheme

private data class MinecraftProfile(
    val name: String,
    val version: String,
    val loader: String,
    val color: Color
)

private val profiles = listOf(
    MinecraftProfile("Survival", "1.21.1", "Vanilla", Color(0xFF79D58A)),
    MinecraftProfile("Create & Build", "1.20.1", "Forge 47.3", Color(0xFFFFBD6E)),
    MinecraftProfile("Performance", "1.21", "Fabric 0.16", Color(0xFF79D6CF))
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ZalithLauncherScreen() {
    var selectedProfile by remember { mutableStateOf(profiles.first()) }
    var selectedTab by remember { mutableStateOf("Home") }
    var query by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            Surface(color = Color(0xFF101A16), tonalElevation = 8.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("Home", "Instances", "Settings").forEach { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = { selectedTab = tab },
                            label = { Text(tab) },
                            leadingIcon = if (tab == "Settings") {
                                { Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }
            }
        }
    ) { scaffoldPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF101A16), Color(0xFF17251C), Color(0xFF0D1512))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Header()
                SearchField(query = query, onQueryChange = { query = it })
                HeroCard(profile = selectedProfile, onPlay = { selectedTab = "Instances" })
                JavaRuntimeCard()
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Your instances", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Separate worlds, loaders and settings", color = Color(0xFFB2C4B4), style = MaterialTheme.typography.bodySmall)
                    }
                    IconButton(onClick = { selectedTab = "Instances" }) {
                        Icon(Icons.Default.Add, contentDescription = "Add instance", tint = Color(0xFFB9F6CA))
                    }
                }
                val filteredProfiles = profiles.filter {
                    it.name.contains(query, ignoreCase = true) || it.version.contains(query, ignoreCase = true) || it.loader.contains(query, ignoreCase = true)
                }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    filteredProfiles.forEach { profile ->
                        InstanceCard(
                            profile = profile,
                            selected = profile == selectedProfile,
                            onClick = { selectedProfile = profile }
                        )
                    }
                }
                DeepWikiCard()
                Text(
                    "ZALITH • JAVA EDITION",
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                    color = Color(0xFF718477),
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
private fun Header() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PixelCrest()
            Column {
                Text("ZALITH", color = Color(0xFFF1F6EE), fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text("JAVA EDITION LAUNCHER", color = Color(0xFF9FB39F), fontSize = 10.sp, letterSpacing = 1.5.sp)
            }
        }
        Surface(shape = CircleShape, color = Color(0xFF24382A)) {
            Row(modifier = Modifier.padding(horizontal = 11.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF82E69A)))
                Spacer(Modifier.width(7.dp))
                Text("READY", color = Color(0xFFC9F4CE), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PixelCrest() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF83D968), Color(0xFF286943))))
            .border(1.dp, Color(0xFFB5F19D).copy(alpha = 0.45f), RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text("Z", color = Color(0xFF102117), fontSize = 29.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text("Search instances, versions, loaders…") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(18.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF18251D),
            unfocusedContainerColor = Color(0xFF18251D),
            focusedIndicatorColor = Color(0xFF86D879),
            unfocusedIndicatorColor = Color(0xFF35483A),
            focusedTextColor = Color(0xFFF1F6EE),
            unfocusedTextColor = Color(0xFFF1F6EE),
            focusedLeadingIconColor = Color(0xFF9BE58C),
            unfocusedLeadingIconColor = Color(0xFF9FB39F),
            focusedPlaceholderColor = Color(0xFF829385),
            unfocusedPlaceholderColor = Color(0xFF829385),
            cursorColor = Color(0xFF9BE58C)
        )
    )
}

@Composable
private fun HeroCard(profile: MinecraftProfile, onPlay: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF203829)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box {
            BlockLandscape(Modifier.align(Alignment.TopEnd))
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                AssistChip(onClick = {}, label = { Text("JAVA • ${profile.version}") }, leadingIcon = {
                    Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                })
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Your next world\nstarts here.", color = Color.White, fontSize = 27.sp, lineHeight = 32.sp, fontWeight = FontWeight.ExtraBold)
                    Text("${profile.name}  ·  ${profile.loader}", color = Color(0xFFC2D8C1), style = MaterialTheme.typography.bodyMedium)
                }
                Button(
                    onClick = onPlay,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BE58C), contentColor = Color(0xFF122116))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Launch selected instance", fontWeight = FontWeight.Bold)
                }
                Text("Launcher UI preview — Java runtime and game launch backend are not connected yet.", color = Color(0xFFAFC6B0), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun BlockLandscape(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(top = 18.dp, end = 18.dp), horizontalAlignment = Alignment.End) {
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf(Color(0xFFFFD978), Color(0xFFFFE6A1), Color(0xFFFFD978)).forEach { color ->
                Box(Modifier.size(10.dp).background(color.copy(alpha = 0.8f)))
            }
        }
        Spacer(Modifier.height(15.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.Bottom) {
            Box(Modifier.size(28.dp, 44.dp).background(Color(0xFF477A4D).copy(alpha = 0.55f)))
            Box(Modifier.size(38.dp, 64.dp).background(Color(0xFF69A55C).copy(alpha = 0.55f)))
            Box(Modifier.size(24.dp, 34.dp).background(Color(0xFF3D7049).copy(alpha = 0.55f)))
        }
    }
}

@Composable
private fun JavaRuntimeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF18251D))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = RoundedCornerShape(15.dp), color = Color(0xFF293F2E), modifier = Modifier.size(46.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Memory, contentDescription = null, tint = Color(0xFF9BE58C))
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text("Java runtime", color = Color(0xFFF1F6EE), fontWeight = FontWeight.Bold)
                Text("Runtime setup required", color = Color(0xFFB6C7B7), style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.Default.ExpandMore, contentDescription = "Runtime details", tint = Color(0xFFB6C7B7))
        }
    }
}

@Composable
private fun InstanceCard(profile: MinecraftProfile, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(158.dp)
            .clickable(onClick = onClick)
            .then(if (selected) Modifier.border(1.5.dp, profile.color, RoundedCornerShape(22.dp)) else Modifier),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A281F))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(34.dp).clip(RoundedCornerShape(10.dp)).background(profile.color.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Storage, contentDescription = null, tint = profile.color, modifier = Modifier.size(19.dp))
                }
                if (selected) Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = profile.color, modifier = Modifier.size(18.dp))
            }
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(profile.name, color = Color(0xFFF1F6EE), fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(profile.version, color = profile.color, style = MaterialTheme.typography.labelMedium)
                Text(profile.loader, color = Color(0xFFAFC0B0), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun DeepWikiCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C2922))
    ) {
        Row(modifier = Modifier.padding(18.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
            Box(Modifier.size(42.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFF2C4937)), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Code, contentDescription = null, tint = Color(0xFF9BE58C))
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("DeepWiki field notes", color = Color(0xFFF1F6EE), fontWeight = FontWeight.Bold)
                Text("Keep setup guides, mod notes and world details close to each instance. Documentation cards are ready to connect.", color = Color(0xFFB6C7B7), style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420, heightDp = 900)
@Composable
private fun ZalithLauncherPreview() {
    ZalithLauncherTheme { ZalithLauncherScreen() }
}
