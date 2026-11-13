package com.zalith.launcher.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

private val Background = Color(0xFF0C120F)
private val Panel = Color(0xFF17231B)
private val PanelLight = Color(0xFF203329)
private val Green = Color(0xFF9BE58C)
private val White = Color(0xFFF1F6EF)
private val Muted = Color(0xFF91A497)

@Composable
fun ZalithLauncherScreen() {

    var page by remember { mutableStateOf("Home") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Header()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (page) {

                    "Home" -> HomePage {
                        page = it
                    }

                    "Controls" -> ControlsPage()

                    "Instances" -> InstancesPage()

                    "Settings" -> SettingsPage()
                }
            }

            NavigationBar(
                containerColor = Panel,
                contentColor = Green
            ) {

                listOf(
                    "Home" to Icons.Default.Home,
                    "Instances" to Icons.Default.Storage,
                    "Controls" to Icons.Default.Gamepad,
                    "Settings" to Icons.Default.Settings
                ).forEach { (name, icon) ->

                    NavigationBarItem(
                        selected = page == name,
                        onClick = { page = name },
                        icon = {
                            Icon(icon, contentDescription = name)
                        },
                        label = {
                            Text(name)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Green,
                            selectedTextColor = Green,
                            indicatorColor = PanelLight,
                            unselectedIconColor = Muted,
                            unselectedTextColor = Muted
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun Header() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Panel)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(46.dp)
                .background(Green, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Z",
                color = Background,
                fontSize = 30.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                "ZALITH",
                color = White,
                fontSize = 23.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp
            )

            Text(
                "JAVA EDITION LAUNCHER",
                color = Muted,
                fontSize = 9.sp,
                letterSpacing = 1.5.sp
            )
        }

        Spacer(Modifier.weight(1f))

        Surface(
            color = Color(0xFF263D2C),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                "READY",
                color = Green,
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 7.dp
                ),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun HomePage(
    navigate: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            "Welcome back.",
            color = White,
            fontSize = 29.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Your Minecraft experience starts here.",
            color = Muted
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = PanelLight
            ),
            shape = RoundedCornerShape(26.dp)
        ) {

            Column(
                Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(13.dp)
            ) {

                Text(
                    "MINECRAFT JAVA",
                    color = Green,
                    fontSize = 11.sp,
                    letterSpacing = 2.sp
                )

                Text(
                    "Ready to play?",
                    color = White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Manage your instances and configure your controls.",
                    color = Muted
                )

                Button(
                    onClick = {
                        navigate("Controls")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green,
                        contentColor = Background
                    ),
                    shape = RoundedCornerShape(15.dp)
                ) {

                    Icon(Icons.Default.Gamepad, null)

                    Spacer(Modifier.width(8.dp))

                    Text(
                        "OPEN CONTROLS",
                        fontWeight = FontWeight.Black
                    )
                }

                OutlinedButton(
                    onClick = {
                        navigate("Instances")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text("MANAGE INSTANCES")
                }
            }
        }

        SectionTitle("QUICK ACCESS")

        QuickCard(
            Icons.Default.Gamepad,
            "Control Panel",
            "F1, F2, F3, F4 and keyboard",
            Green
        ) {
            navigate("Controls")
        }

        QuickCard(
            Icons.Default.Storage,
            "Instances",
            "Your Minecraft profiles",
            Color(0xFF80C9FF)
        ) {
            navigate("Instances")
        }

        QuickCard(
            Icons.Default.Settings,
            "Preferences",
            "Configure your launcher",
            Color(0xFFFFC477)
        ) {
            navigate("Settings")
        }
    }
}

@Composable
private fun SectionTitle(title: String) {

    Text(
        title,
        color = Muted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp
    )
}

@Composable
private fun QuickCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    accent: Color,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Panel, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            icon,
            null,
            tint = accent,
            modifier = Modifier.size(28.dp)
        )

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                title,
                color = White,
                fontWeight = FontWeight.Bold
            )

            Text(
                description,
                color = Muted,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.weight(1f))

        Icon(
            Icons.Default.ChevronRight,
            null,
            tint = Muted
        )
    }
}

@Composable
private fun ControlsPage() {

    val context = LocalContext.current

    var command by remember { mutableStateOf("") }
    var lastAction by remember {
        mutableStateOf("No control pressed")
    }

    var clickCount by remember { mutableIntStateOf(0) }

    val controls = listOf(
        listOf("F1", "F2"),
        listOf("F3", "F4")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            "Control Panel",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Minecraft keyboard shortcuts",
            color = Muted
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Panel
            ),
            shape = RoundedCornerShape(22.dp)
        ) {

            Column(
                Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SectionTitle("FUNCTION KEYS")

                controls.forEach { row ->

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        row.forEach { key ->

                            ControlButton(
                                key = key,
                                description = when (key) {
                                    "F1" -> "Toggle HUD"
                                    "F2" -> "Screenshot"
                                    "F3" -> "Debug screen"
                                    else -> "Game menu shortcut"
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                lastAction = "$key pressed"
                                Toast.makeText(
                                    context,
                                    "$key control selected",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Panel
            ),
            shape = RoundedCornerShape(22.dp)
        ) {

            Column(
                Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SectionTitle("MOVEMENT")

                Button(
                    onClick = {
                        lastAction = "SPACE pressed - Jump"
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green,
                        contentColor = Background
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {

                    Icon(Icons.Default.Keyboard, null)

                    Spacer(Modifier.width(10.dp))

                    Text(
                        "SPACE  /  JUMP",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ControlButton(
                        "CLICK",
                        "Left mouse click",
                        Modifier.weight(1f)
                    ) {
                        clickCount++
                        lastAction = "Mouse click #$clickCount"
                    }

                    ControlButton(
                        "ESC",
                        "Pause / menu",
                        Modifier.weight(1f)
                    ) {
                        lastAction = "ESC pressed"
                    }
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Panel
            ),
            shape = RoundedCornerShape(22.dp)
        ) {

            Column(
                Modifier.padding(17.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SectionTitle("TEXT INPUT")

                BasicTextField(
                    value = command,
                    onValueChange = { command = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Background,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(15.dp),
                    textStyle = androidx.compose.ui.text.TextStyle(
                        color = White,
                        fontSize = 15.sp
                    ),
                    singleLine = true,
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Green),
                    decorationBox = { innerTextField ->
                        Box {
                            if (command.isEmpty()) {
                                Text(
                                    "Type a command or message...",
                                    color = Muted,
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Button(
                    onClick = {
                        if (command.isNotBlank()) {
                            lastAction = "Text submitted: $command"
                            command = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PanelLight,
                        contentColor = Green
                    )
                ) {
                    Icon(Icons.Default.Send, null)

                    Spacer(Modifier.width(8.dp))

                    Text("SEND TEXT")
                }
            }
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF263D2C)
            ),
            shape = RoundedCornerShape(15.dp)
        ) {

            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {

                Text(
                    "LAST ACTION",
                    color = Green,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    lastAction,
                    color = White,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Text(
            "Control panel preview. Actions are recorded locally and are not sent to a running Minecraft game.",
            color = Muted,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ControlButton(
    key: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    var pressed by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(
                if (pressed) PanelLight else Background,
                RoundedCornerShape(15.dp)
            )
            .border(
                1.dp,
                if (pressed) Green else Color(0xFF304437),
                RoundedCornerShape(15.dp)
            )
            .clickable {
                pressed = !pressed
                onClick()
            }
            .padding(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {

        Text(
            key,
            color = if (pressed) Green else White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            description,
            color = Muted,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun InstancesPage() {

    var name by remember { mutableStateOf("") }

    val instances = remember {
        mutableStateListOf<String>()
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {

        Text(
            "Instances",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Create local launcher profiles.",
            color = Muted
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Instance name") },
            singleLine = true
        )

        Button(
            onClick = {
                if (name.isNotBlank()) {
                    instances.add(name)
                    name = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Add, null)
            Spacer(Modifier.width(8.dp))
            Text("CREATE INSTANCE")
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            items(instances) { instance ->

                QuickCard(
                    Icons.Default.Storage,
                    instance,
                    "Local profile",
                    Green
                ) {
                    instances.remove(instance)
                }
            }
        }
    }
}

@Composable
private fun SettingsPage() {

    var performance by remember { mutableStateOf(true) }
    var fps by remember { mutableStateOf(false) }
    var memory by remember { mutableFloatStateOf(4f) }

    Column(
        Modifier
            .fillMaxSize()
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(17.dp)
    ) {

        Text(
            "Settings",
            color = White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        SettingItem(
            "Performance mode",
            "Prefer performance-oriented defaults",
            performance
        ) {
            performance = it
        }

        SettingItem(
            "FPS counter preference",
            "Remember FPS overlay preference",
            fps
        ) {
            fps = it
        }

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Panel
            ),
            shape = RoundedCornerShape(18.dp)
        ) {

            Column(Modifier.padding(17.dp)) {

                Text(
                    "Memory preference",
                    color = White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "${memory.toInt()} GB",
                    color = Green
                )

                Slider(
                    value = memory,
                    onValueChange = { memory = it },
                    valueRange = 1f..8f,
                    steps = 6
                )
            }
        }

        Text(
            "Zalith Launcher • Android",
            color = Muted,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun SettingItem(
    title: String,
    description: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Panel, RoundedCornerShape(17.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(Modifier.weight(1f)) {

            Text(
                title,
                color = White,
                fontWeight = FontWeight.Bold
            )

            Text(
                description,
                color = Muted,
                fontSize = 11.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onChange
        )
    }
}
