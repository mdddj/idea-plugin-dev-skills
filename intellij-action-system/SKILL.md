---
name: intellij-action-system
description: Build or review IntelliJ Platform plugin actions, menus, toolbars, and popup menus. Use when requests mention `AnAction`, `DumbAwareAction`, `ActionManager`, `ActionToolbar`, `ActionPopupMenu`, the `actions` section in `plugin.xml`, `ActionPlaces`, `DefaultActionGroup`, `ToggleAction`, keyboard shortcuts, action groups, or Action IDs in IntelliJ/IDEA plugins. Prefer Kotlin for plugin-side code.
---

# IntelliJ Action System

## Overview

Build IntelliJ Platform plugin actions with the current Action System guidance. Use this skill to choose the right base class, implement `update()` and `actionPerformed()` safely, register actions without placement mistakes, and wire action groups into menus, toolbars, and popup menus.

## Workflow

1. Choose the action shape before writing code.
   Use `DumbAwareAction` for actions that must stay available during dumb mode; otherwise use `AnAction`.
   Use `ToggleAction` or `DumbAwareToggleAction` for checked or pressed state.
   Use `BackAction` or `ForwardAction` when the requirement is IDE-style history navigation instead of cloning that behavior manually.

2. Keep the action instance stateless.
   Do not store `Project`, editor, PSI, VFS, or Swing references in action fields.
   Move reusable behavior into services or utility classes instead of `static` methods or Kotlin `companion object` helpers inside the action class.

3. Split lifecycle responsibilities correctly.
   Always implement `update()` and `actionPerformed()`.
   Always implement `getActionUpdateThread()` and default to `ActionUpdateThread.BGT` unless `update()` must inspect Swing-only UI state.
   Keep `update()` fast and side-effect free. Read lightweight context from `AnActionEvent` and `CommonDataKeys`, then change `Presentation`.
   Put slow checks, writes, and real work in `actionPerformed()`. If the context can still be invalid there, notify the user instead of bloating `update()`.

4. Match thread access to the data you need.
   On `ActionUpdateThread.BGT`, it is safe to read PSI, VFS, and project models, but do not touch Swing hierarchy directly.
   On `ActionUpdateThread.EDT`, it is safe to read UI state, but do not read PSI, VFS, or project-model data directly.
   If a BGT update needs a small UI-only read, use `AnActionEvent.getUpdateSession().compute()` to hop to EDT for that fragment.

5. Register the action using the least surprising path.
   Prefer declarative registration in `plugin.xml` for normal static actions and groups.
   Use runtime registration only when the action set is dynamic. Register with `ActionManager.registerAction()`, then add the action to one or more `DefaultActionGroup` instances.
   If a runtime-created action should still appear in Settings | Keymap, reserve its ID in `plugin.xml` with `EmptyAction`.

6. Place the action with real platform IDs instead of guesses.
   Give every action and group a unique ID.
   If the same implementation appears in multiple UI locations, assign a distinct action ID per place.
   Use `ActionPlaces`, `PlatformActions.xml`, IDE code insight, Quick Documentation, or UI Inspector to find valid group IDs and `relative-to-action` anchors.

7. Configure presentation and localization deliberately.
   Remember each place gets its own `Presentation`, copied from the template presentation.
   Prefer resource bundles over hard-coded constructor text and description.
   When localizing, omit `text` and `description` from `plugin.xml` and use the documented `action.<id>.*` and `group.<id>.*` bundle keys instead.
   Use `<override-text>`, `<synonym>`, `<keyboard-shortcut>`, `<mouse-shortcut>`, and `compact` only when the placement actually needs them.

8. Build plugin-owned toolbars and popups with action groups.
   Use `ActionManager.createActionToolbar()` or `createActionPopupMenu()` and then render the returned Swing component.
   Call `ActionToolbar.setTargetComponent()` when the toolbar belongs to a specific panel or tool window so enablement tracks that component's context rather than global focus.
   If toolbar state changes without user activity or focus transfer, call `ActivityTracker.getInstance().inc()` to force refresh.

9. Avoid programmatic action execution unless you do not control the action.
   If the logic is yours, call a service or utility directly.
   Only use `ActionUtils.invokeAction()` when reusing an external action implementation that cannot be refactored.

10. Preserve Action ID ergonomics in code and tests.
    Prefer constants from `IdeActions` where applicable.
    In test helpers or custom string-based call sites, annotate action-ID strings with `@Language("devkit-action-id")` or configure IntelliLang Action ID injection.

## Guardrails

- Do not override `isDumbAware()` manually. Pick `DumbAwareAction` or `DumbAwareToggleAction` instead.
- Do not keep per-project or per-editor state in action fields. `AnAction` instances live for the application's lifetime.
- Do not do real work in `update()`, and do not let state transitions get stuck by forgetting to reset both enabled and visible states.
- Do not read PSI, VFS, or project models from EDT-bound `update()` implementations.
- Do not touch Swing hierarchy from BGT-bound `update()` implementations.
- If a menu action disappears while disabled, inspect the host group's `compact` behavior before debugging `Presentation`.
- For popup toggles that should remain open after click, explicitly request that behavior with `Presentation.setKeepPopupOnPerform()` and `KeepPopupOnPerform.IfRequested`.
- To expose a group under Settings | Appearance & Behavior | Menus and Toolbars, implement `CustomizableActionGroupProvider` and ensure the group has `text` or localization.

## References

- Read `references/official-docs.md` for the canonical JetBrains URLs, registration XML surface, localization keys, runtime registration notes, and action-ID lookup tips.
