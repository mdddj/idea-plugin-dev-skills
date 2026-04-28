---
name: intellij-status-bar-widgets
description: Build or review IntelliJ Platform plugin status bar widgets. Use when requests mention status bar widgets, `StatusBarWidgetFactory`, `StatusBarEditorBasedWidgetFactory`, `EditorBasedWidget`, `EditorBasedStatusBarPopup`, `CustomStatusBarWidget`, `LightEditCompatible`, `com.intellij.statusBarWidgetFactory`, or `plugin.xml` registration for IntelliJ/IDEA plugins. Prefer Kotlin for plugin-side code.
---

# IntelliJ Status Bar Widgets

## Overview

Build IntelliJ Platform status bar widgets against the current JetBrains docs and source-backed API details. Use this skill to choose the right widget base class, register the factory correctly, and avoid lifecycle, visibility, and threading mistakes.

## Workflow

1. Confirm the status bar is the right UI surface before writing code.
   Use it only for compact, always-visible information or settings.
   If the request is really a transient chooser, notification, tooltip, or larger docked UI, route away from status bar widgets.

2. Start from the factory and registration.
   Register `statusBarWidgetFactory` under `defaultExtensionNs="com.intellij"`.
   Set the XML `id` attribute and make it exactly match `StatusBarWidgetFactory.getId()`.
   Use `StatusBarEditorBasedWidgetFactory` when the widget only makes sense with a text editor attached to the current status bar.

3. Choose the narrowest widget implementation.
   Use plain `StatusBarWidget` with `IconPresentation`, `TextPresentation`, or `MultipleTextValuesPresentation` when the standard presentation is enough.
   Use `EditorBasedWidget` for editor-aware widgets without the popup helper.
   Use `EditorBasedStatusBarPopup` for editor-aware widgets that show a list popup on click.
   Use `CustomStatusBarWidget` only when you really need a custom Swing component.

4. Respect lifecycle and threading constraints.
   Widgets may be instantiated on a background thread, so do not create Swing components in constructors or eager field initializers.
   `createWidget()` is not automatically rerun when availability or visibility changes; update the widget manager explicitly when those conditions change.
   Keep `disposeWidget()` aligned with widget ownership; default disposal through `Disposer.dispose(widget)` is usually correct.

5. Implement updates in the right layer.
   Use `isAvailable(project)` for project-level creation and disposal decisions.
   Keep `canBeEnabledOn(statusBar)` aligned with whether the widget can actually render on that status bar.
   For `EditorBasedStatusBarPopup`, implement `getWidgetState()` and call `update()` when state inputs change.
   For simpler widgets, update the installed status bar through `statusBar.updateWidget(ID())` when presentation changes.

6. Handle LightEdit and programmatic lookup deliberately.
   Widgets are hidden in LightEdit by default; implement `LightEditCompatible` on the factory only when the widget should appear there.
   To access a widget instance programmatically, retrieve it from `WindowManager.getInstance().getStatusBar(project).getWidget(...)`.

## Guardrails

- Do not let the `plugin.xml` registration `id` diverge from `getId()`.
- Do not expect the predefined presentation interfaces to combine into icon-plus-text; use a custom component if that UX is required.
- Do not build Swing UI eagerly in constructors; lazy-create components in `getComponent()` or an equivalent path.
- Do not assume changing `isAvailable()` alone refreshes the UI; call the status bar widgets manager update path explicitly.
- Do not use a status bar widget for information that is not important enough to stay visible most of the time.

## References

- Read `references/official-docs.md` for the live-checked JetBrains URLs, API map, plugin XML pattern, implementation choices, and source-derived caveats.
