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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
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
import com.zalith.launcher.data.MojangVersionManifest
import com.zalith.launcher.data.MojangVersionRepository
import com.zalith.launcher.ui.theme.ZalithLauncherTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private val versionAccent = listOf(
    Color(0xFF79D58A), Color(0xFFFFBD6E), Color(0xFF79D6CF), Color(0xFF9EAAFF)
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZalithLauncherScreen() {
    var selectedTab by remember { mutableStateOf("Home") }
    var query by remember { mutableStateOf("") }
    var manifest by remember { mutableStateOf<MojangVersionManifest?>(null) }
    var selectedVersion by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(true) }
    var loadError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            manifest = withContext(Dispatchers.IO) { MojangVersionRepository.fetchManifest() }
            selectedVersion = manifest?.latestRelease
        } catch (error: Exception) {
            loadError = error.message ?: "Could not reach Mojang's version service."
        } finally {
            loading = false
        }
    }

    val releases = manifest?.versions?.filter { it.type == "release" }.orEmpty()
    val shownVersions = releases
        .filter { it.id.contains(query, ignoreCase = true) }
        .take(8)

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            Surface(color = Color(0xFF101A16), tonalElevation = 8.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf("Home", "Versions", "Settings").forEach { tab ->
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
            modifier = Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color(0xFF101A16), Color(0xFF17251C), Color(0xFF0D1512)))
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(scaffoldPadding)
                    .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Header()
                SearchField(query = query, onQueryChange = { query = it })
                HeroCard(version = selectedVersion ?: "Loading…", onBrowse = { selectedTab = "Versions" })
                OfficialVersionsCard(
                    loading = loading,
                    error = loadError,
                    manifest = manifest,
                    versions = shownVersions.map { it.id },
                    selectedVersion = selectedVersion,
                    onSelectVersion = { selectedVersion = it }
                )
                JavaRuntimeCard()
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
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PixelCrest()
            Column {
                Text("ZALITH", color = Color(0xFFF1F6EE), fontSize = 25.sp, fontWeight = FontWeight.Black, letterSpacing = 2.sp)
                Text("JAVA EDITION LAUNCHER", color = Color(0xFF9FB39F), fontSize = 10.sp, letterSpacing = 1.5.sp)
            }
        }
        Surface(shape = CircleShape, color = Color(0xFF24382A)) {
            Row(Modifier.padding(horizontal = 11.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF82E69A)))
                Spacer(Modifier.width(7.dp))
                Text("ONLINE", color = Color(0xFFC9F4CE), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun PixelCrest() {
    Box(
        Modifier.size(48.dp).clip(RoundedCornerShape(15.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF83D968), Color(0xFF286943))))
            .border(1.dp, Color(0xFFB5F19D).copy(alpha = 0.45f), RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ) { Text("Z", color = Color(0xFF102117), fontSize = 29.sp, fontWeight = FontWeight.Black) }
}

@Composable
private fun SearchField(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = { Text("Search official Java releases…") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        shape = RoundedCornerShape(18.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFF18251D), unfocusedContainerColor = Color(0xFF18251D),
            focusedIndicatorColor = Color(0xFF86D879), unfocusedIndicatorColor = Color(0xFF35483A),
            focusedTextColor = Color(0xFFF1F6EE), unfocusedTextColor = Color(0xFFF1F6EE),
            focusedLeadingIconColor = Color(0xFF9BE58C), unfocusedLeadingIconColor = Color(0xFF9FB39F),
            focusedPlaceholderColor = Color(0xFF829385), unfocusedPlaceholderColor = Color(0xFF829385),
            cursorColor = Color(0xFF9BE58C)
        )
    )
}

@Composable
private fun HeroCard(version: String, onBrowse: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF203829)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box {
            BlockLandscape(Modifier.align(Alignment.TopEnd))
            Column(Modifier.fillMaxWidth().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                AssistChip(onClick = onBrowse, label = { Text("JAVA • $version") }, leadingIcon = {
                    Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp))
                })
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Your next world\nstarts here.", color = Color.White, fontSize = 27.sp, lineHeight = 32.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Official Mojang release metadata", color = Color(0xFFC2D8C1), style = MaterialTheme.typography.bodyMedium)
                }
                Button(
                    onClick = onBrowse, shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9BE58C), contentColor = Color(0xFF122116))
                ) {
                    Icon(Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Choose game version", fontWeight = FontWeight.Bold)
                }
                Text("Version discovery is live. Installing, signing in and launching the game are not connected yet.", color = Color(0xFFAFC6B0), style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun BlockLandscape(modifier: Modifier = Modifier) {
    Column(modifier.padding(top = 18.dp, end = 18.dp), horizontalAlignment = Alignment.End) {
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OfficialVersionsCard(
    loading: Boolean,
    error: String?,
    manifest: MojangVersionManifest?,
    versions: List<String>,
    selectedVersion: String?,
    onSelectVersion: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF18251D))
    ) {
        Column(Modifier.fillMaxWidth().padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Official Java versions", color = Color(0xFFF1F6EE), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Fetched from Mojang version manifest", color = Color(0xFFB2C4B4), style = MaterialTheme.typography.bodySmall)
                }
                if (loading) CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.dp, color = Color(0xFF9BE58C))
                else Icon(Icons.Default.CheckCircle, contentDescription = "Manifest loaded", tint = Color(0xFF9BE58C))
            }
            if (manifest != null) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    StatusPill("Latest release", manifest.latestRelease)
                    StatusPill("Snapshot", manifest.latestSnapshot)
                }
                Text("Select a release", color = Color(0xFFDCE8DC), style = MaterialTheme.typography.labelLarge)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    versions.forEachIndexed { index, version ->
                        FilterChip(
                            selected = selectedVersion == version,
                            onClick = { onSelectVersion(version) },
                            label = { Text(version) },
                            leadingIcon = if (selectedVersion == version) {
                                { Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null
                        )
                    }
                }
                Text("Showing the newest matching releases. Search above to filter the Mojang release list.", color = Color(0xFF9FB39F), style = MaterialTheme.typography.labelSmall)
            } else if (error != null) {
                Text("Could not load official versions: $error", color = Color(0xFFFFB4AB), style = MaterialTheme.typography.bodySmall)
                Text("Check your connection and reopen the app to retry.", color = Color(0xFFB2C4B4), style = MaterialTheme.typography.labelSmall)
            } else {
                Text("Connecting to Mojang…", color = Color(0xFFB2C4B4), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun StatusPill(label: String, value: String) {
    Surface(color = Color(0xFF293F2E), shape = RoundedCornerShape(12.dp)) {
        Column(Modifier.padding(horizontal = 11.dp, vertical = 8.dp)) {
            Text(label, color = Color(0xFFB2C4B4), style = MaterialTheme.typography.labelSmall)
            Text(value, color = Color(0xFFC9F4CE), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun JavaRuntimeCard() {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF18251D))) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(shape = RoundedCornerShape(15.dp), color = Color(0xFF293F2E), modifier = Modifier.size(46.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Memory, contentDescription = null, tint = Color(0xFF9BE58C)) }
            }
            Column(Modifier.weight(1f)) {
                Text("Java runtime", color = Color(0xFFF1F6EE), fontWeight = FontWeight.Bold)
                Text("Runtime detection and setup not implemented", color = Color(0xFFB6C7B7), style = MaterialTheme.typography.bodySmall)
            }
            Icon(Icons.Default.ExpandMore, contentDescription = null, tint = Color(0xFFB6C7B7))
        }
    }
}

@Composable
private fun DeepWikiCard() {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1C2922))) {
        Row(Modifier.padding(18.dp), horizontalArrangement = Arrangement.spacedBy(14.dp), verticalAlignment = Alignment.Top) {
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
