# Module Map

## Overview

Use this file when the request is about source navigation, architecture, symbol lookup, or where to patch code inside `platform/jewel`.

## Contents

- Top-level modules
- Canonical project files
- Source navigation shortcuts
- Search strategy

## Top-level modules

### `foundation`

Core primitives and low-level abstractions:

- base theme contracts such as `JewelTheme`
- composition locals
- state management primitives
- basic unstyled components

Inspect first when the question is about shared abstractions, state, or theme plumbing.

### `ui`

Styled components and painter logic.

Inspect first when the question is about component appearance, behavior, or painting.

### `ui-tests`

Tests for the `ui` module.

Inspect first when changing component behavior or when you need usage examples encoded as tests.

### `decorated-window`

Custom window decoration primitives for JetBrains Runtime.

Key entry points:

- `platform/jewel/decorated-window/src/main/kotlin/org/jetbrains/jewel/window/DecoratedWindow.kt`
- `platform/jewel/decorated-window/src/main/kotlin/org/jetbrains/jewel/window/TitleBar.kt`

### `int-ui`

Standalone IntelliJ New UI styling.

Submodules:

- `int-ui/int-ui-standalone`
- `int-ui/int-ui-decorated-window`
- `int-ui/int-ui-standalone-tests`

Key entry point:

- `platform/jewel/int-ui/int-ui-standalone/src/main/kotlin/org/jetbrains/jewel/intui/standalone/theme/IntUiTheme.kt`

### `ide-laf-bridge`

Bridge layer that mirrors Swing Look and Feel into Compose for IntelliJ plugins.

Key entry points:

- `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/theme/SwingBridgeTheme.kt`
- `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/ToolWindowExtensions.kt`
- `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/JewelComposePanelWrapper.kt`

Internal plumbing worth recognizing but usually not building against directly:

- `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/actionSystem/RootDataProviderModifier.kt`
- `platform/jewel/ide-laf-bridge/src/main/kotlin/org/jetbrains/jewel/bridge/actionSystem/ComposePasteProvider.kt`

### `markdown`

Markdown parsing and rendering modules with standalone and bridge styling variants.

Submodules from `settings.gradle.kts`:

- `markdown/core`
- `markdown/extensions/autolink`
- `markdown/extensions/gfm-alerts`
- `markdown/extensions/gfm-strikethrough`
- `markdown/extensions/gfm-tables`
- `markdown/extensions/images`
- `markdown/int-ui-standalone-styling`
- `markdown/ide-laf-bridge-styling`

Start with:

- `platform/jewel/markdown/README.md`
- `platform/jewel/markdown/ide-laf-bridge-styling/src/main/kotlin/org/jetbrains/jewel/intui/markdown/bridge/BridgeProvideMarkdownStyling.kt`

### `samples`

Executable examples and showcase code.

Submodules:

- `samples/standalone`
- `samples/showcase`

Use first when the user asks for example usage or minimal setup.

Read [samples-and-recipes.md](samples-and-recipes.md) when the user needs a concrete example trail instead of just module names.

## Canonical project files

Read these before guessing project structure:

- `platform/jewel/README.md`
- `platform/jewel/settings.gradle.kts`
- `platform/jewel/VERSIONS.md`
- `platform/jewel/docs/jewel-contribution-guide.md`
- `platform/jewel/docs/upgrade-compose.md`

## Source navigation shortcuts

Use these starting points for common requests:

- Theme setup: `IntUiTheme.kt`, `SwingBridgeTheme.kt`
- Window chrome: `DecoratedWindow.kt`, `TitleBar.kt`
- Compose tool window integration: `ToolWindowExtensions.kt`
- Swing `JComponent` host integration: `JewelComposePanelWrapper.kt`
- Markdown support: `markdown/README.md`, then `markdown/core`
- Bridge Markdown setup: `BridgeProvideMarkdownStyling.kt`
- Samples: `samples/standalone/build.gradle.kts`

## Search strategy

Prefer this order when investigating:

1. Read `README.md` for product-level guidance.
2. Read `settings.gradle.kts` for module names.
3. Open the closest sample or module README.
4. Open the main symbol file for the requested API.
5. Open tests only after the entry points are clear.

Keep searches scoped to `platform/jewel` unless the issue clearly crosses into listed companion paths such as Compose, Skiko, or Jewel build targets.
