---
name: jetbrains-jewel
description: JetBrains Jewel guide for the IntelliJ Platform `platform/jewel` subtree. Use when requests mention Jewel, Compose for Desktop IntelliJ-style UI, `IntUiTheme`, `SwingBridgeTheme`, `JewelTheme`, `DecoratedWindow`, `ide-laf-bridge`, Jewel Markdown, IntelliJ plugin Compose UI integration, or source navigation and contribution work inside `JetBrains/intellij-community/platform/jewel`.
---

# JetBrains Jewel

## Overview

Use this skill to reason about JetBrains Jewel without cloning the whole `intellij-community` repository. Determine the target scenario first, then load only the relevant reference file and inspect only the needed files under `platform/jewel`.

## Workflow

1. Determine the request type:
   - Standalone Compose for Desktop app
   - IntelliJ Platform plugin using the Swing bridge
   - Source reading, debugging, architecture explanation, or contribution work
   - Markdown renderer or decorated window support

2. Load only the reference file you need:
   - Read [references/local-code-examples.md](references/local-code-examples.md) first when the user wants local copyable code and should not depend on remote sample fetching.
   - Read [references/usage-paths.md](references/usage-paths.md) for setup, integration, runtime constraints, and common pitfalls.
   - Read [references/module-map.md](references/module-map.md) for module boundaries, symbol entry points, sample locations, and source navigation.
   - Read [references/contribution-and-compat.md](references/contribution-and-compat.md) for version parity, branch strategy, PR rules, and contribution flow.
   - Read [references/samples-and-recipes.md](references/samples-and-recipes.md) for component showcase entry points and Markdown usage examples.

3. Keep the scope tight:
   - Prefer the bundled local code examples before requesting upstream sample code.
   - Use the bundled references and examples as the default answer surface; inspect upstream files only when local coverage is missing or exact API behavior must be verified.
   - Prefer reading or downloading only `platform/jewel/...` paths.
   - Do not clone the full `JetBrains/intellij-community` repository unless the user explicitly asks for it.
   - Prefer targeted GitHub file reads, sparse checkout of `platform/jewel`, or direct file fetches over whole-repo operations.

4. Start from the highest-signal sources:
   - `platform/jewel/README.md` for project structure, setup, icons, fonts, and bridge vs standalone guidance
   - `platform/jewel/settings.gradle.kts` for the canonical module list
   - `platform/jewel/samples/standalone` and `platform/jewel/samples/showcase` for runnable examples
   - `platform/jewel/docs/*` for contribution and maintenance workflows
   - Module-specific entry files from [references/module-map.md](references/module-map.md) for implementation details

## Decision Rules

Use these defaults unless the user asks otherwise:

- For a standalone app, use `IntUiTheme` and the standalone `int-ui` artifacts.
- For an IntelliJ plugin, use bundled IntelliJ Platform modules instead of standalone artifacts.
- For plugin `JComponent` hosts, prefer `JewelComposePanel`.
- For tool windows, prefer `ToolWindow.addComposeTab(...)` over hand-rolled bridge setup.
- Use `SwingBridgeTheme` directly only when the host is not already provided by `JewelComposePanel` or `addComposeTab(...)` content.
- For custom title bar work, inspect `decorated-window` first.
- For Markdown rendering, pair `markdown/core` with the matching standalone or bridge styling module and any needed extensions.
- For icon questions, inspect `Icon`, `IconKey`, `PainterHint`, and `AllIconsKeys` usage paths before inventing a custom loader.
- For Swing interop issues, check `ToolWindowExtensions.kt` and `enableNewSwingCompositing()` guidance before changing rendering code.

## Common Pitfalls

- Do not recommend standalone Jewel artifacts inside an IntelliJ plugin.
- Do not recommend bridge artifacts inside a standalone Compose app.
- Do not wrap `SwingBridgeTheme` again inside `JewelComposePanel` or `ToolWindow.addComposeTab(...)` content.
- Do not ignore the JetBrains Runtime requirement for standalone use.
- Do not mix unrelated Compose, Kotlin, and Jewel versions without checking compatibility.
- Do not assume Material is needed; Jewel typically excludes `org.jetbrains.compose.material`.
- Do not answer versioning questions from memory when `VERSIONS.md` or release notes are available.

## Output Expectations

When answering, do the following:

- State whether the guidance applies to standalone, IntelliJ plugin, or both.
- Cite concrete module or file paths when explaining architecture or API usage.
- Call out runtime or compatibility constraints explicitly.
- Prefer a shortest-path answer that points to the right module and sample before diving into internals.

## References

- [references/usage-paths.md](references/usage-paths.md)
- [references/module-map.md](references/module-map.md)
- [references/contribution-and-compat.md](references/contribution-and-compat.md)
- [references/samples-and-recipes.md](references/samples-and-recipes.md)
- [references/local-code-examples.md](references/local-code-examples.md)
