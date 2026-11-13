# Zalith Launcher

A Java Edition-inspired Android launcher prototype built with Kotlin, Jetpack Compose and Material 3.

## Included
- Local instance profiles saved on the device (name, Minecraft version, loader choice).
- Create and delete profile cards.
- Loader choices: Vanilla, Fabric, Forge, NeoForge and OptiFine.
- Live release metadata from Mojang's public version manifest.
- Custom vector launcher icon and editable SVG source in `assets/`.
- DeepWiki-inspired notes card and Material 3 dark green styling.

## Build
Open in Android Studio with Android SDK 35. Build the debug APK with `./gradlew assembleDebug` if a Gradle wrapper is present, or use Android Studio's Gradle integration. APK output is normally at `app/build/outputs/apk/debug/`.

## Scope and compatibility
Instances are local profile records only. The loader names are selectable metadata, not installed integrations. The app does not download Minecraft/assets, install Java, authenticate Microsoft accounts, install Fabric/Forge/NeoForge/OptiFine, or launch the game. A production launcher needs these components implemented with the loader vendors' supported installers and Minecraft's licensing/account requirements.

The listed loader support is not interchangeable: OptiFine support depends on the selected Minecraft version and its installer availability, and mod-loader compatibility must be validated per version.
