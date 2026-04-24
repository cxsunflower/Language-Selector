## Language Selector

[中文说明](README.zh-CN.md)

Language Selector allows you to set languages for individual apps on Android 13 and higher. It tries to replicate the behavior of Android's built-in "App languages" feature on ROMs that do not expose the setting in system UI.

Language Selector does not translate apps. It only asks Android to launch an app with a selected locale. The selected language will only appear when the target app supports that locale.

<div>
<img src="https://raw.githubusercontent.com/VegaBobo/Language-Selector/main/other/preview_1.jpg" alt="preview" width="200"/>
<img src="https://raw.githubusercontent.com/VegaBobo/Language-Selector/main/other/preview_2.jpg" alt="preview" width="200"/>
</div>

## Requirements

- Android 13 or higher.
- Shizuku must be installed, running, and granted to Language Selector.
- Root mode is also supported when available.

## Features

- Set per-app languages using Android locale APIs.
- Browse user apps and system apps separately.
- Search apps by name or package name.
- Pin frequently used languages.
- Quickly change the current app language from a QS tile.
- Open app settings or source information from the app detail and about pages.

## Latest Updates

- Added a Material 3 app bar on the home screen with the app name, About action, and a light/dark theme toggle.
- Added persistent light/dark theme switching. The app follows the system theme on first launch and remembers manual changes afterward.
- Simplified the home search bar into a focused search-only field with rounded corners and horizontal spacing.
- Moved app filters between the search bar and app list, using selectable chips for user apps and system apps.
- Localized and restyled the app list labels: User App, System App, and Modified.
- Added missing translations for the search placeholder and theme toggle descriptions.
- Improved Compose previews and fixed preview crashes caused by drawables without intrinsic size.
- Fixed service binding so RootService is bound from the main thread.
- Ignored the local `.codex` workspace entry.

## Usage

1. Install Language Selector from the Releases section.
2. Install and start Shizuku.
3. Open Language Selector, grant Shizuku permission, and tap Proceed.
4. Search for an app or filter by user/system apps.
5. Select an app and choose the language you want it to use.

Changing locales for unsupported apps or system apps may cause unexpected behavior and is not recommended.

## Pinning Languages

Long-press a language to pin it. Pinned languages appear at the top of the language list and are also available in the QS tile.

## Quick Settings Tile

Add the Language Selector QS tile to quickly change the language of the current foreground app. The tile uses pinned languages. If no pinned language exists, the tile is marked as unavailable. Changing system app languages from the QS tile is not supported.

## Language Availability

Language Selector builds its language list from `Locale.getAvailableLocales()`. This makes many locales available, but the list can be large and not perfectly filtered.

## Background

This project was created because some Android 13 ROMs, such as MIUI builds, include Android's per-app locale service but do not expose a Settings UI for it. Since ADB can control app locales through `cmd locale`, Language Selector provides a friendly front-end for the same capability through Shizuku or root access.
