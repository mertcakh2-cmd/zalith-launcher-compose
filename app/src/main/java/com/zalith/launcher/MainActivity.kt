package com.zalith.launcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zalith.launcher.ui.ZalithLauncherScreen
import com.zalith.launcher.ui.theme.ZalithLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ZalithLauncherTheme {
                ZalithLauncherScreen()
            }
        }
    }
}
