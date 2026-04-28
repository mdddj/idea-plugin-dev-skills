---
name: intellij-ui-faq
description: Build or review common IntelliJ Platform plugin UI details using the official JetBrains User Interface FAQ. Use when requests mention `JBColor`, `JBColor.namedColor()`, `JBColor.lazy()`, `JBColor.isBright()`, `JBUI.Borders`, `JBUI.Insets`, `JBInsets.update()`, `UI Inspector`, `RecentsManager`, `StringUtil`, `NaturalComparator`, `NlsMessages`, `FileIconProvider`, `IconProvider`, `LoadingDecorator`, `IconUtil`, `RowIcon`, `LayeredIcon`, `IconWithOverlay`, or Chinese requests such as IntelliJ UI 常见问题, 主题颜色, DPI 边框, 最近使用记录, 自定义文件图标, and 图标叠加.
---

# IntelliJ UI FAQ

## Overview

Route narrow IntelliJ Platform UI implementation questions to the exact API, extension point, or linked JetBrains doc section documented in the official User Interface FAQ. Use this skill when the task is a focused UI detail, not a whole UI surface such as a full tool window or settings page.

## Workflow

1. Decide whether the request fits this FAQ surface.
   - Color and theme awareness, dark/light detection, DPI-safe borders or insets, localized UI text, recent-value storage, runtime UI inspection, file or PSI icon overrides, loading placeholders, icon transformations, and combined icons fit this skill.
   - Broader plugin UI work such as tool windows, dialogs, popups, settings pages, or complete component design should be handled by the narrower dedicated skill if one exists.

2. Read [references/official-docs.md](references/official-docs.md) before giving API-level advice.
   It contains the verified official URLs, the request-to-API routing table, and the version-sensitive notes that are easy to get wrong from memory.

3. Apply the narrowest documented API.
   - Inspect existing IDE UI or locate implementation details -> UI Inspector, and use `added-at` when you need to trace where a component was attached.
   - Theme-aware colors -> `JBColor.namedColor()` with light and dark defaults.
   - Colors derived from another dynamic source -> `JBColor.lazy()` with a fast, paint-safe lambda.
   - Generic UI constants or color tuning -> `UIUtil`, `JBUI`, `ColorUtil`, plus `Gray` and `LightColors` when the FAQ explicitly points there.
   - UI-oriented text formatting or sorting -> `NaturalComparator`, `StringUtil`, `NlsMessages`, and the linked internationalization guidance.
   - "Recently used" entries -> `RecentsManager`.
   - Current theme style -> `JBColor.isBright()`.
   - DPI-aware borders and insets -> `JBUI.Borders`, `JBUI.Insets`, and `JBInsets.update()` in `updateUI()` when the insets are reused outside `JBUI.Borders.empty()`.
   - File icons -> `FileIconProvider` registered in `com.intellij.fileIconProvider`.
   - PSI element icons -> `IconProvider` registered in `com.intellij.iconProvider`.
   - Loading placeholder panel -> `LoadingDecorator`.
   - Icon transformations -> `IconUtil`.
   - Horizontal icon composition -> `RowIcon`.
   - Overlay or stacked icons -> `LayeredIcon` or `IconWithOverlay`.

4. Keep the answer aligned with theme and localization rules.
   - Prefer theme keys and documented metadata over hardcoded colors.
   - Prefer message bundles and NLS-aware helpers over UI string concatenation or hardcoded user-facing text.
   - Prefer the documented extension points over ad hoc icon replacement hacks.

5. Hand off explicitly when the task outgrows the FAQ.
   - If the user actually needs a full UI surface, say so and switch to the more relevant IntelliJ UI skill instead of stretching this FAQ page beyond what it documents.

## Guardrails

- Do not recommend plain `java.awt.Color` for plugin UI colors when theme-aware `JBColor` or theme metadata is required.
- Do not resolve a dynamic color once at startup if it must follow theme or scheme changes; use `JBColor.lazy()` or re-query correctly.
- Do not use standard Swing border or inset factories for scale-sensitive plugin UI.
- Do not implement custom file or PSI icon logic without the corresponding extension point.
- Do not answer broad IntelliJ UI design questions from memory; read [references/official-docs.md](references/official-docs.md) first and stay inside the documented scope.

## References

- [references/official-docs.md](references/official-docs.md)
