---
name: intellij-tool-windows
description: Build or review IntelliJ Platform plugin tool windows. Use when requests mention tool windows, `ToolWindowFactory`, `com.intellij.toolWindow`, `ToolWindowManager.registerToolWindow()`, `RegisterToolWindowTask`, stripe title localization, `canCloseContents`, or Chinese requests such as 工具窗口, 侧边栏面板, 停靠窗口, and 动态工具窗口.
---

# IntelliJ Tool Windows

## Overview

Build, debug, or extend IntelliJ Platform tool windows using the official JetBrains docs plus current platform sources. Default to Kotlin for plugin-side code unless the surrounding plugin is already Java-heavy.

## Workflow

1. Decide whether the request really needs a tool window.
   - Persistent docked UI, revisit-able results, or multi-tab side/bottom panel -> tool window
   - One-shot prompt, confirmation, or transient choice -> dialog or popup instead

2. Choose the registration model before writing code.
   - Stable plugin surface available whenever the project is open -> declarative `com.intellij.toolWindow` registration in `plugin.xml`
   - Operation-scoped or dynamically appearing result panel -> programmatic `ToolWindowManager.registerToolWindow(...)`

3. Read only the needed reference.
   - Read [references/official-docs.md](references/official-docs.md) for canonical URLs, extension attributes, lifecycle rules, dynamic registration APIs, and Kotlin-first starter snippets.

4. Implement the factory lazily.
   - Put UI construction in `createToolWindowContent(project, toolWindow)`.
   - Use `isApplicableAsync(project)` or `isApplicable(project)` only for one-time project-load gating.
   - Use `shouldBeAvailable(project)` for initial stripe visibility, then `ToolWindow.setAvailable(...)` for later state changes.
   - Add tabs through the content manager, set preferred focus, and register a disposer for long-lived resources.
   - Make the factory dumb-aware only when the content can safely run while indexes are unavailable.

5. Verify behavior in the IDE.
   - Check the `id`, `factoryClass`, `anchor`, `secondary`, `icon`, and `canCloseContents` settings are intentional.
   - Check the stripe title is localized when needed.
   - Check dynamic tool-window actions use `ToolWindowManager.invokeLater()` for EDT scheduling.
   - Check tab close behavior matches both the global `canCloseContents` switch and per-content `setCloseable(...)`.

## Guardrails

- Do not default to programmatic registration for ordinary plugin surfaces; use declarative registration unless the window truly appears only for a specific operation.
- Do not rely on `isApplicable*()` or `shouldBeAvailable()` for live toggling. Both are one-time decisions at project load.
- Do not eagerly build heavyweight UI before the tool window is opened.
- Do not assume `Content.setCloseable(true)` works unless `canCloseContents` is enabled on the tool window itself.
- Do not use a tool window where a dialog, popup, or editor inlay would match the interaction model better.

## References

- [references/official-docs.md](references/official-docs.md)
