---
name: verify-basemvvm
description: "Drive BaseMVVM-AndroidKt (Compose Android todo list). Use when proving a change: doctor, unit tests, assembleDebug, adb screenshot, logcat."
---

# verify-basemvvm

Compose MVVM template. Home loads todos from `https://jsonplaceholder.typicode.com/todos`. Tap a row for Detail.

## Launch

```
./gradlew :app:assembleDebug
./verify screenshot --install --launch   # cold start only
./verify screenshot                      # capture whatever is on screen now
```

Ready = `com.msomu.androidkt.MainActivity` in the foreground and the top bar reads `Todo List`. Default `screenshot` does not reinstall or relaunch. Teardown = `adbharbor release -s <serial>` if this run acquired a lease. Never `--force`. Never the Samsung (`RZGL41JKGFT`).

Fresh worktrees need `local.properties` with `sdk.dir`. `BASE_URL` is optional — missing file defaults to jsonplaceholder.

## Doctor

```
./verify doctor
./verify --dry-run doctor
```

Healthy: `java`, `adb`, `gradlew`, and `ok: true` in the JSON. Device is optional for `test` / `assemble`. For `screenshot` / `logcat`: a local device, **or** `adbharbor submit` when harbor is on PATH and doctor has no device. Cloud with harbor MCP: `wait_for_run` + `get_proof`. This skill ships no harbor URL.

## Drive

No Compose test tags. Drive by visible text.

- Home: heading `Todo List`. First jsonplaceholder row is usually `delectus aut autem` with `User #1`.
- Detail: heading `Todo Details`. Close contentDescription `Back`. Edit is a no-op (`Edit Todo`).
- Checkbox on both screens is display-only (`onCheckedChange = {}`).

```
adb -s $SERIAL shell uiautomator dump /sdcard/window_dump.xml
```

## Evidence

`.receipt/proof/<utc>/` — `test_task.log`, `assemble_task.log`, `screen.png`, `logcat.txt`. Cleanup must not delete this directory.

## Cleanup

Release a lease this CLI acquired. Leave proof files. Do not uninstall the app unless you installed it for this run and the human asked.

## Helpers

```
./verify --help
./verify doctor
./verify test
./verify assemble
./verify screenshot
./verify screenshot --install --launch
./verify logcat --lines 200
```

`screenshot` captures the current frame. `--install` / `--launch` are opt-in. `ok` is true only when every step exits 0 and `screen.png` starts with PNG magic. `logcat` filters to the app pid; it is not ok if the package is not running.

Pin a serial with `./verify --device emulator-5554 screenshot` or `ANDROID_SERIAL`.

Upkeep: `/maintain-receipt`.
