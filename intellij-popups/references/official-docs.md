# IntelliJ Popups Reference

Canonical docs checked live on 2026-04-17:

- `https://plugins.jetbrains.com/docs/intellij/popups.html` (updated 2025-04-29)
- `https://plugins.jetbrains.com/docs/intellij/grouping-actions-tutorial.html` (used only to keep `ActionGroup` terminology aligned with popup action groups; updated 2025-05-07)

## Scope

The JetBrains Popups page describes lightweight semimodal UI built from `JBPopupFactory`. Treat it as the reference for choosing popup construction APIs, selection behavior, and positioning helpers.

## API Routing

Use this matrix before writing code:

| Need | Start from | Why |
| --- | --- | --- |
| Show arbitrary Swing content in a transient popup | `JBPopupFactory.createComponentPopupBuilder()` | Best fit when the popup is really a container for custom UI |
| Let the user choose from a plain list of values | `JBPopupFactory.createPopupChooserBuilder()` | Simplest builder for chooser-style popups |
| Ask for a short confirmation | `JBPopupFactory.createConfirmation()` | The page documents it as the confirmation-specific popup path |
| Reuse existing `AnAction` choices | `JBPopupFactory.createActionGroupPopup()` | Keeps presentation, enablement, and action execution tied to the action system |
| Render custom items or open nested follow-up popups | `JBPopupFactory.createListPopup()` with `BaseListPopupStep` | Supports custom text and multi-step popup flow |

## Selection Guidance

- For small fixed menus, prefer sequential numbering.
- For large or dynamic lists, prefer speed search.
- Use mnemonics only when the labels are stable enough that explicit letters help more than they confuse.
- If the choices already live as `AnAction`s, use an action-group popup instead of duplicating the data in a chooser popup.

The Popups page explicitly calls out `ActionSelectionAid` for action-group popups and lists sequential numbers, speed search, and mnemonics as the documented aids.

## Positioning Helpers

- `showInBestPositionFor()`
  Default choice when the surrounding context already suggests a useful anchor.
- `showUnderneathOf()`
  Use when the popup should sit below a specific component.
- `showInCenterOf()`
  Use when the popup should be centered relative to a specific component.
- `showCenteredInCurrentWindow()`
  Use when the popup should be centered in the active IDE window rather than a component.

Prefer `showInBestPositionFor()` first. Reach for explicit placement only when the UX requires a fixed visual relationship.

## `BaseListPopupStep` Notes

Use `BaseListPopupStep` when a simple chooser is not enough.

- Override `getTextFor()` when item labels are not just `toString()`.
- Override `onChosen()` to control what happens after selection.
- Return another `PopupStep` from `onChosen()` when the selection should open a nested popup instead of immediately ending the interaction.

This is the documented path for custom list popups and multi-step popup flows.

## Practical Decision Rules

- If the user says "I already have actions," route to `createActionGroupPopup()`.
- If the user says "I need a quick pick from strings or objects," route to `createPopupChooserBuilder()`.
- If the user says "the next choice depends on the first one," route to `createListPopup()` with `BaseListPopupStep`.
- If the request starts describing text fields, checkboxes, or several custom controls inside the popup, route to `createComponentPopupBuilder()`.
- If the request is actually about popup menus declared through the action system, do not answer from this page alone; that is a different UI surface.
