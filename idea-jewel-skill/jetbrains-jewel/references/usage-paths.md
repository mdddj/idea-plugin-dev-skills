# Usage Paths

## Overview

Use this file when the request is about how to apply Jewel, which dependency path to choose, or why a given integration is failing.

## Contents

- Decide the integration mode
- Standalone Compose for Desktop
- IntelliJ Platform plugin
- Decorated window
- Markdown renderer
- Icons, fonts, and Swing interop

## Decide the integration mode

- Use standalone mode for a regular Compose for Desktop app that is not running inside the IntelliJ Platform.
- Use bridge mode for an IntelliJ Platform plugin that needs Compose UI to follow the active IDE Look and Feel.
- Use markdown mode when the request is specifically about rendering Markdown with Jewel-native styling.
- Use decorated-window mode when the request is about custom title bars or JetBrains-like window chrome.

## Standalone Compose for Desktop

Use the standalone `int-ui` artifacts.

Minimum pattern:

```kotlin
dependencies {
    implementation("org.jetbrains.jewel:jewel-int-ui-standalone:[jewel version]")
    implementation(compose.desktop.currentOs) {
        exclude(group = "org.jetbrains.compose.material")
    }
}
```

Wrap the app with `IntUiTheme`:

```kotlin
IntUiTheme(isDark = false) {
    // UI
}
```

Add `jewel-int-ui-decorated-window` only when custom window decoration is needed.

Important constraints:

- Require JetBrains Runtime for supported standalone execution.
- Align Kotlin, Compose Multiplatform, and Jewel versions.
- Use `platform/jewel/VERSIONS.md` and release notes when answering compatibility questions.

## IntelliJ Platform plugin

Use bundled IntelliJ Platform modules instead of standalone artifacts.

Minimum pattern:

```kotlin
dependencies {
    intellijPlatform {
        bundledModule("intellij.platform.jewel.foundation")
        bundledModule("intellij.platform.jewel.ui")
        bundledModule("intellij.platform.jewel.ideLafBridge")
        bundledModule("intellij.platform.jewel.markdown.core")
        bundledModule("intellij.platform.jewel.markdown.ideLafBridgeStyling")
        bundledModule("intellij.libraries.compose.foundation.desktop")
        bundledModule("intellij.libraries.skiko")
    }
}
```

Wrap Compose content with `SwingBridgeTheme`:

```kotlin
SwingBridgeTheme {
    // UI
}
```

When you need a Swing `JComponent` host, prefer `JewelComposePanel { ... }`. The bridge wrapper already applies
`SwingBridgeTheme` and uses `JBPopup`-backed popup rendering inside the IDE.

If the host already comes from `JewelComposePanel` or `ToolWindow.addComposeTab(...)`, do not wrap another
`SwingBridgeTheme` inside the content lambda. That usually just duplicates the bridge theme layer and makes examples
harder to follow.

Avoid building a public plugin API around `ComposeSearchableConfigurable` unless you know you are targeting internal
mode only. The upstream class is marked internal-only; use a regular `SearchableConfigurable` plus `JewelComposePanel`
for reusable plugin code.

For persisted settings pages, pair the configurable with an application- or project-level service based on
`SimplePersistentStateComponent` or `SerializablePersistentStateComponent`.
Use app-level services for IDE-wide settings and project-level services for workspace-specific behavior.
For project-local settings that should stay in the workspace file, use
`@Service(Service.Level.PROJECT)` with `Storage(StoragePathMacros.WORKSPACE_FILE)`.

Do not treat `platform/jewel/ide-laf-bridge/.../actionSystem/*` as a stable public plugin API. Those bridge classes are
useful for source reading, but public plugin examples should stay on top of `JewelComposePanel`,
`ToolWindow.addComposeTab(...)`, and normal IntelliJ actions.

For modal plugin UI, `DialogWrapper` plus `JewelComposePanel` is the simplest public host path when you need a
Jewel-styled dialog without building a custom bridge container.

Local copyable examples:

- `references/example-configurable-panel.kt`
- `references/example-project-settings-panel.kt`
- `references/example-bridge-popup-panel.kt`
- `references/example-dialog-wrapper.kt`
- `references/example-bridge-markdown-preview.kt`
- `references/example-swing-bridge-theme.kt`
- `references/example-toolbar-actions.kt`
- `references/example-toolwindow-compose-tab.kt`

Important constraints:

- Do not recommend standalone styling artifacts inside an IntelliJ plugin.
- Expect Bridge theming to mirror the current Swing Look and Feel.
- Prefer `ToolWindow.addComposeTab()` or the bridge helpers when integrating Compose into IDE UI.
- Prefer `JewelComposePanel` for settings pages and other plugin-hosted `JComponent` entry points.
- Prefer `SimplePersistentStateComponent` for compact mutable settings models and `SerializablePersistentStateComponent`
  when an immutable data-class state is a better fit.

## Decorated window

Use the `decorated-window` module for custom title bars and JetBrains-like window chrome.

Standalone Int UI example pattern:

```kotlin
IntUiTheme(
    theme = themeDefinition,
    styling = ComponentStyling.default().decoratedWindow(
        titleBarStyle = TitleBarStyle.light()
    ),
) {
    DecoratedWindow(onCloseRequest = { exitApplication() }) {
        // UI
    }
}
```

Inspect these paths first:

- `platform/jewel/decorated-window/src/main/kotlin/org/jetbrains/jewel/window/DecoratedWindow.kt`
- `platform/jewel/decorated-window/src/main/kotlin/org/jetbrains/jewel/window/TitleBar.kt`

## Markdown renderer

Treat Jewel Markdown as a two-pass pipeline:

1. Process raw Markdown into `MarkdownBlock`s outside composition when possible.
2. Render the processed blocks with `Markdown` or `LazyMarkdown` inside composition.

Use matching styling modules:

- Standalone app: `markdown/int-ui-standalone-styling`
- IntelliJ plugin: `markdown/ide-laf-bridge-styling`

Local copyable examples:

- `references/example-markdown-preview.kt`
- `references/example-markdown-advanced.kt`
- `references/example-markdown-split-editor.kt`
- `references/example-bridge-markdown-preview.kt`

Add parsing and rendering extensions in pairs. Do not enable an extension on the parser only.

## Icons, fonts, and Swing interop

For icon questions:

- Prefer `Icon` plus `IconKey`.
- Use `AllIconsKeys` for IntelliJ Platform icons.
- Inspect `PainterHint` before inventing custom icon-variant logic.

For font questions:

- Use `EmbeddedFontFamily` when the answer depends on JetBrains Runtime embedded fonts.
- Mention `asComposeFontFamily()` when converting from AWT or `JBFont`.

For Swing interop questions:

- Check whether `enableNewSwingCompositing()` is required.
- Check `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/ToolWindowExtensions.kt` first.
- Check `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/JewelComposePanelWrapper.kt` when the
  question is about embedding Compose into a Swing `JComponent`, popup behavior, or focus handling.
