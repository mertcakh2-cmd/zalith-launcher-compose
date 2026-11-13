# Zalith Launcher

A Java Edition-inspired Android launcher UI concept built with Kotlin, Jetpack Compose and Material 3. The visual direction uses block-world colors, instance cards, Java runtime status, and a DeepWiki-style notes panel.

## What is included
- Android Gradle application module using Kotlin and Jetpack Compose Material 3.
- Responsive dark launcher dashboard with a Minecraft Java Edition-inspired visual style.
- Search and selection for sample instance profiles (Vanilla, Forge, and Fabric examples).
- Runtime status and launch controls presented as UI affordances.
- DeepWiki-inspired documentation panel.

## Build
Open this repository in Android Studio with Android SDK 35 installed, then run the `app` configuration. From a configured command line, build a debug APK with:

```bash
./gradlew assembleDebug
```

The APK output will be under `app/build/outputs/apk/debug/` after a successful build. This repository does not currently include the Gradle wrapper scripts, so use Android Studio's Gradle integration or generate a wrapper with a locally installed Gradle distribution.

## Important scope note
This is currently a **launcher UI prototype**, not a functioning Minecraft Java launcher. It does not download Minecraft files, authenticate Microsoft accounts, install/manage Java runtimes, or start the game process. The sample versions/loaders are display data only. Those integrations need to be implemented and tested separately, with appropriate licensing and account-security handling.
