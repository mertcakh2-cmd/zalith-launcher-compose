#!/usr/bin/env sh

set -eu

GRADLE_VERSION="8.7"
BASE_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"

CACHE="${GRADLE_USER_HOME:-$HOME/.gradle}/bootstrap"
DIST="$CACHE/gradle-$GRADLE_VERSION"
BIN="$DIST/gradle-$GRADLE_VERSION/bin/gradle"
ZIP="$CACHE/gradle-$GRADLE_VERSION-bin.zip"

if [ ! -x "$BIN" ]; then
    mkdir -p "$CACHE"

    if [ ! -f "$ZIP" ]; then
        echo "Downloading Gradle $GRADLE_VERSION..."

        curl -fL \
        "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip" \
        -o "$ZIP"
    fi

    mkdir -p "$DIST"
    unzip -q -o "$ZIP" -d "$DIST"
fi

exec "$BIN" -p "$BASE_DIR" "$@"
