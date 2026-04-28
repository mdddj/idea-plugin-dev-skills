---
name: intellij-lists-and-trees
description: Build or review IntelliJ Platform plugin list and tree UIs. Use when requests mention IntelliJ or IDEA plugin lists or trees, `JBList`, `Tree`, `ColoredListCellRenderer`, `ColoredTreeCellRenderer`, `ListSpeedSearch`, `TreeSpeedSearch`, `ToolbarDecorator`, `EditableModel`, `CollectionListModel`, empty text, busy state, or reorderable list/tree toolbars, including Chinese requests such as 列表, 树, 速度搜索, 工具栏装饰器, and 可重排序列表.
---

# IntelliJ Lists and Trees

## Overview

Use this skill for IntelliJ Platform plugin work built around JetBrains list and tree controls from the official List and Tree Controls page. Default to Kotlin for plugin-side examples unless the surrounding plugin is already Java-heavy.

## Workflow

1. Classify the request before writing code.
   - Basic IntelliJ-flavored list or tree widget -> `JBList` or `Tree`
   - Rich text fragments or icons per row -> `ColoredListCellRenderer` or `ColoredTreeCellRenderer`
   - Keyboard filtering or quick selection -> `ListSpeedSearch` or `TreeSpeedSearch`
   - Add, remove, edit, or reorder actions around a list or tree -> `ToolbarDecorator`

2. Pick the JetBrains component before customizing behavior.
   - Prefer `JBList` over plain `JList` when building plugin UI. The official docs call out overflow tooltips, empty-text support via `getEmptyText().setText()`, and busy-state painting via `setPaintBusy()`.
   - Prefer `Tree` over plain `JTree` when the UI should match IDE tree behavior. The docs call out the `JBList` conveniences plus wide selection painting and drag-and-drop auto-scroll.

3. Add custom rendering only when the row presentation really needs it.
   - Use `ColoredListCellRenderer` or `ColoredTreeCellRenderer` when an item needs multiple text fragments, different attributes, or an icon.
   - Build the row with `append()` and `setIcon()`.
   - Let the renderer handle selected-state colors and platform-specific painting details instead of hardcoding them yourself.

4. Add speed search when keyboard lookup matters.
   - Install it with `new ListSpeedSearch(list)` or `new TreeSpeedSearch(tree)`.
   - If search text differs from the default string form, override `getElementText()` or pass the mapping function supported by the documented constructors.

5. Use `ToolbarDecorator` only after the backing model is chosen.
   - For removable or reorderable list content, make sure the model implements `EditableModel`. The docs explicitly call out `CollectionListModel` as the convenient default.
   - Start with `ToolbarDecorator.createDecorator(...)`.
   - Add actions with `setAddAction()` and `setRemoveAction()`.
   - Add nonstandard buttons with `addExtraAction()` or replace the standard action set with `setActionGroup()`.
   - Finish with `createPanel()` and add the returned component to the parent UI.
   - Do not hardcode toolbar position assumptions; the docs say placement above or below depends on platform.

6. Route out when the request is actually about a different UI surface.
   - If the request is about Project View structure, pane extensions, or node decoration, use the Project View guidance instead of forcing it through this skill.
   - If the request is really about popup choosers rather than embedded lists or trees, use popup-specific guidance.

## Guardrails

- Do not recommend plain Swing `JList` or `JTree` when JetBrains replacements fit the job.
- Do not hand-roll selection colors inside `Colored*CellRenderer`; that is already part of the renderer contract.
- Do not assume `ToolbarDecorator` can reorder arbitrary models. Reordering support depends on an `EditableModel`.
- Do not describe toolbar placement as fixed; the official docs say it is platform-dependent.
- Do not use this skill for Project View APIs, tree-structure providers, or popup APIs that live on different docs pages.

## References

- Read `references/official-docs.md` for the canonical JetBrains URL, API routing table, Kotlin-first starter snippets, and scope boundaries.
