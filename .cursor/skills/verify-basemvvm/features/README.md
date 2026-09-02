# BaseMVVM-AndroidKt feature map

Sweep top to bottom. Drive from the user path. Proof lands in `.receipt/proof/`.

## Baseline

- `./verify doctor` reports `ok: true`.
- APK from `:app:assembleDebug` at `app/build/outputs/apk/debug/app-debug.apk`.
- Pin `adb -s`. If `adbharbor` is present, `screenshot` / `logcat` take a lease and never `--force`.
- Start on Home. Top bar is `Todo List`. Default API is `https://jsonplaceholder.typicode.com/`.
- No Compose test tags. Drive by heading and row text.

## Features

- [Home list](./home.md) — fetch todos, show title + user id.
- [Detail](./detail.md) — tap a row, back, display-only checkbox.
- [Loading and error](./loading-error.md) — 6-row skeleton, then success or error text.
- [Theme](./theme.md) — system dark/light, dynamic color on API 31+.
