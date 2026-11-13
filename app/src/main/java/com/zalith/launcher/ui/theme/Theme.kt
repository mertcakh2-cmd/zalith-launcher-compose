package com.zalith.launcher.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ZalithDarkColorScheme = darkColorScheme(
    primary = Color(0xFF7C9CFF),
    secondary = Color(0xFF78E3C5),
    tertiary = Color(0xFFF6BE6B),
    background = Color(0xFF08111F),
    surface = Color(0xFF111B2D),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFEAF4FF),
    onSurface = Color(0xFFEAF4FF),
    surfaceVariant = Color(0xFF182539),
    outline = Color(0xFF2B3E5A)
)

@Composable
fun ZalithLauncherTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ZalithDarkColorScheme,
        typography = Typography(),
        content = content
    )
}
