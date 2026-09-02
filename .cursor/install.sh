#!/usr/bin/env bash
#
# Idempotent Cloud Agent bootstrap for the BaseMVVM-AndroidKt project.
# Ensures the Android SDK, local.properties, and Gradle dependencies are ready.
# Safe to run repeatedly and on top of a prebuilt snapshot.
set -euo pipefail

REPO_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ANDROID_SDK_ROOT="${ANDROID_SDK_ROOT:-/opt/android-sdk}"
CMDLINE_TOOLS_VERSION="11076708"
PLATFORM="platforms;android-37.0"
BUILD_TOOLS="build-tools;37.0.0"

log() { printf '\n[install] %s\n' "$*"; }

# --- 1. Ensure the Android command-line tools exist -------------------------
if [ ! -x "${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager" ]; then
  log "Installing Android command-line tools into ${ANDROID_SDK_ROOT}"
  sudo mkdir -p "${ANDROID_SDK_ROOT}/cmdline-tools"
  sudo chown -R "$(id -u):$(id -g)" "${ANDROID_SDK_ROOT}"
  tmp="$(mktemp -d)"
  curl -fsSL -o "${tmp}/cmdline-tools.zip" \
    "https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_VERSION}_latest.zip"
  unzip -q "${tmp}/cmdline-tools.zip" -d "${tmp}/extracted"
  rm -rf "${ANDROID_SDK_ROOT}/cmdline-tools/latest"
  mv "${tmp}/extracted/cmdline-tools" "${ANDROID_SDK_ROOT}/cmdline-tools/latest"
  rm -rf "${tmp}"
else
  log "Android command-line tools already present"
fi

SDKMANAGER="${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin/sdkmanager"

# --- 2. Accept licenses and ensure required SDK packages --------------------
log "Accepting SDK licenses"
yes | "${SDKMANAGER}" --sdk_root="${ANDROID_SDK_ROOT}" --licenses >/dev/null 2>&1 || true

log "Ensuring platform-tools, ${PLATFORM}, ${BUILD_TOOLS}"
"${SDKMANAGER}" --sdk_root="${ANDROID_SDK_ROOT}" \
  "platform-tools" "${PLATFORM}" "${BUILD_TOOLS}" >/dev/null

# --- 3. Point the Gradle build at the SDK ----------------------------------
log "Writing ${REPO_ROOT}/local.properties"
printf 'sdk.dir=%s\n' "${ANDROID_SDK_ROOT}" > "${REPO_ROOT}/local.properties"

# --- 4. Warm the Gradle dependency cache -----------------------------------
log "Warming Gradle dependencies"
cd "${REPO_ROOT}"
chmod +x ./gradlew
export ANDROID_SDK_ROOT ANDROID_HOME="${ANDROID_SDK_ROOT}"
./gradlew --no-daemon help -q >/dev/null

log "Environment ready. SDK at ${ANDROID_SDK_ROOT}"
