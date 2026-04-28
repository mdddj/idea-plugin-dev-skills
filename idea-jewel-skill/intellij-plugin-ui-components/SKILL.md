---
name: intellij-plugin-ui-components
description: Guide for IntelliJ Platform plugin UI surfaces and user-interface components from the official User Interface Components docs. Use when requests mention IntelliJ or IDEA plugin UI, tool windows, dialogs, Kotlin UI DSL, popups, notifications, file or class choosers, editor components, lists, trees, status bar widgets, Swing UI helpers, icons, JCEF embedded browser, color scheme management, exposing theme metadata, editor intentions, Alt+Enter intention actions, Search Everywhere contributors, clipboard helpers, progress dialogs, process handlers, saving documents, or generating UI scaffolds and starter files, including Chinese requests such as 工具窗口, 对话框, 弹窗, 通知, 文件选择器, 编辑器组件, 列表树, 状态栏组件, 图标, 内嵌浏览器, 配色方案, 主题元数据, 意图动作, Alt+Enter, Search Everywhere, 剪贴板, 进度条弹窗, 执行命令行, 保存文档, 脚手架, 模板, and 生成 plugin.xml.
---

# IntelliJ Plugin UI Components

## Overview

Use this skill to choose the correct IntelliJ Platform UI surface and answer from the official JetBrains User Interface Components documentation without re-reading the whole doc tree. This skill intentionally covers the page set shown in the provided screenshot, not the entire Plugin SDK.

## Workflow

1. Classify the request before reading references:
   - Persistent IDE surface or tabbed side panel -> tool window
   - Data-entry form, settings panel, or validation-heavy flow -> dialog or Kotlin UI DSL
   - Lightweight chooser or contextual transient UI -> popup
   - Non-modal feedback or user attention -> informing users or notification balloons
   - File, class, or package picking -> chooser APIs
   - Code-like text input or completion -> editor components
   - Tree or list CRUD UI -> list and tree controls
   - Always-visible compact info -> status bar widget
   - Icon, theme, color, or browser surface -> styling, theme, and JCEF references

2. Read only the relevant bundled reference:
   - Read [references/page-map.md](references/page-map.md) first for the official page index and fastest routing.
   - Read [references/request-routing.md](references/request-routing.md) first when the user asks in vague product language such as "做个侧边栏", "弹个选择框", or "别打断用户".
   - Read [references/windows-dialogs-and-feedback.md](references/windows-dialogs-and-feedback.md) for tool windows, dialogs, Kotlin UI DSL, popups, informing users, and notification balloons.
   - Read [references/selection-editor-and-widgets.md](references/selection-editor-and-widgets.md) for choosers, editor components, lists and trees, status bar widgets, and miscellaneous Swing helpers.
   - Read [references/icons-themes-and-web.md](references/icons-themes-and-web.md) for icons, UI FAQ guidance, JCEF, color scheme management, and theme metadata.
   - Read [references/minimal-recipes.md](references/minimal-recipes.md) when the user wants copyable starter code, `plugin.xml` entries, or a concrete implementation skeleton.
   - Read [references/scaffold-workflow.md](references/scaffold-workflow.md) and run `scripts/scaffold_ui_recipe.py` when the user wants ready-to-create files or a starter directory layout, especially for `intention-action` and `search-everywhere-contributor`.
   - Read [references/practical-plugin-tips.md](references/practical-plugin-tips.md) when the user asks for editor intentions, Search Everywhere tabs, clipboard copy, syntax-highlighted HTML output, progress wrappers, process launching, editor font access, or document-save helpers.

3. Apply these defaults unless the user asks otherwise:
   - Prefer `DialogWrapper` plus Kotlin UI DSL for forms, validation, and settings-style panels.
   - Prefer a tool window only for persistent or repeatedly revisited functionality.
   - Prefer popups for lightweight transient selection instead of opening a full dialog.
   - Prefer non-modal notifications, hints, or editor banners over modal `Messages`.
   - Prefer IntelliJ components such as `JBList`, `Tree`, `EditorTextField`, `JBSplitter`, and `ToolbarDecorator` over raw Swing equivalents when the platform offers them.
   - Prefer platform icons and theme-aware colors over custom hardcoded assets.
   - Prefer JCEF only when HTML or browser-style rendering is genuinely required.

## Decision Rules

- Do not recommend Kotlin UI DSL for general tool window chrome; the official docs position it around forms with bound state.
- Do not hardcode `Color` values for theme-aware UI. Use `JBColor`, `JBColor.namedColor()`, `JBColor.lazy()`, `UIUtil`, `JBUI`, or editor color keys as appropriate.
- Do not use deprecated notification APIs when `Notification` plus plugin.xml `notificationGroup` covers the use case.
- Do not add JCEF without checking `JBCefApp.isSupported()` and a disposal plan.
- Do not invent icon sizes or New UI mappings from memory; consult the icon reference before answering.
- Do not overwrite an existing `plugin.xml` by default when generating files; generate `*.xml.fragment` files and merge intentionally.

## Output Expectations

- Name the recommended UI surface first.
- Mention the primary API or extension point to start from.
- Call out lifecycle or UX constraints such as dumb mode, disposal, theme-awareness, or macOS/native dialog caveats.
- Quote the official JetBrains page URL when the answer depends on exact platform guidance.
- When the user asks for implementation, prefer the smallest working Kotlin or `plugin.xml` skeleton from [references/minimal-recipes.md](references/minimal-recipes.md) instead of vague pseudocode.
- When the user asks in product language rather than API language, translate the request through [references/request-routing.md](references/request-routing.md) before proposing code.
- When the user asks for starter files, prefer `scripts/scaffold_ui_recipe.py` over manually rewriting the same boilerplate.
- When the answer relies on the bundled blog-derived practice notes, say that the guidance is distilled from a practical article rather than an official JetBrains page.

## References

- [references/page-map.md](references/page-map.md)
- [references/request-routing.md](references/request-routing.md)
- [references/windows-dialogs-and-feedback.md](references/windows-dialogs-and-feedback.md)
- [references/selection-editor-and-widgets.md](references/selection-editor-and-widgets.md)
- [references/icons-themes-and-web.md](references/icons-themes-and-web.md)
- [references/minimal-recipes.md](references/minimal-recipes.md)
- [references/scaffold-workflow.md](references/scaffold-workflow.md)
- [references/practical-plugin-tips.md](references/practical-plugin-tips.md)
