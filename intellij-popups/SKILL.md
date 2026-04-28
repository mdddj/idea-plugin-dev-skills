---
name: intellij-popups
description: Build or review IntelliJ Platform plugin popups created with JBPopupFactory. Use when requests mention IntelliJ or IDEA plugin popups, JBPopup, JBPopupFactory, createPopupChooserBuilder, createActionGroupPopup, createListPopup, BaseListPopupStep, ComponentPopupBuilder, ActionSelectionAid, showInBestPositionFor, showUnderneathOf, or showInCenterOf. Prefer Kotlin for plugin-side code.
---

# IntelliJ Popups

## Overview

Use this skill to choose the correct `JBPopupFactory` popup API for IntelliJ Platform plugins and answer from the official JetBrains Popups documentation without re-reading the whole page. Keep the response focused on popup type selection, display positioning, and popup-step behavior.

## Workflow

1. Confirm the request is really about `JBPopup`-style transient UI.
   Use this skill for lightweight chooser or content popups that should disappear on focus loss.
   If the request is actually about action-system popup menus such as `popup="true"` groups or installed popup handlers, switch to the action-system guidance instead of forcing it through `JBPopupFactory`.

2. Choose the narrowest popup API first.
   Use `createPopupChooserBuilder()` for plain list selection from values or lightweight domain objects.
   Use `createActionGroupPopup()` when the choices already exist as `AnAction`s.
   Use `createListPopup()` plus `BaseListPopupStep` when the popup needs custom text, follow-up steps, or nested popup flow.
   Use `createConfirmation()` for short confirmation prompts.
   Use `createComponentPopupBuilder()` when the popup must host arbitrary Swing UI.

3. Choose the selection aid only after the popup type is correct.
   Prefer numeric shortcuts for small fixed menus.
   Prefer mnemonics only when labels are stable and intentional.
   Prefer speed search for large or changing lists.
   Keep `ActionGroup` as the source of truth for action-driven menus instead of flattening actions into strings.

4. Pick popup placement after the content model is settled.
   Default to `showInBestPositionFor()` when an editor, project, or `DataContext` already defines the natural anchor.
   Use `showUnderneathOf()` or `showInCenterOf()` only when the UX calls for explicit placement.
   Use `showCenteredInCurrentWindow()` when the popup should be centered relative to the active IDE window.

5. Model completion and substeps explicitly.
   Use chooser callbacks or popup listeners for one-step selection popups.
   In `BaseListPopupStep.onChosen()`, return the next `PopupStep` when a selection opens another popup instead of mixing navigation and final side effects.
   Keep rendering and business actions separate so nested popup flows remain composable.

## Guardrails

- Do not rebuild existing `AnAction` choices as raw strings just to fit a chooser popup.
- Do not reach for `createComponentPopupBuilder()` when a list or action-group popup already matches the job.
- Do not hardcode popup placement before checking whether `showInBestPositionFor()` already gives the correct result.
- Do not describe these APIs as blocking dialogs; JetBrains positions them as lightweight semimodal UI.
- When the request is ambiguous between popup menu and popup window, resolve that distinction before writing code.

## References

- Read `references/official-docs.md` for the API routing matrix, selection-aid guidance, positioning helpers, and canonical JetBrains documentation URLs.
