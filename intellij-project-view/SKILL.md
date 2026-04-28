---
name: intellij-project-view
description: Build or review IntelliJ Platform plugin integrations for Project View and the Project tool window. Use when requests mention Project View, Project tool window, `ProjectViewNodeDecorator`, `treeStructureProvider`, `TreeStructureProvider`, `projectViewNestingRulesProvider`, `projectViewPane`, `AbstractProjectViewPane`, `SelectInTarget`, node decoration, tree filtering, file nesting, custom project panes, or refreshing Project View, including Chinese requests such as 项目视图, Project 工具窗口, 树结构过滤, 文件嵌套, and 自定义项目面板.
---

# IntelliJ Project View

## Overview

Build, debug, or extend IntelliJ Platform Project View behavior using the official JetBrains docs plus the relevant platform sources. Default to Kotlin for plugin-side code unless the surrounding plugin is already Java-heavy.

## Workflow

1. Classify the request before writing code.
   - Existing node text, icon, color, or appended status -> `ProjectViewNodeDecorator`
   - Filtering, hiding, regrouping, or injecting child nodes -> `TreeStructureProvider`
   - Nesting sibling files like `foo.js` -> `foo.min.js` -> `ProjectViewNestingRulesProvider`
   - Adding a whole new pane or subview in the Project tool window -> `AbstractProjectViewPane`
   - Underlying state changed but the tree did not update -> `ProjectView.refresh()` or pane-specific update APIs

2. Read only the bundled reference you need.
   - Read [references/official-docs.md](references/official-docs.md) for the canonical JetBrains URLs, extension point names, API signatures, refresh rules, and Kotlin-first starter snippets.

3. Apply the platform defaults.
   - Register Project View extensions under `defaultExtensionNs="com.intellij"`.
   - Keep decorators presentation-only and side-effect-free.
   - Return the original `children` collection unchanged when a `TreeStructureProvider` has nothing to modify.
   - Prefer dumb-aware implementations only when the logic does not depend on indexes.
   - Prefer the smallest integration surface that solves the request. Do not build a custom pane when decoration or tree filtering is enough.

4. Verify the behavior in the IDE.
   - Check the extension point name matches the class responsibility.
   - Check decorations do not try to remove or reorder nodes.
   - Check structure providers preserve important directories and expected ordering unless the change is intentional.
   - Check file nesting rules match file-name suffixes only.
   - Check refresh runs after the model changes if the tree content is dynamic.
   - Check custom panes expose a stable `getId()`, `getWeight()`, and `createSelectInTarget()`.

## Guardrails

- Do not use `TreeStructureProvider` for pure styling; use `ProjectViewNodeDecorator`.
- Do not use `ProjectViewNodeDecorator` to hide nodes or rewrite tree structure.
- Do not override the deprecated package-dependencies `decorate(...)` overload; the platform source says it is never called.
- Do not keep using deprecated `TreeStructureProvider.getData(...)` in new code; prefer `uiDataSnapshot(...)` or a `UiDataRule`.
- Do not describe file nesting as content-aware. The platform API is filename-suffix based.

## References

- [references/official-docs.md](references/official-docs.md)
