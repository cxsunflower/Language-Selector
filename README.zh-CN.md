## Language Selector

[English README](README.md)

Language Selector 可以在 Android 13 及以上系统中为单个应用设置语言。它尝试复刻 Android 原生的“应用语言”功能，适合系统已经具备相关能力、但 ROM 没有在设置中暴露入口的设备。

Language Selector 不会翻译应用。它只会请求 Android 使用指定 Locale 启动目标应用；只有目标应用本身支持该语言时，界面才会按预期显示。

<div>
<img src="https://raw.githubusercontent.com/VegaBobo/Language-Selector/main/other/preview_1.jpg" alt="preview" width="200"/>
<img src="https://raw.githubusercontent.com/VegaBobo/Language-Selector/main/other/preview_2.jpg" alt="preview" width="200"/>
</div>

## 使用要求

- Android 13 或更高版本。
- 必须安装并启动 Shizuku，并授予 Language Selector 权限。
- 如果设备具备 Root，也支持 Root 模式。

## 功能

- 通过 Android Locale API 为单个应用设置语言。
- 分别浏览用户应用和系统应用。
- 按应用名或包名搜索应用。
- 置顶常用语言。
- 通过快捷设置磁贴快速切换当前应用语言。
- 在应用详情页和关于页打开应用设置或项目信息。

## 本次更新

- 首页加入 Material 3 Appbar，显示应用名，并在右侧放置 About 和浅色/深色主题切换按钮。
- 新增持久化浅色/深色主题切换：首次启动跟随系统主题，手动切换后会记住用户选择。
- 将首页搜索框简化为纯搜索输入框，并增加圆角和水平间距。
- 将应用筛选移动到搜索框与列表之间，使用可勾选标签分别控制用户应用和系统应用。
- 将应用列表标签 User App、System App、Modified 加入多语言资源，并改为更清晰的胶囊样式。
- 补齐搜索占位文案和主题切换无障碍文案的多语言翻译。
- 改进 Compose Preview，并修复无固有尺寸 Drawable 导致的预览崩溃。
- 修复 RootService 绑定需要在主线程执行的问题。
- 忽略本地 `.codex` 工作区入口。

## 使用方式

1. 从 Releases 页面安装 Language Selector。
2. 安装并启动 Shizuku。
3. 打开 Language Selector，授予 Shizuku 权限，并点击“继续”。
4. 搜索应用，或通过用户应用/系统应用标签筛选。
5. 选择一个应用，并为它设置想使用的语言。

不建议为不支持目标语言的应用或系统应用强制修改语言，这可能导致不可预期的行为。

## 置顶语言

长按语言即可置顶。置顶语言会显示在语言列表顶部，也会出现在快捷设置磁贴中。

## 快捷设置磁贴

添加 Language Selector 快捷设置磁贴后，可以快速切换当前前台应用的语言。磁贴会使用已置顶语言；如果没有置顶语言，磁贴会显示为不可用。快捷设置磁贴不支持切换系统应用语言。

## 语言来源

Language Selector 使用 `Locale.getAvailableLocales()` 构建语言列表。这会带来大量可选 Locale，但列表较长，且过滤不一定完全准确。

## 背景

这个项目最初是为了解决部分 Android 13 ROM 已经具备按应用设置语言的系统服务，却没有在系统设置中提供入口的问题。由于 ADB 可以通过 `cmd locale` 控制应用语言，Language Selector 使用 Shizuku 或 Root 权限为该能力提供一个更友好的前端界面。
