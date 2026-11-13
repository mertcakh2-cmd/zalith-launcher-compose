package com.zalith.launcher.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class LauncherAppItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val tint: Color,
    val icon: ImageVector
)

object LauncherCatalog {
    fun apps(): List<LauncherAppItem> = listOf(
        LauncherAppItem("forge", "Forge", "Projects", Color(0xFF7C9CFF), Icons.Default.Build),
        LauncherAppItem("deepwiki", "DeepWiki", "Docs", Color(0xFFB07CFF), Icons.Default.Bookmarks),
        LauncherAppItem("studio", "Studio", "Code", Color(0xFF6FE7C1), Icons.Default.Code),
        LauncherAppItem("worlds", "Worlds", "Games", Color(0xFFF2B66D), Icons.Default.Gamepad),
        LauncherAppItem("modes", "Modes", "Layouts", Color(0xFF7ED6FF), Icons.Default.Layers),
        LauncherAppItem("mods", "Mods", "Assets", Color(0xFF7EE7A9), Icons.Default.Palette),
        LauncherAppItem("memory", "Memory", "System", Color(0xFF81B4FF), Icons.Default.Memory),
        LauncherAppItem("hub", "Hub", "Smart", Color(0xFFFF9DD0), Icons.Default.SmartToy),
        LauncherAppItem("settings", "Settings", "Control", Color(0xFFB2C7FF), Icons.Default.Settings),
        LauncherAppItem("favorite", "Favorites", "Pinned", Color(0xFFF2D480), Icons.Default.Star)
    )
}
