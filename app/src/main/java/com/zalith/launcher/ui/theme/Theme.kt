package com.zalith.launcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ZalithDarkColorScheme = darkColorScheme(
    primary = Color(0xFF9BE58C),
    onPrimary = Color(0xFF102116),
    primaryContainer = Color(0xFF285236),
    onPrimaryContainer = Color(0xFFCBF5C4),
    secondary = Color(0xFFB9D7A7),
    onSecondary = Color(0xFF1C321E),
    tertiary = Color(0xFFFFD18B),
    background = Color(0xFF101A16),
    onBackground = Color(0xFFF1F6EE),
    surface = Color(0xFF18251D),
    onSurface = Color(0xFFF1F6EE),
    surfaceVariant = Color(0xFF29382D),
    onSurfaceVariant = Color(0xFFC0CFC1),
    outline = Color(0xFF526657)
)

@Composable
fun ZalithLauncherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ZalithDarkColorScheme,
        typography = Typography(),
        content = content
    )
}
