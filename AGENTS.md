# AGENTS.md

Instructions for coding agents working in this repository. `CLAUDE.md` covers architecture and Gradle commands; this file covers how to prove a change and when to stop.

## Prove every change

Run from the repository root. Each command prints JSON; `"ok": true` is the only pass.

| Command | What it proves |
|---|---|
| `./verify doctor` | Java, adb, Gradle wrapper and Android SDK are found. Run first in a fresh checkout. |
| `./verify test` | `:app:testDebugUnitTest` passes. |
| `./verify assemble` | `:app:assembleDebug` builds an APK. |
| `./verify screenshot --install --launch` | The APK installs and starts on a device; saves `screen.png`. |
| `./verify logcat` | App log lines from the running process. |

CI (`.github/workflows/build_apk.yml`) runs `./gradlew detekt test assembleDebug`. Run the same three tasks locally before you push.

A fresh checkout needs `local.properties` containing `sdk.dir=<Android SDK path>`.

Evidence lands in `.receipt/proof/<utc>/`. Put the proof directory listing, or the screenshot and log, in the pull request body.

## What a feature does and how to drive it

`.cursor/skills/verify-basemvvm/features/` maps every user-visible feature to the text on screen and the steps that reach it. Read the matching file before you change a screen, and update it in the same pull request when the screen changes.

## When to stop

Stop when `detekt`, `test` and `assembleDebug` are green **and** you have proof for the feature you changed. Do not stop on "it compiles". Do not claim a device result you did not capture.

If the same check fails twice for the same reason, stop and report the failure log instead of retrying.

## Risk tiers

Read `RISK_TIERS.md` before opening a pull request and state the tier in the pull request body.

- `blast-radius` changes need a code owner (`.github/CODEOWNERS`). Never enable auto-merge.
- Never edit `.github/workflows/`, `gradle/` or `CODEOWNERS` as a side effect of another task.

## Golden-set eval

`evals/` checks that an AI reviewer assigns the tiers in `RISK_TIERS.md` correctly.

```
python3 -m unittest discover -s evals -p 'test_*.py'
python3 evals/run.py                 # replays committed model outputs, no key needed
python3 evals/run.py --replay evals/recorded/2026-10-05-cursor-agent-harness-errors.jsonl   # a failing run: two model calls errored
```

Replay scores outputs recorded earlier. It proves the scoring and the CI wiring; it does not measure the current model. Re-record with `--live` (see `evals/run.py`) when the prompt, model or golden set changes.

## Devices

Pin the device: `./verify --device <serial> …`. Take an adbharbor lease if the device is shared, and release it afterwards.
