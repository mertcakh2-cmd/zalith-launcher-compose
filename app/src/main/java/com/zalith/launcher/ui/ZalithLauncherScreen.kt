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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zalith.launcher.data.GameInstance
import com.zalith.launcher.data.InstanceStore
import com.zalith.launcher.data.LauncherSettings
import com.zalith.launcher.data.MojangVersionManifest
import com.zalith.launcher.data.MojangVersionRepository
import com.zalith.launcher.data.supportedLoaderChoices
import com.zalith.launcher.ui.theme.ZalithLauncherTheme

private val accents = listOf(Color(0xFF79D58A), Color(0xFFFFBD6E), Color(0xFF79D6CF), Color(0xFF9EAAFF))

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZalithLauncherScreen() {
    val context = LocalContext.current
    val instances = remember { mutableStateListOf<GameInstance>().apply { addAll(InstanceStore.load(context)) } }
    val settings = remember { LauncherSettings(context) }
    var selectedTab by remember { mutableStateOf("Home") }
    var query by remember { mutableStateOf("") }
    var manifest by remember { mutableStateOf<MojangVersionManifest?>(null) }
    var selectedVersion by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var performance by remember { mutableStateOf(settings.performanceMode) }
    var fps by remember { mutableStateOf(settings.showFps) }
    var reducedMotion by remember { mutableStateOf(settings.reducedAnimations) }
    var memoryGb by remember { mutableStateOf(settings.memoryGb) }

    LaunchedEffect(Unit) {
        try {
            manifest = MojangVersionRepository.fetchManifest()
            selectedVersion = manifest?.latestRelease
        } catch (error: Exception) {
            loadError = error.message ?: "Unable to load version list."
        } finally { loading = false }
    }

    val versions = manifest?.versions?.filter { it.type == "release" }
        ?.filter { it.id.contains(query, ignoreCase = true) }?.take(8).orEmpty()

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            Surface(color = Color(0xFF101A16), tonalElevation = 8.dp) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 7.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                    listOf("Home", "Instances", "Settings").forEach { tab ->
                        FilterChip(selected = selectedTab == tab, onClick = { selectedTab = tab }, label = { Text(tab) },
                            leadingIcon = if (tab == "Settings") ({ Icon(Icons.Default.Settings, null, Modifier.size(16.dp)) }) else null)
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFF101A16), Color(0xFF17251C), Color(0xFF0D1512))))) {
            Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp), verticalArrangement = Arrangement.spacedBy(17.dp)) {
                Header()
                if (selectedTab != "Settings") SearchField(query) { query = it }
                when (selectedTab) {
                    "Settings" -> SettingsPage(
                        performance, { performance = it; settings.performanceMode = it },
                        fps, { fps = it; settings.showFps = it },
                        reducedMotion, { reducedMotion = it; settings.reducedAnimations = it },
                        memoryGb, { memoryGb = it; settings.memoryGb = it }
                    )
                    "Instances" -> {
                        InstanceSection(instances, selectedVersion ?: manifest?.latestRelease ?: "1.21.1", showCreate = { showCreateDialog = true }, onDelete = {
                            instances.remove(it); InstanceStore.save(context, instances)
                        })
                        VersionsCard(loading, loadError, manifest, versions.map { it.id }, selectedVersion) { selectedVersion = it }
                    }
                    else -> {
                        HeroCard(version = selectedVersion ?: "Loading…", onBrowse = { selectedTab = "Instances" })
                        InstanceSection(instances.take(3), selectedVersion ?: manifest?.latestRelease ?: "1.21.1", showCreate = { showCreateDialog = true }, onDelete = {
                            instances.remove(it); InstanceStore.save(context, instances)
                        })
                        VersionsCard(loading, loadError, manifest, versions.map { it.id }, selectedVersion) { selectedVersion = it }
                        RuntimeCard(memoryGb)
                        DeepWikiCard()
                    }
                }
                Text("ZALITH • JAVA EDITION", Modifier.fillMaxWidth().padding(bottom = 8.dp), color = Color(0xFF718477), style = MaterialTheme.typography.labelSmall, letterSpacing = 2.sp)
            }
        }
    }

    if (showCreateDialog) CreateInstanceDialog(
        defaultVersion = selectedVersion ?: manifest?.latestRelease ?: "1.21.1",
        onDismiss = { showCreateDialog = false },
        onCreate = { name, version, loader ->
            val instance = GameInstance(name.trim(), version, loader)
            instances.add(instance); InstanceStore.save(context, instances)
            showCreateDialog = false; selectedTab = "Instances"
        }
    )
}

@Composable
private fun InstanceSection(instances: List<GameInstance>, defaultVersion: String, showCreate: () -> Unit, onDelete: (GameInstance) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Instances", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("Profiles for versions and mod loaders", color = Color(0xFFB2C4B4), style = MaterialTheme.typography.bodySmall)
        }
        Button(onClick = showCreate, shape = RoundedCornerShape(14.dp)) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(5.dp)); Text("New") }
    }
    if (instances.isEmpty()) EmptyInstancesCard(showCreate) else instances.forEachIndexed { index, instance ->
        InstanceCard(instance, accents[index % accents.size]) { onDelete(instance) }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CreateInstanceDialog(defaultVersion: String, onDismiss: () -> Unit, onCreate: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("My Minecraft instance") }
    var version by remember { mutableStateOf(defaultVersion) }
    var loader by remember { mutableStateOf("Vanilla") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create instance") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(11.dp)) {
                Text("Save a local profile. Creating it does not install game files or loaders.", style = MaterialTheme.typography.bodySmall)
                OutlinedTextField(name, { name = it }, label = { Text("Instance name") }, singleLine = true)
                OutlinedTextField(version, { version = it }, label = { Text("Minecraft version") }, singleLine = true)
                Text("Loader", style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    supportedLoaderChoices.forEach { choice -> FilterChip(loader == choice, { loader = choice }, label = { Text(choice) }) }
                }
            }
        },
        confirmButton = { TextButton(onClick = { onCreate(name, version, loader) }, enabled = name.isNotBlank() && version.isNotBlank()) { Text("Create profile") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

@Composable
private fun SettingsPage(performance: Boolean, onPerformance: (Boolean) -> Unit, fps: Boolean, onFps: (Boolean) -> Unit, reducedMotion: Boolean, onReducedMotion: (Boolean) -> Unit, memoryGb: Float, onMemory: (Float) -> Unit) {
    Text("Launcher settings", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
    Text("Saved on this device. These preferences are ready for a future game runtime.", color = Color(0xFFB2C4B4), style = MaterialTheme.typography.bodySmall)
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF18251D))) {
        Column(Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SettingSwitch("Performance profile", "Prefer performance-oriented defaults", performance, onPerformance)
            SettingSwitch("FPS overlay preference", "Remember overlay choice for a future game session", fps, onFps)
            SettingSwitch("Reduce UI motion", "Use less animated launcher transitions", reducedMotion, onReducedMotion)
            Spacer(Modifier.height(4.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Memory budget preference", fontWeight = FontWeight.SemiBold)
                Text("${memoryGb.toInt()} GB", color = Color(0xFFB9F6CA))
            }
            Slider(value = memoryGb, onValueChange = onMemory, valueRange = 1f..8f, steps = 6)
            Text("A preference only: the app does not allocate game memory until a runtime is implemented.", color = Color(0xFF9FB39F), style = MaterialTheme.typography.labelSmall)
        }
    }
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF203829))) {
        Column(Modifier.padding(17.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("Account & security", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("No sign-in or token storage is enabled. Never use fake Minecraft tokens; account login requires the supported Microsoft authorization flow.", color = Color(0xFFC2D8C1), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun SettingSwitch(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = FontWeight.SemiBold)
            Text(description, color = Color(0xFF9FB39F), style = MaterialTheme.typography.labelSmall)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun Header() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(15.dp)).background(Brush.linearGradient(listOf(Color(0xFF83D968), Color(0xFF286943)))).border(1.dp, Color(0xFFB5F19D).copy(alpha = .45f), RoundedCornerShape(15.dp)), contentAlignment = Alignment.Center) { Text("Z", color = Color(0xFF102117), fontSize = 29.sp, fontWeight = FontWeight.Black) }
            Column { Text("ZALITH", color = Color(0xFFF1F6EE), fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp); Text("JAVA EDITION LAUNCHER", color = Color(0xFF9FB39F), fontSize = 10.sp, letterSpacing = 1.5.sp) }
        }
        Surface(shape = CircleShape, color = Color(0xFF24382A)) { Text("READY", Modifier.padding(horizontal = 12.dp, vertical = 8.dp), color = Color(0xFFC9F4CE), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) }
    }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(query, onQueryChange, Modifier.fillMaxWidth(), singleLine = true, placeholder = { Text("Search releases…") }, leadingIcon = { Icon(Icons.Default.Search, null) }, shape = RoundedCornerShape(18.dp),
        colors = TextFieldDefaults.colors(focusedContainerColor = Color(0xFF18251D), unfocusedContainerColor = Color(0xFF18251D), focusedIndicatorColor = Color(0xFF86D879), unfocusedIndicatorColor = Color(0xFF35483A), focusedTextColor = Color(0xFFF1F6EE), unfocusedTextColor = Color(0xFFF1F6EE), cursorColor = Color(0xFF9BE58C))
}

@Composable
private fun HeroCard(version: String, onBrowse: () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF203829)), elevation = CardDefaults.cardElevation(5.dp)) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AssistChip(onClick = onBrowse, label = { Text("JAVA • $version") }, leadingIcon = { Icon(Icons.Default.Code, null, Modifier.size(16.dp)) })
            Text("Your next world\nstarts here.", color = Color.White, fontSize = 27.sp, lineHeight = 32.sp, fontWeight = FontWeight.ExtraBold)
            Text("Minecraft Java version discovery", color = Color(0xFFC2D8C1), style = MaterialTheme.typography.bodyMedium)
            Button(onClick = onBrowse, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BE58C), contentColor = Color(0xFF122116))) { Icon(Icons.Default.PlayArrow, null); Spacer(Modifier.width(7.dp)); Text("Manage instances", fontWeight = FontWeight.Bold) }
            Text("This prototype does not authenticate, install, or launch Minecraft yet.", color = Color(0xFFAFC6B0), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun EmptyInstancesCard(onCreate: () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF18251D))) {
        Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(9.dp)) {
            Icon(Icons.Default.Storage, null, tint = Color(0xFF9BE58C), modifier = Modifier.size(32.dp))
            Text("No instances yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Create a profile for Vanilla, Fabric, Forge, NeoForge, or OptiFine.", color = Color(0xFFB6C7B7), style = MaterialTheme.typography.bodySmall)
            Button(onClick = onCreate) { Icon(Icons.Default.Add, null); Spacer(Modifier.width(5.dp)); Text("Create instance") }
        }
    }
}

@Composable
private fun InstanceCard(instance: GameInstance, accent: Color, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1A281F))) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(Modifier.size(46.dp).clip(RoundedCornerShape(14.dp)).background(accent.copy(alpha = .18f)), contentAlignment = Alignment.Center) { Icon(Icons.Default.Storage, null, tint = accent) }
            Column(Modifier.weight(1f)) {
                Text(instance.name, color = Color(0xFFF1F6EE), fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text("Minecraft ${instance.version} • ${instance.loader}", color = accent, style = MaterialTheme.typography.bodySmall)
                Text("Saved profile · game files not installed", color = Color(0xFF9FB39F), style = MaterialTheme.typography.labelSmall)
            }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, "Delete profile", tint = Color(0xFFB6C7B7)) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun VersionsCard(loading: Boolean, error: String?, manifest: MojangVersionManifest?, versions: List<String>, selected: String?, onSelect: (String) -> Unit) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF18251D))) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(11.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column { Text("Official Java releases", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold); Text("Mojang version manifest", color = Color(0xFFB2C4B4), style = MaterialTheme.typography.bodySmall) }
                if (loading) CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp, color = Color(0xFF9BE58C)) else if (manifest != null) Icon(Icons.Default.CheckCircle, "Loaded", tint = Color(0xFF9BE58C))
            }
            if (manifest != null) {
                Text("Latest ${manifest.latestRelease}  •  Snapshot ${manifest.latestSnapshot}", color = Color(0xFFC9F4CE), style = MaterialTheme.typography.labelMedium)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) { versions.forEach { version -> FilterChip(selected == version, { onSelect(version) }, label = { Text(version) }) } }
            } else if (error != null) Text("Could not load versions: $error", color = Color(0xFFFFB4AB), style = MaterialTheme.typography.bodySmall) else Text("Connecting to Mojang…", color = Color(0xFFB2C4B4), style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun RuntimeCard(memoryGb: Float) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF18251D))) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.spacedBy(13.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Memory, null, tint = Color(0xFF9BE58C), modifier = Modifier.size(32.dp))
            Column { Text("Runtime preferences", fontWeight = FontWeight.Bold); Text("${memoryGb.toInt()} GB preference · Java runtime not configured", color = Color(0xFFB6C7B7), style = MaterialTheme.typography.bodySmall) }
        }
    }
}

@Composable
private fun DeepWikiCard() {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1C2922))) {
        Row(Modifier.padding(18.dp), horizontalArrangement = Arrangement.spacedBy(13.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.Code, null, tint = Color(0xFF9BE58C), modifier = Modifier.size(30.dp))
            Column { Text("DeepWiki field notes", fontWeight = FontWeight.Bold); Text("Keep setup guides, mod notes and world details close to each instance.", color = Color(0xFFB6C7B7), style = MaterialTheme.typography.bodySmall, lineHeight = 18.sp) }
        }
    }
}

@Preview(showBackground = true, widthDp = 420, heightDp = 900)
@Composable
private fun ZalithLauncherPreview() { ZalithLauncherTheme { ZalithLauncherScreen() } }
