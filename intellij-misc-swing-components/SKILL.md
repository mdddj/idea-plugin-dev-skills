---
name: intellij-misc-swing-components
description: Build or review IntelliJ Platform plugin UI that uses the Misc Swing Components APIs from the official JetBrains docs. Use when requests mention IntelliJ or IDEA plugin Messages, JBSplitter, JBTabs, TabInfo, ActionToolbar, ToolbarDecorator, 标准对话框, 分栏, 标签页, 工具栏, splitter, tabs, or misc swing components.
---

# IntelliJ Misc Swing Components

## Overview

Use this skill to route IntelliJ Platform plugin UI requests to the correct component from the official JetBrains Misc Swing Components page without defaulting to raw Swing. Keep the answer narrow: modal dialogs, split panes, embedded tabs, and action-driven toolbars.

## Workflow

1. Confirm the request actually belongs to this page.
   - `Messages` or quick modal prompt -> standard dialogs
   - two resizable panes -> `JBSplitter`
   - tabs inside one plugin surface -> `JBTabs` and `TabInfo`
   - list, tree, or table actions -> `ToolbarDecorator`
   - arbitrary action strip -> `ActionToolbar`
   - if the request is really about tool windows, popups, Kotlin UI DSL, notifications, or JCEF, switch to the narrower skill instead of stretching this one

2. Choose the least intrusive surface first.
   - Prefer notifications, editor hints, or other non-modal UI when the task is only feedback.
   - Use `Messages` only for short blocking confirmation, error, info, or text input flows.
   - Mention that the official docs explicitly say to use these dialogs sparingly because they are modal.

3. Read only the relevant reference.
   - Read [references/official-docs.md](references/official-docs.md) for the selection matrix, official URLs, and source-backed API notes.
   - Read [references/minimal-recipes.md](references/minimal-recipes.md) when the user wants copyable Kotlin snippets or a concrete starting point.

4. Apply these defaults unless the user asks otherwise.
   - For dialogs, prefer the narrowest `Messages` method that matches the UX.
   - For split layouts, prefer `JBSplitter` over raw `JSplitPane`, and persist proportion with a stable key when the split is user-adjustable.
   - For tabs, model each tab as a `TabInfo` and configure behavior through `JBTabsPresentation`.
   - For list, tree, or table CRUD toolbars, prefer `ToolbarDecorator.createDecorator(...)` over manually rebuilding add/remove/up/down buttons.
   - For arbitrary action strips, build an `ActionToolbar`, set a target component, and add `toolbar.component` to the container.

## Guardrails

- Do not recommend modal `Messages` dialogs for passive success feedback or background status.
- Do not hardcode a transient splitter proportion when the layout is meant to be user-adjustable across sessions.
- Do not describe `JBTabs` as tool window tabs; this skill is about embedded tab containers inside your UI.
- Do not use `ToolbarDecorator` for unsupported arbitrary components; its decorators target `JList`, `JTree`, `JTable`, `TableView`, and related wrappers.
- Do not invent APIs from memory. Quote the official docs URL or the bundled reference when the answer depends on exact method names.

## References

- Read [references/official-docs.md](references/official-docs.md) for official coverage and source-backed API notes.
- Read [references/minimal-recipes.md](references/minimal-recipes.md) for Kotlin starter snippets.
