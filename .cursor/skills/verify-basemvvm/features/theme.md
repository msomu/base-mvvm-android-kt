# Theme

`BaseMVVMAndroidKtTheme` follows the system dark/light setting. API 31+ uses dynamic color.

## What it does

- `theme-system` reads `isSystemInDarkTheme()`.
- `theme-dynamic` uses `dynamicDarkColorScheme` / `dynamicLightColorScheme` when `dynamicColor` is true and SDK ≥ 31.
- `theme-static` falls back to `DarkColorScheme` / `LightColorScheme` in `Theme.kt`.

## How a user opens it

- There is no in-app toggle. Change the device dark-mode setting, then relaunch or wait for recreation.

## How verify drives it

Preconditions: app in foreground on Home.

- **Light.** System dark mode off. Dump. Surface is light; `Todo List` is readable.
- **Dark.** System dark mode on. Same heading, inverted surface.
- **Proof.** `./verify screenshot` once per setting. Do not pass `--launch` between dumps if you only flipped the setting — Activity may already have recreated.

## What usually lies

- Dynamic color on the emulator can look like a failed theme switch. Compare the heading contrast, not a remembered purple.
- A screenshot taken during Home Loading (gray skeletons) hides the theme. Capture after Success.
